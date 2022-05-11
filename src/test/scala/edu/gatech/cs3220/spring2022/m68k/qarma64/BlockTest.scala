package edu.gatech.cs3220.spring2022.m68k.qarma64

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation

class BlockTest extends AnyFlatSpec with Matchers {

  "A block" should "take sixteen cells as input" in {
    withClue("Can't take in exactly sixteen cells.") {
      noException should be thrownBy (Block(Seq.fill(16)(Cell(0))))
    }
    withClue("Can take in fewer than sixteen cells.") {
      an[IllegalArgumentException] should be thrownBy (Block(
        Seq.fill(15)(Cell(0))
      ))
    }
    withClue("Can take in more than sixteen cells.") {
      an[IllegalArgumentException] should be thrownBy (Block(
        Seq.fill(17)(Cell(0))
      ))
    }
  }

  it should "be constructable from eight bytes" in {
    withClue("Can't take in exactly eight bytes.") {
      noException should be thrownBy (Block.fromBytes(Seq.fill(8)(0x00.toByte)))
    }
    withClue("Can take in fewer than eight bytes.") {
      an[IllegalArgumentException] should be thrownBy (Block.fromBytes(
        Seq.fill(7)(0x00.toByte)
      ))
    }
    withClue("Can take in more than eight bytes.") {
      an[IllegalArgumentException] should be thrownBy (Block.fromBytes(
        Seq.fill(9)(0x00.toByte)
      ))
    }
  }

  it should "be constructed in big-endian order" in {
    val dut = Block.fromBytes(
      Seq(0x01, 0x23, 0x45, 0x67, 0x89, 0xab, 0xcd, 0xef).map(_.toByte)
    )
    for (i <- 0 until 16) {
      dut.cells(i).value should be(i)
    }
  }

  "Mapping over the special cells in a block" should "apply the input function to only those cells" in {
    val dut = Block(Seq.fill(16)(Cell(0)))
    val fun = (c: Cell) => Cell(1)
    dut.mapSpecial(fun).cells should equal(
      Seq(
        Cell(1),
        Cell(1),
        Cell(0),
        Cell(1),
        Cell(1),
        Cell(0),
        Cell(0),
        Cell(0),
        Cell(1),
        Cell(0),
        Cell(0),
        Cell(1),
        Cell(0),
        Cell(1),
        Cell(0),
        Cell(0)
      )
    )
  }

  "XORing two blocks" should "give the same result as XORing the individual cells" in {
    val lhs = Block(Seq.fill(16)(Cell(0x5)))
    val rhs = Block(Seq.fill(16)(Cell(0xa)))
    (lhs ^ rhs).cells should equal(Seq.fill(16)(Cell(0xf)))
  }

  "Permuting a block" should "apply the permutation to the cells" in {
    val dut = Block(Seq.range(0, 16).map(Cell(_)))
    val s = Permutation(Seq.range(15, -1, -1))
    for (i <- 0 until 16) {
      dut.permute(s).cells(i).value should be(15 - i)
    }
  }

  "Substituting a block" should "substitute each cell with the value from the permutation" in {
    val dut = Block(Seq.range(0, 16).map(Cell(_)))
    val s = Permutation(Seq.range(15, -1, -1))
    for (i <- 0 until 16) {
      dut.substitute(s).cells(i).value should be(15 - i)
    }
  }
}
