package core

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class CoreTest extends AnyFlatSpec with ChiselScalatestTester {
  /*def instruction(op: UInt, dest: UInt, src: UInt, imm: UInt) = {
        if (op.litValue > 13) {
            Cat(imm, src, op)
        } else {
            Cat(dest, src, op)
        }
    }*/
  def write_ins(c: WildcatCore, addr: UInt, code: UInt) = {
    c.io.is_write.poke(true.B)
    c.io.boot.poke(false.B)
    c.io.write_addr.poke(addr)
    c.io.write_data.poke(code)
    c.clock.step(1)
  }
  def boot(c: WildcatCore) = {
    c.io.is_write.poke(false.B)
    c.io.boot.poke(true.B)
    c.clock.step(1)
  }
  behavior of "WildcatCore"
  it should "test writing to memory" in {
    test(new WildcatCore()) { c =>
      // write one instruction
      write_ins(c, 0.U, 13056.U)
      // do that instruction
      // partially tests decoding, too
      c.io.is_write.poke(false.B)
      c.io.boot.poke(true.B)
      c.io.inst.expect(13056.U)
      c.io.valid.expect(true.B)
    }
  }
  it should "test instruction decoding" in {
    test(new WildcatCore()) { c =>
      //  add
      write_ins(c, 0.U, 13056.U)
      //  addi
      write_ins(c, 1.U, 13069.U)
      //  sub
      write_ins(c, 2.U, 13057.U)
      //  subi
      write_ins(c, 3.U, 13070.U)
      //  mul
      write_ins(c, 4.U, 13058.U)
      //  div
      write_ins(c, 5.U, 13059.U)
      //  mod
      write_ins(c, 6.U, 13060.U)
      //  mov
      write_ins(c, 7.U, 13061.U)
      //  ldi
      write_ins(c, 8.U, 13071.U)
      //  sto
      write_ins(c, 9.U, 13072.U)
      //  ld
      write_ins(c, 10.U, 13073.U)
      //  and
      write_ins(c, 11.U, 13062.U)
      //  or
      write_ins(c, 12.U, 13063.U)
      //  xor
      write_ins(c, 13.U, 13064.U)
      //  not
      write_ins(c, 14.U, 13065.U)
      //  gre
      write_ins(c, 15.U, 13066.U)
      //  les
      write_ins(c, 16.U, 13067.U)
      //  equ
      write_ins(c, 17.U, 13068.U)
      //  jmp
      write_ins(c, 18.U, 13074.U)
      //  skp
      write_ins(c, 19.U, 13075.U)
      //  bsl
      write_ins(c, 20.U, 13076.U)
      //  bsr
      write_ins(c, 21.U, 13077.U)
      //  invalid
      write_ins(c, 22.U, 13078.U)

      // boot the CPU
      boot(c)
      c.io.boot.poke(false.B)
      // first 19 instructions should be valid
      for (ins <- 0 until 22) {
        println(c.io.pc.peek())
        println(c.io.inst.peek())
        c.io.valid.expect(true.B)
        c.io.pc.expect(ins.U)
        c.clock.step(1)
      }
      // last should be invalid
      c.io.valid.expect(false.B)
      c.io.pc.expect(22.U)
    }
  }
  it should "test imm arithmetic instructions" in {
    test(new WildcatCore()) { c =>
      write_ins(c, 0.U, 3117.U) // ADDI r1 0h3
      write_ins(c, 1.U, 3118.U) // SUBI r1 0h3
      write_ins(c, 2.U, 3085.U) // ADDI r0 0h3
      write_ins(c, 3.U, 3086.U) // SUBI r0 0h3
      boot(c)
      c.io.boot.poke(false.B)
      // test ADDI
      c.io.valid.expect(true.B)
      c.io.op.expect(13.U)
      c.io.result.expect(3.U)
      c.clock.step(1)

      // test SUBI
      c.io.valid.expect(true.B)
      c.io.op.expect(14.U)
      c.io.result.expect(0.U)
      c.clock.step(1)

      // test fail on 0 regs
      c.io.valid.expect(false.B)
      c.clock.step(1)
      c.io.valid.expect(false.B)
      c.clock.step(1)
    }
  }
  it should "test arithmetic instructions" in {
      test(new WildcatCore()) { c => 
        /* seed some values */
      write_ins(c, 0.U, 157773.U) // ADDI r2 0h9A
      write_ins(c, 1.U, 2093.U) // ADDI r1 0h2
      write_ins(c, 2.U, 2080.U) // ADD r2 r1
      write_ins(c, 3.U, 2081.U) // SUB r2 r1
      write_ins(c, 4.U, 2082.U) // MUL r2 r1
      write_ins(c, 5.U, 2083.U) // DIV r2 r1
      write_ins(c, 6.U, 2084.U) // MOD r2 r1
      boot(c)
      c.io.boot.poke(false.B)
      c.clock.step(2) // seed the registers
      // test add: expect 156
      println("ADD r2 r1")
      println(c.io.inst.peek())
      println(c.io.op.peek())
      println(c.io.imm.peek())
      println(c.io.src.peek())
      c.io.valid.expect(true.B)
      c.io.result.expect(156.U)
      c.clock.step(1)
      // test sub: expect 154
      c.io.valid.expect(true.B)
      c.io.result.expect(154.U)
      c.clock.step(1)
      // test mul: expect 308
      c.io.valid.expect(true.B)
      c.io.result.expect(308.U)
      c.clock.step(1)
      // test div: expect 154
      c.io.valid.expect(true.B)
      c.io.result.expect(154.U)
      c.clock.step(1)
      // test mod: expect 0
      c.io.valid.expect(true.B)
      c.io.result.expect(0.U)
              
    }
  }
  it should "test mov instruction" in {
    test(new WildcatCore()) {c => 
      write_ins(c, 0.U, 5592109.U) // ADDI r1 0b1010101010101
      write_ins(c, 1.U, 2085.U) // MOV r1 r2
      write_ins(c, 2.U, 5592142.U) // SUBI r2 0b1010101010101
      boot(c)
      c.io.boot.poke(false.B)
      c.clock.step(2)
      c.io.result.expect(0.U)
      }
  }
  it should "test boolean instructions" in {
    test(new WildcatCore()) { c =>
      write_ins(c, 0.U, 5592109.U) // ADDI r1 0b1010101010101
      write_ins(c, 1.U, 11184205.U) // ADDI r2 0b10101010101010
      write_ins(c, 2.U, 3109.U) // MOV r1 r3
      write_ins(c, 3.U, 4165.U) // MOV r2 r4
      write_ins(c, 4.U, 4166.U) // AND r2 r4
      write_ins(c, 5.U, 1095.U) // OR r2 r1
      write_ins(c, 6.U, 5129.U) // NOT r5
      write_ins(c, 7.U, 3112.U) // XOR r1 r3
      boot(c)
      c.io.boot.poke(false.B)
      c.clock.step(4)
      // AND
      c.io.pc.expect(4.U)
      c.io.result.expect(10922.U)
      c.clock.step(1)
      // OR
      c.io.result.expect(16383.U)
      c.clock.step(1)
      // NOT
      c.io.result.expect(16777215.U)
      c.clock.step(1)
      // XOR
      c.io.result.expect(10922.U)
    }
  }
  it should "test bitshift instructions" in {
    test(new WildcatCore()) { c =>
        write_ins(c, 0.U, 3117.U) // ADDI r1 0d3
        write_ins(c, 1.U, 1076.U) // BSL r1 0x1
        write_ins(c, 2.U, 1077.U) // BSR r1 0x1
        boot(c)
        c.io.boot.poke(false.B)
        c.clock.step(1)
        c.io.result.expect(6.U)
        c.clock.step(1)
        c.io.result.expect(3.U)
    }
  }
}
