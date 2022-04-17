package edu.gatech.cs3220.spring2022.m68k.qarma64.util

/** A permutation to be applied to a block
  *
  * @param repr
  *   Should have repr[i] be the image of i under this permutation
  */
case class Permutation(val repr: Seq[Int]) {

  // Should have exactly sixteen elements
  if (repr.length != 16)
    throw new IllegalArgumentException(
      "Permutation must have exactly sixteen elements"
    )

  // Should have every element exactly once
  for (i <- 0 until 16) {
    if (repr.count(_ == i) != 1)
      throw new IllegalArgumentException(
        s"Permutation must have exactly one instance of ${i}"
      )
  }

  /** Get the value of a permutation input */
  def apply(i: Int): Int = this.repr(i)

  /** Get the inverse of a permutation */
  lazy val inverse: Permutation = Permutation((0 until 16).map { (i) =>
    this.repr.indexOf(i)
  })
}
