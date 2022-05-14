package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import chisel3._

import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation

/** Wrapper around sixteen [Cell]s */
class BlockHW extends Bundle {

  /** The cells laid out in row-major order */
  val cells = Vec(16, new CellHW)

  /** Exclusive-or two blocks cell-wise */
  def ^(that: BlockHW): BlockHW = {
    val ret = Wire(new BlockHW)
    for (i <- 0 to 15)
      ret.cells(i) := this.cells(i) ^ that.cells(i)
    return ret
  }

  /** Permute the cells */
  def permute(p: Permutation): BlockHW = {
    val ret = Wire(new BlockHW)
    for (i <- 0 to 15)
      ret.cells(i) := this.cells(p(i))
    return ret
  }

  /** Substitute the cells' values */
  def substitute(s: Permutation): BlockHW = {
    val ret = Wire(new BlockHW)
    for (i <- 0 to 15)
      ret.cells(i) := this.cells(i).substitute(s)
    return ret
  }
}
