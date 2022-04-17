package edu.gatech.cs3220.spring2022.m68k.qarma64

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class QarmaTest extends AnyFlatSpec with Matchers {

  "Qarma64" should "give the correct output on test vectors (s = s1, rounds = 5)" in {
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
}
