package se.mattec.advent2019

fun main(args: Array<String>) {
    println(Day11.problem1())
    Day11.problem2()
}

object Day11 {

    fun problem1(): Int {
        return tiles(0).size
    }

    private fun tiles(initialTileColor: Long): Map<Pair<Int, Int>, Long> {
        val computer = LongComputer(data)

        val tiles = mutableMapOf<Pair<Int, Int>, Long>()
        var currentTile = 0 to 0
        var direction = 0

        while (true) {
            val input = tiles[currentTile] ?: if (currentTile == 0 to 0) initialTileColor else 0L

            val output1 = computer.execute(listOf(input))

            val output2 = computer.execute(emptyList())

            if (computer.halted) {
                break
            }

            tiles[currentTile] = output1

            direction = when (output2) {
                0L -> when (direction) {
                    0 -> 270
                    90 -> 0
                    180 -> 90
                    270 -> 180
                    else -> throw IllegalArgumentException("Unknown direction")
                }
                1L -> when (direction) {
                    0 -> 90
                    90 -> 180
                    180 -> 270
                    270 -> 0
                    else -> throw IllegalArgumentException("Unknown direction")
                }
                else -> throw IllegalArgumentException("Unknown turning direction.")
            }

            currentTile = when (direction) {
                0 -> currentTile.first to currentTile.second - 1
                90 -> currentTile.first + 1 to currentTile.second
                180 -> currentTile.first to currentTile.second + 1
                270 -> currentTile.first - 1 to currentTile.second
                else -> throw IllegalArgumentException("Uknown direction")
            }
        }

        return tiles
    }

    fun problem2() {
        val tiles = tiles(1)

        val maxX = tiles.keys.maxBy { it.first }!!.first
        val maxY = tiles.keys.maxBy { it.second }!!.second
        val minX = tiles.keys.minBy { it.first }!!.first
        val minY = tiles.keys.minBy { it.second }!!.second

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val tileColor = tiles[x to y]
                print(if (tileColor == 0L || tileColor == null) " " else "X")
            }
            print("\n")
        }
    }

    class LongComputer(program : List<Long>) {
        private val internalData = program.toTypedArray()
        private val outputs = mutableListOf<Long>()
        private var relativeBase = 0
        private var pointer = 0
        var halted = false

        fun execute(inputs: List<Long>): Long {
            var inputIndex = 0

            fun add(data: Array<Long>, opcode: Long, param1: Long, param2: Long, outputPos: Long): Long {
                val value1 = value(relativeBase, data, mode1(opcode), param1)
                val value2 = value(relativeBase, data, mode2(opcode), param2)
                val output = outputPos(relativeBase, mode3(opcode), outputPos)
                data[output.toInt()] = value1 + value2
                return 4
            }


            fun multiply(data: Array<Long>, opcode: Long, param1: Long, param2: Long, outputPos: Long): Long {
                val value1 = value(relativeBase, data, mode1(opcode), param1)
                val value2 = value(relativeBase, data, mode2(opcode), param2)
                val output = outputPos(relativeBase, mode3(opcode), outputPos)
                data[output.toInt()] = value1 * value2
                return 4
            }

            fun input(data: Array<Long>, opcode: Long, outputPos: Long): Long {
                val input = inputs[inputIndex++]
                val output = outputPos(relativeBase, mode1(opcode), outputPos)
                data[output.toInt()] = input
                return 2
            }

            fun output(data: Array<Long>, opcode: Long, outputPos: Long): Long {
                val output = value(relativeBase, data, mode1(opcode), outputPos)
                outputs += output
                return 2
            }

            fun jumpIfTrue(data: Array<Long>, opcode: Long, param1: Long, param2: Long): Long {
                val value1 = value(relativeBase, data, mode1(opcode), param1)
                val value2 = value(relativeBase, data, mode2(opcode), param2)
                if (value1 != 0L) {
                    pointer = value2.toInt()
                    return 0
                }
                return 3
            }

            fun jumpIfFalse(data: Array<Long>, opcode: Long, param1: Long, param2: Long): Long {
                val value1 = value(relativeBase, data, mode1(opcode), param1)
                val value2 = value(relativeBase, data, mode2(opcode), param2)
                if (value1 == 0L) {
                    pointer = value2.toInt()
                    return 0
                }
                return 3
            }

            fun lessThan(data: Array<Long>, opcode: Long, param1: Long, param2: Long, outputPos: Long): Long {
                val value1 = value(relativeBase, data, mode1(opcode), param1)
                val value2 = value(relativeBase, data, mode2(opcode), param2)
                val output = outputPos(relativeBase, mode3(opcode), outputPos)
                if (value1 < value2) {
                    data[output.toInt()] = 1
                } else {
                    data[output.toInt()] = 0
                }
                return 4
            }

            fun equals(data: Array<Long>, opcode: Long, param1: Long, param2: Long, outputPos: Long): Long {
                val value1 = value(relativeBase, data, mode1(opcode), param1)
                val value2 = value(relativeBase, data, mode2(opcode), param2)
                val output = outputPos(relativeBase, mode3(opcode), outputPos)
                if (value1 == value2) {
                    data[output.toInt()] = 1
                } else {
                    data[output.toInt()] = 0
                }
                return 4
            }

            fun updateRelativeBase(data: Array<Long>, opcode: Long, param1: Long): Long {
                relativeBase += value(relativeBase, data, mode1(opcode), param1).toInt()
                return 2
            }

            loop@ while (true) {
                val opcode = internalData[pointer.toInt()]
                val pointerOffset = when (rawOpcode(opcode)) {
                    99L -> {
                        halted = true
                        break@loop
                    }
                    1L -> add(internalData, opcode, internalData[pointer.toInt() + 1], internalData[pointer + 2], internalData[pointer + 3])
                    2L -> multiply(internalData, opcode, internalData[pointer + 1], internalData[pointer + 2], internalData[pointer + 3])
                    3L -> input(internalData, opcode, internalData[pointer + 1])
                    4L -> {
                        pointer += output(internalData, opcode, internalData[pointer + 1]).toInt()
                        break@loop
                    }
                    5L -> jumpIfTrue(internalData, opcode, internalData[pointer + 1], internalData[pointer + 2])
                    6L -> jumpIfFalse(internalData, opcode, internalData[pointer + 1], internalData[pointer + 2])
                    7L -> lessThan(internalData, opcode, internalData[pointer + 1], internalData[pointer + 2], internalData[pointer + 3])
                    8L -> equals(internalData, opcode, internalData[pointer + 1], internalData[pointer + 2], internalData[pointer + 3])
                    9L -> updateRelativeBase(internalData, opcode, internalData[pointer + 1])
                    else -> throw IllegalArgumentException("Unknown operation")
                }

                pointer += pointerOffset.toInt()
            }

            return outputs.last()
        }

        private fun rawOpcode(opcode: Long) = opcode % 100L

        private fun mode1(opcode: Long) = (opcode / 100L) % 10L

        private fun mode2(opcode: Long) = (opcode / 1000L) % 10L

        private fun mode3(opcode: Long) = (opcode / 10000L) % 10L

        private fun value(relativeBase: Int, data: Array<Long>, opcode: Long, param: Long) = when (opcode) {
            0L -> data[param.toInt()]
            1L -> param
            2L -> data[(relativeBase + param).toInt()]
            else -> throw IllegalArgumentException("Unknown mode")
        }

        private fun outputPos(relativeBase: Int, opcode: Long, outputPos: Long) = when (opcode) {
            0L -> outputPos
            1L -> throw IllegalArgumentException("Input never has instant mode.")
            2L -> relativeBase + outputPos
            else -> throw IllegalArgumentException("Unknown mode")
        }
    }

    private val data = """
3,8,1005,8,328,1106,0,11,0,0,0,104,1,104,0,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,1002,8,1,28,1,1003,10,10,3,8,1002,8,-1,10,101,1,10,10,4,10,108,1,8,10,4,10,102,1,8,54,2,1103,6,10,3,8,1002,8,-1,10,101,1,10,10,4,10,108,0,8,10,4,10,101,0,8,80,3,8,1002,8,-1,10,1001,10,1,10,4,10,108,1,8,10,4,10,1002,8,1,102,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,1001,8,0,124,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,1,10,4,10,1001,8,0,147,1006,0,35,1,7,3,10,2,106,13,10,2,1104,9,10,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,1002,8,1,183,2,7,16,10,2,105,14,10,1,1002,12,10,1006,0,13,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,1002,8,1,220,1006,0,78,2,5,3,10,1006,0,92,1006,0,92,3,8,1002,8,-1,10,101,1,10,10,4,10,108,1,8,10,4,10,1001,8,0,255,1006,0,57,2,1001,11,10,1006,0,34,2,1007,18,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,1,10,4,10,1002,8,1,292,2,109,3,10,1,1103,14,10,2,2,5,10,2,1006,3,10,101,1,9,9,1007,9,997,10,1005,10,15,99,109,650,104,0,104,1,21101,932700762920,0,1,21101,0,345,0,1105,1,449,21102,1,386577306516,1,21102,356,1,0,1106,0,449,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,21101,179355975827,0,1,21101,403,0,0,1106,0,449,21102,1,46413220903,1,21102,1,414,0,1106,0,449,3,10,104,0,104,0,3,10,104,0,104,0,21101,988224959252,0,1,21102,1,437,0,1106,0,449,21101,717637968660,0,1,21101,0,448,0,1106,0,449,99,109,2,22101,0,-1,1,21102,40,1,2,21101,480,0,3,21101,470,0,0,1106,0,513,109,-2,2105,1,0,0,1,0,0,1,109,2,3,10,204,-1,1001,475,476,491,4,0,1001,475,1,475,108,4,475,10,1006,10,507,1102,1,0,475,109,-2,2105,1,0,0,109,4,2102,1,-1,512,1207,-3,0,10,1006,10,530,21102,1,0,-3,22102,1,-3,1,22101,0,-2,2,21102,1,1,3,21101,0,549,0,1105,1,554,109,-4,2105,1,0,109,5,1207,-3,1,10,1006,10,577,2207,-4,-2,10,1006,10,577,21202,-4,1,-4,1106,0,645,21202,-4,1,1,21201,-3,-1,2,21202,-2,2,3,21102,1,596,0,1106,0,554,21201,1,0,-4,21101,1,0,-1,2207,-4,-2,10,1006,10,615,21101,0,0,-1,22202,-2,-1,-2,2107,0,-3,10,1006,10,637,21201,-1,0,1,21101,0,637,0,105,1,512,21202,-2,-1,-2,22201,-4,-2,-4,109,-5,2105,1,0"""
            .plus("," + Array(100000) { 0 }.joinToString(","))
            .trimIndent()
            .split(",")
            .map { it.toLong() }

}