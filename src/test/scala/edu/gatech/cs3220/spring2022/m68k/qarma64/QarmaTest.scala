package edu.gatech.cs3220.spring2022.m68k.qarma64

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class QarmaTest extends AnyFlatSpec with Matchers {

  "Qarma64" should "give the correct output on test vectors (s = s0, rounds = 5)" in {
    val dut = Qarma(
      rounds = 5,
      k0 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      k1 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      w0 = Block.fromBytes(
        Seq(0x84, 0xbe, 0x85, 0xce, 0x98, 0x04, 0xe9, 0x4b).map(_.toByte)
      ),
      w1 = Block.fromBytes(
        Seq(0xc2, 0x5f, 0x42, 0xe7, 0x4c, 0x02, 0x74, 0xa4).map(_.toByte)
      ),
      s = Qarma.CELL_SBOX_0
    )

    val exp = Block.fromBytes(
      Seq(0x3e, 0xe9, 0x9a, 0x6c, 0x82, 0xaf, 0x0c, 0x38).map(_.toByte)
    )
    val act = dut(
      p = Block.fromBytes(
        Seq(0xfb, 0x62, 0x35, 0x99, 0xda, 0x6e, 0x81, 0x27).map(_.toByte)
      ),
      t = Block.fromBytes(
        Seq(0x47, 0x7d, 0x46, 0x9d, 0xec, 0x0b, 0x87, 0x62).map(_.toByte)
      )
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s1, rounds = 5)" in {
    val dut = Qarma(
      rounds = 5,
      k0 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      k1 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      w0 = Block.fromBytes(
        Seq(0x84, 0xbe, 0x85, 0xce, 0x98, 0x04, 0xe9, 0x4b).map(_.toByte)
      ),
      w1 = Block.fromBytes(
        Seq(0xc2, 0x5f, 0x42, 0xe7, 0x4c, 0x02, 0x74, 0xa4).map(_.toByte)
      ),
      s = Qarma.CELL_SBOX_1
    )

    val exp = Block.fromBytes(
      Seq(0x54, 0x4b, 0x0a, 0xb9, 0x5b, 0xda, 0x7c, 0x3a).map(_.toByte)
    )
    val act = dut(
      p = Block.fromBytes(
        Seq(0xfb, 0x62, 0x35, 0x99, 0xda, 0x6e, 0x81, 0x27).map(_.toByte)
      ),
      t = Block.fromBytes(
        Seq(0x47, 0x7d, 0x46, 0x9d, 0xec, 0x0b, 0x87, 0x62).map(_.toByte)
      )
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s2, rounds = 5)" in {
    val dut = Qarma(
      rounds = 5,
      k0 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      k1 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      w0 = Block.fromBytes(
        Seq(0x84, 0xbe, 0x85, 0xce, 0x98, 0x04, 0xe9, 0x4b).map(_.toByte)
      ),
      w1 = Block.fromBytes(
        Seq(0xc2, 0x5f, 0x42, 0xe7, 0x4c, 0x02, 0x74, 0xa4).map(_.toByte)
      ),
      s = Qarma.CELL_SBOX_2
    )

    val exp = Block.fromBytes(
      Seq(0xc0, 0x03, 0xb9, 0x39, 0x99, 0xb3, 0x37, 0x65).map(_.toByte)
    )
    val act = dut(
      p = Block.fromBytes(
        Seq(0xfb, 0x62, 0x35, 0x99, 0xda, 0x6e, 0x81, 0x27).map(_.toByte)
      ),
      t = Block.fromBytes(
        Seq(0x47, 0x7d, 0x46, 0x9d, 0xec, 0x0b, 0x87, 0x62).map(_.toByte)
      )
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s0, rounds = 6)" in {
    val dut = Qarma(
      rounds = 6,
      k0 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      k1 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      w0 = Block.fromBytes(
        Seq(0x84, 0xbe, 0x85, 0xce, 0x98, 0x04, 0xe9, 0x4b).map(_.toByte)
      ),
      w1 = Block.fromBytes(
        Seq(0xc2, 0x5f, 0x42, 0xe7, 0x4c, 0x02, 0x74, 0xa4).map(_.toByte)
      ),
      s = Qarma.CELL_SBOX_0
    )

    val exp = Block.fromBytes(
      Seq(0x9f, 0x5c, 0x41, 0xec, 0x52, 0x56, 0x03, 0xc9).map(_.toByte)
    )
    val act = dut(
      p = Block.fromBytes(
        Seq(0xfb, 0x62, 0x35, 0x99, 0xda, 0x6e, 0x81, 0x27).map(_.toByte)
      ),
      t = Block.fromBytes(
        Seq(0x47, 0x7d, 0x46, 0x9d, 0xec, 0x0b, 0x87, 0x62).map(_.toByte)
      )
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s1, rounds = 6)" in {
    val dut = Qarma(
      rounds = 6,
      k0 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      k1 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      w0 = Block.fromBytes(
        Seq(0x84, 0xbe, 0x85, 0xce, 0x98, 0x04, 0xe9, 0x4b).map(_.toByte)
      ),
      w1 = Block.fromBytes(
        Seq(0xc2, 0x5f, 0x42, 0xe7, 0x4c, 0x02, 0x74, 0xa4).map(_.toByte)
      ),
      s = Qarma.CELL_SBOX_1
    )

    val exp = Block.fromBytes(
      Seq(0xa5, 0x12, 0xdd, 0x1e, 0x4e, 0x3e, 0xc5, 0x82).map(_.toByte)
    )
    val act = dut(
      p = Block.fromBytes(
        Seq(0xfb, 0x62, 0x35, 0x99, 0xda, 0x6e, 0x81, 0x27).map(_.toByte)
      ),
      t = Block.fromBytes(
        Seq(0x47, 0x7d, 0x46, 0x9d, 0xec, 0x0b, 0x87, 0x62).map(_.toByte)
      )
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s2, rounds = 6)" in {
    val dut = Qarma(
      rounds = 6,
      k0 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      k1 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      w0 = Block.fromBytes(
        Seq(0x84, 0xbe, 0x85, 0xce, 0x98, 0x04, 0xe9, 0x4b).map(_.toByte)
      ),
      w1 = Block.fromBytes(
        Seq(0xc2, 0x5f, 0x42, 0xe7, 0x4c, 0x02, 0x74, 0xa4).map(_.toByte)
      ),
      s = Qarma.CELL_SBOX_2
    )

    val exp = Block.fromBytes(
      Seq(0x27, 0x0a, 0x78, 0x72, 0x75, 0xc4, 0x8d, 0x10).map(_.toByte)
    )
    val act = dut(
      p = Block.fromBytes(
        Seq(0xfb, 0x62, 0x35, 0x99, 0xda, 0x6e, 0x81, 0x27).map(_.toByte)
      ),
      t = Block.fromBytes(
        Seq(0x47, 0x7d, 0x46, 0x9d, 0xec, 0x0b, 0x87, 0x62).map(_.toByte)
      )
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s0, rounds = 7)" in {
    val dut = Qarma(
      rounds = 7,
      k0 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      k1 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      w0 = Block.fromBytes(
        Seq(0x84, 0xbe, 0x85, 0xce, 0x98, 0x04, 0xe9, 0x4b).map(_.toByte)
      ),
      w1 = Block.fromBytes(
        Seq(0xc2, 0x5f, 0x42, 0xe7, 0x4c, 0x02, 0x74, 0xa4).map(_.toByte)
      ),
      s = Qarma.CELL_SBOX_0
    )

    val exp = Block.fromBytes(
      Seq(0xbc, 0xaf, 0x6c, 0x89, 0xde, 0x93, 0x07, 0x65).map(_.toByte)
    )
    val act = dut(
      p = Block.fromBytes(
        Seq(0xfb, 0x62, 0x35, 0x99, 0xda, 0x6e, 0x81, 0x27).map(_.toByte)
      ),
      t = Block.fromBytes(
        Seq(0x47, 0x7d, 0x46, 0x9d, 0xec, 0x0b, 0x87, 0x62).map(_.toByte)
      )
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s1, rounds = 7)" in {
    val dut = Qarma(
      rounds = 7,
      k0 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      k1 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      w0 = Block.fromBytes(
        Seq(0x84, 0xbe, 0x85, 0xce, 0x98, 0x04, 0xe9, 0x4b).map(_.toByte)
      ),
      w1 = Block.fromBytes(
        Seq(0xc2, 0x5f, 0x42, 0xe7, 0x4c, 0x02, 0x74, 0xa4).map(_.toByte)
      ),
      s = Qarma.CELL_SBOX_1
    )

    val exp = Block.fromBytes(
      Seq(0xed, 0xf6, 0x7f, 0xf3, 0x70, 0xa4, 0x83, 0xf2).map(_.toByte)
    )
    val act = dut(
      p = Block.fromBytes(
        Seq(0xfb, 0x62, 0x35, 0x99, 0xda, 0x6e, 0x81, 0x27).map(_.toByte)
      ),
      t = Block.fromBytes(
        Seq(0x47, 0x7d, 0x46, 0x9d, 0xec, 0x0b, 0x87, 0x62).map(_.toByte)
      )
    )

    act should be(exp)
  }

  it should "give the correct output on test vectors (s = s2, rounds = 7)" in {
    val dut = Qarma(
      rounds = 7,
      k0 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      k1 = Block.fromBytes(
        Seq(0xec, 0x28, 0x02, 0xd4, 0xe0, 0xa4, 0x88, 0xe9).map(_.toByte)
      ),
      w0 = Block.fromBytes(
        Seq(0x84, 0xbe, 0x85, 0xce, 0x98, 0x04, 0xe9, 0x4b).map(_.toByte)
      ),
      w1 = Block.fromBytes(
        Seq(0xc2, 0x5f, 0x42, 0xe7, 0x4c, 0x02, 0x74, 0xa4).map(_.toByte)
      ),
      s = Qarma.CELL_SBOX_2
    )

    val exp = Block.fromBytes(
      Seq(0x5c, 0x06, 0xa7, 0x50, 0x1b, 0x63, 0xb2, 0xfd).map(_.toByte)
    )
    val act = dut(
      p = Block.fromBytes(
        Seq(0xfb, 0x62, 0x35, 0x99, 0xda, 0x6e, 0x81, 0x27).map(_.toByte)
      ),
      t = Block.fromBytes(
        Seq(0x47, 0x7d, 0x46, 0x9d, 0xec, 0x0b, 0x87, 0x62).map(_.toByte)
      )
    )

    act should be(exp)
  }
}