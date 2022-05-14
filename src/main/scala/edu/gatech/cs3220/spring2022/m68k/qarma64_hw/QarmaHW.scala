package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import chisel3._
import chisel3.util.Decoupled

import edu.gatech.cs3220.spring2022.m68k.qarma64.Qarma
import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.util.LFSRHW
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.rounds.ForwardRound

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
  val a_block = BlockHWInit(this.a)
  val c_block = this.c.map(BlockHWInit.apply)
  val k0_block = BlockHWInit(key.k0)
  val w0_block = BlockHWInit(key.w0)
  val w1_block = BlockHWInit(key.w1)
  val ptext_block = BlockHWInit(inp.bits.ptext)
  val tweak_block = BlockHWInit(inp.bits.tweak)

  // Create the forward rounds
  // Connect all the common pins
  val forward_rounds = (0 to 4)
    .map { i => Module(new ForwardRound(i == 0, this.sBox)) }
  forward_rounds.zipWithIndex
    .foreach { case (r, i) =>
      r.io.k0 := k0_block
      r.io.c := c_block(i)
    }
  // Chain them all together
  // Then seed the first
  for (i <- 1 to 4) {
    forward_rounds(i).io.inp <> forward_rounds(i - 1).io.out
  }
  forward_rounds(0).io.inp.ptext := ptext_block
  forward_rounds(0).io.inp.ctext := ptext_block ^ w0_block
  forward_rounds(0).io.inp.otweak := tweak_block
  forward_rounds(0).io.inp.ntweak := tweak_block

  inp.ready := true.B
  out.valid := inp.valid
  out.bits.ptext := inp.bits.ptext
  out.bits.tweak := inp.bits.tweak
  out.bits.ctext := (forward_rounds(4).io.out.ctext ^ w1_block).intoUInt()
}
