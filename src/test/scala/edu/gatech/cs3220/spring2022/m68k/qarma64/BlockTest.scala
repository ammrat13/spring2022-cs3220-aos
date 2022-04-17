package edu.gatech.cs3220.spring2022.m68k.qarma64

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BlockTest extends AnyFlatSpec with Matchers {

  "A block" should "take sixteen cells as input" in {
    withClue("Can't take in exactly sixteen cells.") {
      val dut = new Block(Seq.fill(16)(new Cell(0)))
      dut.cells should equal(Seq.fill(16)(new Cell(0)))
    }
    withClue("Can take in fewer than sixteen cells.") {
      an[IllegalArgumentException] should be thrownBy (new Block(
        Seq.fill(15)(new Cell(0))
      ))
    }
    withClue("Can take in more than sixteen cells.") {
      an[IllegalArgumentException] should be thrownBy (new Block(
        Seq.fill(17)(new Cell(0))
      ))
    }
  }

  "Mapping over a block" should "apply the input function to all cells" in {
    val dut = new Block(Seq.fill(16)(new Cell(0)))
    val fun = (c: Cell) => new Cell(1)
    dut.map(fun).cells should equal(Seq.fill(16)(new Cell(1)))
  }

  "Mapping over the special cells in a block" should "apply the input function to only those cells" in {
    val dut = new Block(Seq.fill(16)(new Cell(0)))
    val fun = (c: Cell) => new Cell(1)
    dut.mapSpecial(fun).cells should equal(
      Seq(
        new Cell(1),
        new Cell(1),
        new Cell(0),
        new Cell(1),
        new Cell(1),
        new Cell(0),
        new Cell(0),
        new Cell(0),
        new Cell(1),
        new Cell(0),
        new Cell(0),
        new Cell(1),
        new Cell(0),
        new Cell(1),
        new Cell(0),
        new Cell(0)
      )
    )
  }

  "XORing two blocks" should "give the same result as XORing the individual cells" in {
    val lhs = new Block(Seq.fill(16)(new Cell(0x5)))
    val rhs = new Block(Seq.fill(16)(new Cell(0xa)))
    (lhs ^ rhs).cells should equal(Seq.fill(16)(new Cell(0xf)))
  }
}
