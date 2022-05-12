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
  */
class QarmaHW extends Module {

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
