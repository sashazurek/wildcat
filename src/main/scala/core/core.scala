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
     /* I wonder if we can implement PC through this */
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
   })
   val pc = RegInit(0.U(14.W)) /* 2^14 is maximum size of mem */
   val cond_pass = Reg(Bool()) /* one flag, for "previous cond pass", i guess */
   /* adding these as stub until needed
   val working_mem = Mem()
   */
   val program_mem = Mem(256, UInt(24.W))

   val inst = program_mem(pc)
   val imm = inst(23,10)
   val src = inst(9,5)
   val op = inst(4,0)
   // here might eventually be a "write to memory" section
   // we're not worrying about that now
   when (io.is_write) {
        program_mem(io.write_addr) := io.write_data
        io.valid := true.B
   } .elsewhen (io.boot) {
        pc := 0.U
        io.valid := true.B
   } .otherwise {
        when (op > 19.U) {
            io.valid := false.B
        } .otherwise {
           /* decode instruction */
            io.valid := true.B
            switch(op) {
                is (Opcode.add) {}
                is (Opcode.addi) {}
                is (Opcode.sub) {}
                is (Opcode.subi) {}
                is (Opcode.mul) {}
                is (Opcode.div) {}
                is (Opcode.mod) {}
                is (Opcode.and) {}
                is (Opcode.or) {}
                is (Opcode.xor) {}
                is (Opcode.not) {}
                is (Opcode.gre) {}
                is (Opcode.les) {}
                is (Opcode.equ) {}
                is (Opcode.mov) {}
                is (Opcode.ldi) {}
                is (Opcode.sto) {}
                is (Opcode.ld) {}
                is (Opcode.jmp) {}
                is (Opcode.skp) {}
            }
        }
        pc := pc + 1.U
   } 
   io.inst := inst 
   io.pc := pc
   io.imm := imm
   io.src := src
   io.op := op

      
}