package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import chisel3._
import chisel3.util.Cat

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
}
