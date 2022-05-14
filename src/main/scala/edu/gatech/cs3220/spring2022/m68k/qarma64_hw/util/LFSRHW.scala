package edu.gatech.cs3220.spring2022.m68k.qarma64_hw.util

import chisel3._
import chisel3.util.Cat

import edu.gatech.cs3220.spring2022.m68k.qarma64.util.LFSR
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.CellHW

/** Hardware implementation of an LFSR
  *
  * SBoxes and PBoxes were simple enough to implement inline, but LFSRs are
  * complex enough to warrant this module.
  *
  * @param l
  *   The LFSR to use as a base
  */
class LFSRHW(l: LFSR) {

  /** Shift a single cell right */
  private def shiftRight(c: CellHW): CellHW = {
    // Error checking
    require(l.dir == LFSR.RIGHT, "Incorrect direction for shiftRight")

    // Compute the bit we have to shift
    val new_b = Wire(Bool())
    new_b := l.terms.map(c.bits(_)).reduce(_ ^ _)
    // Return
    val ret = Wire(new CellHW)
    ret.bits := Cat(new_b, c.bits(3, 1))
    return ret
  }

  /** Shift a single cell left */
  private def shiftLeft(c: CellHW): CellHW = {
    // Error checking
    require(l.dir == LFSR.LEFT, "Incorrect direction for shiftLeft")

    // Compute the bit we have to shift
    val new_b = Wire(Bool())
    new_b := l.terms.map(c.bits(_)).reduce(_ ^ _)
    // Return
    val ret = Wire(new CellHW)
    ret.bits := Cat(c.bits(2, 0), new_b)
    return ret
  }

  /** Shift a cell */
  def shift(c: CellHW): CellHW = l.dir match {
    case LFSR.RIGHT => this.shiftRight(c)
    case LFSR.LEFT  => this.shiftLeft(c)
  }
}
