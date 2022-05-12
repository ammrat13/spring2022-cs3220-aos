package edu.gatech.cs3220.spring2022.m68k

import chisel3.emitVerilog

import edu.gatech.cs3220.spring2022.m68k.qarma_hw.Qarma

/** Main program to generate the hardware */
object Gen extends App {
  emitVerilog(new Qarma, args)
}
