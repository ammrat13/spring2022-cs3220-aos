package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import chisel3._
import chisel3.util.Cat

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
}
