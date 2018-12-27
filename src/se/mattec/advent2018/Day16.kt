package se.mattec.advent2018

fun main(args: Array<String>) {
    println(Day16.problem1())
    println(Day16.problem2())
}

object Day16 {

    fun problem1(): Int {
        return findMatches().count { it.value.size >= 3 }
    }

    private fun findMatches(): MutableMap<Test, MutableList<Instruction>> {
        val allInstructions = listOf(addr, addi, multr, multi, banr, bani, borr, bori, setr, seti, gtir, gtri, gtrr, eqir, eqri, eqrr)

        val availableInstructions = mutableMapOf<Int, MutableList<Instruction>>()
        for (i in 0 until allInstructions.size) {
            availableInstructions[i] = allInstructions.toMutableList()
        }

        val matches = mutableMapOf<Test, MutableList<Instruction>>()
        for (testData in data) {
            val instructions = availableInstructions[testData.instruction[0]]!!
            for (instruction in instructions) {
                try {
                    val result = instruction.perform(testData.before, testData.instruction)
                    if (result == testData.after) {
                        matches.getOrPut(testData) { mutableListOf() }.add(instruction)
                    }
                } catch (ignore: Throwable) {
                    //Some test data may try to refer to indices of registers that does not exist
                }
            }
        }

        return matches
    }

    fun problem2(): Int {
        val matches = findMatches()

        val mapping = mutableMapOf<Int, Instruction>()
        while (mapping.size < 16) {
            val singleMatches = matches.filter { it.value.size == 1 }.map { it.key to it.value }.groupBy { it.second }
            for (match in singleMatches) {
                val instructionNumber = match.value[0].first.instruction[0]
                val instruction = match.key[0]
                mapping[instructionNumber] = instruction
                matches.forEach { _, instructions -> instructions.remove(instruction) }
            }
        }

        var registers = listOf(0, 0, 0, 0)

        for (instruction in program) {
            registers = mapping[instruction[0]]!!.perform(registers, instruction)
        }

        return registers[0]
    }
    //0 incorrect

    val addr = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = registers[arguments[1]] + registers[arguments[2]]
            }
        }
    }

    val addi = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = registers[arguments[1]] + arguments[2]
            }
        }
    }

    val multr = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = registers[arguments[1]] * registers[arguments[2]]
            }
        }
    }

    val multi = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = registers[arguments[1]] * arguments[2]
            }
        }
    }

    val banr = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = registers[arguments[1]] and registers[arguments[2]]
            }
        }
    }

    val bani = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = registers[arguments[1]] and arguments[2]
            }
        }
    }

    val borr = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = registers[arguments[1]] or registers[arguments[2]]
            }
        }
    }

    val bori = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = registers[arguments[1]] or arguments[2]
            }
        }
    }

    val setr = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = registers[arguments[1]]
            }
        }
    }

    val seti = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = arguments[1]
            }
        }
    }

    val gtir = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = if (arguments[1] > registers[arguments[2]]) 1 else 0
            }
        }
    }

    val gtri = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = if (registers[arguments[1]] > arguments[2]) 1 else 0
            }
        }
    }

    val gtrr = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = if (registers[arguments[1]] > registers[arguments[2]]) 1 else 0
            }
        }
    }

    val eqir = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = if (arguments[1] == registers[arguments[2]]) 1 else 0
            }
        }
    }

    val eqri = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = if (registers[arguments[1]] == arguments[2]) 1 else 0
            }
        }
    }

    val eqrr = object : Instruction {
        override fun perform(registers: List<Int>, arguments: List<Int>): List<Int> {
            return registers.toMutableList().also {
                it[arguments[3]] = if (registers[arguments[1]] == registers[arguments[2]]) 1 else 0
            }
        }
    }

    interface Instruction {

        fun perform(registers: List<Int>, arguments: List<Int>): List<Int>

    }

    private data class Test(val before: List<Int>, val after: List<Int>, val instruction: List<Int>)

    private val data = """
Before: [0, 2, 0, 2]
6 0 1 1
After:  [0, 1, 0, 2]

Before: [0, 3, 0, 1]
3 3 1 3
After:  [0, 3, 0, 1]

Before: [2, 0, 3, 3]
7 2 0 2
After:  [2, 0, 2, 3]

Before: [0, 2, 3, 3]
13 0 3 0
After:  [3, 2, 3, 3]

Before: [2, 2, 3, 3]
7 2 1 3
After:  [2, 2, 3, 2]

Before: [0, 2, 0, 0]
4 0 1 0
After:  [0, 2, 0, 0]

Before: [2, 0, 3, 2]
15 3 2 0
After:  [4, 0, 3, 2]

Before: [3, 3, 0, 3]
12 3 3 0
After:  [3, 3, 0, 3]

Before: [2, 1, 1, 0]
14 3 1 3
After:  [2, 1, 1, 1]

Before: [2, 2, 3, 2]
7 2 3 1
After:  [2, 2, 3, 2]

Before: [0, 3, 2, 1]
5 2 1 3
After:  [0, 3, 2, 3]

Before: [0, 2, 2, 3]
10 1 2 0
After:  [4, 2, 2, 3]

Before: [2, 3, 2, 2]
5 0 1 2
After:  [2, 3, 3, 2]

Before: [2, 0, 3, 0]
7 2 0 1
After:  [2, 2, 3, 0]

Before: [3, 2, 3, 0]
6 3 2 1
After:  [3, 2, 3, 0]

Before: [0, 3, 3, 0]
4 0 1 0
After:  [0, 3, 3, 0]

Before: [0, 0, 2, 0]
1 0 0 1
After:  [0, 0, 2, 0]

Before: [0, 2, 3, 3]
1 0 0 0
After:  [0, 2, 3, 3]

Before: [1, 3, 1, 0]
9 0 1 3
After:  [1, 3, 1, 1]

Before: [1, 3, 1, 3]
9 0 1 1
After:  [1, 1, 1, 3]

Before: [2, 0, 3, 2]
6 1 3 2
After:  [2, 0, 3, 2]

Before: [1, 1, 0, 0]
14 3 1 0
After:  [1, 1, 0, 0]

Before: [2, 3, 0, 0]
5 0 1 3
After:  [2, 3, 0, 3]

Before: [2, 3, 2, 3]
10 0 2 2
After:  [2, 3, 4, 3]

Before: [0, 1, 3, 3]
13 0 3 0
After:  [3, 1, 3, 3]

Before: [2, 0, 1, 2]
15 0 2 2
After:  [2, 0, 4, 2]

Before: [2, 1, 0, 3]
14 2 1 2
After:  [2, 1, 1, 3]

Before: [0, 3, 2, 2]
4 0 2 0
After:  [0, 3, 2, 2]

Before: [1, 0, 3, 3]
12 3 1 1
After:  [1, 3, 3, 3]

Before: [0, 2, 3, 2]
7 2 3 3
After:  [0, 2, 3, 2]

Before: [1, 1, 2, 3]
10 1 3 3
After:  [1, 1, 2, 3]

Before: [0, 2, 1, 3]
1 0 0 1
After:  [0, 0, 1, 3]

Before: [3, 1, 2, 2]
2 3 1 3
After:  [3, 1, 2, 3]

Before: [1, 0, 3, 3]
10 0 2 0
After:  [3, 0, 3, 3]

Before: [1, 0, 2, 3]
11 0 1 0
After:  [1, 0, 2, 3]

Before: [2, 1, 2, 2]
13 1 3 2
After:  [2, 1, 3, 2]

Before: [0, 1, 2, 1]
2 3 2 0
After:  [3, 1, 2, 1]

Before: [2, 0, 0, 3]
15 0 2 3
After:  [2, 0, 0, 4]

Before: [1, 3, 3, 2]
15 3 2 1
After:  [1, 4, 3, 2]

Before: [1, 2, 3, 0]
10 0 2 1
After:  [1, 3, 3, 0]

Before: [2, 2, 2, 1]
2 3 2 3
After:  [2, 2, 2, 3]

Before: [3, 3, 2, 1]
10 2 2 2
After:  [3, 3, 4, 1]

Before: [3, 2, 2, 2]
10 3 2 2
After:  [3, 2, 4, 2]

Before: [1, 1, 3, 1]
2 3 2 0
After:  [3, 1, 3, 1]

Before: [2, 1, 1, 0]
14 3 1 1
After:  [2, 1, 1, 0]

Before: [1, 0, 2, 1]
11 3 1 0
After:  [1, 0, 2, 1]

Before: [1, 3, 0, 2]
9 0 1 1
After:  [1, 1, 0, 2]

Before: [0, 1, 1, 0]
4 0 2 0
After:  [0, 1, 1, 0]

Before: [3, 3, 2, 1]
5 2 1 0
After:  [3, 3, 2, 1]

Before: [3, 1, 1, 0]
14 3 1 3
After:  [3, 1, 1, 1]

Before: [1, 3, 2, 3]
5 2 1 1
After:  [1, 3, 2, 3]

Before: [1, 2, 2, 1]
2 3 2 2
After:  [1, 2, 3, 1]

Before: [3, 3, 3, 2]
15 3 2 0
After:  [4, 3, 3, 2]

Before: [0, 3, 2, 1]
5 2 1 2
After:  [0, 3, 3, 1]

Before: [1, 1, 0, 2]
2 3 1 2
After:  [1, 1, 3, 2]

Before: [3, 1, 0, 3]
8 3 2 3
After:  [3, 1, 0, 6]

Before: [0, 1, 1, 3]
1 0 0 0
After:  [0, 1, 1, 3]

Before: [2, 3, 2, 3]
5 0 1 3
After:  [2, 3, 2, 3]

Before: [3, 0, 2, 1]
2 3 2 0
After:  [3, 0, 2, 1]

Before: [2, 3, 3, 1]
3 3 1 0
After:  [1, 3, 3, 1]

Before: [2, 2, 3, 3]
15 1 2 2
After:  [2, 2, 4, 3]

Before: [1, 2, 1, 1]
9 3 1 1
After:  [1, 1, 1, 1]

Before: [1, 3, 1, 1]
3 3 1 1
After:  [1, 1, 1, 1]

Before: [1, 3, 0, 1]
9 0 1 3
After:  [1, 3, 0, 1]

Before: [0, 1, 3, 2]
2 3 1 2
After:  [0, 1, 3, 2]

Before: [1, 3, 2, 3]
0 3 2 1
After:  [1, 2, 2, 3]

Before: [0, 2, 0, 2]
15 3 2 3
After:  [0, 2, 0, 4]

Before: [0, 1, 3, 3]
1 0 0 1
After:  [0, 0, 3, 3]

Before: [0, 2, 3, 2]
7 2 1 1
After:  [0, 2, 3, 2]

Before: [0, 2, 3, 2]
7 2 3 2
After:  [0, 2, 2, 2]

Before: [1, 0, 2, 1]
2 3 2 2
After:  [1, 0, 3, 1]

Before: [1, 2, 3, 0]
8 1 3 1
After:  [1, 6, 3, 0]

Before: [2, 3, 0, 1]
5 0 1 2
After:  [2, 3, 3, 1]

Before: [3, 2, 2, 2]
10 1 2 3
After:  [3, 2, 2, 4]

Before: [0, 3, 1, 2]
8 3 3 2
After:  [0, 3, 6, 2]

Before: [3, 0, 3, 1]
11 3 1 3
After:  [3, 0, 3, 1]

Before: [3, 3, 3, 1]
3 3 1 0
After:  [1, 3, 3, 1]

Before: [1, 2, 3, 1]
7 2 1 0
After:  [2, 2, 3, 1]

Before: [0, 0, 2, 0]
4 0 2 0
After:  [0, 0, 2, 0]

Before: [0, 3, 1, 1]
3 3 1 2
After:  [0, 3, 1, 1]

Before: [1, 1, 0, 2]
14 2 1 0
After:  [1, 1, 0, 2]

Before: [3, 3, 1, 1]
8 1 3 1
After:  [3, 9, 1, 1]

Before: [1, 0, 0, 1]
6 0 3 0
After:  [3, 0, 0, 1]

Before: [0, 2, 2, 0]
12 2 0 3
After:  [0, 2, 2, 2]

Before: [0, 2, 3, 3]
12 3 0 0
After:  [3, 2, 3, 3]

Before: [0, 1, 2, 1]
13 1 2 1
After:  [0, 3, 2, 1]

Before: [3, 3, 3, 0]
0 2 2 0
After:  [2, 3, 3, 0]

Before: [0, 1, 0, 3]
13 0 3 3
After:  [0, 1, 0, 3]

Before: [0, 1, 2, 1]
13 0 2 2
After:  [0, 1, 2, 1]

Before: [2, 3, 2, 3]
0 3 2 3
After:  [2, 3, 2, 2]

Before: [2, 3, 2, 0]
5 2 1 2
After:  [2, 3, 3, 0]

Before: [2, 0, 3, 0]
7 2 0 3
After:  [2, 0, 3, 2]

Before: [0, 3, 2, 2]
5 2 1 2
After:  [0, 3, 3, 2]

Before: [3, 2, 1, 2]
15 1 2 3
After:  [3, 2, 1, 4]

Before: [3, 3, 3, 1]
3 3 1 1
After:  [3, 1, 3, 1]

Before: [0, 1, 0, 3]
4 0 1 2
After:  [0, 1, 0, 3]

Before: [3, 3, 3, 1]
3 3 1 3
After:  [3, 3, 3, 1]

Before: [2, 1, 3, 0]
13 0 1 1
After:  [2, 3, 3, 0]

Before: [3, 1, 2, 2]
12 2 2 3
After:  [3, 1, 2, 2]

Before: [0, 0, 2, 1]
1 0 0 2
After:  [0, 0, 0, 1]

Before: [0, 2, 3, 1]
2 3 2 3
After:  [0, 2, 3, 3]

Before: [2, 0, 3, 0]
4 1 0 0
After:  [0, 0, 3, 0]

Before: [2, 0, 3, 2]
7 2 3 3
After:  [2, 0, 3, 2]

Before: [2, 2, 2, 1]
12 2 0 3
After:  [2, 2, 2, 2]

Before: [3, 1, 0, 2]
6 1 2 2
After:  [3, 1, 3, 2]

Before: [1, 0, 3, 0]
11 0 1 0
After:  [1, 0, 3, 0]

Before: [0, 3, 1, 3]
12 3 1 1
After:  [0, 3, 1, 3]

Before: [2, 1, 0, 1]
14 2 1 0
After:  [1, 1, 0, 1]

Before: [2, 0, 0, 1]
11 3 1 1
After:  [2, 1, 0, 1]

Before: [1, 1, 3, 0]
0 2 2 0
After:  [2, 1, 3, 0]

Before: [2, 0, 3, 2]
0 2 2 0
After:  [2, 0, 3, 2]

Before: [1, 3, 0, 3]
9 0 1 1
After:  [1, 1, 0, 3]

Before: [0, 3, 0, 0]
1 0 0 2
After:  [0, 3, 0, 0]

Before: [2, 1, 2, 2]
2 3 1 1
After:  [2, 3, 2, 2]

Before: [1, 2, 3, 0]
10 0 2 0
After:  [3, 2, 3, 0]

Before: [2, 2, 1, 2]
6 0 1 0
After:  [3, 2, 1, 2]

Before: [3, 2, 2, 3]
0 0 2 1
After:  [3, 2, 2, 3]

Before: [2, 3, 2, 1]
3 3 1 2
After:  [2, 3, 1, 1]

Before: [2, 3, 3, 2]
15 0 2 2
After:  [2, 3, 4, 2]

Before: [0, 2, 3, 3]
7 2 1 0
After:  [2, 2, 3, 3]

Before: [1, 0, 2, 3]
12 3 1 0
After:  [3, 0, 2, 3]

Before: [2, 3, 2, 3]
5 2 1 0
After:  [3, 3, 2, 3]

Before: [1, 3, 0, 3]
9 0 1 2
After:  [1, 3, 1, 3]

Before: [0, 1, 2, 2]
12 2 2 0
After:  [2, 1, 2, 2]

Before: [1, 1, 2, 3]
12 3 1 2
After:  [1, 1, 3, 3]

Before: [0, 2, 0, 1]
9 3 1 2
After:  [0, 2, 1, 1]

Before: [3, 1, 1, 2]
2 3 1 1
After:  [3, 3, 1, 2]

Before: [3, 3, 2, 1]
3 3 1 0
After:  [1, 3, 2, 1]

Before: [3, 0, 3, 0]
6 1 2 1
After:  [3, 2, 3, 0]

Before: [3, 1, 0, 1]
8 1 2 0
After:  [2, 1, 0, 1]

Before: [3, 1, 1, 0]
14 3 1 2
After:  [3, 1, 1, 0]

Before: [0, 0, 2, 0]
13 3 2 3
After:  [0, 0, 2, 2]

Before: [1, 0, 2, 1]
11 3 1 2
After:  [1, 0, 1, 1]

Before: [0, 1, 3, 0]
14 3 1 1
After:  [0, 1, 3, 0]

Before: [0, 1, 0, 2]
13 0 1 2
After:  [0, 1, 1, 2]

Before: [3, 3, 1, 1]
3 3 1 3
After:  [3, 3, 1, 1]

Before: [3, 1, 0, 2]
6 1 2 0
After:  [3, 1, 0, 2]

Before: [0, 0, 1, 2]
11 2 1 0
After:  [1, 0, 1, 2]

Before: [3, 1, 3, 0]
14 3 1 2
After:  [3, 1, 1, 0]

Before: [2, 3, 3, 0]
5 0 1 3
After:  [2, 3, 3, 3]

Before: [1, 1, 0, 0]
6 1 3 3
After:  [1, 1, 0, 3]

Before: [0, 2, 0, 1]
6 2 3 1
After:  [0, 3, 0, 1]

Before: [3, 2, 3, 0]
0 2 2 1
After:  [3, 2, 3, 0]

Before: [3, 0, 2, 0]
0 0 2 3
After:  [3, 0, 2, 2]

Before: [0, 3, 1, 3]
4 0 2 3
After:  [0, 3, 1, 0]

Before: [0, 0, 3, 2]
4 0 3 2
After:  [0, 0, 0, 2]

Before: [1, 3, 2, 1]
13 0 2 2
After:  [1, 3, 3, 1]

Before: [3, 3, 1, 3]
10 2 3 0
After:  [3, 3, 1, 3]

Before: [2, 3, 2, 1]
3 3 1 0
After:  [1, 3, 2, 1]

Before: [2, 2, 3, 1]
7 2 0 1
After:  [2, 2, 3, 1]

Before: [3, 0, 3, 3]
0 2 2 2
After:  [3, 0, 2, 3]

Before: [2, 3, 0, 2]
5 0 1 3
After:  [2, 3, 0, 3]

Before: [3, 1, 1, 1]
8 0 3 3
After:  [3, 1, 1, 9]

Before: [0, 3, 2, 3]
5 2 1 0
After:  [3, 3, 2, 3]

Before: [2, 1, 2, 1]
10 2 2 0
After:  [4, 1, 2, 1]

Before: [1, 3, 3, 1]
3 3 1 1
After:  [1, 1, 3, 1]

Before: [1, 3, 3, 1]
3 3 1 0
After:  [1, 3, 3, 1]

Before: [3, 3, 0, 1]
4 2 0 3
After:  [3, 3, 0, 0]

Before: [1, 0, 2, 1]
11 0 1 1
After:  [1, 1, 2, 1]

Before: [1, 3, 0, 2]
6 2 3 1
After:  [1, 3, 0, 2]

Before: [1, 3, 2, 2]
5 2 1 1
After:  [1, 3, 2, 2]

Before: [1, 1, 2, 3]
13 2 1 0
After:  [3, 1, 2, 3]

Before: [2, 2, 1, 1]
9 3 1 3
After:  [2, 2, 1, 1]

Before: [3, 1, 3, 2]
2 3 1 3
After:  [3, 1, 3, 3]

Before: [2, 3, 0, 1]
3 3 1 0
After:  [1, 3, 0, 1]

Before: [2, 0, 3, 1]
2 3 2 0
After:  [3, 0, 3, 1]

Before: [1, 3, 2, 2]
5 2 1 3
After:  [1, 3, 2, 3]

Before: [2, 3, 0, 3]
12 3 3 0
After:  [3, 3, 0, 3]

Before: [1, 2, 2, 3]
10 0 3 0
After:  [3, 2, 2, 3]

Before: [2, 1, 3, 2]
7 2 0 2
After:  [2, 1, 2, 2]

Before: [2, 2, 1, 0]
6 0 1 2
After:  [2, 2, 3, 0]

Before: [0, 2, 3, 0]
0 2 2 1
After:  [0, 2, 3, 0]

Before: [2, 1, 2, 3]
13 0 1 3
After:  [2, 1, 2, 3]

Before: [0, 2, 3, 3]
13 0 3 2
After:  [0, 2, 3, 3]

Before: [0, 2, 3, 3]
7 2 1 1
After:  [0, 2, 3, 3]

Before: [3, 1, 0, 1]
14 2 1 1
After:  [3, 1, 0, 1]

Before: [2, 2, 3, 1]
8 2 3 2
After:  [2, 2, 9, 1]

Before: [1, 1, 1, 0]
14 3 1 2
After:  [1, 1, 1, 0]

Before: [0, 3, 1, 0]
1 0 0 1
After:  [0, 0, 1, 0]

Before: [3, 3, 2, 0]
12 2 2 2
After:  [3, 3, 2, 0]

Before: [3, 3, 0, 1]
3 3 1 3
After:  [3, 3, 0, 1]

Before: [1, 3, 2, 2]
5 2 1 0
After:  [3, 3, 2, 2]

Before: [1, 1, 1, 0]
14 3 1 0
After:  [1, 1, 1, 0]

Before: [1, 2, 3, 2]
6 1 1 1
After:  [1, 3, 3, 2]

Before: [0, 3, 0, 1]
4 0 1 3
After:  [0, 3, 0, 0]

Before: [0, 0, 2, 1]
1 0 0 3
After:  [0, 0, 2, 0]

Before: [0, 0, 1, 0]
1 0 0 3
After:  [0, 0, 1, 0]

Before: [2, 0, 2, 1]
8 0 3 1
After:  [2, 6, 2, 1]

Before: [2, 1, 0, 0]
8 1 2 3
After:  [2, 1, 0, 2]

Before: [1, 3, 2, 1]
9 0 1 0
After:  [1, 3, 2, 1]

Before: [3, 0, 3, 0]
0 2 2 1
After:  [3, 2, 3, 0]

Before: [3, 0, 0, 1]
4 2 0 0
After:  [0, 0, 0, 1]

Before: [3, 2, 3, 1]
2 3 2 1
After:  [3, 3, 3, 1]

Before: [1, 1, 1, 2]
2 3 1 3
After:  [1, 1, 1, 3]

Before: [1, 0, 1, 0]
11 0 1 1
After:  [1, 1, 1, 0]

Before: [0, 3, 3, 1]
0 2 2 1
After:  [0, 2, 3, 1]

Before: [0, 0, 2, 2]
1 0 0 0
After:  [0, 0, 2, 2]

Before: [0, 0, 2, 3]
10 2 2 0
After:  [4, 0, 2, 3]

Before: [0, 1, 0, 1]
1 0 0 0
After:  [0, 1, 0, 1]

Before: [0, 2, 3, 0]
7 2 1 0
After:  [2, 2, 3, 0]

Before: [3, 1, 3, 2]
8 0 3 3
After:  [3, 1, 3, 9]

Before: [0, 2, 1, 1]
1 0 0 0
After:  [0, 2, 1, 1]

Before: [0, 0, 1, 2]
11 2 1 3
After:  [0, 0, 1, 1]

Before: [2, 3, 2, 2]
5 2 1 0
After:  [3, 3, 2, 2]

Before: [1, 0, 0, 1]
6 0 2 0
After:  [3, 0, 0, 1]

Before: [1, 0, 0, 2]
13 0 3 1
After:  [1, 3, 0, 2]

Before: [3, 3, 1, 2]
9 2 1 1
After:  [3, 1, 1, 2]

Before: [1, 1, 3, 3]
10 1 3 1
After:  [1, 3, 3, 3]

Before: [0, 3, 2, 0]
12 2 2 3
After:  [0, 3, 2, 2]

Before: [0, 1, 2, 0]
6 1 3 2
After:  [0, 1, 3, 0]

Before: [0, 2, 3, 2]
7 2 1 0
After:  [2, 2, 3, 2]

Before: [3, 3, 3, 3]
12 3 1 3
After:  [3, 3, 3, 3]

Before: [1, 3, 3, 1]
8 0 2 0
After:  [2, 3, 3, 1]

Before: [0, 0, 1, 2]
4 0 2 0
After:  [0, 0, 1, 2]

Before: [1, 0, 3, 0]
8 0 2 2
After:  [1, 0, 2, 0]

Before: [2, 0, 1, 0]
11 2 1 2
After:  [2, 0, 1, 0]

Before: [0, 1, 2, 2]
10 2 2 1
After:  [0, 4, 2, 2]

Before: [3, 2, 2, 1]
9 3 1 1
After:  [3, 1, 2, 1]

Before: [2, 2, 2, 0]
6 1 1 0
After:  [3, 2, 2, 0]

Before: [0, 0, 2, 1]
11 3 1 3
After:  [0, 0, 2, 1]

Before: [0, 2, 0, 1]
6 0 2 2
After:  [0, 2, 2, 1]

Before: [1, 1, 1, 3]
10 2 3 0
After:  [3, 1, 1, 3]

Before: [1, 2, 3, 0]
6 1 1 2
After:  [1, 2, 3, 0]

Before: [2, 3, 0, 3]
5 0 1 2
After:  [2, 3, 3, 3]

Before: [3, 2, 3, 1]
7 2 1 3
After:  [3, 2, 3, 2]

Before: [3, 1, 0, 3]
14 2 1 2
After:  [3, 1, 1, 3]

Before: [3, 0, 3, 3]
12 3 0 2
After:  [3, 0, 3, 3]

Before: [0, 2, 1, 3]
6 0 1 2
After:  [0, 2, 1, 3]

Before: [0, 2, 0, 1]
15 1 2 1
After:  [0, 4, 0, 1]

Before: [0, 3, 0, 1]
6 0 1 0
After:  [1, 3, 0, 1]

Before: [0, 0, 2, 3]
0 3 2 3
After:  [0, 0, 2, 2]

Before: [3, 3, 2, 3]
0 3 2 2
After:  [3, 3, 2, 3]

Before: [2, 2, 3, 3]
12 3 3 3
After:  [2, 2, 3, 3]

Before: [3, 1, 3, 2]
10 1 2 1
After:  [3, 3, 3, 2]

Before: [0, 2, 3, 1]
8 1 3 2
After:  [0, 2, 6, 1]

Before: [2, 2, 3, 2]
7 2 0 1
After:  [2, 2, 3, 2]

Before: [2, 1, 0, 2]
14 2 1 3
After:  [2, 1, 0, 1]

Before: [0, 1, 2, 0]
13 0 1 0
After:  [1, 1, 2, 0]

Before: [3, 3, 0, 1]
8 0 3 1
After:  [3, 9, 0, 1]

Before: [0, 1, 3, 2]
2 3 1 1
After:  [0, 3, 3, 2]

Before: [0, 2, 0, 2]
1 0 0 2
After:  [0, 2, 0, 2]

Before: [0, 3, 1, 3]
9 2 1 0
After:  [1, 3, 1, 3]

Before: [1, 2, 2, 1]
10 2 2 2
After:  [1, 2, 4, 1]

Before: [0, 0, 3, 0]
6 0 3 2
After:  [0, 0, 3, 0]

Before: [2, 0, 2, 3]
12 2 2 0
After:  [2, 0, 2, 3]

Before: [2, 3, 3, 1]
5 0 1 2
After:  [2, 3, 3, 1]

Before: [2, 3, 3, 3]
5 0 1 1
After:  [2, 3, 3, 3]

Before: [0, 0, 0, 2]
1 0 0 0
After:  [0, 0, 0, 2]

Before: [0, 1, 1, 1]
4 0 3 3
After:  [0, 1, 1, 0]

Before: [3, 2, 1, 2]
15 3 2 1
After:  [3, 4, 1, 2]

Before: [0, 2, 3, 1]
7 2 1 1
After:  [0, 2, 3, 1]

Before: [3, 2, 1, 3]
6 2 2 3
After:  [3, 2, 1, 3]

Before: [3, 2, 2, 1]
9 3 1 2
After:  [3, 2, 1, 1]

Before: [2, 3, 0, 2]
8 1 2 2
After:  [2, 3, 6, 2]

Before: [1, 0, 1, 1]
11 3 1 0
After:  [1, 0, 1, 1]

Before: [1, 3, 0, 1]
3 3 1 2
After:  [1, 3, 1, 1]

Before: [3, 3, 3, 2]
7 2 3 2
After:  [3, 3, 2, 2]

Before: [1, 1, 0, 0]
14 2 1 0
After:  [1, 1, 0, 0]

Before: [2, 3, 3, 2]
15 0 2 1
After:  [2, 4, 3, 2]

Before: [2, 3, 2, 3]
10 0 2 0
After:  [4, 3, 2, 3]

Before: [1, 0, 0, 3]
12 3 1 3
After:  [1, 0, 0, 3]

Before: [2, 1, 2, 2]
2 3 1 2
After:  [2, 1, 3, 2]

Before: [1, 1, 3, 2]
2 3 1 3
After:  [1, 1, 3, 3]

Before: [3, 0, 1, 1]
6 2 3 0
After:  [3, 0, 1, 1]

Before: [0, 2, 3, 2]
4 0 3 1
After:  [0, 0, 3, 2]

Before: [3, 3, 3, 2]
0 2 2 1
After:  [3, 2, 3, 2]

Before: [2, 0, 3, 1]
0 2 2 1
After:  [2, 2, 3, 1]

Before: [3, 1, 1, 1]
6 2 2 3
After:  [3, 1, 1, 3]

Before: [0, 0, 1, 0]
11 2 1 3
After:  [0, 0, 1, 1]

Before: [2, 0, 2, 0]
8 2 3 3
After:  [2, 0, 2, 6]

Before: [1, 0, 1, 1]
11 2 1 1
After:  [1, 1, 1, 1]

Before: [0, 2, 3, 3]
4 0 1 3
After:  [0, 2, 3, 0]

Before: [0, 1, 0, 2]
2 3 1 3
After:  [0, 1, 0, 3]

Before: [0, 1, 0, 3]
4 0 1 0
After:  [0, 1, 0, 3]

Before: [2, 1, 0, 3]
14 2 1 0
After:  [1, 1, 0, 3]

Before: [2, 3, 3, 3]
5 0 1 0
After:  [3, 3, 3, 3]

Before: [0, 0, 1, 3]
1 0 0 3
After:  [0, 0, 1, 0]

Before: [0, 1, 2, 0]
4 0 2 3
After:  [0, 1, 2, 0]

Before: [0, 0, 3, 2]
7 2 3 1
After:  [0, 2, 3, 2]

Before: [2, 3, 2, 1]
3 3 1 3
After:  [2, 3, 2, 1]

Before: [0, 1, 3, 3]
12 3 0 2
After:  [0, 1, 3, 3]

Before: [0, 3, 2, 3]
0 3 2 3
After:  [0, 3, 2, 2]

Before: [3, 3, 1, 0]
8 1 3 2
After:  [3, 3, 9, 0]

Before: [0, 2, 3, 3]
4 0 2 2
After:  [0, 2, 0, 3]

Before: [0, 3, 2, 2]
0 1 2 1
After:  [0, 2, 2, 2]

Before: [0, 0, 2, 2]
12 2 2 0
After:  [2, 0, 2, 2]

Before: [1, 3, 0, 1]
3 3 1 0
After:  [1, 3, 0, 1]

Before: [0, 0, 2, 2]
13 1 2 0
After:  [2, 0, 2, 2]

Before: [1, 0, 0, 0]
6 0 3 0
After:  [3, 0, 0, 0]

Before: [1, 2, 3, 3]
7 2 1 0
After:  [2, 2, 3, 3]

Before: [1, 3, 3, 2]
7 2 3 3
After:  [1, 3, 3, 2]

Before: [0, 2, 3, 3]
7 2 1 3
After:  [0, 2, 3, 2]

Before: [0, 1, 1, 2]
1 0 0 1
After:  [0, 0, 1, 2]

Before: [0, 1, 0, 3]
1 0 0 0
After:  [0, 1, 0, 3]

Before: [3, 0, 1, 0]
11 2 1 2
After:  [3, 0, 1, 0]

Before: [2, 3, 0, 3]
5 0 1 1
After:  [2, 3, 0, 3]

Before: [1, 3, 2, 1]
8 1 3 0
After:  [9, 3, 2, 1]

Before: [0, 1, 0, 3]
4 0 1 3
After:  [0, 1, 0, 0]

Before: [1, 0, 2, 1]
2 3 2 3
After:  [1, 0, 2, 3]

Before: [2, 0, 2, 3]
10 2 2 2
After:  [2, 0, 4, 3]

Before: [2, 3, 1, 1]
3 3 1 0
After:  [1, 3, 1, 1]

Before: [2, 2, 0, 2]
6 1 1 1
After:  [2, 3, 0, 2]

Before: [3, 2, 2, 2]
0 0 2 1
After:  [3, 2, 2, 2]

Before: [1, 1, 2, 3]
0 3 2 2
After:  [1, 1, 2, 3]

Before: [1, 1, 2, 1]
2 3 2 1
After:  [1, 3, 2, 1]

Before: [3, 0, 1, 3]
11 2 1 2
After:  [3, 0, 1, 3]

Before: [2, 3, 1, 3]
12 3 3 0
After:  [3, 3, 1, 3]

Before: [0, 2, 2, 1]
10 2 2 2
After:  [0, 2, 4, 1]

Before: [2, 2, 2, 1]
10 2 2 0
After:  [4, 2, 2, 1]

Before: [1, 0, 1, 2]
11 0 1 0
After:  [1, 0, 1, 2]

Before: [1, 0, 0, 2]
11 0 1 2
After:  [1, 0, 1, 2]

Before: [2, 1, 3, 2]
7 2 3 2
After:  [2, 1, 2, 2]

Before: [1, 3, 1, 3]
9 2 1 2
After:  [1, 3, 1, 3]

Before: [1, 0, 3, 3]
10 0 2 2
After:  [1, 0, 3, 3]

Before: [0, 3, 3, 0]
8 2 3 0
After:  [9, 3, 3, 0]

Before: [1, 3, 2, 1]
5 2 1 0
After:  [3, 3, 2, 1]

Before: [1, 2, 0, 0]
15 1 2 3
After:  [1, 2, 0, 4]

Before: [3, 2, 3, 1]
6 1 1 3
After:  [3, 2, 3, 3]

Before: [2, 3, 3, 1]
3 3 1 2
After:  [2, 3, 1, 1]

Before: [3, 0, 1, 1]
11 2 1 2
After:  [3, 0, 1, 1]

Before: [3, 0, 2, 1]
11 3 1 0
After:  [1, 0, 2, 1]

Before: [2, 3, 1, 1]
3 3 1 3
After:  [2, 3, 1, 1]

Before: [3, 3, 0, 0]
8 1 3 2
After:  [3, 3, 9, 0]

Before: [2, 2, 0, 2]
15 3 2 2
After:  [2, 2, 4, 2]

Before: [0, 2, 0, 0]
6 3 2 3
After:  [0, 2, 0, 2]

Before: [1, 3, 3, 1]
9 0 1 3
After:  [1, 3, 3, 1]

Before: [2, 1, 3, 1]
6 1 3 3
After:  [2, 1, 3, 3]

Before: [0, 2, 3, 3]
1 0 0 3
After:  [0, 2, 3, 0]

Before: [2, 3, 3, 2]
5 0 1 0
After:  [3, 3, 3, 2]

Before: [3, 3, 2, 0]
5 2 1 3
After:  [3, 3, 2, 3]

Before: [3, 0, 3, 1]
11 3 1 1
After:  [3, 1, 3, 1]

Before: [0, 2, 1, 1]
4 0 1 2
After:  [0, 2, 0, 1]

Before: [3, 3, 2, 2]
5 2 1 3
After:  [3, 3, 2, 3]

Before: [1, 0, 3, 3]
10 0 2 3
After:  [1, 0, 3, 3]

Before: [3, 2, 2, 2]
10 1 2 1
After:  [3, 4, 2, 2]

Before: [2, 2, 3, 1]
7 2 0 2
After:  [2, 2, 2, 1]

Before: [2, 3, 1, 3]
12 3 1 2
After:  [2, 3, 3, 3]

Before: [2, 3, 1, 0]
5 0 1 1
After:  [2, 3, 1, 0]

Before: [0, 3, 1, 1]
9 2 1 0
After:  [1, 3, 1, 1]

Before: [1, 1, 3, 0]
14 3 1 3
After:  [1, 1, 3, 1]

Before: [3, 3, 2, 0]
5 2 1 0
After:  [3, 3, 2, 0]

Before: [1, 0, 3, 2]
8 2 3 1
After:  [1, 9, 3, 2]

Before: [0, 1, 2, 3]
4 0 2 0
After:  [0, 1, 2, 3]

Before: [0, 3, 3, 2]
0 2 2 3
After:  [0, 3, 3, 2]

Before: [2, 3, 3, 3]
7 2 0 2
After:  [2, 3, 2, 3]

Before: [1, 2, 0, 1]
15 1 2 3
After:  [1, 2, 0, 4]

Before: [2, 1, 1, 2]
15 3 2 0
After:  [4, 1, 1, 2]

Before: [2, 0, 3, 3]
13 1 3 1
After:  [2, 3, 3, 3]

Before: [2, 3, 2, 0]
12 2 0 3
After:  [2, 3, 2, 2]

Before: [1, 1, 3, 1]
2 3 2 2
After:  [1, 1, 3, 1]

Before: [1, 3, 3, 2]
7 2 3 2
After:  [1, 3, 2, 2]

Before: [1, 2, 2, 0]
13 3 2 0
After:  [2, 2, 2, 0]

Before: [0, 1, 1, 0]
1 0 0 3
After:  [0, 1, 1, 0]

Before: [0, 0, 0, 1]
11 3 1 2
After:  [0, 0, 1, 1]

Before: [0, 0, 3, 1]
11 3 1 1
After:  [0, 1, 3, 1]

Before: [3, 1, 2, 0]
14 3 1 2
After:  [3, 1, 1, 0]

Before: [1, 3, 2, 3]
0 1 2 0
After:  [2, 3, 2, 3]

Before: [3, 3, 1, 0]
9 2 1 3
After:  [3, 3, 1, 1]

Before: [2, 3, 1, 2]
5 0 1 3
After:  [2, 3, 1, 3]

Before: [2, 3, 2, 2]
10 2 2 0
After:  [4, 3, 2, 2]

Before: [0, 0, 2, 2]
1 0 0 1
After:  [0, 0, 2, 2]

Before: [3, 1, 3, 2]
7 2 3 0
After:  [2, 1, 3, 2]

Before: [0, 3, 2, 1]
4 0 3 2
After:  [0, 3, 0, 1]

Before: [3, 0, 1, 1]
11 3 1 1
After:  [3, 1, 1, 1]

Before: [3, 1, 1, 2]
15 3 2 1
After:  [3, 4, 1, 2]

Before: [3, 2, 1, 2]
8 0 3 0
After:  [9, 2, 1, 2]

Before: [0, 1, 1, 2]
15 3 2 0
After:  [4, 1, 1, 2]

Before: [0, 3, 2, 1]
3 3 1 1
After:  [0, 1, 2, 1]

Before: [1, 1, 2, 2]
2 3 1 2
After:  [1, 1, 3, 2]

Before: [3, 2, 1, 0]
8 0 3 0
After:  [9, 2, 1, 0]

Before: [1, 0, 0, 0]
11 0 1 2
After:  [1, 0, 1, 0]

Before: [2, 2, 3, 3]
7 2 1 1
After:  [2, 2, 3, 3]

Before: [0, 1, 0, 3]
12 3 1 3
After:  [0, 1, 0, 3]

Before: [3, 1, 3, 3]
0 2 2 1
After:  [3, 2, 3, 3]

Before: [3, 2, 0, 0]
15 1 2 0
After:  [4, 2, 0, 0]

Before: [0, 3, 3, 3]
12 3 0 0
After:  [3, 3, 3, 3]

Before: [0, 2, 2, 3]
10 2 2 1
After:  [0, 4, 2, 3]

Before: [0, 1, 1, 1]
1 0 0 3
After:  [0, 1, 1, 0]

Before: [0, 1, 0, 2]
1 0 0 3
After:  [0, 1, 0, 0]

Before: [1, 1, 2, 1]
2 3 2 3
After:  [1, 1, 2, 3]

Before: [2, 1, 2, 1]
2 3 2 0
After:  [3, 1, 2, 1]

Before: [0, 0, 1, 2]
1 0 0 1
After:  [0, 0, 1, 2]

Before: [1, 2, 1, 3]
12 3 0 3
After:  [1, 2, 1, 3]

Before: [2, 2, 1, 0]
15 1 2 0
After:  [4, 2, 1, 0]

Before: [2, 2, 1, 2]
13 2 3 2
After:  [2, 2, 3, 2]

Before: [1, 0, 1, 2]
11 2 1 0
After:  [1, 0, 1, 2]

Before: [2, 3, 1, 2]
15 3 2 1
After:  [2, 4, 1, 2]

Before: [1, 3, 2, 1]
3 3 1 2
After:  [1, 3, 1, 1]

Before: [3, 2, 0, 3]
12 3 3 1
After:  [3, 3, 0, 3]

Before: [3, 1, 0, 3]
14 2 1 3
After:  [3, 1, 0, 1]

Before: [3, 2, 1, 2]
8 3 3 1
After:  [3, 6, 1, 2]

Before: [1, 3, 2, 3]
0 1 2 2
After:  [1, 3, 2, 3]

Before: [0, 2, 3, 2]
7 2 3 0
After:  [2, 2, 3, 2]

Before: [1, 0, 1, 2]
11 0 1 2
After:  [1, 0, 1, 2]

Before: [2, 1, 2, 0]
13 0 1 2
After:  [2, 1, 3, 0]

Before: [0, 0, 2, 1]
8 2 3 1
After:  [0, 6, 2, 1]

Before: [3, 0, 2, 3]
13 1 2 1
After:  [3, 2, 2, 3]

Before: [2, 1, 3, 0]
14 3 1 0
After:  [1, 1, 3, 0]

Before: [0, 3, 1, 1]
3 3 1 0
After:  [1, 3, 1, 1]

Before: [0, 3, 2, 3]
4 0 2 0
After:  [0, 3, 2, 3]

Before: [1, 2, 2, 1]
8 1 3 3
After:  [1, 2, 2, 6]

Before: [3, 0, 1, 2]
4 1 0 2
After:  [3, 0, 0, 2]

Before: [2, 3, 3, 1]
2 3 2 3
After:  [2, 3, 3, 3]

Before: [1, 1, 1, 0]
6 0 2 3
After:  [1, 1, 1, 3]

Before: [0, 1, 1, 2]
2 3 1 2
After:  [0, 1, 3, 2]

Before: [2, 3, 0, 3]
8 3 2 1
After:  [2, 6, 0, 3]

Before: [2, 3, 3, 0]
7 2 0 0
After:  [2, 3, 3, 0]

Before: [1, 1, 1, 3]
10 2 3 1
After:  [1, 3, 1, 3]

Before: [2, 0, 3, 0]
0 2 2 2
After:  [2, 0, 2, 0]

Before: [2, 1, 2, 0]
13 1 2 1
After:  [2, 3, 2, 0]

Before: [0, 1, 0, 3]
13 0 3 2
After:  [0, 1, 3, 3]

Before: [0, 2, 3, 0]
4 0 2 1
After:  [0, 0, 3, 0]

Before: [0, 2, 1, 1]
9 3 1 3
After:  [0, 2, 1, 1]

Before: [3, 1, 3, 2]
7 2 3 2
After:  [3, 1, 2, 2]

Before: [0, 3, 2, 2]
1 0 0 2
After:  [0, 3, 0, 2]

Before: [3, 1, 2, 2]
10 3 2 0
After:  [4, 1, 2, 2]

Before: [2, 3, 0, 1]
3 3 1 1
After:  [2, 1, 0, 1]

Before: [1, 0, 3, 1]
2 3 2 0
After:  [3, 0, 3, 1]

Before: [2, 1, 1, 2]
15 3 2 2
After:  [2, 1, 4, 2]

Before: [1, 3, 2, 2]
5 2 1 2
After:  [1, 3, 3, 2]

Before: [0, 3, 2, 3]
5 2 1 1
After:  [0, 3, 2, 3]

Before: [0, 1, 0, 2]
6 2 3 3
After:  [0, 1, 0, 3]

Before: [2, 3, 0, 1]
3 3 1 3
After:  [2, 3, 0, 1]

Before: [0, 1, 1, 0]
14 3 1 1
After:  [0, 1, 1, 0]

Before: [2, 1, 3, 0]
7 2 0 0
After:  [2, 1, 3, 0]

Before: [2, 3, 1, 3]
5 0 1 2
After:  [2, 3, 3, 3]

Before: [1, 3, 2, 1]
5 2 1 2
After:  [1, 3, 3, 1]

Before: [0, 1, 0, 2]
4 0 1 3
After:  [0, 1, 0, 0]

Before: [2, 3, 3, 3]
0 2 2 3
After:  [2, 3, 3, 2]

Before: [1, 3, 2, 3]
10 0 3 2
After:  [1, 3, 3, 3]

Before: [2, 0, 2, 3]
4 1 0 2
After:  [2, 0, 0, 3]

Before: [2, 0, 1, 3]
11 2 1 3
After:  [2, 0, 1, 1]

Before: [3, 1, 1, 3]
12 3 3 0
After:  [3, 1, 1, 3]

Before: [3, 3, 1, 1]
3 3 1 1
After:  [3, 1, 1, 1]

Before: [1, 2, 1, 2]
15 1 2 2
After:  [1, 2, 4, 2]

Before: [1, 3, 1, 1]
8 1 2 2
After:  [1, 3, 6, 1]

Before: [3, 1, 1, 3]
12 3 0 0
After:  [3, 1, 1, 3]

Before: [3, 1, 0, 2]
14 2 1 1
After:  [3, 1, 0, 2]

Before: [1, 0, 2, 2]
11 0 1 3
After:  [1, 0, 2, 1]

Before: [1, 1, 2, 2]
2 3 1 3
After:  [1, 1, 2, 3]

Before: [0, 1, 2, 3]
13 0 1 0
After:  [1, 1, 2, 3]

Before: [2, 0, 2, 1]
10 0 2 1
After:  [2, 4, 2, 1]

Before: [2, 3, 1, 3]
5 0 1 1
After:  [2, 3, 1, 3]

Before: [3, 2, 2, 1]
9 3 1 0
After:  [1, 2, 2, 1]

Before: [1, 2, 3, 2]
15 1 2 3
After:  [1, 2, 3, 4]

Before: [3, 2, 1, 3]
15 1 2 3
After:  [3, 2, 1, 4]

Before: [3, 2, 0, 0]
8 1 3 1
After:  [3, 6, 0, 0]

Before: [2, 1, 3, 1]
8 3 2 3
After:  [2, 1, 3, 2]

Before: [0, 3, 1, 3]
9 2 1 3
After:  [0, 3, 1, 1]

Before: [1, 3, 0, 1]
9 0 1 1
After:  [1, 1, 0, 1]

Before: [3, 1, 2, 2]
2 3 1 1
After:  [3, 3, 2, 2]

Before: [0, 0, 2, 3]
0 3 2 2
After:  [0, 0, 2, 3]

Before: [2, 3, 1, 1]
9 2 1 0
After:  [1, 3, 1, 1]

Before: [2, 1, 0, 0]
13 0 1 3
After:  [2, 1, 0, 3]

Before: [3, 3, 2, 2]
5 2 1 1
After:  [3, 3, 2, 2]

Before: [1, 2, 3, 3]
7 2 1 1
After:  [1, 2, 3, 3]

Before: [0, 3, 1, 3]
12 3 0 2
After:  [0, 3, 3, 3]

Before: [3, 2, 3, 2]
7 2 1 2
After:  [3, 2, 2, 2]

Before: [0, 1, 3, 3]
12 3 0 1
After:  [0, 3, 3, 3]

Before: [1, 1, 0, 2]
2 3 1 1
After:  [1, 3, 0, 2]

Before: [2, 3, 2, 0]
10 0 2 0
After:  [4, 3, 2, 0]

Before: [2, 2, 2, 1]
6 1 1 3
After:  [2, 2, 2, 3]

Before: [3, 3, 2, 0]
0 0 2 0
After:  [2, 3, 2, 0]

Before: [0, 0, 3, 0]
1 0 0 3
After:  [0, 0, 3, 0]

Before: [2, 1, 2, 1]
12 2 0 0
After:  [2, 1, 2, 1]

Before: [0, 2, 3, 1]
1 0 0 2
After:  [0, 2, 0, 1]

Before: [1, 3, 2, 1]
3 3 1 3
After:  [1, 3, 2, 1]

Before: [0, 1, 2, 3]
1 0 0 0
After:  [0, 1, 2, 3]

Before: [0, 1, 3, 0]
14 3 1 3
After:  [0, 1, 3, 1]

Before: [3, 0, 0, 3]
8 3 2 2
After:  [3, 0, 6, 3]

Before: [2, 0, 1, 1]
11 3 1 1
After:  [2, 1, 1, 1]

Before: [3, 2, 3, 0]
7 2 1 2
After:  [3, 2, 2, 0]

Before: [0, 0, 1, 2]
11 2 1 2
After:  [0, 0, 1, 2]

Before: [3, 2, 3, 3]
7 2 1 0
After:  [2, 2, 3, 3]

Before: [3, 1, 2, 0]
8 2 3 3
After:  [3, 1, 2, 6]

Before: [1, 1, 0, 3]
14 2 1 2
After:  [1, 1, 1, 3]

Before: [0, 1, 3, 1]
2 3 2 3
After:  [0, 1, 3, 3]

Before: [3, 0, 2, 1]
11 3 1 3
After:  [3, 0, 2, 1]

Before: [1, 1, 2, 2]
12 2 2 1
After:  [1, 2, 2, 2]

Before: [1, 0, 2, 0]
6 0 3 0
After:  [3, 0, 2, 0]

Before: [1, 3, 3, 3]
10 0 3 0
After:  [3, 3, 3, 3]

Before: [0, 1, 0, 2]
1 0 0 0
After:  [0, 1, 0, 2]

Before: [2, 2, 3, 3]
0 2 2 3
After:  [2, 2, 3, 2]

Before: [3, 2, 3, 3]
7 2 1 3
After:  [3, 2, 3, 2]

Before: [3, 3, 2, 1]
5 2 1 1
After:  [3, 3, 2, 1]

Before: [2, 0, 0, 2]
6 1 2 1
After:  [2, 2, 0, 2]

Before: [2, 1, 0, 2]
15 3 2 0
After:  [4, 1, 0, 2]

Before: [1, 0, 3, 2]
6 1 3 2
After:  [1, 0, 3, 2]

Before: [2, 0, 2, 0]
13 3 2 0
After:  [2, 0, 2, 0]

Before: [1, 3, 2, 1]
3 3 1 0
After:  [1, 3, 2, 1]

Before: [1, 2, 0, 1]
9 3 1 3
After:  [1, 2, 0, 1]

Before: [1, 0, 3, 2]
11 0 1 3
After:  [1, 0, 3, 1]

Before: [2, 1, 0, 0]
13 0 1 0
After:  [3, 1, 0, 0]

Before: [0, 1, 2, 1]
2 3 2 2
After:  [0, 1, 3, 1]

Before: [2, 1, 2, 2]
13 2 1 3
After:  [2, 1, 2, 3]

Before: [3, 3, 0, 0]
8 0 3 2
After:  [3, 3, 9, 0]

Before: [2, 1, 2, 3]
12 2 2 1
After:  [2, 2, 2, 3]

Before: [0, 0, 1, 1]
11 3 1 2
After:  [0, 0, 1, 1]

Before: [1, 2, 0, 2]
6 0 2 1
After:  [1, 3, 0, 2]

Before: [1, 3, 3, 3]
12 3 1 2
After:  [1, 3, 3, 3]

Before: [3, 0, 2, 1]
11 3 1 1
After:  [3, 1, 2, 1]

Before: [2, 3, 3, 1]
7 2 0 1
After:  [2, 2, 3, 1]

Before: [2, 0, 2, 0]
4 1 0 0
After:  [0, 0, 2, 0]

Before: [0, 3, 3, 1]
2 3 2 2
After:  [0, 3, 3, 1]

Before: [3, 3, 2, 1]
3 3 1 1
After:  [3, 1, 2, 1]

Before: [3, 0, 2, 0]
8 2 3 3
After:  [3, 0, 2, 6]

Before: [2, 1, 2, 1]
2 3 2 3
After:  [2, 1, 2, 3]

Before: [3, 1, 1, 2]
15 3 2 3
After:  [3, 1, 1, 4]

Before: [0, 1, 2, 2]
2 3 1 0
After:  [3, 1, 2, 2]

Before: [0, 3, 0, 3]
4 0 3 0
After:  [0, 3, 0, 3]

Before: [2, 0, 0, 3]
4 1 0 2
After:  [2, 0, 0, 3]

Before: [2, 3, 1, 2]
5 0 1 2
After:  [2, 3, 3, 2]

Before: [3, 3, 2, 1]
0 0 2 2
After:  [3, 3, 2, 1]

Before: [1, 1, 2, 2]
10 2 2 2
After:  [1, 1, 4, 2]

Before: [3, 1, 1, 2]
13 1 3 3
After:  [3, 1, 1, 3]

Before: [1, 2, 2, 1]
9 3 1 2
After:  [1, 2, 1, 1]

Before: [3, 0, 1, 2]
11 2 1 1
After:  [3, 1, 1, 2]

Before: [1, 2, 0, 2]
8 0 2 0
After:  [2, 2, 0, 2]

Before: [2, 2, 3, 2]
15 1 2 0
After:  [4, 2, 3, 2]

Before: [2, 3, 3, 0]
0 2 2 1
After:  [2, 2, 3, 0]

Before: [0, 2, 3, 1]
2 3 2 1
After:  [0, 3, 3, 1]

Before: [2, 3, 3, 1]
5 0 1 3
After:  [2, 3, 3, 3]

Before: [0, 3, 2, 1]
1 0 0 3
After:  [0, 3, 2, 0]

Before: [3, 2, 2, 3]
0 3 2 1
After:  [3, 2, 2, 3]

Before: [1, 2, 2, 1]
9 3 1 0
After:  [1, 2, 2, 1]

Before: [3, 3, 3, 0]
8 2 3 1
After:  [3, 9, 3, 0]

Before: [2, 3, 3, 1]
3 3 1 3
After:  [2, 3, 3, 1]

Before: [0, 2, 3, 1]
2 3 2 0
After:  [3, 2, 3, 1]

Before: [2, 0, 0, 3]
13 2 3 1
After:  [2, 3, 0, 3]

Before: [0, 0, 2, 1]
13 1 2 1
After:  [0, 2, 2, 1]

Before: [2, 2, 3, 3]
7 2 0 0
After:  [2, 2, 3, 3]

Before: [1, 0, 2, 3]
11 0 1 1
After:  [1, 1, 2, 3]

Before: [0, 0, 1, 2]
15 3 2 0
After:  [4, 0, 1, 2]

Before: [3, 3, 3, 2]
7 2 3 3
After:  [3, 3, 3, 2]

Before: [1, 2, 3, 1]
9 3 1 1
After:  [1, 1, 3, 1]

Before: [1, 3, 1, 1]
3 3 1 0
After:  [1, 3, 1, 1]

Before: [0, 2, 3, 2]
7 2 1 3
After:  [0, 2, 3, 2]

Before: [0, 3, 3, 2]
1 0 0 3
After:  [0, 3, 3, 0]

Before: [0, 0, 3, 3]
6 0 2 1
After:  [0, 2, 3, 3]

Before: [0, 0, 2, 2]
12 2 2 2
After:  [0, 0, 2, 2]

Before: [0, 0, 0, 3]
1 0 0 0
After:  [0, 0, 0, 3]

Before: [0, 2, 0, 3]
1 0 0 0
After:  [0, 2, 0, 3]

Before: [0, 2, 2, 1]
4 0 3 3
After:  [0, 2, 2, 0]

Before: [2, 0, 1, 2]
11 2 1 2
After:  [2, 0, 1, 2]

Before: [3, 1, 2, 1]
13 1 2 0
After:  [3, 1, 2, 1]

Before: [2, 2, 1, 0]
8 0 3 2
After:  [2, 2, 6, 0]

Before: [0, 3, 1, 1]
3 3 1 3
After:  [0, 3, 1, 1]

Before: [2, 1, 3, 3]
0 2 2 1
After:  [2, 2, 3, 3]

Before: [0, 2, 1, 3]
15 1 2 2
After:  [0, 2, 4, 3]

Before: [3, 0, 1, 2]
11 2 1 3
After:  [3, 0, 1, 1]

Before: [2, 3, 1, 1]
5 0 1 3
After:  [2, 3, 1, 3]

Before: [2, 3, 1, 0]
5 0 1 3
After:  [2, 3, 1, 3]

Before: [3, 3, 3, 2]
8 0 3 2
After:  [3, 3, 9, 2]

Before: [1, 3, 1, 1]
9 2 1 1
After:  [1, 1, 1, 1]

Before: [2, 1, 1, 2]
13 0 1 2
After:  [2, 1, 3, 2]

Before: [0, 3, 2, 1]
1 0 0 1
After:  [0, 0, 2, 1]

Before: [0, 3, 2, 1]
5 2 1 1
After:  [0, 3, 2, 1]

Before: [2, 0, 0, 2]
15 0 2 0
After:  [4, 0, 0, 2]

Before: [1, 1, 0, 0]
14 3 1 3
After:  [1, 1, 0, 1]

Before: [0, 1, 3, 3]
12 3 3 3
After:  [0, 1, 3, 3]

Before: [0, 2, 3, 0]
4 0 2 2
After:  [0, 2, 0, 0]

Before: [0, 2, 1, 1]
1 0 0 2
After:  [0, 2, 0, 1]

Before: [1, 3, 2, 0]
5 2 1 0
After:  [3, 3, 2, 0]

Before: [3, 3, 2, 2]
0 1 2 3
After:  [3, 3, 2, 2]

Before: [3, 1, 0, 0]
14 3 1 3
After:  [3, 1, 0, 1]

Before: [3, 3, 2, 0]
5 2 1 1
After:  [3, 3, 2, 0]

Before: [0, 0, 2, 1]
2 3 2 2
After:  [0, 0, 3, 1]

Before: [3, 3, 2, 1]
3 3 1 2
After:  [3, 3, 1, 1]

Before: [2, 3, 0, 2]
5 0 1 2
After:  [2, 3, 3, 2]

Before: [3, 1, 0, 3]
10 1 3 0
After:  [3, 1, 0, 3]

Before: [2, 1, 1, 2]
15 0 2 0
After:  [4, 1, 1, 2]

Before: [1, 1, 3, 3]
10 1 3 2
After:  [1, 1, 3, 3]

Before: [0, 0, 1, 1]
1 0 0 2
After:  [0, 0, 0, 1]

Before: [3, 1, 1, 3]
10 2 3 0
After:  [3, 1, 1, 3]

Before: [1, 3, 2, 1]
8 2 3 2
After:  [1, 3, 6, 1]

Before: [2, 0, 1, 1]
11 2 1 1
After:  [2, 1, 1, 1]

Before: [1, 3, 3, 1]
3 3 1 3
After:  [1, 3, 3, 1]

Before: [2, 1, 3, 3]
12 3 1 2
After:  [2, 1, 3, 3]

Before: [1, 0, 0, 2]
6 1 3 3
After:  [1, 0, 0, 3]

Before: [3, 2, 1, 0]
15 1 2 2
After:  [3, 2, 4, 0]

Before: [0, 3, 0, 2]
1 0 0 3
After:  [0, 3, 0, 0]

Before: [3, 0, 2, 0]
13 1 2 1
After:  [3, 2, 2, 0]

Before: [2, 2, 3, 0]
7 2 1 2
After:  [2, 2, 2, 0]

Before: [0, 0, 1, 3]
1 0 0 2
After:  [0, 0, 0, 3]

Before: [0, 0, 1, 2]
8 3 3 1
After:  [0, 6, 1, 2]

Before: [1, 1, 3, 2]
2 3 1 1
After:  [1, 3, 3, 2]

Before: [3, 2, 2, 2]
8 0 3 1
After:  [3, 9, 2, 2]

Before: [0, 3, 3, 1]
3 3 1 2
After:  [0, 3, 1, 1]

Before: [1, 0, 1, 2]
11 2 1 3
After:  [1, 0, 1, 1]

Before: [1, 2, 2, 0]
13 3 2 2
After:  [1, 2, 2, 0]

Before: [0, 2, 3, 2]
15 3 2 3
After:  [0, 2, 3, 4]

Before: [0, 2, 1, 1]
4 0 3 3
After:  [0, 2, 1, 0]

Before: [1, 1, 3, 3]
10 0 2 3
After:  [1, 1, 3, 3]

Before: [3, 2, 2, 2]
8 0 3 3
After:  [3, 2, 2, 9]

Before: [2, 1, 2, 1]
13 1 2 2
After:  [2, 1, 3, 1]

Before: [1, 2, 3, 2]
7 2 3 3
After:  [1, 2, 3, 2]

Before: [0, 3, 3, 0]
1 0 0 0
After:  [0, 3, 3, 0]

Before: [1, 3, 2, 1]
2 3 2 3
After:  [1, 3, 2, 3]

Before: [0, 1, 3, 3]
0 2 2 2
After:  [0, 1, 2, 3]

Before: [2, 0, 2, 1]
2 3 2 0
After:  [3, 0, 2, 1]

Before: [1, 0, 1, 0]
6 3 2 3
After:  [1, 0, 1, 2]

Before: [3, 1, 1, 2]
2 3 1 3
After:  [3, 1, 1, 3]

Before: [2, 3, 2, 1]
5 2 1 2
After:  [2, 3, 3, 1]

Before: [1, 2, 3, 2]
7 2 1 3
After:  [1, 2, 3, 2]

Before: [2, 3, 2, 0]
5 0 1 1
After:  [2, 3, 2, 0]

Before: [0, 3, 1, 1]
8 1 3 2
After:  [0, 3, 9, 1]

Before: [2, 3, 2, 3]
12 3 3 2
After:  [2, 3, 3, 3]

Before: [1, 3, 1, 1]
9 0 1 1
After:  [1, 1, 1, 1]

Before: [2, 0, 2, 2]
10 2 2 0
After:  [4, 0, 2, 2]

Before: [0, 2, 1, 0]
1 0 0 3
After:  [0, 2, 1, 0]

Before: [0, 0, 2, 3]
4 0 2 0
After:  [0, 0, 2, 3]

Before: [3, 1, 0, 0]
14 2 1 1
After:  [3, 1, 0, 0]

Before: [0, 3, 3, 2]
8 3 3 2
After:  [0, 3, 6, 2]

Before: [2, 3, 2, 1]
3 3 1 1
After:  [2, 1, 2, 1]

Before: [0, 3, 3, 1]
1 0 0 3
After:  [0, 3, 3, 0]

Before: [0, 3, 3, 2]
8 2 3 3
After:  [0, 3, 3, 9]

Before: [1, 3, 2, 3]
9 0 1 2
After:  [1, 3, 1, 3]

Before: [1, 0, 3, 3]
13 1 3 0
After:  [3, 0, 3, 3]

Before: [3, 0, 3, 0]
6 1 2 3
After:  [3, 0, 3, 2]

Before: [0, 0, 0, 3]
12 3 0 2
After:  [0, 0, 3, 3]

Before: [0, 0, 1, 1]
1 0 0 1
After:  [0, 0, 1, 1]

Before: [2, 3, 3, 2]
5 0 1 2
After:  [2, 3, 3, 2]

Before: [0, 3, 3, 3]
1 0 0 3
After:  [0, 3, 3, 0]

Before: [1, 1, 0, 2]
6 0 2 1
After:  [1, 3, 0, 2]

Before: [0, 3, 2, 1]
3 3 1 3
After:  [0, 3, 2, 1]

Before: [1, 0, 3, 1]
11 3 1 0
After:  [1, 0, 3, 1]

Before: [0, 2, 1, 3]
15 1 2 0
After:  [4, 2, 1, 3]

Before: [1, 3, 0, 3]
9 0 1 3
After:  [1, 3, 0, 1]

Before: [2, 0, 3, 2]
15 0 2 1
After:  [2, 4, 3, 2]

Before: [1, 2, 3, 2]
13 0 3 3
After:  [1, 2, 3, 3]

Before: [1, 1, 0, 2]
14 2 1 3
After:  [1, 1, 0, 1]

Before: [0, 1, 2, 1]
2 3 2 3
After:  [0, 1, 2, 3]

Before: [3, 0, 1, 2]
6 1 3 2
After:  [3, 0, 3, 2]

Before: [2, 1, 0, 0]
14 2 1 1
After:  [2, 1, 0, 0]

Before: [2, 3, 1, 2]
5 0 1 1
After:  [2, 3, 1, 2]

Before: [1, 1, 0, 0]
14 2 1 3
After:  [1, 1, 0, 1]

Before: [2, 3, 1, 1]
5 0 1 1
After:  [2, 3, 1, 1]

Before: [0, 0, 1, 0]
6 1 2 1
After:  [0, 2, 1, 0]

Before: [0, 2, 2, 0]
12 2 2 2
After:  [0, 2, 2, 0]

Before: [3, 3, 1, 2]
8 0 3 3
After:  [3, 3, 1, 9]

Before: [3, 1, 0, 0]
14 2 1 3
After:  [3, 1, 0, 1]

Before: [3, 2, 1, 1]
15 1 2 3
After:  [3, 2, 1, 4]

Before: [3, 0, 2, 1]
0 0 2 1
After:  [3, 2, 2, 1]

Before: [0, 3, 3, 0]
4 0 2 1
After:  [0, 0, 3, 0]

Before: [2, 1, 3, 3]
10 1 2 1
After:  [2, 3, 3, 3]

Before: [2, 0, 0, 3]
13 2 3 3
After:  [2, 0, 0, 3]

Before: [2, 3, 1, 1]
8 1 3 2
After:  [2, 3, 9, 1]

Before: [2, 3, 0, 0]
5 0 1 0
After:  [3, 3, 0, 0]

Before: [0, 2, 2, 3]
12 2 0 0
After:  [2, 2, 2, 3]

Before: [1, 3, 0, 1]
3 3 1 3
After:  [1, 3, 0, 1]

Before: [1, 3, 1, 3]
9 0 1 0
After:  [1, 3, 1, 3]

Before: [0, 1, 2, 3]
13 1 2 3
After:  [0, 1, 2, 3]

Before: [0, 0, 0, 2]
1 0 0 2
After:  [0, 0, 0, 2]

Before: [0, 3, 0, 0]
1 0 0 1
After:  [0, 0, 0, 0]

Before: [0, 2, 2, 1]
10 2 2 3
After:  [0, 2, 2, 4]

Before: [0, 2, 1, 1]
9 3 1 0
After:  [1, 2, 1, 1]

Before: [3, 1, 2, 3]
10 1 3 1
After:  [3, 3, 2, 3]

Before: [3, 1, 1, 1]
8 0 3 2
After:  [3, 1, 9, 1]

Before: [1, 3, 2, 3]
12 2 2 1
After:  [1, 2, 2, 3]

Before: [2, 1, 1, 3]
10 2 3 0
After:  [3, 1, 1, 3]

Before: [2, 1, 1, 2]
13 2 3 0
After:  [3, 1, 1, 2]

Before: [1, 0, 2, 3]
0 3 2 0
After:  [2, 0, 2, 3]

Before: [2, 3, 2, 1]
5 2 1 3
After:  [2, 3, 2, 3]

Before: [0, 1, 3, 2]
4 0 2 3
After:  [0, 1, 3, 0]

Before: [0, 3, 3, 3]
4 0 1 0
After:  [0, 3, 3, 3]

Before: [2, 1, 1, 1]
13 0 1 0
After:  [3, 1, 1, 1]

Before: [1, 1, 0, 1]
6 0 2 3
After:  [1, 1, 0, 3]

Before: [0, 3, 2, 3]
4 0 3 0
After:  [0, 3, 2, 3]

Before: [0, 0, 3, 1]
11 3 1 0
After:  [1, 0, 3, 1]

Before: [0, 1, 2, 1]
1 0 0 2
After:  [0, 1, 0, 1]

Before: [0, 3, 2, 1]
2 3 2 3
After:  [0, 3, 2, 3]

Before: [0, 1, 3, 1]
1 0 0 2
After:  [0, 1, 0, 1]

Before: [1, 2, 0, 0]
8 1 3 2
After:  [1, 2, 6, 0]

Before: [2, 1, 2, 2]
10 0 2 1
After:  [2, 4, 2, 2]

Before: [1, 2, 2, 1]
9 3 1 1
After:  [1, 1, 2, 1]

Before: [3, 3, 2, 3]
12 3 0 2
After:  [3, 3, 3, 3]

Before: [3, 3, 0, 1]
3 3 1 0
After:  [1, 3, 0, 1]

Before: [2, 2, 1, 1]
9 3 1 2
After:  [2, 2, 1, 1]

Before: [1, 0, 3, 2]
15 3 2 3
After:  [1, 0, 3, 4]

Before: [2, 0, 0, 1]
11 3 1 3
After:  [2, 0, 0, 1]

Before: [0, 3, 2, 3]
5 2 1 3
After:  [0, 3, 2, 3]

Before: [2, 3, 2, 3]
12 2 0 3
After:  [2, 3, 2, 2]

Before: [1, 3, 3, 3]
9 0 1 2
After:  [1, 3, 1, 3]

Before: [2, 2, 1, 3]
6 0 1 0
After:  [3, 2, 1, 3]

Before: [0, 3, 0, 2]
4 0 3 1
After:  [0, 0, 0, 2]

Before: [1, 0, 2, 2]
10 3 2 1
After:  [1, 4, 2, 2]

Before: [2, 1, 1, 3]
8 3 2 0
After:  [6, 1, 1, 3]

Before: [1, 0, 3, 2]
7 2 3 2
After:  [1, 0, 2, 2]

Before: [1, 2, 2, 3]
12 2 2 1
After:  [1, 2, 2, 3]

Before: [3, 0, 0, 1]
11 3 1 3
After:  [3, 0, 0, 1]

Before: [0, 2, 2, 2]
12 2 0 3
After:  [0, 2, 2, 2]

Before: [2, 1, 0, 0]
14 3 1 0
After:  [1, 1, 0, 0]

Before: [1, 1, 2, 0]
13 1 2 0
After:  [3, 1, 2, 0]

Before: [0, 3, 3, 1]
3 3 1 0
After:  [1, 3, 3, 1]

Before: [3, 3, 2, 2]
5 2 1 0
After:  [3, 3, 2, 2]

Before: [0, 3, 1, 2]
9 2 1 2
After:  [0, 3, 1, 2]

Before: [0, 2, 3, 2]
1 0 0 1
After:  [0, 0, 3, 2]

Before: [2, 3, 2, 0]
8 2 3 1
After:  [2, 6, 2, 0]

Before: [0, 1, 2, 0]
1 0 0 1
After:  [0, 0, 2, 0]

Before: [0, 3, 1, 1]
3 3 1 1
After:  [0, 1, 1, 1]

Before: [3, 0, 3, 1]
2 3 2 0
After:  [3, 0, 3, 1]

Before: [0, 0, 1, 2]
1 0 0 3
After:  [0, 0, 1, 0]

Before: [1, 2, 3, 1]
7 2 1 2
After:  [1, 2, 2, 1]

Before: [1, 1, 0, 1]
8 3 2 1
After:  [1, 2, 0, 1]

Before: [3, 0, 1, 3]
11 2 1 1
After:  [3, 1, 1, 3]

Before: [1, 1, 0, 0]
14 3 1 2
After:  [1, 1, 1, 0]

Before: [1, 3, 2, 2]
8 3 3 3
After:  [1, 3, 2, 6]

Before: [2, 3, 1, 1]
3 3 1 1
After:  [2, 1, 1, 1]

Before: [1, 1, 3, 1]
8 0 2 2
After:  [1, 1, 2, 1]

Before: [0, 3, 3, 3]
4 0 3 3
After:  [0, 3, 3, 0]

Before: [3, 0, 1, 2]
6 1 3 1
After:  [3, 3, 1, 2]

Before: [0, 2, 3, 3]
7 2 1 2
After:  [0, 2, 2, 3]

Before: [2, 0, 3, 2]
7 2 0 3
After:  [2, 0, 3, 2]

Before: [2, 2, 2, 3]
6 1 1 2
After:  [2, 2, 3, 3]

Before: [0, 0, 3, 2]
7 2 3 3
After:  [0, 0, 3, 2]

Before: [2, 1, 3, 1]
2 3 2 3
After:  [2, 1, 3, 3]

Before: [1, 0, 3, 2]
11 0 1 1
After:  [1, 1, 3, 2]

Before: [3, 3, 2, 3]
0 0 2 0
After:  [2, 3, 2, 3]

Before: [3, 2, 2, 1]
2 3 2 1
After:  [3, 3, 2, 1]

Before: [3, 2, 0, 0]
4 2 0 0
After:  [0, 2, 0, 0]

Before: [0, 3, 1, 1]
4 0 2 0
After:  [0, 3, 1, 1]

Before: [1, 3, 3, 2]
8 1 3 1
After:  [1, 9, 3, 2]

Before: [2, 2, 3, 0]
7 2 0 0
After:  [2, 2, 3, 0]

Before: [0, 0, 3, 2]
1 0 0 0
After:  [0, 0, 3, 2]

Before: [2, 2, 0, 2]
8 0 3 3
After:  [2, 2, 0, 6]

Before: [1, 0, 3, 0]
6 1 3 1
After:  [1, 3, 3, 0]

Before: [3, 2, 3, 1]
2 3 2 3
After:  [3, 2, 3, 3]

Before: [2, 1, 3, 0]
14 3 1 2
After:  [2, 1, 1, 0]

Before: [2, 0, 2, 3]
4 1 0 1
After:  [2, 0, 2, 3]

Before: [0, 3, 0, 3]
12 3 0 2
After:  [0, 3, 3, 3]

Before: [1, 0, 2, 3]
12 3 0 3
After:  [1, 0, 2, 3]

Before: [0, 3, 1, 3]
1 0 0 3
After:  [0, 3, 1, 0]

Before: [2, 3, 1, 1]
3 3 1 2
After:  [2, 3, 1, 1]

Before: [1, 1, 3, 3]
10 1 3 0
After:  [3, 1, 3, 3]

Before: [2, 2, 2, 0]
10 1 2 0
After:  [4, 2, 2, 0]

Before: [0, 0, 0, 2]
6 1 2 2
After:  [0, 0, 2, 2]

Before: [1, 1, 3, 2]
2 3 1 0
After:  [3, 1, 3, 2]

Before: [2, 2, 1, 2]
13 2 3 3
After:  [2, 2, 1, 3]

Before: [3, 1, 0, 3]
10 1 3 2
After:  [3, 1, 3, 3]

Before: [2, 2, 3, 3]
7 2 0 1
After:  [2, 2, 3, 3]

Before: [0, 3, 0, 3]
4 0 1 1
After:  [0, 0, 0, 3]

Before: [2, 1, 3, 3]
13 0 1 0
After:  [3, 1, 3, 3]

Before: [0, 3, 0, 0]
4 0 1 2
After:  [0, 3, 0, 0]

Before: [0, 1, 1, 0]
14 3 1 3
After:  [0, 1, 1, 1]

Before: [3, 3, 1, 2]
9 2 1 3
After:  [3, 3, 1, 1]

Before: [0, 2, 0, 0]
15 1 2 3
After:  [0, 2, 0, 4]

Before: [0, 3, 2, 0]
1 0 0 1
After:  [0, 0, 2, 0]

Before: [1, 0, 2, 3]
13 1 3 0
After:  [3, 0, 2, 3]

Before: [0, 0, 3, 3]
1 0 0 1
After:  [0, 0, 3, 3]

Before: [3, 1, 0, 2]
2 3 1 1
After:  [3, 3, 0, 2]

Before: [0, 3, 3, 2]
1 0 0 2
After:  [0, 3, 0, 2]

Before: [0, 2, 3, 0]
0 2 2 3
After:  [0, 2, 3, 2]

Before: [2, 3, 3, 3]
12 3 3 3
After:  [2, 3, 3, 3]

Before: [3, 2, 2, 3]
0 3 2 0
After:  [2, 2, 2, 3]

Before: [1, 2, 0, 1]
15 1 2 2
After:  [1, 2, 4, 1]

Before: [3, 0, 3, 2]
0 2 2 3
After:  [3, 0, 3, 2]

Before: [0, 3, 1, 2]
9 2 1 1
After:  [0, 1, 1, 2]

Before: [0, 3, 3, 0]
6 0 1 1
After:  [0, 1, 3, 0]

Before: [2, 3, 2, 2]
5 2 1 2
After:  [2, 3, 3, 2]

Before: [2, 3, 2, 0]
10 2 2 2
After:  [2, 3, 4, 0]

Before: [3, 1, 2, 0]
14 3 1 1
After:  [3, 1, 2, 0]

Before: [0, 3, 1, 2]
1 0 0 1
After:  [0, 0, 1, 2]

Before: [1, 0, 1, 1]
11 2 1 0
After:  [1, 0, 1, 1]

Before: [3, 2, 3, 1]
8 2 3 3
After:  [3, 2, 3, 9]

Before: [3, 3, 2, 1]
5 2 1 2
After:  [3, 3, 3, 1]

Before: [1, 3, 1, 1]
3 3 1 3
After:  [1, 3, 1, 1]

Before: [2, 1, 0, 1]
14 2 1 3
After:  [2, 1, 0, 1]

Before: [0, 3, 2, 1]
1 0 0 2
After:  [0, 3, 0, 1]

Before: [2, 2, 2, 1]
2 3 2 0
After:  [3, 2, 2, 1]

Before: [3, 3, 0, 3]
12 3 3 3
After:  [3, 3, 0, 3]

Before: [0, 1, 0, 0]
14 2 1 1
After:  [0, 1, 0, 0]

Before: [3, 3, 1, 1]
9 2 1 0
After:  [1, 3, 1, 1]

Before: [2, 2, 1, 2]
8 3 3 1
After:  [2, 6, 1, 2]

Before: [3, 3, 2, 3]
5 2 1 1
After:  [3, 3, 2, 3]

Before: [3, 2, 3, 2]
7 2 3 2
After:  [3, 2, 2, 2]

Before: [2, 0, 1, 2]
11 2 1 0
After:  [1, 0, 1, 2]

Before: [3, 3, 2, 1]
2 3 2 2
After:  [3, 3, 3, 1]

Before: [2, 0, 3, 0]
0 2 2 0
After:  [2, 0, 3, 0]

Before: [0, 2, 0, 2]
1 0 0 0
After:  [0, 2, 0, 2]

Before: [3, 1, 3, 1]
2 3 2 2
After:  [3, 1, 3, 1]

Before: [2, 3, 1, 3]
5 0 1 0
After:  [3, 3, 1, 3]

Before: [0, 1, 0, 1]
6 1 2 3
After:  [0, 1, 0, 3]

Before: [1, 0, 2, 3]
10 2 2 2
After:  [1, 0, 4, 3]

Before: [1, 2, 2, 1]
2 3 2 3
After:  [1, 2, 2, 3]

Before: [0, 3, 0, 3]
4 0 3 2
After:  [0, 3, 0, 3]

Before: [2, 1, 0, 2]
2 3 1 0
After:  [3, 1, 0, 2]

Before: [3, 0, 2, 0]
4 1 0 3
After:  [3, 0, 2, 0]

Before: [2, 3, 1, 3]
9 2 1 1
After:  [2, 1, 1, 3]

Before: [2, 0, 0, 3]
13 1 3 3
After:  [2, 0, 0, 3]

Before: [2, 1, 0, 1]
14 2 1 1
After:  [2, 1, 0, 1]

Before: [1, 0, 1, 0]
6 2 2 2
After:  [1, 0, 3, 0]

Before: [1, 1, 2, 2]
2 3 1 1
After:  [1, 3, 2, 2]

Before: [2, 3, 1, 3]
5 0 1 3
After:  [2, 3, 1, 3]

Before: [1, 3, 0, 1]
3 3 1 1
After:  [1, 1, 0, 1]

Before: [1, 3, 0, 2]
15 3 2 2
After:  [1, 3, 4, 2]

Before: [1, 2, 1, 3]
15 1 2 0
After:  [4, 2, 1, 3]

Before: [2, 3, 3, 3]
15 0 2 0
After:  [4, 3, 3, 3]

Before: [0, 1, 1, 2]
4 0 3 3
After:  [0, 1, 1, 0]

Before: [0, 0, 3, 3]
4 0 2 0
After:  [0, 0, 3, 3]

Before: [0, 3, 3, 1]
3 3 1 1
After:  [0, 1, 3, 1]

Before: [0, 0, 2, 3]
13 1 2 3
After:  [0, 0, 2, 2]

Before: [0, 1, 3, 0]
4 0 2 2
After:  [0, 1, 0, 0]

Before: [0, 3, 2, 1]
2 3 2 2
After:  [0, 3, 3, 1]

Before: [0, 0, 1, 0]
11 2 1 2
After:  [0, 0, 1, 0]

Before: [2, 1, 1, 0]
15 0 2 1
After:  [2, 4, 1, 0]

Before: [3, 1, 0, 3]
13 2 3 0
After:  [3, 1, 0, 3]

Before: [2, 0, 1, 3]
11 2 1 0
After:  [1, 0, 1, 3]

Before: [2, 3, 3, 1]
3 3 1 1
After:  [2, 1, 3, 1]

Before: [0, 2, 3, 1]
15 1 2 2
After:  [0, 2, 4, 1]

Before: [1, 0, 3, 0]
10 0 2 3
After:  [1, 0, 3, 3]

Before: [1, 2, 2, 1]
9 3 1 3
After:  [1, 2, 2, 1]
    """.trimIndent()
            .split("\n\n")
            .map {
                val split = it.split("\n")
                val before = split[0].replace("Before: [", "").replace("]", "").split(", ").map { it.toInt() }
                val instruction = split[1].split(" ").map { it.toInt() }
                val after = split[2].replace("After:  [", "").replace("]", "").split(", ").map { it.toInt() }
                Test(before, after, instruction)
            }

    private val program = """
2 0 1 1
2 2 2 3
2 0 0 2
3 2 3 1
8 1 2 1
13 1 0 0
12 0 1 1
2 3 1 3
2 2 0 0
2 3 3 2
9 0 2 2
8 2 3 2
13 2 1 1
12 1 3 3
2 2 3 1
2 3 3 0
2 1 2 2
7 0 1 1
8 1 2 1
8 1 1 1
13 3 1 3
12 3 1 1
8 0 0 2
15 2 0 2
2 1 2 0
2 2 3 3
8 0 2 0
8 0 1 0
13 1 0 1
2 1 0 0
2 0 2 3
2 2 0 2
6 2 3 2
8 2 3 2
13 2 1 1
12 1 2 3
8 1 0 2
15 2 2 2
2 2 3 1
12 0 2 1
8 1 2 1
13 1 3 3
12 3 3 2
8 0 0 0
15 0 2 0
2 0 1 1
2 2 0 3
4 0 3 3
8 3 3 3
13 2 3 2
12 2 3 3
2 1 1 1
2 2 2 2
10 1 0 0
8 0 1 0
13 0 3 3
12 3 0 2
2 2 0 0
2 1 3 3
11 0 3 0
8 0 2 0
13 2 0 2
12 2 3 3
2 1 0 0
2 1 1 2
13 1 0 2
8 2 2 2
13 2 3 3
2 0 0 1
2 3 0 2
15 0 1 2
8 2 3 2
8 2 2 2
13 2 3 3
12 3 0 1
2 2 3 0
2 0 0 3
2 3 3 2
5 0 2 0
8 0 2 0
13 1 0 1
12 1 2 3
2 1 0 0
2 1 3 2
8 0 0 1
15 1 0 1
15 0 1 1
8 1 1 1
13 1 3 3
12 3 0 1
8 1 0 2
15 2 3 2
2 2 0 3
8 3 0 0
15 0 2 0
4 0 3 2
8 2 2 2
13 2 1 1
12 1 0 2
8 1 0 1
15 1 1 1
2 1 3 0
10 1 3 3
8 3 1 3
13 2 3 2
12 2 2 0
2 0 1 2
2 2 2 3
2 2 1 1
3 2 3 1
8 1 3 1
13 0 1 0
12 0 1 1
2 3 2 0
2 0 2 3
9 2 0 0
8 0 2 0
8 0 3 0
13 1 0 1
12 1 0 2
2 0 1 0
2 0 2 1
8 3 0 3
15 3 2 3
2 3 0 0
8 0 1 0
8 0 1 0
13 0 2 2
12 2 0 3
2 0 0 2
2 3 0 0
9 2 0 2
8 2 2 2
8 2 2 2
13 2 3 3
12 3 3 1
8 2 0 3
15 3 2 3
2 1 2 0
2 1 3 2
13 0 0 2
8 2 3 2
13 2 1 1
2 2 2 2
2 2 2 0
4 0 3 3
8 3 2 3
13 3 1 1
12 1 3 0
2 1 0 2
8 3 0 1
15 1 3 1
8 1 0 3
15 3 1 3
15 3 1 2
8 2 1 2
8 2 3 2
13 2 0 0
12 0 1 1
2 3 2 2
2 1 1 0
2 0 3 3
8 0 2 0
8 0 3 0
13 0 1 1
12 1 3 0
2 2 0 3
2 1 1 1
8 1 0 2
15 2 0 2
3 2 3 1
8 1 1 1
13 0 1 0
12 0 0 1
8 1 0 3
15 3 1 3
2 2 0 0
2 2 3 2
11 0 3 3
8 3 2 3
8 3 3 3
13 3 1 1
12 1 1 2
2 2 0 1
2 2 0 3
2 1 2 0
13 0 0 1
8 1 1 1
13 2 1 2
12 2 3 0
2 2 2 1
2 0 1 2
2 1 0 3
8 3 2 1
8 1 2 1
13 0 1 0
12 0 1 2
2 2 3 0
2 0 0 1
2 2 0 3
4 0 3 0
8 0 1 0
13 0 2 2
12 2 3 3
2 2 0 2
2 1 0 0
12 0 2 0
8 0 1 0
8 0 2 0
13 3 0 3
12 3 3 2
2 2 0 0
2 1 3 3
2 1 1 1
11 0 3 3
8 3 3 3
8 3 3 3
13 2 3 2
12 2 0 0
2 0 3 3
2 2 2 2
14 3 2 3
8 3 2 3
13 0 3 0
12 0 0 1
2 2 2 0
2 2 2 3
2 3 1 2
9 0 2 0
8 0 3 0
13 0 1 1
8 2 0 0
15 0 2 0
2 0 2 2
3 2 3 3
8 3 3 3
13 3 1 1
2 1 0 2
2 0 2 3
2 3 0 0
0 0 2 0
8 0 3 0
8 0 3 0
13 0 1 1
2 1 0 0
2 3 1 3
8 3 0 2
15 2 2 2
12 0 2 2
8 2 2 2
13 2 1 1
12 1 3 3
2 2 3 0
2 1 3 1
2 1 1 2
10 1 0 2
8 2 1 2
13 3 2 3
12 3 0 2
8 1 0 3
15 3 3 3
7 3 0 0
8 0 2 0
13 2 0 2
12 2 2 0
2 2 1 2
2 0 2 1
8 1 0 3
15 3 1 3
13 3 3 1
8 1 3 1
13 1 0 0
12 0 1 2
2 2 2 1
2 0 0 3
2 3 0 0
5 1 0 3
8 3 2 3
8 3 2 3
13 3 2 2
12 2 1 0
2 1 1 3
2 3 2 1
2 2 0 2
1 2 1 2
8 2 2 2
8 2 3 2
13 2 0 0
12 0 1 2
8 2 0 3
15 3 2 3
8 2 0 0
15 0 2 0
2 2 3 1
4 0 3 1
8 1 3 1
8 1 1 1
13 2 1 2
2 2 1 1
8 0 0 0
15 0 3 0
2 1 0 3
5 1 0 1
8 1 2 1
13 2 1 2
2 0 0 3
2 2 1 1
6 1 3 3
8 3 3 3
13 2 3 2
12 2 2 1
2 1 1 3
2 2 1 0
8 1 0 2
15 2 3 2
8 3 2 0
8 0 1 0
13 0 1 1
12 1 1 0
2 0 2 1
2 0 3 3
2 1 2 1
8 1 2 1
13 0 1 0
12 0 3 1
8 0 0 2
15 2 1 2
8 0 0 3
15 3 1 3
2 3 3 0
13 3 3 3
8 3 2 3
13 1 3 1
2 2 0 3
2 2 3 0
4 0 3 3
8 3 1 3
13 3 1 1
12 1 3 3
2 1 1 1
10 1 0 0
8 0 2 0
13 3 0 3
12 3 0 2
2 1 2 3
2 2 0 0
2 0 3 1
15 3 1 3
8 3 2 3
8 3 2 3
13 3 2 2
12 2 3 1
2 1 1 3
2 2 0 2
2 1 2 0
12 0 2 2
8 2 1 2
8 2 3 2
13 1 2 1
12 1 3 3
2 0 0 2
8 0 0 1
15 1 1 1
13 0 0 0
8 0 1 0
13 3 0 3
12 3 2 2
8 3 0 3
15 3 3 3
2 3 3 0
2 2 1 1
2 1 3 1
8 1 1 1
13 1 2 2
12 2 0 3
8 3 0 0
15 0 1 0
2 1 3 1
2 0 1 2
8 0 2 2
8 2 2 2
13 3 2 3
12 3 1 0
2 1 3 2
2 3 0 1
2 2 3 3
7 1 3 1
8 1 1 1
13 1 0 0
12 0 1 3
2 2 2 1
2 2 2 2
2 1 0 0
12 0 2 0
8 0 3 0
13 3 0 3
12 3 0 1
2 2 3 0
8 1 0 2
15 2 3 2
2 3 0 3
7 3 0 0
8 0 2 0
13 1 0 1
12 1 1 2
2 3 1 1
2 2 2 0
2 1 3 3
7 1 0 3
8 3 1 3
13 3 2 2
8 2 0 1
15 1 2 1
8 2 0 3
15 3 0 3
2 3 0 0
8 0 2 0
13 0 2 2
12 2 1 0
2 2 2 3
2 0 3 2
2 0 0 1
3 2 3 3
8 3 1 3
13 0 3 0
8 3 0 1
15 1 3 1
2 3 1 3
2 3 3 2
0 3 2 1
8 1 1 1
13 1 0 0
2 1 3 3
2 2 1 1
5 1 2 3
8 3 1 3
13 3 0 0
12 0 1 1
2 3 2 0
8 1 0 3
15 3 2 3
8 1 0 2
15 2 1 2
0 0 2 3
8 3 3 3
13 3 1 1
12 1 1 0
2 2 1 2
2 0 2 3
2 2 1 1
14 3 2 1
8 1 2 1
13 1 0 0
12 0 3 1
2 1 0 0
8 2 0 3
15 3 2 3
2 1 1 2
10 0 3 0
8 0 3 0
13 1 0 1
2 0 0 0
2 2 3 2
6 2 3 3
8 3 2 3
13 1 3 1
2 0 1 2
2 1 3 3
2 1 2 0
13 0 0 2
8 2 2 2
13 1 2 1
12 1 1 2
8 0 0 1
15 1 3 1
2 2 0 0
11 0 3 1
8 1 3 1
13 2 1 2
12 2 1 1
2 1 1 2
2 2 0 3
4 0 3 2
8 2 2 2
8 2 3 2
13 1 2 1
2 3 3 0
2 2 0 2
1 2 0 0
8 0 1 0
8 0 1 0
13 1 0 1
12 1 2 0
2 1 3 1
2 0 2 3
8 0 0 2
15 2 3 2
2 2 3 2
8 2 3 2
8 2 3 2
13 0 2 0
2 0 0 2
2 2 3 3
2 2 1 1
6 1 3 1
8 1 1 1
13 1 0 0
12 0 3 2
2 2 1 0
2 3 2 1
4 0 3 1
8 1 1 1
13 1 2 2
12 2 0 3
2 0 3 1
2 3 0 2
5 0 2 0
8 0 3 0
8 0 1 0
13 0 3 3
12 3 1 2
8 0 0 3
15 3 2 3
2 1 0 1
2 0 0 0
10 1 3 0
8 0 2 0
13 2 0 2
12 2 3 0
8 1 0 1
15 1 3 1
2 0 0 3
2 0 1 2
0 1 2 3
8 3 1 3
13 0 3 0
12 0 3 2
2 0 1 1
8 0 0 0
15 0 1 0
2 0 0 3
13 0 0 3
8 3 1 3
13 3 2 2
12 2 0 1
8 3 0 3
15 3 2 3
2 2 1 0
2 3 1 2
4 0 3 0
8 0 1 0
13 0 1 1
8 3 0 3
15 3 0 3
2 3 0 0
0 0 2 2
8 2 1 2
13 2 1 1
12 1 2 3
8 1 0 1
15 1 3 1
2 0 0 2
9 2 0 2
8 2 2 2
8 2 3 2
13 3 2 3
12 3 0 2
2 2 3 0
8 3 0 3
15 3 2 3
7 1 0 3
8 3 2 3
13 2 3 2
12 2 2 3
2 2 2 1
2 1 1 0
2 0 1 2
8 0 2 2
8 2 2 2
13 3 2 3
12 3 3 0
2 1 2 3
8 0 0 1
15 1 0 1
2 0 3 2
8 3 2 1
8 1 2 1
13 1 0 0
12 0 1 3
2 0 1 1
2 2 3 0
2 3 0 2
5 0 2 1
8 1 2 1
13 1 3 3
12 3 3 0
2 1 1 3
2 0 3 2
2 3 1 1
8 3 2 2
8 2 2 2
13 2 0 0
12 0 1 1
2 3 3 3
2 3 3 0
2 1 1 2
0 3 2 0
8 0 3 0
13 1 0 1
12 1 3 2
2 1 1 0
2 2 2 3
2 0 3 1
13 0 0 3
8 3 2 3
13 2 3 2
12 2 3 1
2 3 1 3
2 3 1 0
2 3 0 2
0 3 2 3
8 3 2 3
8 3 1 3
13 1 3 1
2 1 2 3
8 3 0 0
15 0 2 0
11 0 3 3
8 3 2 3
13 1 3 1
12 1 1 0
2 2 2 2
8 3 0 3
15 3 0 3
2 2 1 1
2 3 2 1
8 1 3 1
13 1 0 0
2 3 1 2
2 1 0 3
8 2 0 1
15 1 3 1
15 3 1 1
8 1 2 1
13 0 1 0
12 0 3 1
8 2 0 0
15 0 1 0
8 3 2 3
8 3 1 3
13 3 1 1
12 1 1 3
2 2 1 2
2 3 3 0
2 2 0 1
7 0 1 2
8 2 3 2
8 2 1 2
13 3 2 3
12 3 2 1
2 0 0 2
2 1 2 3
0 0 2 2
8 2 1 2
13 2 1 1
2 0 0 3
2 1 0 0
2 2 1 2
12 0 2 3
8 3 1 3
8 3 1 3
13 3 1 1
12 1 1 3
8 2 0 1
15 1 0 1
12 0 2 1
8 1 1 1
13 3 1 3
12 3 1 1
2 3 1 3
2 3 1 0
2 0 2 2
9 2 0 0
8 0 3 0
13 1 0 1
12 1 0 3
2 3 1 2
8 2 0 0
15 0 2 0
2 3 1 1
2 2 1 2
8 2 1 2
13 3 2 3
2 0 0 2
2 3 1 0
9 2 0 0
8 0 2 0
13 3 0 3
12 3 3 2
2 3 0 3
2 0 3 1
2 1 1 0
13 0 0 0
8 0 1 0
8 0 1 0
13 0 2 2
12 2 2 1
2 1 2 3
2 0 3 2
8 3 0 0
15 0 2 0
11 0 3 0
8 0 1 0
13 1 0 1
2 2 2 3
2 1 3 0
2 3 2 2
8 0 2 0
8 0 3 0
8 0 3 0
13 1 0 1
12 1 3 0
2 0 3 3
2 2 3 2
2 0 2 1
14 3 2 2
8 2 1 2
8 2 3 2
13 0 2 0
12 0 2 1
8 1 0 3
15 3 1 3
2 1 0 2
2 3 0 0
0 0 2 2
8 2 3 2
13 1 2 1
12 1 0 3
2 0 2 1
2 1 3 2
2 0 2 0
2 2 0 0
8 0 2 0
13 3 0 3
12 3 2 1
2 2 1 2
2 2 2 3
2 3 0 0
1 2 0 2
8 2 1 2
8 2 2 2
13 1 2 1
12 1 2 3
2 3 2 2
2 2 2 0
2 3 1 1
5 0 2 1
8 1 3 1
8 1 2 1
13 1 3 3
12 3 3 2
2 3 3 1
2 0 2 3
1 0 1 1
8 1 1 1
13 2 1 2
12 2 1 1
2 0 2 2
2 1 2 0
2 1 3 3
13 3 0 3
8 3 2 3
8 3 1 3
13 1 3 1
12 1 3 0
8 3 0 1
15 1 0 1
2 2 3 3
2 2 3 2
2 3 2 1
8 1 2 1
13 0 1 0
12 0 3 1
2 1 1 3
2 2 2 0
11 0 3 0
8 0 2 0
13 0 1 1
12 1 2 0
2 2 2 3
2 2 1 1
6 1 3 3
8 3 3 3
13 3 0 0
12 0 0 3
8 3 0 0
15 0 2 0
2 3 0 1
1 0 1 0
8 0 1 0
13 0 3 3
12 3 2 2
2 2 0 3
8 0 0 0
15 0 2 0
4 0 3 0
8 0 3 0
13 0 2 2
12 2 1 0
8 3 0 3
15 3 1 3
8 2 0 2
15 2 0 2
15 3 1 1
8 1 1 1
13 0 1 0
2 3 3 1
8 3 2 1
8 1 1 1
13 1 0 0
12 0 1 2
8 2 0 0
15 0 2 0
2 0 2 1
11 0 3 3
8 3 1 3
13 2 3 2
12 2 3 1
8 0 0 3
15 3 0 3
2 3 1 2
5 0 2 3
8 3 2 3
8 3 3 3
13 1 3 1
12 1 1 2
8 3 0 3
15 3 3 3
2 2 0 1
2 3 3 0
5 1 0 3
8 3 3 3
13 3 2 2
12 2 3 3
2 1 1 0
2 1 2 1
2 2 1 2
12 0 2 2
8 2 3 2
13 2 3 3
12 3 1 2
2 0 3 3
8 0 0 0
15 0 2 0
6 0 3 1
8 1 3 1
13 2 1 2
2 3 3 0
2 2 0 3
8 1 0 1
15 1 3 1
7 1 3 3
8 3 3 3
13 3 2 2
2 1 2 0
2 2 1 3
2 2 2 1
6 1 3 1
8 1 2 1
13 1 2 2
12 2 0 1
2 0 1 2
2 1 2 3
2 2 3 0
11 0 3 2
8 2 3 2
8 2 1 2
13 1 2 1
2 1 0 0
2 2 3 2
12 0 2 0
8 0 3 0
8 0 1 0
13 1 0 1
12 1 1 0
2 3 0 1
8 0 0 2
15 2 0 2
0 1 2 3
8 3 3 3
13 0 3 0
12 0 1 3
2 2 0 2
2 1 1 0
12 0 2 1
8 1 2 1
13 1 3 3
2 0 0 1
2 3 1 0
2 1 2 2
0 0 2 1
8 1 2 1
8 1 2 1
13 3 1 3
12 3 3 2
2 2 0 3
2 2 3 0
2 1 0 1
4 0 3 3
8 3 3 3
13 2 3 2
8 0 0 3
15 3 3 3
2 3 1 1
1 0 1 1
8 1 3 1
13 1 2 2
12 2 0 1
2 2 2 2
2 1 0 0
2 0 3 3
14 3 2 3
8 3 3 3
8 3 1 3
13 3 1 1
12 1 2 3
2 3 0 0
8 2 0 1
15 1 2 1
5 1 0 1
8 1 1 1
13 1 3 3
12 3 2 1
8 1 0 3
15 3 0 3
14 3 2 2
8 2 1 2
13 1 2 1
12 1 2 3
2 3 1 1
2 1 3 0
2 2 0 2
12 0 2 0
8 0 1 0
13 0 3 3
12 3 1 0
2 1 0 3
15 3 1 1
8 1 3 1
13 1 0 0
12 0 1 2
2 2 3 0
8 3 0 3
15 3 2 3
2 3 3 1
4 0 3 3
8 3 2 3
13 2 3 2
2 1 2 1
2 1 0 0
2 2 2 3
10 1 3 1
8 1 2 1
13 1 2 2
12 2 3 1
2 2 0 2
2 0 0 3
12 0 2 2
8 2 3 2
13 2 1 1
12 1 3 3
2 2 3 2
2 0 0 1
2 3 1 0
1 2 0 2
8 2 1 2
13 2 3 3
12 3 3 1
2 1 0 0
2 1 0 3
2 0 1 2
8 0 2 2
8 2 1 2
8 2 3 2
13 2 1 1
12 1 1 2
2 3 1 3
2 2 2 1
2 2 1 0
7 3 0 3
8 3 3 3
13 3 2 2
12 2 2 1
2 3 3 3
2 0 1 2
0 3 2 2
8 2 2 2
13 2 1 1
12 1 0 2
2 3 2 1
2 1 0 0
2 2 0 3
15 0 1 1
8 1 3 1
8 1 1 1
13 2 1 2
2 2 3 0
2 2 0 1
8 0 0 3
15 3 0 3
6 0 3 1
8 1 2 1
13 1 2 2
2 0 1 1
2 1 0 3
11 0 3 0
8 0 3 0
8 0 2 0
13 0 2 2
12 2 0 0
    """.trimIndent()
            .split("\n")
            .map { it.split(" ").map { it.toInt() } }

}