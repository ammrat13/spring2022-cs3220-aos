package edu.gatech.cs3220.spring2022.m68k.qarma64.util

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class PermutationTest extends AnyFlatSpec with Matchers {

  "A permutation" should "accept valid representations" in {
    noException should be thrownBy (new Permutation(Seq.range(0, 16)))
  }

  it should "reject representations that are too short" in {
    an[IllegalArgumentException] should be thrownBy (new Permutation(
      Seq.range(0, 15)
    ))
  }

  it should "reject representations that are too long" in {
    an[IllegalArgumentException] should be thrownBy (new Permutation(
      Seq.range(0, 17)
    ))
  }

  it should "reject representations with indicies that are out of range" in {
    an[IllegalArgumentException] should be thrownBy (new Permutation(
      Seq.range(0, 15) ++ Seq(16)
    ))
    an[IllegalArgumentException] should be thrownBy (new Permutation(
      Seq(-1) ++ Seq.range(1, 16)
    ))
  }

  it should "reject representations with duplicates" in {
    an[IllegalArgumentException] should be thrownBy (new Permutation(
      Seq.range(0, 15) ++ Seq(14)
    ))
  }

  "Applying a permutation" should "return the result of that permutation" in {
    val dut = new Permutation(Seq.range(0, 16))
    for (i <- 0 until 16) {
      dut(i) should be(i)
    }
  }

  it should "fail when the index is out of range" in {
    val dut = new Permutation(Seq.range(0, 16))
    an[IndexOutOfBoundsException] should be thrownBy (dut(16))
    an[IndexOutOfBoundsException] should be thrownBy (dut(-1))
  }

  "Inverting a permutation" should "give the correct inverse" in {
    val rand = new Random(10131885)
    val dut = new Permutation(rand.shuffle(Seq.range(0, 16)))
    for (i <- 0 until 16) {
      dut.inverse(dut(i)) should be(i)
      dut(dut.inverse(i)) should be(i)
    }
  }
}
