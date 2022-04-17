package edu.gatech.cs3220.spring2022.m68k.qarma64

import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation

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
  def ^(that: Block): Block = new Block(
    this.cells.zip(that.cells).map { case (a, b) => a ^ b }
  )

  /** Maps a function over all cells */
  def map(f: (Cell) => Cell): Block = new Block(this.cells.map(f))

  /** Maps a function over only the hardcoded "special" cells */
  def mapSpecial(f: (Cell) => Cell): Block = new Block(
    this.cells.zipWithIndex.map { case (c: Cell, i: Int) =>
      if (Seq(0, 1, 3, 4, 8, 11, 13) contains i) f(c) else c
    }
  )

  /** Permute the cells */
  def permute(s: Permutation): Block = new Block((0 until 16).map { i =>
    this.cells(s(i))
  })
}

object Block {

  /** Construct a block for a sequence of bytes */
  def fromBytes(bytes: Seq[Byte]): Block = new Block(
    bytes.map((b) => Seq(new Cell((b >> 4) & 0xf), new Cell(b & 0xf))).flatten
  )
}
