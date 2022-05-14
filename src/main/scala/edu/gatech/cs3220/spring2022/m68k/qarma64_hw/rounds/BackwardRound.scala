package edu.gatech.cs3220.spring2022.m68k.qarma64_hw.rounds

import chisel3._

import edu.gatech.cs3220.spring2022.m68k.qarma64.Qarma
import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.BlockHW
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.BlockHWInit
import edu.gatech.cs3220.spring2022.qarma64_hw.m68k.rounds.QarmaHWIntermediate

/** A module that performs a backward round */
class BackwardRound(
    val short: Boolean,
    val a: UInt,
    val c: UInt,
    val sBox: Permutation
) extends Module {

  val io = IO(new Bundle {
    val inp = Input(new QarmaHWIntermediate)
    val out = Output(new QarmaHWIntermediate)

    val k0 = Input(new BlockHW)
  })

  // Pass the originals through
  io.out.ptext := io.inp.ptext
  io.out.otweak := io.inp.otweak

  // First, do sInv, then mInv and tInv (if not short)
  val ctext_after_scramble = Wire(new BlockHW)
  ctext_after_scramble := (if (!short) {
                             io.inp.ctext
                               .substitute(this.sBox.inv)
                               .mulMatR(Qarma.M42)
                               .permute(Qarma.CELL_PERMUTATION.inv)
                           } else {
                             io.inp.ctext
                               .substitute(this.sBox.inv)
                           })
  // Then, add the round tweakkey
  io.out.ctext := ctext_after_scramble ^ io.inp.ntweak ^ io.k0 ^ BlockHWInit(
    this.a
  ) ^ BlockHWInit(this.c)

  // Do wInv and hInv on the tweak
  io.out.ntweak := io.inp.ntweak
    .shift(Qarma.TWEAK_UPDATE_LFSR.inv)
    .permute(Qarma.TWEAK_UPDATE_PERMUTATION.inv)
}
