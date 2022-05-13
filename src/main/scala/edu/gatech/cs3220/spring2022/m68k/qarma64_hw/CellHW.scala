package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import chisel3._
import chisel3.util.Cat

/** Wrapper class around a 4-bit value */
class CellHW(val bits: UInt) {

  // Check that it's the right width
  require(this.bits.widthOption == Some(4), "Cells should be four bits wide")

  /** Construct with empty */
  def this() = this(Wire(UInt(4.W)))

  /** Exclusive-or between cells */
  def ^(that: CellHW): CellHW = {
    val ret = new CellHW
    ret.bits := this.bits ^ that.bits
    return ret
  }

  /** Rotate-left */
  def <<<(amt: Int): CellHW = {
    // Error checking
    if(amt < 0)
      return this >>> (-amt)

    // Get the actual amount
    val a = amt % 4
    // More error checking on the actual amount
    if(a == 0)
      return this

    // Do the shift
    val ret = new CellHW
    ret.bits := Cat((3 to 0 by -1).map { i => this.bits((i - a + 4) % 4) })
    return ret
  }

  /** Rotate-right */
  def >>>(amt: Int): CellHW = {
    // Error checking
    if(amt < 0)
      return this <<< (-amt)

    // Get the actual amount
    val a = amt % 4
    // More error checking on the actual amount
    if(a == 0)
      return this

    // Do the shift
    val ret = new CellHW
    ret.bits := Cat((3 to 0 by -1).map { i => this.bits((i + a) % 4) })
    return ret
  }
}
