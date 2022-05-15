package edu.gatech.cs3220.spring2022.m68k.qarma64

import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation
import edu.gatech.cs3220.spring2022.m68k.qarma64.util.LFSR

/** Represents a single block in Qarma
  *
  * Composed of exactly 16 cells. Also provides functions to permute, map, LFSR,
  * and mix the cells.
  *
  * @param cells
  *   The cells of the block
  */
case class Block(val cells: Seq[Cell]) {

  // Must have exactly sixteen cells
  if (cells.length != 16)
    throw new IllegalArgumentException("Blocks must have sixteen cells")

  /** XORs two blocks cell-wise */
  def ^(that: Block): Block = Block(
    this.cells.zip(that.cells).map { case (a, b) => a ^ b }
  )

  /** Permute the cells */
  def permute(s: Permutation): Block = Block((0 until 16).map { i =>
    this.cells(s(i))
  })

  /** Substitute the cells' values */
  def substitute(s: Permutation): Block = this.map(_.map(s.apply))

  /** Maps a function over all cells */
  private def map(f: (Cell) => Cell): Block = Block(this.cells.map(f))

  /** Applies an LFSR to the "special" cells */
  def shift(l: LFSR): Block = this.mapSpecial(l.apply)

  /** Maps a function over only the hardcoded "special" cells */
  private def mapSpecial(f: (Cell) => Cell): Block = Block(
    this.cells.zipWithIndex.map { case (c: Cell, i: Int) =>
      if (Seq(0, 1, 3, 4, 8, 11, 13) contains i) f(c) else c
    }
  )

  /** Multiply two blocks by treating them as matricies
    *
    * Sometimes, the internal state block is treated as a 4*4 matrix over the
    * ring R_4 = F_2[x] / x^4 - 1. Here, x satisifies x^4 == 1. Matrix
    * multiplication over this ring satisfies the usual rules for matrix
    * multiplication, with element-wise multiplication being [Cell.mulR] and
    * element-wise addition being XOR.
    *
    * The matrix's cells are stored in row major order. So, index 1 is the
    * element at row 0 column 1. This matrix is multiplied on the left.
    *
    * See https://eprint.iacr.org/2016/444.pdf Section 2.1
    *
    * @param that
    *   The matrix to multiply this with. That is multiplied on the right, and
    *   this is on the left.
    */
  def mulMatR(that: Block): Block = Block(
    (0 until 4).map { r =>
      (0 until 4).map { c =>
        (0 until 4)
          .map { i =>
            this.cells(4 * r + i) mulR that.cells(4 * i + c)
          }
          .fold(Cell(0))(_ ^ _)
      }
    }.flatten
  )

  /** Convert this Block to a `BigInt` */
  def toBigInt: BigInt = this.cells.reverse.zipWithIndex.map { case (c, i) =>
    BigInt(c.value) << (4 * i)
  }.sum
}

object Block {

  /** Construct a block for a sequence of bytes */
  def fromBytes(bytes: Seq[Byte]): Block = Block(
    bytes.map { b => Seq(Cell((b >> 4) & 0xf), Cell(b & 0xf)) }.flatten
  )

  /** Construct a block from a `BigInt` */
  def fromBigInt(n: BigInt): Block = {
    // Do error checking here, because we don't catch it later
    if (n > BigInt("ffffffffffffffff", 16))
      throw new IllegalArgumentException("Blocks must be less than 2^64")
    // Otherwise, we're good
    return Block(
      (15 to 0 by -1).map { i => Cell(((n >> (4 * i)) & 0xf).toInt) }
    )
  }
}
