package edu.gatech.cs3220.spring2022.m68k.qarma64.util

import scala.collection.mutable.ArrayBuffer

import edu.gatech.cs3220.spring2022.m68k.qarma64.Cell

/** An LFSR to be applied to a single [Cell].
  *
  * It can have shifts going to the right or to the left. The least/most
  * signigicant bit is always included in the XOR. The parameter specifies which
  * other bits should be included. They're one indexed with zero being the least
  * significant bit.
  *
  * @param terms
  *   A set of the terms to XOR to get the new most/least significant bit
  * @param dir
  *   The direction of the shift
  */
case class LFSR(terms: Set[Int], dir: LFSR.Direction = LFSR.RIGHT) {

  // Terms must have the least/most significant bit
  if (dir == LFSR.RIGHT && !(terms contains 0))
    throw new IllegalArgumentException("Must include LSB in XOR")
  if (dir == LFSR.LEFT && !(terms contains 3))
    throw new IllegalArgumentException("Must include MSB in XOR")

  // Terms must be valid bit numbers
  if (terms.exists { n => n < 0 || 3 < n })
    throw new IllegalArgumentException("All terms must specify bits")

  /** Apply the LFSR to a cell */
  def apply(c: Cell): Cell = {
    // Extract the individual bits
    // Careful: everything here is LSB first
    var bits = ArrayBuffer[Boolean]()
    for (i <- 0 until 4) {
      bits += (c.value & (1 << i)) != 0
    }

    // Get the value of the bit we want to put
    var pad = false
    for (i <- this.terms)
      pad ^= bits(i)

    // Compute the new bit vector
    val outBits = this.dir match {
      case LFSR.RIGHT => bits.slice(1, 4) ++ Seq(pad)
      case LFSR.LEFT  => Seq(pad) ++ bits.slice(0, 3)
    }

    // Compute the new value as an integer
    // Because its LSB first, we have to reverse
    var out = 0
    for (b <- outBits.reverse)
      out = 2 * out + (if (b) 1 else 0)

    // Return as a cell
    return Cell(out)
  }

  /** Inverse of this LFSR */
  lazy val inv: LFSR = LFSR(
    if (this.dir == LFSR.RIGHT) {
      this.terms.map(t => if (t == 0) 3 else (t - 1))
    } else {
      this.terms.map(t => if (t == 3) 0 else (t + 1))
    },
    !this.dir
  )
}

object LFSR {

  /** Which way the LFSR should shift */
  sealed abstract class Direction {
    def unary_! : Direction
  }
  case object LEFT extends Direction {
    def unary_! : Direction = RIGHT
  }
  case object RIGHT extends Direction {
    def unary_! : Direction = LEFT
  }
}
