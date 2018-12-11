package se.mattec.advent2018

fun main(args: Array<String>) {
    println(Day11.problem1())
    println(Day11.problem2())
}

object Day11 {

    fun problem1(): String {
        val grid = generateGrid()
        val result = findMax(grid, 3)
        return "${result[0]},${result[1]}"
    }

    /*
     * Kind of slow, naive solution. Should be able to reuse result from previous executions.
     * Ex. Sum of 4x4 is the same of 4x2x2
     * -------------     -------------
     * | x x | x x |     | x x   x x |
     * | x x | x x |     | x x   x x |
     * | --------- |  => |           |
     * | x x | x x |     | x x   x x |
     * | x x | x x |     | x x   x x |
     * -------------     -------------
     */
    fun problem2(): String {
        val grid = generateGrid()

        var maxX = 0
        var maxY = 0
        var maxSum = 0
        var maxSize = 0
        for (size in 3 until 300) {
            val result = findMax(grid, size)
            if (result[2] > maxSum) {
                maxX = result[0]
                maxY = result[1]
                maxSum = result[2]
                maxSize = size
            }
        }

        return "$maxX,$maxY,$maxSize"
    }

    private fun generateGrid(): Array<IntArray> {
        val grid = Array(300) { IntArray(300) }

        for (x in 0 until 300) {
            for (y in 0 until 300) {
                val rackId = x + 10
                var powerLevel = rackId * y
                powerLevel += serialNumber
                powerLevel *= rackId
                powerLevel = (powerLevel / 100) % 10
                powerLevel -= 5
                grid[x][y] = powerLevel
            }
        }

        return grid
    }

    private fun findMax(grid: Array<IntArray>, size: Int): IntArray {
        var maxX = 0
        var maxY = 0
        var max = Int.MIN_VALUE
        for (x in 0 until 300 - size) {
            for (y in 0 until 300 - size) {
                var sum = 0
                for (internalX in x until x + size) {
                    for (internalY in y until y + size) {
                        sum += grid[internalX][internalY]
                    }
                }
                if (sum > max) {
                    max = sum
                    maxX = x
                    maxY = y
                }
            }
        }

        return IntArray(3).also {
            it[0] = maxX
            it[1] = maxY
            it[2] = max
        }
    }

    private data class Result(val x: Int, val y: Int, val sum: Int, val size: Int)

    private val serialNumber = 6878

}