package edu.gatech.cs3220.spring2022.motorolla68k.qarma64

object Qarma {

  /** Default value for alpha See: https://eprint.iacr.org/2016/444.pdf Table 1
    */
  val REFLECTION_CONSTANT: Seq[Byte] =
    Seq(0xc0, 0xac, 0x29, 0xb7, 0xc9, 0x7c, 0x50, 0xdd).map(_.toByte)

  /** Default values for round keys See: https://eprint.iacr.org/2016/444.pdf
    * Table 1
    */
  val ROUND_KEYS: Seq[Seq[Byte]] = Seq(
    Seq(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00).map(_.toByte),
    Seq(0x13, 0x19, 0x8a, 0x2e, 0x03, 0x70, 0x73, 0x44).map(_.toByte),
    Seq(0xa4, 0x09, 0x38, 0x22, 0x29, 0x9f, 0x31, 0xd0).map(_.toByte),
    Seq(0x08, 0x2e, 0xfa, 0x98, 0xec, 0x4e, 0x6c, 0x89).map(_.toByte),
    Seq(0x45, 0x28, 0x21, 0xe6, 0x38, 0xd0, 0x13, 0x77).map(_.toByte),
    Seq(0xbe, 0x54, 0x66, 0xcf, 0x34, 0xe9, 0x0c, 0x6c).map(_.toByte),
    Seq(0x3f, 0x84, 0xd5, 0xb5, 0xb5, 0x47, 0x09, 0x17).map(_.toByte),
    Seq(0x92, 0x16, 0xd5, 0xd9, 0x89, 0x79, 0xfb, 0x1b).map(_.toByte)
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
  */
class Qarma(
    val rounds: Int,
    val k0: Seq[Byte],
    val k1: Seq[Byte],
    val w0: Seq[Byte],
    val w1: Seq[Byte],
    val a: Seq[Byte] = Qarma.REFLECTION_CONSTANT,
    val c: Seq[Seq[Byte]] = Qarma.ROUND_KEYS
) {}
