# Hardware Implementation of Qarma

This code is part of our final project for CS 3220 A: Processor Design in Spring
2022 with [Dr. Hyesoon Kim][1]. Siddhant and I worked with Yonghae to create an
implementation of the [Qarma Cipher][2] in hardware. This code was wrapped in an
execution unit and integrated into the [BOOM Core][3]. The module was used to
provide pointer signing primitives as an extension to the RISC-V instruction
set.

* [Modified BOOM Core][4]
* CARRV 2022 Paper *(not available)*


## Compilation

The code, like [BOOM][3], is written in a Scala DSL called [Chisel][6]. It has
to be compiled to Verilog (or FIRRTL). Pre-compiled output is available under
*Releases*. Alternatively, the code can be compiled using [`sbt`][7] by running
```
$ sbt run
```
in the root of this project. The compiler also takes command-line options,
perhaps most usefully one to set the output directory. All the options can be
listed with
```
$ sbt "run --help"
```

Regardless, the top module of the compiled HDL is called `QarmaHW`.


## Usage

Four of the ports on the `QarmaHW` module are used to specify the key.
Specifically, the

* `key_k0`,
* `key_k1`,
* `key_w0`, and
* `key_w1`

ports must each be connected to their corresponding parameter. Note that `k1`
and `w1` must be set explicitly. Set them according to the computation in the
[paper][2] for encryption or decryption.

Both the input and output are wrapped inside a ready/valid interface. For the
input, assert `inp_valid` when it's valid. On the first clock cycle where both
that and `inp_ready` are asserted, the module has latched the data and
`inp_valid` can be deasserted. Likewise for output, assert `out_ready` when
ready to latch. On the first clock cycle both that and `out_valid` are asserted,
the module will assume the data has been latched externally.

For the input data, `inp_bits_ptext` should be the plaintext to encrypt and
`inp_bits_tweak` should be the tweak to use. For the output, `out_bits_ctext` is
the ciphertext. The plaintext and tweak used to generate it are provided as
`out_bits_ptext` and `out_bits_tweak` respectively.

This module has a four stage pipeline. Hence, it preserves the plaintext and
tweak on the output for validation. It also assumes that the keys are never
changed. If they are changed, any encryptions still "in flight" will give
incorrect answers.


[1]: https://faculty.cc.gatech.edu/~hyesoon/ "Dr. Hyesoon Kim"
[2]: https://doi.org/10.13154/tosc.v2017.i1.4-44 "The QARMA Block Cipher Family. Almost MDS Matrices Over Rings With Zero Divisors, Nearly Symmetric Even-Mansour Constructions With Non-Involutory Central Rounds, and Search Heuristics for Low-Latency S-Boxes"
[3]: https://boom-core.org/ "RISC-V BOOM: The Berkeley Out-of-Order RISC-V Processor"
[4]: https://github.com/yonghaekim/riscv-boom/commit/528230f07b46313b2c3189d1ef7409187211816a
[6]: https://www.chisel-lang.org/ "Chisel/FIRRTL Hardware Compiler Framework"
[7]: https://www.scala-sbt.org/ "SBT: The Interactive Build Tool"
