package edu.gatech.cs3220.spring2022.m68k

import chisel3._

import edu.gatech.cs3220.spring2022.m68k.qarma_hw.QarmaHW

/** Main program to generate the hardware */
object Gen extends App {
  emitVerilog(
    new QarmaHW(
      a = "h_c0ac29b7c97c50dd".U(64.W),
      c = Seq(
        "h_0000000000000000".U(64.W),
        "h_13198a2e03707344".U(64.W),
        "h_a4093822299f31d0".U(64.W),
        "h_082efa98ec4e6c89".U(64.W),
        "h_452821e638d01377".U(64.W)
      )
    ),
    args
  )
}
