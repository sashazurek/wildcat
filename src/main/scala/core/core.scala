package core

import chisel3._


class WildcatCore() {
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
       val in = Input(UInt(width.W))
       val out = Output(UInt(width.W))
   })
   val pc = Reg(Uint(24.U)) /* this is a 24-bit core */
   val status = Reg(Uint(1.U)) /* one flag, for "previous cond pass", i guess */

   /* decode logic */
  
   /* ... doing logic? */
}