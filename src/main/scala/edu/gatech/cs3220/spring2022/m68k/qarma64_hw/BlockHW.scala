package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import chisel3._

import edu.gatech.cs3220.spring2022.m68k.qarma64.Block
import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation
import edu.gatech.cs3220.spring2022.m68k.qarma64.util.LFSR

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

  /** Apply the LFSR to the "special" cells */
  def shift(l: LFSR): BlockHW = {
    val ret = Wire(new BlockHW)
    for (i <- 0 to 15)
      if (Seq(0, 1, 3, 4, 8, 11, 13) contains i)
        ret.cells(i) := this.cells(i).shift(l)
      else
        ret.cells(i) := this.cells(i)
    return ret
  }

  /** Multiply with a matrix
    *
    * Remember that blocks are sometimes treated as 4*4 matricies containing
    * elements in R_4 = F_2[x] / x^4 - 1. This method generates hardware to
    * multiply by a hard-coded matrix on the left.
    *
    * Note that the parameter is multiplied on the left of this. This is
    * multiplied with the parameter on the right.
    *
    * @param m
    *   The matrix to multiply by
    */
  def mulMatR(m: Block): BlockHW = {
    val ret = Wire(new BlockHW)

    for (r <- 0 to 3) {
      // Get the appropriate row of the input
      val dotRow = (0 to 3).map { i => m.cells(4 * r + i) }

      for (c <- 0 to 3) {
        // Get the appropriate column of the output
        val dotCol = (0 to 3).map { i => this.cells(4 * i + c) }

        // Only take the indicies that aren't guaranteed to be zero under
        // multiplication
        // Otherwise, the normal dot product
        val dotRes = (0 to 3)
          .filter { i => dotRow(i).value != 0 }
          .map { i => dotCol(i) mulR dotRow(i) }
          .reduce(_ ^ _)

        // Assign the result
        ret.cells(4 * r + c) := dotRes
      }
    }

    return ret
  }
}
