package se.mattec.advent2021

fun main(args: Array<String>) {
    println(Day11.problem1())
    println(Day11.problem2())
}

object Day11 {

    fun problem1(): String {
        var flashes = 0

        val copy = data.toList()

        repeat(100) {
            flashes += performStep(copy)
        }

        return flashes.toString()
    }

    private fun performStep(data: List<MutableList<Int>>): Int {
        var flashesForStep = 0

        val flashed = Array(data.size) { Array(data[0].size) { false } }

        data.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, energy ->
                data[rowIndex][colIndex] = energy + 1
            }
        }

        do {
            var flashesLastRound = 0
            data.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, energy ->
                    val alreadyFlashed = flashed[rowIndex][colIndex]
                    if (energy > 9 && !alreadyFlashed) {
                        flashesLastRound++
                        increaseAdjacent(data, rowIndex, colIndex)
                        flashed[rowIndex][colIndex] = true
                    }
                }
            }
            flashesForStep += flashesLastRound
        } while (flashesLastRound != 0)

        data.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, energy ->
                data[rowIndex][colIndex] = if (energy > 9) 0 else energy
            }
        }

        return flashesForStep
    }

    private fun increaseAdjacent(data: List<MutableList<Int>>, rowIndex: Int, colIndex: Int) {
        listOf(
                rowIndex - 1 to colIndex - 1,
                rowIndex - 1 to colIndex,
                rowIndex - 1 to colIndex + 1,
                rowIndex to colIndex - 1,
                rowIndex to colIndex + 1,
                rowIndex + 1 to colIndex - 1,
                rowIndex + 1 to colIndex,
                rowIndex + 1 to colIndex + 1
        ).forEach {
            try {
                data[it.first][it.second]++
            } catch (ignore: IndexOutOfBoundsException) {
                // Don't care if outside
            }
        }
    }

    fun problem2(): String {
        val copy = data.toList()
        val numberOfOctopus = copy.flatten().size

        var steps = 1
        while (performStep(copy) != numberOfOctopus) {
            steps++
        }

        return steps.toString()
    }

    private val data = """
1254117228
4416873224
8354381553
1372637614
5586538553
7213333427
3571362825
1681126243
8718312138
5254266347
    """.trimIndent()
            .split("\n")
            .map { it.toCharArray().map { it.toString().toInt() }.toMutableList() }

}