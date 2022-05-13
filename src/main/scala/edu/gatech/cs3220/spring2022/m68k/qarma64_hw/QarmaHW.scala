package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import chisel3._
import chisel3.util.Decoupled

import edu.gatech.cs3220.spring2022.m68k.qarma64.Qarma
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.util.SBox

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
  */
class QarmaHW(
    a: UInt = QarmaHW.REFLECTION_CONSTANT,
    c: Seq[UInt] = QarmaHW.ROUND_KEYS
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
}
