package edu.gatech.cs3220.spring2022.m68k.qarma64.util

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import edu.gatech.cs3220.spring2022.m68k.qarma64.Cell
import edu.gatech.cs3220.spring2022.m68k.qarma64.Qarma

class LFSRTest extends AnyFlatSpec with Matchers {

  "An LFSR" should "accept valid representations" in {
    noException should be thrownBy (LFSR(Set(0, 2), LFSR.RIGHT))
    noException should be thrownBy (LFSR(Set(3, 1), LFSR.LEFT))
  }

  it should "reject representation with indicies out of range" in {
    an[IllegalArgumentException] should be thrownBy (LFSR(
      Set(0, -1),
      LFSR.RIGHT
    ))
    an[IllegalArgumentException] should be thrownBy (LFSR(
      Set(0, 4),
      LFSR.RIGHT
    ))
    an[IllegalArgumentException] should be thrownBy (LFSR(
      Set(3, -1),
      LFSR.LEFT
    ))
    an[IllegalArgumentException] should be thrownBy (LFSR(Set(3, 4), LFSR.LEFT))
  }

  it should "reject representations without the most/least significant bit" in {
    an[IllegalArgumentException] should be thrownBy (LFSR(Set(1), LFSR.RIGHT))
    an[IllegalArgumentException] should be thrownBy (LFSR(Set(1), LFSR.LEFT))
  }

  "Applying an LFSR" should "shift right correctly" in {
    val dut = LFSR(Set(0, 2), LFSR.RIGHT)
    dut(Cell(0x1)) should be(Cell(0x8))
    dut(Cell(0x5)) should be(Cell(0x2))
    dut(Cell(0x4)) should be(Cell(0xa))
  }

  it should "shift left correctly" in {
    val dut = LFSR(Set(3, 2), LFSR.LEFT)
    dut(Cell(0x9)) should be(Cell(0x3))
    dut(Cell(0xd)) should be(Cell(0xa))
    dut(Cell(0x2)) should be(Cell(0x4))
  }

  "Inverting the Qarma LFSR" should "give the correct inverse" in {
    val dut = Qarma.TWEAK_UPDATE_LFSR
    for (i <- 0 until 16) {
      dut.inv(dut(Cell(i))) should be(Cell(i))
      dut(dut.inv(Cell(i))) should be(Cell(i))
    }
  }
}
