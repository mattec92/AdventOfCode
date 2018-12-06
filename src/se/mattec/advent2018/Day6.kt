package se.mattec.advent2018

import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    println(Day6.problem1())
    println(Day6.problem2())
}

object Day6 {

    fun problem1(): Int {
        //Find out size of grid
        val maxX = data.maxBy { it.first }!!.first
        val maxY = data.maxBy { it.second }!!.second

        //Generate all possible grid coordinates
        val grid = mutableListOf<Pair<Int, Int>>()
        for (x in 0..maxX) {
            for (y in 0..maxY) {
                grid += x to y
            }
        }

        //Find which coordinate is closest to the grid positions
        val excluded = mutableSetOf<Pair<Int, Int>>()
        val closestCoordinates = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        for (coordinate in grid) {
            val distances = data.map { distance(coordinate, it) }
            val smallestDistance = distances.min()
            val hasMultipleMatches = distances.count { it == smallestDistance } > 1
            if (hasMultipleMatches) {
                excluded += coordinate
            } else {
                val closestCoordinate = data[distances.indexOf(smallestDistance)]
                closestCoordinates[coordinate] = closestCoordinate
            }
        }

        //Remove coordinates that have areas next to the edges
        val coordinatesLeft = data.toMutableList()
        for (x in 0..maxX) {
            coordinatesLeft.remove(closestCoordinates[Pair(x, 0)])
            coordinatesLeft.remove(closestCoordinates[Pair(x, maxY)])
        }

        for (y in 0..maxY) {
            coordinatesLeft.remove(closestCoordinates[Pair(0, y)])
            coordinatesLeft.remove(closestCoordinates[Pair(maxX, y)])
        }

        //Find out which coordinate has the largest area
        return closestCoordinates.values.groupBy { it }
                .filter { it.key in coordinatesLeft }
                .map { it.value.size }
                .max()!!
    }

    fun problem2(): Int {
        //Find out size of grid
        val maxX = data.maxBy { it.first }!!.first
        val maxY = data.maxBy { it.second }!!.second

        //Generate all possible grid coordinates
        val grid = mutableListOf<Pair<Int, Int>>()
        for (x in 0..maxX) {
            for (y in 0..maxY) {
                grid += x to y
            }
        }

        //Find sum of distance to coordinates from grid position
        val distanceTooCoordinates = mutableMapOf<Pair<Int, Int>, Int>()
        for (coordinate in grid) {
            distanceTooCoordinates[coordinate] = data.sumBy { distance(coordinate, it) }
        }

        //How many grid positions are within 10000 of all coordinates
        return distanceTooCoordinates.values.filter { it < 10000 }.size
    }

    private fun distance(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int {
        return (p1.first - p2.first).absoluteValue + (p1.second - p2.second).absoluteValue
    }

    private val data = """
108, 324
46, 91
356, 216
209, 169
170, 331
332, 215
217, 104
75, 153
110, 207
185, 102
61, 273
233, 301
278, 151
333, 349
236, 249
93, 155
186, 321
203, 138
103, 292
47, 178
178, 212
253, 174
348, 272
83, 65
264, 227
239, 52
243, 61
290, 325
135, 96
165, 339
236, 132
84, 185
94, 248
164, 82
325, 202
345, 323
45, 42
292, 214
349, 148
80, 180
314, 335
210, 264
302, 108
235, 273
253, 170
150, 303
249, 279
255, 159
273, 356
275, 244
    """.trimIndent()
            .split("\n")
            .map {
                val split = it.split(", ")
                split[0].toInt() to split[1].toInt()
            }

}