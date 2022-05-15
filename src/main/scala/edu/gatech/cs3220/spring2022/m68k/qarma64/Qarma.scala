package edu.gatech.cs3220.spring2022.m68k.qarma64

import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation
import edu.gatech.cs3220.spring2022.m68k.qarma64.util.LFSR

object Qarma {

  /** Default value for alpha
    *
    * See: https://eprint.iacr.org/2016/444.pdf Table 1
    */
  val REFLECTION_CONSTANT: Block =
    Block.fromBigInt(BigInt("c0ac29b7c97c50dd", 16))

  /** Default values for round keys
    *
    * See: https://eprint.iacr.org/2016/444.pdf Table 1
    */
  val ROUND_KEYS: Seq[Block] = Seq(
    Block.fromBigInt(BigInt("0000000000000000", 16)),
    Block.fromBigInt(BigInt("13198a2e03707344", 16)),
    Block.fromBigInt(BigInt("a4093822299f31d0", 16)),
    Block.fromBigInt(BigInt("082efa98ec4e6c89", 16)),
    Block.fromBigInt(BigInt("452821e638d01377", 16)),
    Block.fromBigInt(BigInt("be5466cf34e90c6c", 16)),
    Block.fromBigInt(BigInt("3f84d5b5b5470917", 16)),
    Block.fromBigInt(BigInt("9216d5d98979fb1b", 16))
  )

  /** Permutation used in the first phase of the state update
    *
    * See https://eprint.iacr.org/2016/444.pdf Section 2.3
    */
  val CELL_PERMUTATION: Permutation = Permutation(
    Seq(0, 11, 6, 13, 10, 1, 12, 7, 5, 14, 3, 8, 15, 4, 9, 2)
  )

  /** Substitution boxes used in the third phase of state update
    *
    * See https://eprint.iacr.org/2016/444.pdf Section 3.4
    */
  val CELL_SBOX_0: Permutation = Permutation(
    Seq(0, 14, 2, 10, 9, 15, 8, 11, 6, 4, 3, 7, 13, 12, 1, 5)
  )
  val CELL_SBOX_1: Permutation = Permutation(
    Seq(10, 13, 14, 6, 15, 7, 3, 5, 9, 8, 0, 12, 11, 1, 2, 4)
  )
  val CELL_SBOX_2: Permutation = Permutation(
    Seq(11, 6, 8, 15, 12, 0, 9, 14, 3, 7, 4, 5, 13, 2, 1, 10)
  )

  /** Permutation used in the first phase of the tweak update
    *
    * See https://eprint.iacr.org/2016/444.pdf Section 2.4
    */
  val TWEAK_UPDATE_PERMUTATION: Permutation = Permutation(
    Seq(6, 5, 14, 15, 0, 1, 2, 3, 7, 12, 13, 4, 8, 9, 10, 11)
  )

  /** LFSR used in the second phase of tweak update
    *
    * See https://eprint.iacr.org/2016/444.pdf Section 2.4
    */
  val TWEAK_UPDATE_LFSR: LFSR = LFSR(Set(0, 1))

  /** Matrix used in both the forward and reverse rounds
    *
    * It's involutory, so it can be used as its own inverse.
    *
    * See https://eprint.iacr.org/2016/444.pdf Section 3.2
    */
  val M42: Block = Block(
    Seq(
      Seq(0, 2, 4, 2),
      Seq(2, 0, 2, 4),
      Seq(4, 2, 0, 2),
      Seq(2, 4, 2, 0)
    ).flatten.map(Cell(_))
  )
}

/** Class describing a run of Qarma
  *
  * Encryption and decryption are specializations of this class with particular
  * values for the core and whitening keys.
  *
  * @param rounds
  *   How many rounds to do
  * @param k0
  *   The primary core key
  * @param k1
  *   The secondary core key derived from `k0`
  * @param w0
  *   The primary whitening key
  * @param w1
  *   The secondary whitening key derived from `w0`
  * @param a
  *   The reflection constant
  * @param c
  *   The sequence of round keys
  * @param sBox
  *   The substitution box to use
  */
case class Qarma(
    val rounds: Int,
    val k0: Block,
    val k1: Block,
    val w0: Block,
    val w1: Block,
    val a: Block = Qarma.REFLECTION_CONSTANT,
    val c: Seq[Block] = Qarma.ROUND_KEYS,
    val sBox: Permutation = Qarma.CELL_SBOX_1
) {

  // Ensure that the number of rounds is good
  if (this.rounds < 1)
    throw new IllegalArgumentException("Qarma64 must have at least one round")
  if (this.rounds > this.c.length)
    throw new IllegalArgumentException("Must provide all round constants")

  // Tweak functions
  private def w: (Block) => Block = _.shift(Qarma.TWEAK_UPDATE_LFSR)
  private def h: (Block) => Block = _.permute(Qarma.TWEAK_UPDATE_PERMUTATION)
  private def wInv: (Block) => Block =
    _.shift(Qarma.TWEAK_UPDATE_LFSR.inv)
  private def hInv: (Block) => Block =
    _.permute(Qarma.TWEAK_UPDATE_PERMUTATION.inv)

  // IS functions
  private def t: (Block) => Block = _.permute(Qarma.CELL_PERMUTATION)
  private def s: (Block) => Block = _.substitute(this.sBox)
  private def m: (Block) => Block = Qarma.M42.mulMatR
  private def q: (Block) => Block = Qarma.M42.mulMatR
  private def tInv: (Block) => Block = _.permute(Qarma.CELL_PERMUTATION.inv)
  private def sInv: (Block) => Block = _.substitute(this.sBox.inv)
  private def mInv: (Block) => Block = Qarma.M42.mulMatR
  private def qInv: (Block) => Block = Qarma.M42.mulMatR

  /** Encryption
    *
    * Runs a pass of Qarma with the given parameters. Returns the output. Can be
    * encryption or decryption.
    *
    * @param p
    *   The "plaintext"
    * @param t
    *   The tweak
    * @return
    *   The "ciphertext"
    */
  def apply(p: Block, t: Block): Block = {
    // Initialize the state
    var state = p ^ this.w0
    var tweak = t

    // Forward rounds
    for (i <- 0 to this.rounds - 1) {
      // Tweakkey
      val tk = tweak ^ this.k0 ^ this.c(i)
      state = state ^ tk
      // State update
      if (i != 0) {
        state = this.t(state)
        state = this.m(state)
      }
      state = this.s(state)
      // Tweak update
      tweak = this.h(tweak)
      tweak = this.w(tweak)
    }

    // Pseudo-reflector
    // Forward round
    state = state ^ (tweak ^ this.w1)
    state = this.t(state)
    state = this.m(state)
    state = this.s(state)
    // Reflect
    state = this.t(state)
    state = this.q(state)
    state = state ^ this.k1
    state = this.tInv(state)
    // Backward round
    state = this.sInv(state)
    state = this.mInv(state)
    state = this.tInv(state)
    state = state ^ (tweak ^ this.w0)

    // Backward rounds
    for (i <- this.rounds - 1 to 0 by -1) {
      // Tweak update
      tweak = this.wInv(tweak)
      tweak = this.hInv(tweak)
      // State update
      state = this.sInv(state)
      if (i != 0) {
        state = this.mInv(state)
        state = this.tInv(state)
      }
      // Tweakkey
      val tk = tweak ^ this.k0 ^ this.c(i) ^ this.a
      state = state ^ tk
    }

    return state ^ this.w1
  }
}
