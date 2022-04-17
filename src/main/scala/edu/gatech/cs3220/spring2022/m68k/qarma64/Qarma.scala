package edu.gatech.cs3220.spring2022.m68k.qarma64

import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation

object Qarma {

  /** Default value for alpha
    *
    * See: https://eprint.iacr.org/2016/444.pdf Table 1
    */
  val REFLECTION_CONSTANT: Block =
    Block.fromBytes(
      Seq(0xc0, 0xac, 0x29, 0xb7, 0xc9, 0x7c, 0x50, 0xdd).map(_.toByte)
    )

  /** Default values for round keys
    *
    * See: https://eprint.iacr.org/2016/444.pdf Table 1
    */
  val ROUND_KEYS: Seq[Block] = Seq(
    Block.fromBytes(
      Seq(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00).map(_.toByte)
    ),
    Block.fromBytes(
      Seq(0x13, 0x19, 0x8a, 0x2e, 0x03, 0x70, 0x73, 0x44).map(_.toByte)
    ),
    Block.fromBytes(
      Seq(0xa4, 0x09, 0x38, 0x22, 0x29, 0x9f, 0x31, 0xd0).map(_.toByte)
    ),
    Block.fromBytes(
      Seq(0x08, 0x2e, 0xfa, 0x98, 0xec, 0x4e, 0x6c, 0x89).map(_.toByte)
    ),
    Block.fromBytes(
      Seq(0x45, 0x28, 0x21, 0xe6, 0x38, 0xd0, 0x13, 0x77).map(_.toByte)
    ),
    Block.fromBytes(
      Seq(0xbe, 0x54, 0x66, 0xcf, 0x34, 0xe9, 0x0c, 0x6c).map(_.toByte)
    ),
    Block.fromBytes(
      Seq(0x3f, 0x84, 0xd5, 0xb5, 0xb5, 0x47, 0x09, 0x17).map(_.toByte)
    ),
    Block.fromBytes(
      Seq(0x92, 0x16, 0xd5, 0xd9, 0x89, 0x79, 0xfb, 0x1b).map(_.toByte)
    )
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
  * @param s
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
    val s: Permutation = Qarma.CELL_SBOX_1
) {

  // Ensure that the number of rounds is good
  if (this.rounds < 1)
    throw new IllegalArgumentException("Qarma64 must have at least one round")
  if (this.rounds > this.c.length)
    throw new IllegalArgumentException("Must provide all round constants")

  // Key functions
  private def h(b: Block): Block = b.permute(Qarma.TWEAK_UPDATE_PERMUTATION)
  private def w(b: Block): Block = b.mapSpecial(c => {
    val b0 = (c.value & 0x1) >> 0
    val b1 = (c.value & 0x2) >> 1
    val b2 = (c.value & 0x4) >> 2
    val b3 = (c.value & 0x8) >> 3
    val c0 = b1
    val c1 = b2
    val c2 = b3
    val c3 = b0 ^ b1
    Cell((c3 << 3) | (c2 << 2) | (c1 << 1) | (c0 << 0))
  })
  private def hInv(b: Block): Block =
    b.permute(Qarma.TWEAK_UPDATE_PERMUTATION.inverse)
  private def wInv(b: Block): Block = b.mapSpecial(c => {
    val c0 = (c.value & 0x1) >> 0
    val c1 = (c.value & 0x2) >> 1
    val c2 = (c.value & 0x4) >> 2
    val c3 = (c.value & 0x8) >> 3
    val b0 = c0 ^ c3
    val b1 = c0
    val b2 = c1
    val b3 = c2
    Cell((b3 << 3) | (b2 << 2) | (b1 << 1) | (b0 << 0))
  })

  // Matrix functions
  // M42 is involutory
  private def m42(b: Block): Block = Block(
    Seq(
      (b.cells(4) <<< 1) ^ (b.cells(8) <<< 2) ^ (b.cells(12) <<< 1),
      (b.cells(5) <<< 1) ^ (b.cells(9) <<< 2) ^ (b.cells(13) <<< 1),
      (b.cells(6) <<< 1) ^ (b.cells(10) <<< 2) ^ (b.cells(14) <<< 1),
      (b.cells(7) <<< 1) ^ (b.cells(11) <<< 2) ^ (b.cells(15) <<< 1),
      (b.cells(0) <<< 1) ^ (b.cells(8) <<< 1) ^ (b.cells(12) <<< 2),
      (b.cells(1) <<< 1) ^ (b.cells(9) <<< 1) ^ (b.cells(13) <<< 2),
      (b.cells(2) <<< 1) ^ (b.cells(10) <<< 1) ^ (b.cells(14) <<< 2),
      (b.cells(3) <<< 1) ^ (b.cells(11) <<< 1) ^ (b.cells(15) <<< 2),
      (b.cells(0) <<< 2) ^ (b.cells(4) <<< 1) ^ (b.cells(12) <<< 1),
      (b.cells(1) <<< 2) ^ (b.cells(5) <<< 1) ^ (b.cells(13) <<< 1),
      (b.cells(2) <<< 2) ^ (b.cells(6) <<< 1) ^ (b.cells(14) <<< 1),
      (b.cells(3) <<< 2) ^ (b.cells(7) <<< 1) ^ (b.cells(15) <<< 1),
      (b.cells(0) <<< 1) ^ (b.cells(4) <<< 2) ^ (b.cells(8) <<< 1),
      (b.cells(1) <<< 1) ^ (b.cells(5) <<< 2) ^ (b.cells(9) <<< 1),
      (b.cells(2) <<< 1) ^ (b.cells(6) <<< 2) ^ (b.cells(10) <<< 1),
      (b.cells(3) <<< 1) ^ (b.cells(7) <<< 2) ^ (b.cells(11) <<< 1)
    )
  )

  // IS functions
  private def t(b: Block): Block = b.permute(Qarma.CELL_PERMUTATION)
  private def m(b: Block): Block = this.m42(b)
  private def s(b: Block): Block = b.map(_.map(this.s.apply))
  private def q(b: Block): Block = this.m42(b)
  private def tInv(b: Block): Block = b.permute(Qarma.CELL_PERMUTATION.inverse)
  private def mInv(b: Block): Block = this.m42(b)
  private def sInv(b: Block): Block = b.map(_.map(this.s.inverse.apply))
  private def qInv(b: Block): Block = this.m42(b)

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
