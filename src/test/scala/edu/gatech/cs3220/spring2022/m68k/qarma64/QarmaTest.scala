package edu.gatech.cs3220.spring2022.m68k.qarma64

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class QarmaTest extends AnyFlatSpec with Matchers {

  "Qarma64" should "give the correct output on test vectors (s = s0, rounds = 5)" in {
    val dut = Qarma(
      rounds = 5,
      k0 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      k1 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      w0 = Block.fromBigInt(BigInt("84be85ce9804e94b", 16)),
      w1 = Block.fromBigInt(BigInt("c25f42e74c0274a4", 16)),
      sBox = Qarma.CELL_SBOX_0
    )

    val exp = Block.fromBigInt(BigInt("3ee99a6c82af0c38", 16))
    val act = dut(
      p = Block.fromBigInt(BigInt("fb623599da6e8127", 16)),
      t = Block.fromBigInt(BigInt("477d469dec0b8762", 16))
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s1, rounds = 5)" in {
    val dut = Qarma(
      rounds = 5,
      k0 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      k1 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      w0 = Block.fromBigInt(BigInt("84be85ce9804e94b", 16)),
      w1 = Block.fromBigInt(BigInt("c25f42e74c0274a4", 16)),
      sBox = Qarma.CELL_SBOX_1
    )

    val exp = Block.fromBigInt(BigInt("544b0ab95bda7c3a", 16))
    val act = dut(
      p = Block.fromBigInt(BigInt("fb623599da6e8127", 16)),
      t = Block.fromBigInt(BigInt("477d469dec0b8762", 16))
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s2, rounds = 5)" in {
    val dut = Qarma(
      rounds = 5,
      k0 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      k1 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      w0 = Block.fromBigInt(BigInt("84be85ce9804e94b", 16)),
      w1 = Block.fromBigInt(BigInt("c25f42e74c0274a4", 16)),
      sBox = Qarma.CELL_SBOX_2
    )

    val exp = Block.fromBigInt(BigInt("c003b93999b33765", 16))
    val act = dut(
      p = Block.fromBigInt(BigInt("fb623599da6e8127", 16)),
      t = Block.fromBigInt(BigInt("477d469dec0b8762", 16))
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s0, rounds = 6)" in {
    val dut = Qarma(
      rounds = 6,
      k0 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      k1 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      w0 = Block.fromBigInt(BigInt("84be85ce9804e94b", 16)),
      w1 = Block.fromBigInt(BigInt("c25f42e74c0274a4", 16)),
      sBox = Qarma.CELL_SBOX_0
    )

    val exp = Block.fromBigInt(BigInt("9f5c41ec525603c9", 16))
    val act = dut(
      p = Block.fromBigInt(BigInt("fb623599da6e8127", 16)),
      t = Block.fromBigInt(BigInt("477d469dec0b8762", 16))
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s1, rounds = 6)" in {
    val dut = Qarma(
      rounds = 6,
      k0 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      k1 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      w0 = Block.fromBigInt(BigInt("84be85ce9804e94b", 16)),
      w1 = Block.fromBigInt(BigInt("c25f42e74c0274a4", 16)),
      sBox = Qarma.CELL_SBOX_1
    )

    val exp = Block.fromBigInt(BigInt("a512dd1e4e3ec582", 16))
    val act = dut(
      p = Block.fromBigInt(BigInt("fb623599da6e8127", 16)),
      t = Block.fromBigInt(BigInt("477d469dec0b8762", 16))
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s2, rounds = 6)" in {
    val dut = Qarma(
      rounds = 6,
      k0 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      k1 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      w0 = Block.fromBigInt(BigInt("84be85ce9804e94b", 16)),
      w1 = Block.fromBigInt(BigInt("c25f42e74c0274a4", 16)),
      sBox = Qarma.CELL_SBOX_2
    )

    val exp = Block.fromBigInt(BigInt("270a787275c48d10", 16))
    val act = dut(
      p = Block.fromBigInt(BigInt("fb623599da6e8127", 16)),
      t = Block.fromBigInt(BigInt("477d469dec0b8762", 16))
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s0, rounds = 7)" in {
    val dut = Qarma(
      rounds = 7,
      k0 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      k1 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      w0 = Block.fromBigInt(BigInt("84be85ce9804e94b", 16)),
      w1 = Block.fromBigInt(BigInt("c25f42e74c0274a4", 16)),
      sBox = Qarma.CELL_SBOX_0
    )

    val exp = Block.fromBigInt(BigInt("bcaf6c89de930765", 16))
    val act = dut(
      p = Block.fromBigInt(BigInt("fb623599da6e8127", 16)),
      t = Block.fromBigInt(BigInt("477d469dec0b8762", 16))
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s1, rounds = 7)" in {
    val dut = Qarma(
      rounds = 7,
      k0 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      k1 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      w0 = Block.fromBigInt(BigInt("84be85ce9804e94b", 16)),
      w1 = Block.fromBigInt(BigInt("c25f42e74c0274a4", 16)),
      sBox = Qarma.CELL_SBOX_1
    )

    val exp = Block.fromBigInt(BigInt("edf67ff370a483f2", 16))
    val act = dut(
      p = Block.fromBigInt(BigInt("fb623599da6e8127", 16)),
      t = Block.fromBigInt(BigInt("477d469dec0b8762", 16))
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s2, rounds = 7)" in {
    val dut = Qarma(
      rounds = 7,
      k0 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      k1 = Block.fromBigInt(BigInt("ec2802d4e0a488e9", 16)),
      w0 = Block.fromBigInt(BigInt("84be85ce9804e94b", 16)),
      w1 = Block.fromBigInt(BigInt("c25f42e74c0274a4", 16)),
      sBox = Qarma.CELL_SBOX_2
    )

    val exp = Block.fromBigInt(BigInt("5c06a7501b63b2fd", 16))
    val act = dut(
      p = Block.fromBigInt(BigInt("fb623599da6e8127", 16)),
      t = Block.fromBigInt(BigInt("477d469dec0b8762", 16))
    )

    act should be(exp)
  }
}
