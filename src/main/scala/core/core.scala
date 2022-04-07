package core

import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage

class WildcatCore() extends Module {
  /* outline
        so, i'm not really sure how best to lay this out, but i'm getting something down
        so i don't feel bad about how little i've written.

        the flow of the core should go as follows:
            >read an instruction from program memory
            >decode the instruction
            >do the task of the decoded instruction

        seems simple?
        additionally, the spec requires:
            >some amount of working registers (i think we're doing 32?)
            >a basic CSR register for the conditional instructions

        i think the following order for building the core would likely work out:
            >get decode logic working first (this is necessary for everything else)
   * make sure to have a test harness which tests each of the 20 instructions
            >after decode logic works, implement the simple instructions
            >then implement conditional logic
            >then implement skip logic
   * here is where the fibonnaci sequence should work
   * this also requires the program counter
            >implement a memory
            >implement load/store
            >working CPU?
   */
  val io = IO(new Bundle {
    val boot = Input(Bool())
    val is_write = Input(Bool())
    val write_addr = Input(UInt(24.W))
    val write_data = Input(UInt(24.W))
    val valid = Output(Bool())
    val inst = Output(UInt(24.W))
    /* debug outputs */
    val pc = Output(UInt(14.W))
    val imm = Output(UInt(14.W))
    val src = Output(UInt(5.W))
    val op = Output(UInt(5.W))
    val cond_pass = Output(UInt(1.W))
    val result = Output(UInt(24.W)) // output of last ins
  })
  val reg = Mem(32, UInt(24.W))
  reg(0) := 0.U
  val pc = RegInit(0.U(14.W)) /* 2^14 is maximum size of mem */
  val cond_pass = Reg(UInt(1.W)) /* one flag, for "previous cond pass", i guess */
  /* adding these as stub until needed
   val working_mem = Mem()
   */
  val program_mem = Mem(256, UInt(24.W))

  val inst = program_mem(pc)
  val imm = inst(23, 10)
  val src = inst(9, 5)
  val op = inst(4, 0)
  io.result := 127.U
  // decision tree
  when(io.is_write) { // writing an instruction to pmem
    program_mem(io.write_addr) := io.write_data
    io.valid := true.B
  }.elsewhen(io.boot) { // boot the cpu
    pc := 0.U
    io.valid := true.B
  }.otherwise { // we have an instruction
    when(op > 21.U) {
      io.valid := false.B
    }.otherwise {
      /* decode instruction */
      io.valid := true.B
      /* register-immediate */
      when(op > 12.U) {
        switch(op) {
          is(Opcode.addi) {
            // we only modify registers 1-31
            when(src > 0.U) {
              val res = reg(src) + imm
              reg(src) := res
              io.result := res
            }.otherwise {
              io.valid := false.B
            }
          }
          is(Opcode.subi) {
            when(src > 0.U) {
              val res = reg(src) - imm
              reg(src) := res
              io.result := res
            }.otherwise {
              io.valid := false.B
            }
          }
          is(Opcode.ldi) {}
          is(Opcode.sto) {}
          is(Opcode.ld) {}
          is(Opcode.jmp) {}
          is(Opcode.bsl) {
            val res = reg(src) << imm
            reg(src) := res
            io.result := res
          }
          is(Opcode.bsr) {
            val res = reg(src) >> imm
            reg(src) := res
            io.result := res
          }
        }
        /* register-register
      imm is destination register
         */
      }.otherwise {
        /* check for invalid registers
        ins format: $DEST := DEST op SRC
        */
        when((imm > 0.U) && (imm < 32.U) && (src < 32.U)){
          switch(op) {
            is(Opcode.add) {
              val res = reg(imm) + reg(src) 
              reg(imm) := res
              io.result := res
            }
            is(Opcode.sub) {
              val res = reg(imm) - reg(src) 
              reg(imm) := res
              io.result := res
            }
            is(Opcode.mul) {
              val res = reg(imm) * reg(src) 
              reg(imm) := res
              io.result := res
            }
            is(Opcode.div) {
              val res = reg(imm) / reg(src) 
              reg(imm) := res
              io.result := res
            }
            is(Opcode.mod) {
              val res = reg(imm) % reg(src) 
              reg(imm) := res
              io.result := res
            }
            is(Opcode.and) {
              val res = reg(imm) & reg(src)
              reg(imm) := res
              io.result := res
            }
            is(Opcode.or) {
              val res = reg(imm) | reg(src)
              reg(imm) := res
              io.result := res
            }
            is(Opcode.xor) {
              val res = reg(imm) ^ reg(src)
              reg(imm) := res
              io.result := res
            }
            is(Opcode.not) {
              val res = ~reg(imm)
              reg(imm) := res
              io.result := res
            }
            is(Opcode.gre) {
              // imm > src
              cond_pass := reg(imm) > reg(src)
            }
            is(Opcode.les) {
              // imm < src
              cond_pass := reg(imm) < reg(src)
            }
            is(Opcode.equ) {
              // imm = src
              cond_pass := reg(imm) === reg(src)
            }
            is(Opcode.mov) {
              reg(imm) := reg(src)
              io.result := reg(src)
            }
          }
        }.otherwise {
          io.valid := false.B
        }
      }
    }
    // skip/jump logic
    when(op === Opcode.skp){
      pc := pc + 1.U + cond_pass
    }.otherwise{pc := pc + 1.U}
    
  }
  /* filling out debug outputs */
  io.inst := inst
  io.pc := pc
  io.imm := reg(imm)
  io.src := reg(src)
  io.op := op
  io.cond_pass := cond_pass
}
