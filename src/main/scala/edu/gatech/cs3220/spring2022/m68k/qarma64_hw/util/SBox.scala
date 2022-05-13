package edu.gatech.cs3220.spring2022.m68k.qarma64_hw.util

import chisel3._

import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.CellHW
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.BlockHW

/** Class describing a substitution box
  *
  * @param s
  *   The substitution to use
  */
class SBox(s: Permutation) {

  private val lut = VecInit(s.repr.map { _.U })

  /** Substitute a single cell */
  def substitute(c: CellHW): CellHW = {
    val ret = Wire(new CellHW)
    ret.bits := lut(c.bits)
    return ret
  }

  /** Substitute an entire block */
  def substitute(b: BlockHW): BlockHW = {
    val ret = Wire(new BlockHW)
    for (i <- 0 to 15)
      ret.cells(i) := this.substitute(b.cells(i))
    return ret
  }
}
