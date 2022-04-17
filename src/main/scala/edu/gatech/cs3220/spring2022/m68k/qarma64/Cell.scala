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
  def ^(that: Cell): Cell = new Cell(this.value ^ that.value)

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
    return new Cell((lhs | rhs) & 0xf)
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
    return new Cell((lhs | rhs) & 0xf)
  }
}
