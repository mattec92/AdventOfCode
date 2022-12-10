package se.mattec.advent2022

fun main(args: Array<String>) {
    println(Day10.problem1())
    println(Day10.problem2())
}

object Day10 {

    fun problem1(): String {
        val instructions = data.toMutableList()
        var registerValue = 1
        var currentCycle = 1

        var signalStrength = 0

        var postponedAddX: Instruction.AddX? = null
        while (instructions.isNotEmpty()) {
            if (currentCycle == 20 || currentCycle > 20 && currentCycle % 40 == 20) {
                val currentSignalStrength = currentCycle * registerValue
                signalStrength += currentSignalStrength
            }

            if (postponedAddX != null) {
                registerValue += postponedAddX.x
                postponedAddX = null
            } else {
                when (val instruction = instructions.removeFirst()) {
                    is Instruction.AddX -> postponedAddX = instruction
                    Instruction.NoOp -> Unit // Do nothing
                }
            }

            currentCycle++
        }

        return signalStrength.toString()
    }

    fun problem2(): String {
        val instructions = data.toMutableList()
        val pixels = IntArray(241) { 0 }
        var registerValue = 1
        var currentCycle = 1

        var postponedAddX: Instruction.AddX? = null
        while (instructions.isNotEmpty()) {
            pixels[currentCycle] = if ((currentCycle % 40) - 1 in (registerValue - 1)..(registerValue + 1)) 1 else 0

            if (postponedAddX != null) {
                registerValue += postponedAddX.x
                postponedAddX = null
            } else {
                when (val instruction = instructions.removeFirst()) {
                    is Instruction.AddX -> postponedAddX = instruction
                    Instruction.NoOp -> Unit // Do nothing
                }
            }

            currentCycle++
        }

        pixels.forEachIndexed { index, i ->
            if (index == 0) return@forEachIndexed
            print(if (i == 0) "." else "#")
            if (index % 40 == 0) {
                println()
            }
        }

        return ""
    }

    private val data = """
addx 1
addx 4
addx -2
addx 3
addx 3
addx 1
noop
addx 5
noop
noop
noop
addx 5
addx 2
addx 3
noop
addx 2
addx 4
noop
addx -1
noop
addx 3
addx -10
addx -17
noop
addx -3
addx 2
addx 25
addx -24
addx 2
addx 5
addx 2
addx 3
noop
addx 2
addx 14
addx -9
noop
addx 5
noop
noop
addx -2
addx 5
addx 2
addx -5
noop
noop
addx -19
addx -11
addx 5
addx 3
noop
addx 2
addx 3
addx -2
addx 2
noop
addx 3
addx 4
noop
noop
addx 5
noop
noop
noop
addx 5
addx -3
addx 8
noop
addx -15
noop
addx -12
addx -9
noop
addx 6
addx 7
addx -6
addx 4
noop
noop
noop
addx 4
addx 1
addx 5
addx -11
addx 29
addx -15
noop
addx -12
addx 17
addx 7
noop
noop
addx -32
addx 3
addx -8
addx 7
noop
addx -2
addx 5
addx 2
addx 6
addx -8
addx 5
addx 2
addx 5
addx 17
addx -12
addx -2
noop
noop
addx 7
addx 9
addx -8
addx 2
addx -33
addx -1
addx 2
noop
addx 26
addx -22
addx 19
addx -16
addx 8
addx -1
addx 3
addx -2
addx 2
addx -17
addx 24
addx 1
noop
addx 5
addx -1
noop
addx 5
noop
noop
addx 1
noop
noop
    """.trimIndent()
        .split("\n")
        .map {
            if (it == "noop") {
                Instruction.NoOp
            } else {
                val x = it.split(" ")[1].toInt()
                Instruction.AddX(x)
            }
        }

    private sealed interface Instruction {
        data class AddX(val x: Int) : Instruction
        object NoOp : Instruction
    }

}