package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import chisel3._

/** Wrapper around sixteen [Cell]s */
class BlockHW extends Bundle {

  /** The cells laid out in a 2D Vec */
  val cells = Vec(4, Vec(4, new CellHW))
}
