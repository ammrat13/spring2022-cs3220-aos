package edu.gatech.cs3220.spring2022.m68k.qarma64

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CellTest extends AnyFlatSpec with Matchers {

  "A cell" should "be able to take values up to four bits" in {
    for (i <- 0 until 16) {
      withClue(s"Fails for value = ${i}.") {
        val dut = Cell(i)
        dut.value should equal(i)
      }
    }
  }

  it should "fail when the value given is out of range" in {
    withClue("Fails for value = 16.") {
      an[IllegalArgumentException] should be thrownBy { Cell(16) }
    }
    withClue("Fails for value = -1.") {
      an[IllegalArgumentException] should be thrownBy { Cell(-1) }
    }
  }

  "XORing two cells" should "give the expected results" in {
    for (i <- 0 until 16; j <- 0 until 16) {
      withClue(s"Fails for lhs = ${i} and rhs = ${j}") {
        val dutI = Cell(i)
        val dutJ = Cell(j)
        (dutI ^ dutJ).value should equal(i ^ j)
      }
    }
  }

  "Rotating a cell" should "rotate left correctly" in {
    // Test all zeros
    withClue("Fails for value = 0x0") {
      val dut = Cell(0x0)
      for (i <- 0 until 8) {
        (dut <<< i).value should equal(0x0)
      }
    }
    // Test all ones
    withClue("Fails for value = 0xf") {
      val dut = Cell(0xf)
      for (i <- 0 until 8) {
        (dut <<< i).value should equal(0xf)
      }
    }
    // Test a single one
    withClue("Fails for value = 0x1") {
      val dut = Cell(0x1)
      for (i <- 0 until 8) {
        (dut <<< i).value should equal(1 << (i % 4))
      }
    }
    // Test an alternating pattern
    withClue("Fails for value = 0x5") {
      val dut = Cell(0x5)
      for (i <- 0 until 8) {
        (dut <<< i).value should equal(if (i % 2 == 0) 0x5 else 0xa)
      }
    }
  }

  it should "rotate right correctly" in {
    // Test all zeros
    withClue("Fails for value = 0x0") {
      val dut = Cell(0x0)
      for (i <- 0 until 8) {
        (dut >>> i).value should equal(0x0)
      }
    }
    // Test all ones
    withClue("Fails for value = 0xf") {
      val dut = Cell(0xf)
      for (i <- 0 until 8) {
        (dut >>> i).value should equal(0xf)
      }
    }
    // Test a single one
    withClue("Fails for value = 0x1") {
      val dut = Cell(0x1)
      for (i <- 0 until 8) {
        (dut >>> i).value should equal(
          1 << (if (i % 4 == 0) 0 else (4 - i % 4))
        )
      }
    }
    // Test an alternating pattern
    withClue("Fails for value = 0x5") {
      val dut = Cell(0x5)
      for (i <- 0 until 8) {
        (dut >>> i).value should equal(if (i % 2 == 0) 0x5 else 0xa)
      }
    }
  }

  it should "do nothing when rotating by zero" in {
    for (i <- 0 until 16) {
      val dut = Cell(i)
      (dut >>> 0).value should equal(i)
      (dut <<< 0).value should equal(i)
    }
  }

  "Multiplying two cells" should "have zero take everything to zero" in {
    for (i <- 0 until 16) {
      (Cell(i) mulR Cell(0)).value should equal(0)
    }
  }

  it should "have one be the identity" in {
    for (i <- 0 until 16) {
      (Cell(i) mulR Cell(1)).value should equal(i)
    }
  }

  it should "commute" in {
    for (i <- 0 until 16) {
      for (j <- 0 until 16) {
        (Cell(i) mulR Cell(j)) should equal(Cell(j) mulR Cell(i))
      }
    }
  }

  it should "distribute over XOR" in {
    for (i <- 0 until 16) {
      for (j <- 0 until 16) {
        for (k <- 0 until 16) {
          (Cell(k) mulR (Cell(i) ^ Cell(j))) should equal(
            (Cell(k) mulR Cell(i)) ^ (Cell(k) mulR Cell(j))
          )
        }
      }
    }
  }

  it should "have powers of two correspond to rotations" in {
    for (i <- 0 until 4) {
      for (j <- 0 until 16) {
        (Cell(j) mulR Cell(1 << i)) should equal(Cell(j) <<< i)
      }
    }
  }
}
