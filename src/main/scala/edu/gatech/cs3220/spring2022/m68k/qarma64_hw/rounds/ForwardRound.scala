package edu.gatech.cs3220.spring2022.m68k.qarma64_hw.rounds

import chisel3._

import edu.gatech.cs3220.spring2022.m68k.qarma64.Qarma
import edu.gatech.cs3220.spring2022.m68k.qarma64.util.Permutation
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.BlockHW
import edu.gatech.cs3220.spring2022.m68k.qarma64_hw.BlockHWInit
import edu.gatech.cs3220.spring2022.qarma64_hw.m68k.rounds.QarmaHWIntermediate

/** A module that performs a forward round */
class ForwardRound(
    val short: Boolean,
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

  // First, add the round tweakkey
  val ctext_after_tk = Wire(new BlockHW)
  ctext_after_tk := io.inp.ctext ^ io.inp.ntweak ^ io.k0 ^ BlockHWInit(this.c)
  // Then do t and m (if not short), then s
  io.out.ctext := (if (!short) {
                     ctext_after_tk
                       .permute(Qarma.CELL_PERMUTATION)
                       .mulMatR(Qarma.M42)
                       .substitute(this.sBox)
                   } else {
                     ctext_after_tk
                       .substitute(this.sBox)
                   })

  // Do h and w on the tweak
  io.out.ntweak := io.inp.ntweak
    .permute(Qarma.TWEAK_UPDATE_PERMUTATION)
    .shift(Qarma.TWEAK_UPDATE_LFSR)
}
