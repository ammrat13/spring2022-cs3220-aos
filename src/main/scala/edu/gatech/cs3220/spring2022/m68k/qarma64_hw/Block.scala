package edu.gatech.cs3220.spring2022.m68k.qarma64_hw

import chisel3._
import chisel3.util.Cat

/** Represents a 64-bit block as sixteen 4-bit cells */
class Block extends Bundle {
  val cells = Vec(16, UInt(4.W))
}

/** Convert from a `UInt` to a [Block] */
class UInt2Block extends Module {
  val inp = IO(Input(UInt(64.W)))
  val out = IO(Output(new Block))

  // Remember that we're big endian
  for(i <- 0 to 15) {
    out.cells(15-i) := inp(4*i+3, 4*i)
  }
}

/** Convert a [Block] back into a `UInt` */
class Block2UInt extends Module {
  val inp = IO(Input(new Block))
  val out = IO(Output(UInt(64.W)))

  out := Cat(inp.cells)
}
