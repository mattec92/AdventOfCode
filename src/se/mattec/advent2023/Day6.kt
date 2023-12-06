package se.mattec.advent2023

fun main() {
    println(Day6.problem1())
    println(Day6.problem2())
}

object Day6 {

    fun problem1(): String {
        return data[0].mapIndexed { index, time ->
            val distanceToWin = data[1][index]
            (0..time).map { timeHeld ->
                val timeForMovement = time - timeHeld
                val speed = timeHeld
                timeForMovement * speed
            }.count { it > distanceToWin }
        }.reduce { acc, distance -> acc * distance }.toString()
    }

    fun problem2(): String {
        val time = data[0].joinToString("").toLong()
        val distanceToWin = data[1].joinToString("").toLong()
        return (0..time).map { timeHeld ->
            val timeForMovement = time - timeHeld
            val speed = timeHeld
            timeForMovement * speed
        }.count { it > distanceToWin }.toString()
    }

    private val data = """
Time:        34     90     89     86
Distance:   204   1713   1210   1780
    """.trimIndent()
        .split("\n")
        .map { it.split(":")[1] }
        .map { it.split(" ").filter { it.isNotBlank() }.map { it.toInt() } }

}