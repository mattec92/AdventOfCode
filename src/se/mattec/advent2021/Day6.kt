package se.mattec.advent2021

fun main() {
    println(Day6.problem1())
    println(Day6.problem2())
}

object Day6 {

    fun problem1(): String {
        var fishTimers = data.toMutableList()

        (0 until 80).forEach {
            val newFish = mutableListOf<Int>()
            fishTimers = fishTimers.map {
                if (it == 0) {
                    newFish += 8
                    6
                } else {
                    it - 1
                }
            }.toMutableList()
            fishTimers.addAll(newFish)
        }

        return fishTimers.size.toString()
    }

    fun problem2(): String {
        var fishTimers = data.groupBy { it }.mapValues { it.value.size.toLong() }

        (0 until 256).forEach {
            fishTimers = mapOf(
                    0 to (fishTimers[1] ?: 0),
                    1 to (fishTimers[2] ?: 0),
                    2 to (fishTimers[3] ?: 0),
                    3 to (fishTimers[4] ?: 0),
                    4 to (fishTimers[5] ?: 0),
                    5 to (fishTimers[6] ?: 0),
                    6 to (fishTimers[7] ?: 0) + (fishTimers[0] ?: 0),
                    7 to (fishTimers[8] ?: 0),
                    8 to (fishTimers[0] ?: 0)
            ).toMap()
        }

        return fishTimers.map { it.value }.sum().toString()
    }

    private val data = """
3,3,5,1,1,3,4,2,3,4,3,1,1,3,3,1,5,4,4,1,4,1,1,1,3,3,2,3,3,4,2,5,1,4,1,2,2,4,2,5,1,2,2,1,1,1,1,4,5,4,3,1,4,4,4,5,1,1,4,3,4,2,1,1,1,1,5,2,1,4,2,4,2,5,5,5,3,3,5,4,5,1,1,5,5,5,2,1,3,1,1,2,2,2,2,1,1,2,1,5,1,2,1,2,5,5,2,1,1,4,2,1,4,2,1,1,1,4,2,5,1,5,1,1,3,1,4,3,1,3,2,1,3,1,4,1,2,1,5,1,2,1,4,4,1,3,1,1,1,1,1,5,2,1,5,5,5,3,3,1,2,4,3,2,2,2,2,2,4,3,4,4,4,1,2,2,3,1,1,4,1,1,1,2,1,4,2,1,2,1,1,2,1,5,1,1,3,1,4,3,2,1,1,1,5,4,1,2,5,2,2,1,1,1,1,2,3,3,2,5,1,2,1,2,3,4,3,2,1,1,2,4,3,3,1,1,2,5,1,3,3,4,2,3,1,2,1,4,3,2,2,1,1,2,1,4,2,4,1,4,1,4,4,1,4,4,5,4,1,1,1,3,1,1,1,4,3,5,1,1,1,3,4,1,1,4,3,1,4,1,1,5,1,2,2,5,5,2,1,5
    """.trimIndent()
            .split(",")
            .map { it.toInt() }

}