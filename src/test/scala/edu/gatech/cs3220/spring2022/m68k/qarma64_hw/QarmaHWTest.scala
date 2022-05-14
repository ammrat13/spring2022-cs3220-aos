package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import chisel3._
import chiseltest._
import chisel3.experimental.BundleLiterals._
import org.scalatest.flatspec.AnyFlatSpec

import edu.gatech.cs3220.spring2022.m68k.qarma64.Qarma

class QarmaHWTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "QarmaHW"

  it should "give the correct output on test vectors (s = s0)" in {
    test(new QarmaHW(sBox = Qarma.CELL_SBOX_0)) { c =>
      // Set up the keys
      c.key.k0.poke("h_ec2802d4e0a488e9".U(64.W))
      c.key.k1.poke("h_ec2802d4e0a488e9".U(64.W))
      c.key.w0.poke("h_84be85ce9804e94b".U(64.W))
      c.key.w1.poke("h_c25f42e74c0274a4".U(64.W))

      // Initialize the channels
      c.inp.initSource().setSourceClock(c.clock)
      c.out.initSink().setSinkClock(c.clock)

      parallel(
        // Try encrypt
        c.inp.enqueue(
          chiselTypeOf(c.inp.bits).Lit(
            _.ptext -> "h_fb623599da6e8127".U(64.W),
            _.tweak -> "h_477d469dec0b8762".U(64.W)
          )
        ),

        // Check for the expected output
        c.out.expectDequeue(
          chiselTypeOf(c.out.bits).Lit(
            _.ptext -> "h_fb623599da6e8127".U(64.W),
            _.tweak -> "h_477d469dec0b8762".U(64.W),
            _.ctext -> "h_3ee99a6c82af0c38".U(64.W)
          )
        )
      )
    }
  }

  it should "give the correct output on test vectors (s = s1)" in {
    test(new QarmaHW(sBox = Qarma.CELL_SBOX_1)) { c =>
      // Set up the keys
      c.key.k0.poke("h_ec2802d4e0a488e9".U(64.W))
      c.key.k1.poke("h_ec2802d4e0a488e9".U(64.W))
      c.key.w0.poke("h_84be85ce9804e94b".U(64.W))
      c.key.w1.poke("h_c25f42e74c0274a4".U(64.W))

      // Initialize the channels
      c.inp.initSource().setSourceClock(c.clock)
      c.out.initSink().setSinkClock(c.clock)

      parallel(
        // Try encrypt
        c.inp.enqueue(
          chiselTypeOf(c.inp.bits).Lit(
            _.ptext -> "h_fb623599da6e8127".U(64.W),
            _.tweak -> "h_477d469dec0b8762".U(64.W)
          )
        ),

        // Check for the expected output
        c.out.expectDequeue(
          chiselTypeOf(c.out.bits).Lit(
            _.ptext -> "h_fb623599da6e8127".U(64.W),
            _.tweak -> "h_477d469dec0b8762".U(64.W),
            _.ctext -> "h_544b0ab95bda7c3a".U(64.W)
          )
        )
      )
    }
  }

  it should "give the correct output on test vectors (s = s2)" in {
    test(new QarmaHW(sBox = Qarma.CELL_SBOX_2)) { c =>
      // Set up the keys
      c.key.k0.poke("h_ec2802d4e0a488e9".U(64.W))
      c.key.k1.poke("h_ec2802d4e0a488e9".U(64.W))
      c.key.w0.poke("h_84be85ce9804e94b".U(64.W))
      c.key.w1.poke("h_c25f42e74c0274a4".U(64.W))

      // Initialize the channels
      c.inp.initSource().setSourceClock(c.clock)
      c.out.initSink().setSinkClock(c.clock)

      parallel(
        // Try encrypt
        c.inp.enqueue(
          chiselTypeOf(c.inp.bits).Lit(
            _.ptext -> "h_fb623599da6e8127".U(64.W),
            _.tweak -> "h_477d469dec0b8762".U(64.W)
          )
        ),

        // Check for the expected output
        c.out.expectDequeue(
          chiselTypeOf(c.out.bits).Lit(
            _.ptext -> "h_fb623599da6e8127".U(64.W),
            _.tweak -> "h_477d469dec0b8762".U(64.W),
            _.ctext -> "h_c003b93999b33765".U(64.W)
          )
        )
      )
    }
  }
}
