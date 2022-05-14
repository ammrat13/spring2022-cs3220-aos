package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import chisel3._
import chisel3.util.Cat

import edu.gatech.cs3220.spring2022.m68k.qarma64.Cell
import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation
import edu.gatech.cs3220.spring2022.m68k.qarma64.util.LFSR
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.util.LFSRHW

/** Wrapper class around a 4-bit value */
class CellHW extends Bundle {

  /** The value this Cell owns */
  val bits = UInt(4.W)

  /** Exclusive-or between cells */
  def ^(that: CellHW): CellHW = {
    val ret = Wire(new CellHW)
    ret.bits := this.bits ^ that.bits
    return ret
  }

  /** Rotate-left */
  def <<<(amt: Int): CellHW = {
    // Error checking
    if (amt < 0)
      return this >>> (-amt)

    // Get the actual amount
    val a = amt % 4
    // More error checking on the actual amount
    if (a == 0)
      return this

    // Do the shift
    val ret = Wire(new CellHW)
    ret.bits := Cat((3 to 0 by -1).map { i => this.bits((i - a + 4) % 4) })
    return ret
  }

  /** Rotate-right */
  def >>>(amt: Int): CellHW = {
    // Error checking
    if (amt < 0)
      return this <<< (-amt)

    // Get the actual amount
    val a = amt % 4
    // More error checking on the actual amount
    if (a == 0)
      return this

    // Do the shift
    val ret = Wire(new CellHW)
    ret.bits := Cat((3 to 0 by -1).map { i => this.bits((i + a) % 4) })
    return ret
  }

  /** Substitute */
  def substitute(s: Permutation): CellHW = {
    // Create the LUT for the permutation
    val lut = VecInit(s.repr.map { _.U })
    // Do the substitution
    val ret = Wire(new CellHW)
    ret.bits := lut(this.bits)
    return ret
  }

  /** Shift with an LFSR */
  def shift(l: LFSR): CellHW = new LFSRHW(l).shift(this)

  /** Multiply with a constant [Cell] over `R_4`
    *
    * Remember that cells are sometimes treated as elements of the ring R_4 =
    * F_2[x] / x^4 - 1. This method generates the hardware to multiply this cell
    * by a constant.
    *
    * @param m
    *   The constant to multiply by
    */
  def mulR(m: Cell): CellHW = {

    // Handle the trivial case
    if (m.value == 0)
      return CellHWInit(0.U(4.W))

    // Generate all the rotations, filtering only the ones with a one
    // XOR them all together
    return (0 until 3)
      .filter { i => (m.value & (1 << i)) != 0 }
      .map { i => this <<< i }
      .reduce(_ ^ _)
  }
}

object CellHWInit {

  /** Create a [CellHW] from a literal */
  def apply(lit: UInt): CellHW = {
    val ret = Wire(new CellHW)
    ret.bits := lit
    return ret
  }
}
