package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import chisel3._
import chisel3.experimental.BundleLiterals._
import chisel3.util.Decoupled
import chisel3.util.Valid

import edu.gatech.cs3220.spring2022.m68k.qarma64.Qarma
import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.util.LFSRHW
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.rounds.ForwardRound
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.rounds.BackwardRound
import edu.gatech.cs3220.spring2022.qarma64_hw.m68k.rounds.QarmaHWIntermediate

/** Bundle for the keys */
class QarmaHWKeys extends Bundle {
  val k0 = UInt(64.W)
  val k1 = UInt(64.W)
  val w0 = UInt(64.W)
  val w1 = UInt(64.W)
}

/** Bundle for the input plaintext and tweak */
class QarmaHWInput extends Bundle {
  val ptext = UInt(64.W)
  val tweak = UInt(64.W)
}

/** Bundle for the output ciphertext, along with the input that produced it */
class QarmaHWOutput extends Bundle {
  val ptext = UInt(64.W)
  val tweak = UInt(64.W)
  val ctext = UInt(64.W)
}

object QarmaHW {

  /** Same as [Qarma.REFLECTION_CONSTANT], but as a `UInt` */
  val REFLECTION_CONSTANT: UInt = "h_c0ac29b7c97c50dd".U(64.W)

  /** Same as [Qarma.ROUND_KEYS], but as a `Seq[UInt]` */
  val ROUND_KEYS: Seq[UInt] = Seq(
    "h_0000000000000000".U(64.W),
    "h_13198a2e03707344".U(64.W),
    "h_a4093822299f31d0".U(64.W),
    "h_082efa98ec4e6c89".U(64.W),
    "h_452821e638d01377".U(64.W)
  )
}

/** Hardware module for a run of Qarma
  *
  * It's designed to have four cycles of latency, implemented iteratively. Only
  * Qarma64 with five rounds is supported.
  *
  * @param a
  *   The reflection constant
  * @param c
  *   The sequence of round keys
  * @param sBox
  *   The substitution box to use
  */
class QarmaHW(
    val a: UInt = QarmaHW.REFLECTION_CONSTANT,
    val c: Seq[UInt] = QarmaHW.ROUND_KEYS,
    val sBox: Permutation = Qarma.CELL_SBOX_1
) extends Module {

  // Reflection constant must be a 64-bit integer
  require(
    this.a.widthOption == Some(64),
    "Reflection constant must be a 64-bit unsigned integer"
  )
  // Must have five round constants, all 64-bit integers
  require(this.c.length >= 5, "Must have at least five round constants")
  require(
    this.c.forall(n => n.widthOption == Some(64)),
    "All round constants must be 64-bit unsigned integers"
  )

  // IO
  val inp = IO(Flipped(Decoupled(new QarmaHWInput)))
  val out = IO(Decoupled(new QarmaHWOutput))
  val key = IO(Input(new QarmaHWKeys))

  // Convert the parameters to blocks
  val k0_block = BlockHWInit(key.k0)
  val k1_block = BlockHWInit(key.k1)
  val w0_block = BlockHWInit(key.w0)
  val w1_block = BlockHWInit(key.w1)
  val ptext_block = BlockHWInit(inp.bits.ptext)
  val tweak_block = BlockHWInit(inp.bits.tweak)

  // Registers holding intermediate values
  // Arranged in a pipeline
  val regs = RegInit(
    VecInit(Seq.fill(4)(Valid(new QarmaHWIntermediate).Lit(_.valid -> false.B)))
  )

  // Create the forward rounds
  // Connect all the common pins
  val forward_rounds = (0 to 4).map { i =>
    val r = Module(new ForwardRound(i == 0, this.c(i), this.sBox))
    r.io.k0 := k0_block
    r
  }
  // Seed the first round, then chain the rest
  // Have to add registers in the middle
  forward_rounds(0).io.inp <> regs(0).bits
  forward_rounds(1).io.inp <> forward_rounds(0).io.out
  forward_rounds(2).io.inp <> forward_rounds(1).io.out
  forward_rounds(3).io.inp <> forward_rounds(2).io.out
  forward_rounds(4).io.inp <> regs(1).bits

  // Pseudo-reflector
  // First, connect everything as normal
  val psuedo_reflected = Wire(new QarmaHWIntermediate)
  psuedo_reflected.ptext := forward_rounds(4).io.out.ptext
  psuedo_reflected.otweak := forward_rounds(4).io.out.otweak
  psuedo_reflected.ntweak := forward_rounds(4).io.out.ntweak
  // Then do the math
  psuedo_reflected.ctext := forward_rounds(4).io.out.ctext
    .^(forward_rounds(4).io.out.ntweak)
    .^(w1_block)
    .permute(Qarma.CELL_PERMUTATION)
    .mulMatR(Qarma.M42)
    .substitute(this.sBox)
    .permute(Qarma.CELL_PERMUTATION)
    .mulMatR(Qarma.M42)
    .^(k1_block)
    .permute(Qarma.CELL_PERMUTATION.inv)
    .substitute(this.sBox.inv)
    .mulMatR(Qarma.M42)
    .permute(Qarma.CELL_PERMUTATION.inv)
    .^(forward_rounds(4).io.out.ntweak)
    .^(w0_block)

  // The same as above, but for the backward rounds
  // They're kept in the array in reverse order. So Index 0 is the last backward
  // round to be performed.
  val backward_rounds = (0 to 4).map { i =>
    val r = Module(new BackwardRound(i == 0, this.a, this.c(i), this.sBox))
    r.io.k0 := k0_block
    r
  }
  backward_rounds(4).io.inp <> psuedo_reflected
  backward_rounds(3).io.inp <> regs(2).bits
  backward_rounds(2).io.inp <> backward_rounds(3).io.out
  backward_rounds(1).io.inp <> backward_rounds(2).io.out
  backward_rounds(0).io.inp <> backward_rounds(1).io.out

  // Register ready logic
  // A register is ready to accept data if its data is invalid or the next stage
  // is ready
  val regs_ready = Wire(Vec(4, Bool()))
  for (i <- 0 to 3)
    regs_ready(i) := !regs(i).valid || (if (i == 3) out.ready
                                        else regs_ready(i + 1))
  // Registers latch whenever they are ready
  // It's fine if the data isn't valid. It'll just be passed on
  when(regs_ready(0)) {
    regs(0).bits.ptext := ptext_block
    regs(0).bits.ctext := ptext_block ^ w0_block
    regs(0).bits.otweak := tweak_block
    regs(0).bits.ntweak := tweak_block
  }
  when(regs_ready(1)) { regs(1).bits := forward_rounds(3).io.out }
  when(regs_ready(2)) { regs(2).bits := backward_rounds(4).io.out }
  when(regs_ready(3)) { regs(3).bits := backward_rounds(0).io.out }
  // Now we handle the valids
  for (i <- 0 to 3)
    when(regs_ready(i)) {
      regs(i).valid := (if (i == 0) inp.valid else regs(i - 1).valid)
    }

  // Output is just an interface to the last register in the pipeline
  // Keys never change, so we can use them directly
  inp.ready := regs_ready(0)
  out.valid := regs(3).valid
  out.bits.ptext := regs(3).bits.ptext.intoUInt()
  out.bits.tweak := regs(3).bits.otweak.intoUInt()
  out.bits.ctext := (regs(3).bits.ctext ^ w1_block).intoUInt()
}
