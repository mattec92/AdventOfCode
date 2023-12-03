package se.mattec.advent2018

fun main() {
    println(Day19.problem1())
    println(Day19.problem2())
}

object Day19 {

    fun problem1(): Int {
        var registers = IntArray(6) { 0 }.asList().toMutableList()

        while (registers[instructionPointerIndex] < instructions.size) {
            val rawInstruction = instructions[registers[instructionPointerIndex]]
            val instruction = allInstructions[rawInstruction[0]]!!
            registers = perform(rawInstruction, instruction, registers) as MutableList<Int>
            registers[instructionPointerIndex]++
        }

        return registers[0]
    }

    private fun perform(rawInstruction: List<String>, instruction: Day16.Instruction, registers: List<Int>): List<Int> {
        val convertedInstruction = rawInstruction.map {
            try {
                it.toInt()
            } catch (t: Throwable) {
                //Instruction needs an int list of size 4 as input
                0
            }
        }
        return instruction.perform(registers, convertedInstruction)
    }

    //Does not complete in reasonable time.
    fun problem2(): Int {
        var registers = IntArray(6) { 0 }.asList().toMutableList()
        registers[0] = 1

        while (registers[instructionPointerIndex] < instructions.size) {
            val rawInstruction = instructions[registers[instructionPointerIndex]]
            val instruction = allInstructions[rawInstruction[0]]!!
            registers = perform(rawInstruction, instruction, registers) as MutableList<Int>
            registers[instructionPointerIndex]++
        }

        return registers[0]
    }

    private val allInstructions = mapOf(
            "addr" to Day16.addr,
            "addi" to Day16.addi,
            "mulr" to Day16.multr,
            "muli" to Day16.multi,
            "banr" to Day16.banr,
            "bani" to Day16.bani,
            "borr" to Day16.borr,
            "bori" to Day16.bori,
            "setr" to Day16.setr,
            "seti" to Day16.seti,
            "gtir" to Day16.gtir,
            "gtri" to Day16.gtri,
            "gtrr" to Day16.gtrr,
            "eqir" to Day16.eqir,
            "eqri" to Day16.eqri,
            "eqrr" to Day16.eqrr
    )

    private val testData = """
#ip 0
seti 5 0 1
seti 6 0 2
addi 0 1 0
addr 1 2 3
setr 1 0 0
seti 8 0 4
seti 9 0 5
    """.trimIndent()
            .split("\n")

    private val data = """
#ip 4
addi 4 16 4
seti 1 9 3
seti 1 6 2
mulr 3 2 5
eqrr 5 1 5
addr 5 4 4
addi 4 1 4
addr 3 0 0
addi 2 1 2
gtrr 2 1 5
addr 4 5 4
seti 2 9 4
addi 3 1 3
gtrr 3 1 5
addr 5 4 4
seti 1 0 4
mulr 4 4 4
addi 1 2 1
mulr 1 1 1
mulr 4 1 1
muli 1 11 1
addi 5 1 5
mulr 5 4 5
addi 5 2 5
addr 1 5 1
addr 4 0 4
seti 0 1 4
setr 4 3 5
mulr 5 4 5
addr 4 5 5
mulr 4 5 5
muli 5 14 5
mulr 5 4 5
addr 1 5 1
seti 0 6 0
seti 0 7 4
    """.trimIndent()
            .split("\n")

    val instructionPointerIndex = data[0].split(" ")[1].toInt()
    val instructions = data.drop(1).map { it.split(" ") }

}