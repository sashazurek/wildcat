package core

import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage
import chisel3.experimental.ChiselEnum

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
   /*val io = IO(new Bundle {
     /* I wonder if we can implement PC through this */
        val out = Output(UInt(24.W))
   })*/
   object Opcode extends ChiselEnum {
       /* arithmetic */
       val add = Value(0.U)
       val addi = Value(1.U)
       val sub = Value(2.U)
       val subi = Value(3.U)
       val mul = Value(4.U)
       val div = Value(5.U)
       val mod = Value(6.U)
       /* load/store */
       val mov = Value(7.U)
       val ldi = Value(8.U)
       val sto = Value(9.U)
       val ld = Value(10.U)
       /* boolean */
       val and = Value(11.U)
       val or = Value(12.U)
       val xor = Value(13.U)
       val not = Value(14.U)
       /* conditional */
       val gre = Value(15.U)
       val les = Value(16.U)
       val equ = Value(17.U)
       /* branch */
       val jmp = Value(18.U)
       val skp = Value(19.U)
   }

   val pc = Reg(UInt(24.W)) /* this is a 24-bit core */
   val status = Reg(Bool()) /* one flag, for "previous cond pass", i guess */
   /* adding these as stub until needed
   val working_mem = Mem()
   val program_mem = Mem()
   */

   /* decode logic */
  
   /* ... doing logic? */
}