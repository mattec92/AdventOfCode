package se.mattec.advent2019

fun main() {
    Day5.execute(1)
    Day5.execute(5)
}

object Day5 {

    fun execute(input: Int) {
        val data = data.toTypedArray()

        var pointer = 0

        fun add(data: Array<Int>, opcode: Int, param1: Int, param2: Int, outputPos: Int): Int {
            val value1 = if (mode1(opcode) == 0) data[param1] else param1
            val value2 = if (mode2(opcode) == 0) data[param2] else param2
            data[outputPos] = value1 + value2
            return 4
        }

        fun multiply(data: Array<Int>, opcode: Int, param1: Int, param2: Int, outputPos: Int): Int {
            val value1 = if (mode1(opcode) == 0) data[param1] else param1
            val value2 = if (mode2(opcode) == 0) data[param2] else param2
            data[outputPos] = value1 * value2
            return 4
        }

        fun input(data: Array<Int>, outputPos: Int): Int {
            println("Asking for input, providing $input")
            data[outputPos] = input
            return 2
        }

        fun output(data: Array<Int>, opcode: Int, outputPos: Int): Int {
            val output = if (mode1(opcode) == 0) data[outputPos] else outputPos
            println("Output is $output")
            return 2
        }

        fun jumpIfTrue(data: Array<Int>, opcode: Int, param1: Int, param2: Int): Int {
            val value1 = if (mode1(opcode) == 0) data[param1] else param1
            val value2 = if (mode2(opcode) == 0) data[param2] else param2
            if (value1 != 0) {
                pointer = value2
                return 0
            }
            return 3
        }

        fun jumpIfFalse(data: Array<Int>, opcode: Int, param1: Int, param2: Int): Int {
            val value1 = if (mode1(opcode) == 0) data[param1] else param1
            val value2 = if (mode2(opcode) == 0) data[param2] else param2
            if (value1 == 0) {
                pointer = value2
                return 0
            }
            return 3
        }

        fun lessThan(data: Array<Int>, opcode: Int, param1: Int, param2: Int, outputPos: Int): Int {
            val value1 = if (mode1(opcode) == 0) data[param1] else param1
            val value2 = if (mode2(opcode) == 0) data[param2] else param2
            if (value1 < value2) {
                data[outputPos] = 1
            } else {
                data[outputPos] = 0
            }
            return 4
        }

        fun equals(data: Array<Int>, opcode: Int, param1: Int, param2: Int, outputPos: Int): Int {
            val value1 = if (mode1(opcode) == 0) data[param1] else param1
            val value2 = if (mode2(opcode) == 0) data[param2] else param2
            if (value1 == value2) {
                data[outputPos] = 1
            } else {
                data[outputPos] = 0
            }
            return 4
        }

        loop@ while (true) {
            val opcode = data[pointer]
            val pointerOffset = when (rawOpcode(opcode)) {
                99 -> break@loop
                1 -> add(data, opcode, data[pointer + 1], data[pointer + 2], data[pointer + 3])
                2 -> multiply(data, opcode, data[pointer + 1], data[pointer + 2], data[pointer + 3])
                3 -> input(data, data[pointer + 1])
                4 -> output(data, opcode, data[pointer + 1])
                5 -> jumpIfTrue(data, opcode, data[pointer + 1], data[pointer + 2])
                6 -> jumpIfFalse(data, opcode, data[pointer + 1], data[pointer + 2])
                7 -> lessThan(data, opcode, data[pointer + 1], data[pointer + 2], data[pointer + 3])
                8 -> equals(data, opcode, data[pointer + 1], data[pointer + 2], data[pointer + 3])
                else -> throw IllegalArgumentException("Unknown operation")
            }

            pointer += pointerOffset
        }
    }

    private fun rawOpcode(opcode: Int) = opcode % 100

    private fun mode1(opcode: Int) = (opcode / 100) % 10

    private fun mode2(opcode: Int) = (opcode / 1000) % 10

    private fun mode3(opcode: Int) = (opcode / 10000) % 10

    private val data = """
3,225,1,225,6,6,1100,1,238,225,104,0,1,191,196,224,1001,224,-85,224,4,224,1002,223,8,223,1001,224,4,224,1,223,224,223,1101,45,50,225,1102,61,82,225,101,44,39,224,101,-105,224,224,4,224,102,8,223,223,101,5,224,224,1,224,223,223,102,14,187,224,101,-784,224,224,4,224,102,8,223,223,101,7,224,224,1,224,223,223,1001,184,31,224,1001,224,-118,224,4,224,102,8,223,223,1001,224,2,224,1,223,224,223,1102,91,18,225,2,35,110,224,101,-810,224,224,4,224,102,8,223,223,101,3,224,224,1,223,224,223,1101,76,71,224,1001,224,-147,224,4,224,102,8,223,223,101,2,224,224,1,224,223,223,1101,7,16,225,1102,71,76,224,101,-5396,224,224,4,224,1002,223,8,223,101,5,224,224,1,224,223,223,1101,72,87,225,1101,56,77,225,1102,70,31,225,1102,29,15,225,1002,158,14,224,1001,224,-224,224,4,224,102,8,223,223,101,1,224,224,1,223,224,223,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,1007,226,226,224,1002,223,2,223,1006,224,329,1001,223,1,223,8,226,677,224,1002,223,2,223,1005,224,344,1001,223,1,223,107,226,677,224,1002,223,2,223,1006,224,359,1001,223,1,223,8,677,677,224,1002,223,2,223,1005,224,374,1001,223,1,223,1108,226,226,224,1002,223,2,223,1005,224,389,1001,223,1,223,7,677,226,224,1002,223,2,223,1005,224,404,101,1,223,223,7,226,226,224,102,2,223,223,1006,224,419,1001,223,1,223,1108,226,677,224,102,2,223,223,1005,224,434,1001,223,1,223,1107,226,226,224,1002,223,2,223,1006,224,449,1001,223,1,223,1007,677,677,224,102,2,223,223,1006,224,464,1001,223,1,223,107,226,226,224,1002,223,2,223,1005,224,479,101,1,223,223,1107,677,226,224,1002,223,2,223,1005,224,494,1001,223,1,223,1008,677,677,224,102,2,223,223,1005,224,509,101,1,223,223,107,677,677,224,102,2,223,223,1005,224,524,1001,223,1,223,1108,677,226,224,1002,223,2,223,1005,224,539,1001,223,1,223,7,226,677,224,102,2,223,223,1006,224,554,1001,223,1,223,8,677,226,224,1002,223,2,223,1006,224,569,101,1,223,223,108,226,226,224,1002,223,2,223,1006,224,584,1001,223,1,223,1107,226,677,224,1002,223,2,223,1006,224,599,101,1,223,223,1008,226,226,224,102,2,223,223,1005,224,614,1001,223,1,223,1007,226,677,224,1002,223,2,223,1006,224,629,1001,223,1,223,108,677,226,224,102,2,223,223,1005,224,644,101,1,223,223,1008,226,677,224,1002,223,2,223,1005,224,659,101,1,223,223,108,677,677,224,1002,223,2,223,1006,224,674,1001,223,1,223,4,223,99,226
    """.trimIndent()
            .split(",")
            .map { it.toInt() }

}