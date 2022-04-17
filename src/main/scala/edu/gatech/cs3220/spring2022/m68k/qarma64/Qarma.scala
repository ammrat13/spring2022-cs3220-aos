package edu.gatech.cs3220.spring2022.m68k.qarma64

import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation

object Qarma {

  /** Default value for alpha
    *
    * See: https://eprint.iacr.org/2016/444.pdf Table 1
    */
  val REFLECTION_CONSTANT: Block =
    Block(
      Seq(0xc0, 0xac, 0x29, 0xb7, 0xc9, 0x7c, 0x50, 0xdd).map(Cell)
    )

  /** Default values for round keys
    *
    * See: https://eprint.iacr.org/2016/444.pdf Table 1
    */
  val ROUND_KEYS: Seq[Block] = Seq(
    Block(
      Seq(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00).map(Cell)
    ),
    Block(
      Seq(0x13, 0x19, 0x8a, 0x2e, 0x03, 0x70, 0x73, 0x44).map(Cell)
    ),
    Block(
      Seq(0xa4, 0x09, 0x38, 0x22, 0x29, 0x9f, 0x31, 0xd0).map(Cell)
    ),
    Block(
      Seq(0x08, 0x2e, 0xfa, 0x98, 0xec, 0x4e, 0x6c, 0x89).map(Cell)
    ),
    Block(
      Seq(0x45, 0x28, 0x21, 0xe6, 0x38, 0xd0, 0x13, 0x77).map(Cell)
    ),
    Block(
      Seq(0xbe, 0x54, 0x66, 0xcf, 0x34, 0xe9, 0x0c, 0x6c).map(Cell)
    ),
    Block(
      Seq(0x3f, 0x84, 0xd5, 0xb5, 0xb5, 0x47, 0x09, 0x17).map(Cell)
    ),
    Block(
      Seq(0x92, 0x16, 0xd5, 0xd9, 0x89, 0x79, 0xfb, 0x1b).map(Cell)
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
class Qarma(
    val rounds: Int,
    val k0: Seq[Byte],
    val k1: Seq[Byte],
    val w0: Seq[Byte],
    val w1: Seq[Byte],
    val a: Block = Qarma.REFLECTION_CONSTANT,
    val c: Seq[Block] = Qarma.ROUND_KEYS,
    val s: Permutation = Qarma.CELL_SBOX_1
) {}
