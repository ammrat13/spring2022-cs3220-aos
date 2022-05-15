package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import scala.util.Random
import scala.util.control.Breaks._
import chisel3._
import chiseltest._
import chisel3.experimental.BundleLiterals._
import org.scalatest.flatspec.AnyFlatSpec

import edu.gatech.cs3220.spring2022.m68k.qarma64.Qarma
import edu.gatech.cs3220.spring2022.m68k.qarma64.Block

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

  it should "handle different values being presented to it" in {
    // Generate the test vectors randomly
    val rng = new Random(314159)
    val test_vecs =
      Seq.fill(20)(BigInt(64, rng)) zip Seq.fill(20)(BigInt(64, rng))

    // All the hard-coded values
    val k0 = BigInt("ec2802d4e0a488e9", 16)
    val k1 = BigInt("ec2802d4e0a488e9", 16)
    val w0 = BigInt("84be85ce9804e94b", 16)
    val w1 = BigInt("c25f42e74c0274a4", 16)

    // Make the DUT and the reference
    val ref = Qarma(
      rounds = 5,
      k0 = Block.fromBigInt(k0),
      k1 = Block.fromBigInt(k1),
      w0 = Block.fromBigInt(w0),
      w1 = Block.fromBigInt(w1)
    )
    test(new QarmaHW) { c =>
      c.key.k0.poke(k0.U(64.W))
      c.key.k1.poke(k1.U(64.W))
      c.key.w0.poke(w0.U(64.W))
      c.key.w1.poke(w1.U(64.W))

      // Initialize the channels
      c.inp.initSource().setSourceClock(c.clock)
      c.out.initSink().setSinkClock(c.clock)

      parallel(
        // Try encrypt
        c.inp.enqueueSeq(test_vecs.map { case (p, t) =>
          chiselTypeOf(c.inp.bits).Lit(
            _.ptext -> p.U(64.W),
            _.tweak -> t.U(64.W)
          )
        }),

        // Check for the expected output
        c.out.expectDequeueSeq(test_vecs.map { case (p, t) =>
          chiselTypeOf(c.out.bits).Lit(
            _.ptext -> p.U(64.W),
            _.tweak -> t.U(64.W),
            _.ctext -> ref(Block.fromBigInt(p), Block.fromBigInt(t)).toBigInt
              .U(64.W)
          )
        })
      )
    }
  }

  it should "have a latency of at most four cycles" in {
    test(new QarmaHW) { c =>
      // Give everything random values
      // We don't so much care about what they are as much as when they appear
      c.key.k0.poke(0.U(64.W))
      c.key.k1.poke(0.U(64.W))
      c.key.w0.poke(0.U(64.W))
      c.key.w1.poke(0.U(64.W))

      // Wait for ready
      c.out.ready.poke(true.B)
      fork
        .withRegion(Monitor) {
          while (c.inp.ready.peek().litToBoolean == false)
            c.clock.step(1)
        }
        .joinAndStep(c.clock)

      // Send the data
      c.inp.valid.poke(true.B)
      c.inp.bits.poke(
        chiselTypeOf(c.inp.bits).Lit(
          _.ptext -> "h_aaaaaaaaaaaaaaaa".U(64.W),
          _.tweak -> "h_5555555555555555".U(64.W)
        )
      )

      // Wait at most four cycles
      // All the while, test if correct
      // Remember to invalidate the input after the first cycle
      breakable {
        for (i <- 0 to 4) {
          if (c.out.valid.peek().litToBoolean == true) {
            c.out.bits.ptext.expect("h_aaaaaaaaaaaaaaaa".U(64.W))
            c.out.bits.tweak.expect("h_5555555555555555".U(64.W))
            break()
          }
          c.clock.step(1)
          c.inp.valid.poke(false.B)
        }
        fail("Took too many cycles")
      }
    }
  }
}
