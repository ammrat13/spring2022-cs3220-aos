package edu.gatech.cs3220.spring2022.m68k.qarma_hw

import chisel3._
import chisel3.util.Decoupled

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

/** Bundle for the output ciphertext and new tweak */
class QarmaHWOutput extends Bundle {
  val ctext = UInt(64.W)
  val tweak = UInt(64.W)
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
class QarmaHW(a: UInt, c: Seq[UInt]) extends Module {

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

  val io = IO(new Bundle {
    val keys = Input(new QarmaHWKeys)
    val inp = Flipped(Decoupled(new QarmaHWInput))
    val out = Decoupled(new QarmaHWOutput)
  })

  io.inp.ready := true.B
  io.out.valid := io.inp.valid
  io.out.bits.ctext := io.inp.bits.ptext
  io.out.bits.tweak := io.inp.bits.tweak
}
