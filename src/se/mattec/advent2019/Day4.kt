package se.mattec.advent2019

fun main() {
    println(Day4.problem1())
    println(Day4.problem2())
}

object Day4 {

    private val min = 246540
    private val max = 787419
    private val range = (0..9).map { it.toString() }

    fun problem1(): Int {
        var count = 0
        for (password in min..max) {
            if (isValid1(password.toString())) {
                count++
            }
        }
        return count
    }

    private fun isValid1(input: String): Boolean {
        val hasAdjacent = range.any { digit -> input.contains("$digit$digit") }

        val isIncreasing = !input.mapIndexed { index, digit ->
            !(index < input.length - 1 && input[index + 1].toInt() < digit.toInt())
        }.any { !it }

        return hasAdjacent && isIncreasing
    }

    fun problem2(): Int {
        var count = 0
        for (password in min..max) {
            if (isValid2(password.toString())) {
                count++
            }
        }
        return count
    }

    private fun isValid2(input: String): Boolean {
        val hasAdjacent = range.any { digit -> input.contains("$digit$digit") && !input.contains("$digit$digit$digit") }

        val isIncreasing = !input.mapIndexed { index, digit ->
            !(index < input.length - 1 && input[index + 1].toInt() < digit.toInt())
        }.any { !it }

        return hasAdjacent && isIncreasing
    }

}