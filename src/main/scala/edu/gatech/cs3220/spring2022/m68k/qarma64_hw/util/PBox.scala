package edu.gatech.cs3220.spring2022.m68k.qarma64_hw.util

import chisel3._

import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.BlockHW

/** Class describing a permutation box
  *
  * @param s
  *   The permutation to use
  */
class PBox(p: Permutation) {

  /** Permute a block */
  def permute(b: BlockHW): BlockHW = {
    val ret = Wire(new BlockHW)
    for (i <- 0 to 15)
      ret.cells(i) := b.cells(p(i))
    return ret
  }
}
