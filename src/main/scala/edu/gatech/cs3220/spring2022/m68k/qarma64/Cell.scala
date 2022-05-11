package edu.gatech.cs3220.spring2022.m68k.qarma64

/** A single cell in a block
  *
  * Each cell is m = 4 bits long. All operations will truncate.
  *
  * @param value
  *   The value of this cell
  */
case class Cell(val value: Int) {

  // Validation on the value
  if (value < 0 || value > 15)
    throw new IllegalArgumentException("Cell's value must be four bits")

  /** Exclusive-or between cells */
  def ^(that: Cell): Cell = Cell(this.value ^ that.value)

  /** Rotate-left */
  def <<<(amt: Int): Cell = {
    // Check for rotate-right
    if (amt < 0)
      return this >>> (-amt)
    // Otherwise, truncate to four bits of rotation
    val actAmt = amt % 4
    // Compute parts, truncate, and return
    val lhs = this.value << actAmt
    val rhs = this.value >> (4 - actAmt)
    return Cell((lhs | rhs) & 0xf)
  }

  /** Rotate-right */
  def >>>(amt: Int): Cell = {
    // Check for rotate-left
    if (amt < 0)
      return this <<< (-amt)
    // Otherwise, truncate to four bits of rotation
    val actAmt = amt % 4
    // Compute parts, truncate, and return
    val lhs = this.value << (4 - actAmt)
    val rhs = this.value >> actAmt
    return Cell((lhs | rhs) & 0xf)
  }

  /** Map */
  def map(f: (Int) => Int): Cell = Cell(f(this.value))

  /** Multiplication over the ring R
    *
    * Cells in the internal state are sometimes treated as elements of the ring
    * R_4 = F_2[x] / x^4 - 1. Here, x satisfies that x^4 == 1. Each of the four
    * bits are treated as a coefficient in front of a power of x, with bit 0
    * being the x^0 coefficient, bit 1 being x^1, and so on.
    *
    * Addition over that ring is just XOR. Multiplication is this operation.
    *
    * See https://eprint.iacr.org/2016/444.pdf Section 3.1.1
    *
    * @param that
    *   The cell to multiply with this
    */
  def mulR(that: Cell): Cell = {
    // Extract all the bits of this
    val x0 = (this.value & (1 << 0)) >> 0
    val x1 = (this.value & (1 << 1)) >> 1
    val x2 = (this.value & (1 << 2)) >> 2
    val x3 = (this.value & (1 << 3)) >> 3
    // Extract all the bits of that
    val y0 = (that.value & (1 << 0)) >> 0
    val y1 = (that.value & (1 << 1)) >> 1
    val y2 = (that.value & (1 << 2)) >> 2
    val y3 = (that.value & (1 << 3)) >> 3

    // Compute all the bits of the result
    val r0 = (x0 & y0) ^ (x1 & y3) ^ (x2 & y2) ^ (x3 & y1)
    val r1 = (x0 & y1) ^ (x1 & y0) ^ (x2 & y3) ^ (x3 & y2)
    val r2 = (x0 & y2) ^ (x1 & y1) ^ (x2 & y0) ^ (x3 & y3)
    val r3 = (x0 & y3) ^ (x1 & y2) ^ (x2 & y1) ^ (x3 & y0)
    // Merge them together
    return Cell((r3 << 3) | (r2 << 2) | (r1 << 1) | (r0 << 0))
  }
}
