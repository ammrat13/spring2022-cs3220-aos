package edu.gatech.cs3220.spring2022.m68k.qarma_hw

import chisel3._

class Qarma extends Module {

  val inp = IO(Input(Bool()))
  val out = IO(Output(Bool()))

  out := inp
}
