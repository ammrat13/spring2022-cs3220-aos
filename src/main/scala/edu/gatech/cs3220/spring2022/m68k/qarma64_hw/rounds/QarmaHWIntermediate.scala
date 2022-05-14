package edu.gatech.cs3220.spring2022.qarma64_hw.m68k.rounds

import chisel3._

import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.BlockHW

/** Bundle for passing intermediate values between rounds */
class QarmaHWIntermediate extends Bundle {

  val ptext = new BlockHW
  val ctext = new BlockHW

  val otweak = new BlockHW
  val ntweak = new BlockHW
}
