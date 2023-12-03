package se.mattec.advent2019

fun main() {
    println(Day7.problem1())
    println(Day7.problem2())
}

object Day7 {

    fun problem1(): Int {
        var maxAmp = Int.MIN_VALUE

        for (amp1 in 0..4) {
            for (amp2 in 0..4) {
                for (amp3 in 0..4) {
                    for (amp4 in 0..4) {
                        for (amp5 in 0..4) {
                            if (listOf(amp1, amp2, amp3, amp4, amp5).distinct().size != 5) {
                                continue
                            }

                            val output1 = IntComputer().execute(listOf(amp1, 0))
                            val output2 = IntComputer().execute(listOf(amp2, output1))
                            val output3 = IntComputer().execute(listOf(amp3, output2))
                            val output4 = IntComputer().execute(listOf(amp4, output3))
                            val output5 = IntComputer().execute(listOf(amp5, output4))

                            if (output5 > maxAmp) {
                                maxAmp = output5
                            }
                        }
                    }
                }
            }
        }

        return maxAmp
    }

    fun problem2(): Int {
        var maxAmp = Int.MIN_VALUE

        for (amp1 in 5..9) {
            for (amp2 in 5..9) {
                for (amp3 in 5..9) {
                    for (amp4 in 5..9) {
                        for (amp5 in 5..9) {
                            if (listOf(amp1, amp2, amp3, amp4, amp5).distinct().size != 5) {
                                continue
                            }

                            val computer1 = IntComputer()
                            val computer2 = IntComputer()
                            val computer3 = IntComputer()
                            val computer4 = IntComputer()
                            val computer5 = IntComputer()

                            var loopInput = 0
                            var first = true

                            while (true) {
                                val output1 = computer1.execute(listOfNotNull(amp1.takeIf { first }, loopInput))
                                val output2 = computer2.execute(listOfNotNull(amp2.takeIf { first }, output1))
                                val output3 = computer3.execute(listOfNotNull(amp3.takeIf { first }, output2))
                                val output4 = computer4.execute(listOfNotNull(amp4.takeIf { first }, output3))
                                loopInput = computer5.execute(listOfNotNull(amp5.takeIf { first }, output4))
                                first = false
                                if (computer5.halted) {
                                    break
                                }
                            }

                            if (loopInput > maxAmp) {
                                maxAmp = loopInput
                            }
                        }
                    }
                }
            }
        }

        return maxAmp
    }

    class IntComputer {
        private val internalData = data.toTypedArray()
        private val outputs = mutableListOf<Int>()
        var halted = false
        var pointer = 0

        fun execute(inputs: List<Int>): Int {
            var inputIndex = 0

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
                val input = inputs[inputIndex++]
                data[outputPos] = input
                return 2
            }

            fun output(data: Array<Int>, opcode: Int, outputPos: Int): Int {
                val output = if (mode1(opcode) == 0) data[outputPos] else outputPos
                outputs += output
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
                val opcode = internalData[pointer]
                val pointerOffset = when (rawOpcode(opcode)) {
                    99 -> {
                        halted = true
                        break@loop
                    }
                    1 -> add(internalData, opcode, internalData[pointer + 1], internalData[pointer + 2], internalData[pointer + 3])
                    2 -> multiply(internalData, opcode, internalData[pointer + 1], internalData[pointer + 2], internalData[pointer + 3])
                    3 -> input(internalData, internalData[pointer + 1])
                    4 -> {
                        pointer += output(internalData, opcode, internalData[pointer + 1])
                        break@loop
                    }
                    5 -> jumpIfTrue(internalData, opcode, internalData[pointer + 1], internalData[pointer + 2])
                    6 -> jumpIfFalse(internalData, opcode, internalData[pointer + 1], internalData[pointer + 2])
                    7 -> lessThan(internalData, opcode, internalData[pointer + 1], internalData[pointer + 2], internalData[pointer + 3])
                    8 -> equals(internalData, opcode, internalData[pointer + 1], internalData[pointer + 2], internalData[pointer + 3])
                    else -> throw IllegalArgumentException("Unknown operation")
                }

                pointer += pointerOffset
            }

            return outputs.last()
        }

        private fun rawOpcode(opcode: Int) = opcode % 100

        private fun mode1(opcode: Int) = (opcode / 100) % 10

        private fun mode2(opcode: Int) = (opcode / 1000) % 10

        private fun mode3(opcode: Int) = (opcode / 10000) % 10
    }

    private val data = """
3,8,1001,8,10,8,105,1,0,0,21,46,59,72,93,110,191,272,353,434,99999,3,9,101,4,9,9,1002,9,3,9,1001,9,5,9,102,2,9,9,1001,9,5,9,4,9,99,3,9,1002,9,5,9,1001,9,5,9,4,9,99,3,9,101,4,9,9,1002,9,4,9,4,9,99,3,9,102,3,9,9,101,3,9,9,1002,9,2,9,1001,9,5,9,4,9,99,3,9,1001,9,2,9,102,4,9,9,101,2,9,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,99,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,99,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,99"""
            .trimIndent()
            .split(",")
            .map { it.toInt() }

}