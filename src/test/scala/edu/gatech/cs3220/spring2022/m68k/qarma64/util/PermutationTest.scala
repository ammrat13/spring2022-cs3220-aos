package edu.gatech.cs3220.spring2022.m68k.qarma64.util

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import edu.gatech.cs3220.spring2022.m68k.qarma64.Qarma

class PermutationTest extends AnyFlatSpec with Matchers {

  "A permutation" should "accept valid representations" in {
    noException should be thrownBy (Permutation(Seq.range(0, 16)))
  }

  it should "reject representations that are too short" in {
    an[IllegalArgumentException] should be thrownBy (Permutation(
      Seq.range(0, 15)
    ))
  }

  it should "reject representations that are too long" in {
    an[IllegalArgumentException] should be thrownBy (Permutation(
      Seq.range(0, 17)
    ))
  }

  it should "reject representations with indicies that are out of range" in {
    an[IllegalArgumentException] should be thrownBy (Permutation(
      Seq.range(0, 15) ++ Seq(16)
    ))
    an[IllegalArgumentException] should be thrownBy (Permutation(
      Seq(-1) ++ Seq.range(1, 16)
    ))
  }

  it should "reject representations with duplicates" in {
    an[IllegalArgumentException] should be thrownBy (Permutation(
      Seq.range(0, 15) ++ Seq(14)
    ))
  }

  "Applying a permutation" should "return the result of that permutation" in {
    val dut = Permutation(Seq.range(0, 16))
    for (i <- 0 until 16) {
      dut(i) should be(i)
    }
  }

  it should "fail when the index is out of range" in {
    val dut = Permutation(Seq.range(0, 16))
    an[IndexOutOfBoundsException] should be thrownBy (dut(16))
    an[IndexOutOfBoundsException] should be thrownBy (dut(-1))
  }

  "Inverting the Qarma permutations" should "give the correct inverses" in {
    val tests = Set(
      Qarma.CELL_PERMUTATION,
      Qarma.CELL_SBOX_0,
      Qarma.CELL_SBOX_1,
      Qarma.CELL_SBOX_2,
      Qarma.TWEAK_UPDATE_PERMUTATION
    )
    for (dut <- tests) {
      for (i <- 0 until 16) {
        dut.inv(dut(i)) should be(i)
        dut(dut.inv(i)) should be(i)
      }
    }
  }
}
