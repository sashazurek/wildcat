package core

import chisel3._
import chisel3.util._
import chisel3.experimental.ChiselEnum

object Instructions {
    /* Arithmetic */
    def ADD  = BitPat("b???????????????????00000") //0
    def ADDI = BitPat("b???????????????????01101") //13
    def SUB  = BitPat("b???????????????????00001") //1
    def SUBI = BitPat("b???????????????????01110") //14
    def MUL  = BitPat("b???????????????????00010") //2
    def DIV  = BitPat("b???????????????????00101") //3
    def MOD  = BitPat("b???????????????????00110") //4
    /* Load/Store */
    def MOV  = BitPat("b???????????????????00101") //5
    def LDI  = BitPat("b???????????????????01111") //15
    def STO  = BitPat("b???????????????????10000") //16
    def LD   = BitPat("b???????????????????10001") //17
    /* Logic */
    def AND  = BitPat("b???????????????????00110") //6
    def OR   = BitPat("b???????????????????00111") //7
    def XOR  = BitPat("b???????????????????01000") //8
    def NOT  = BitPat("b???????????????????01001") //9
    /* Conditionals */
    def GRE  = BitPat("b???????????????????01010") //10
    def LES  = BitPat("b???????????????????01011") //11
    def EQU  = BitPat("b???????????????????01100") //12
    /* Control flow */
    def JMP  = BitPat("b???????????????????10010") //18
    def SKP  = BitPat("b???????????????????10011") //19
}

object Opcode {
/*register-register*/
   /* arithmetic */
   val add = 0.U
   val sub = 1.U
   val mul = 2.U
   val div = 3.U
   val mod = 4.U
   /* load/store */
   val mov = 5.U
   /* boolean */
   val and = 6.U
   val or = 7.U
   val xor = 8.U
   val not = 9.U
   /* conditional */
   val gre = 10.U
   val les = 11.U
   val equ = 12.U

/* register-immediate */
   /* arithmetic */
   val addi = 13.U
   val subi = 14.U
   /* load/store */
   val ldi = 15.U
   val sto = 16.U
   val ld = 17.U
   /* branch */
   val jmp = 18.U
   val skp = 19.U
   val bsl = 20.U
   val bsr = 21.U
}
