package core

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class CoreTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "WildcatCore"
        it should "test instruction opcodes" in {
            test(new WildcatCore()) { c =>
                c.Opcode.all.foreach(println)
            }
        }
}

/*class CoreTester extends ChiselFlatSpec {
    behavior of "WildcatCore"
    backends foreach {backend =>
        it should s"test instructions for wildcat core" in {
            Driver(() => new WildcatCore)(c => new CoreTests(c)) should be (true)
        }
    }
}*/