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
/* try putting "write instruction" in a funciton again tomorrow
by putting c: WildcatCore */
            test(new WildcatCore()) { c =>
                // write one instruction
                write_ins(c,0.U,13056.U)
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
                write_ins(c,0.U,13056.U)
                //  addi
                write_ins(c,1.U,13069.U)
                //  sub
                write_ins(c,2.U,13057.U)
                //  subi
                write_ins(c,3.U,13070.U)
                //  mul
                write_ins(c,4.U,13058.U)
                //  div
                write_ins(c,5.U,13059.U)
                //  mod
                write_ins(c,6.U,13060.U)
                //  mov
                write_ins(c,7.U,13061.U)
                //  ldi
                write_ins(c,8.U,13071.U)
                //  sto
                write_ins(c,9.U,13072.U)
                //  ld
                write_ins(c,10.U,13073.U)
                //  and
                write_ins(c,11.U,13062.U)
                //  or
                write_ins(c,12.U,13063.U)
                //  xor
                write_ins(c,13.U,13064.U)
                //  not
                write_ins(c,14.U,13065.U)
                //  gre
                write_ins(c,15.U,13066.U)
                //  les
                write_ins(c,16.U,13067.U)
                //  equ
                write_ins(c,17.U,13068.U)
                //  jmp
                write_ins(c,18.U,13074.U)
                //  skp
                write_ins(c,19.U,13075.U)
                //  invalid
                write_ins(c,20.U,13076.U)
                
                // boot the CPU
                boot(c) 
                c.io.boot.poke(false.B)
                // first 19 instructions should be valid
                for (ins <- 0 until 20) {
                    println(c.io.pc.peek())
                    println(c.io.inst.peek())
                    c.io.valid.expect(true.B)
                    c.io.pc.expect(ins.U)
                    c.clock.step(1)
                }
                // last should be invalid
                c.io.valid.expect(false.B)
                c.io.pc.expect(20.U)
            }
        }
}