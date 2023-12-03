package se.mattec.advent2019

fun main() {
    println(Day2.problem1(12, 2))
    println(Day2.problem2())
}

object Day2 {

    fun problem1(noun: Int, verb: Int): Int {
        val data = data.toTypedArray()

        data[1] = noun
        data[2] = verb

        var i = 0
        loop@ while (i < data.size - 4) {
            when (data[i]) {
                99 -> break@loop
                1 -> add(data, data[i + 1], data[i + 2], data[i + 3])
                2 -> multiply(data, data[i + 1], data[i + 2], data[i + 3])
            }

            i += 4
        }

        return data[0]
    }

    fun add(data: Array<Int>, pos1: Int, pos2: Int, outputPos: Int) {
        data[outputPos] = data[pos1] + data[pos2]
    }

    fun multiply(data: Array<Int>, pos1: Int, pos2: Int, outputPos: Int) {
        data[outputPos] = data[pos1] * data[pos2]
    }

    fun problem2(): Int {
        (0 until 99).forEach { noun ->
            (0 until 99).forEach { verb ->
                if (problem1(noun, verb) == 19690720) {
                    return 100 * noun + verb
                }
            }
        }
        throw Exception("No answer found.")
    }

    private val data = "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,10,19,1,9,19,23,1,13,23,27,1,5,27,31,2,31,6,35,1,35,5,39,1,9,39,43,1,43,5,47,1,47,5,51,2,10,51,55,1,5,55,59,1,59,5,63,2,63,9,67,1,67,5,71,2,9,71,75,1,75,5,79,1,10,79,83,1,83,10,87,1,10,87,91,1,6,91,95,2,95,6,99,2,99,9,103,1,103,6,107,1,13,107,111,1,13,111,115,2,115,9,119,1,119,6,123,2,9,123,127,1,127,5,131,1,131,5,135,1,135,5,139,2,10,139,143,2,143,10,147,1,147,5,151,1,151,2,155,1,155,13,0,99,2,14,0,0"
            .split(",")
            .map { it.toInt() }

}