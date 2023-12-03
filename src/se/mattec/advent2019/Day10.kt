package se.mattec.advent2019

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    Day10.problem1And2()
}

object Day10 {

    fun problem1And2() {
        val asteroids = mutableMapOf<Pair<Int, Int>, Unit>()

        data.forEachIndexed { y, s1 ->
            s1.forEachIndexed { x, s2 ->
                if (s2 == '#') {
                    asteroids += (x to y) to Unit
                }
            }
        }

        val asteroidsList = asteroids.keys

        val asteroidsToAngles = mutableMapOf<Pair<Int, Int>, MutableMap<Pair<Int, Int>, Float>>()
        val asteroidsToDistances = mutableMapOf<Pair<Int, Int>, MutableMap<Pair<Int, Int>, Float>>()

        asteroidsList.forEach { current ->
            val angleMap = asteroidsToAngles.getOrElse(current) { mutableMapOf() }
            val distanceMap = asteroidsToDistances.getOrElse(current) { mutableMapOf() }
            asteroidsList.forEach { other ->
                val deltaX = (current.first - other.first).toFloat()
                val deltaY = (current.second - other.second).toFloat()
                angleMap += other to atan2(deltaY, deltaX)
                distanceMap += other to distance(current, other)
            }
            asteroidsToAngles += current to angleMap
            asteroidsToDistances += current to distanceMap
        }

        val asteroidsToInViewCount = asteroidsToAngles.mapValues { it.value.values.distinct().count() }
        val optimalAsteroidToAngle = asteroidsToInViewCount.maxBy { it.value }!!

        println("Problem 1: ${optimalAsteroidToAngle.value}")

        val optimalAsteroidToOtherAsteroidsAngle = asteroidsToAngles[optimalAsteroidToAngle.key]!!
        val optimalAsteroidToOtherAsteroidsDistance = asteroidsToDistances[optimalAsteroidToAngle.key]!!

        val vaporizedAsteroids = mutableListOf<Pair<Int, Int>>()

        // Delete optimal asteroid. Not going to vaporize it...
        optimalAsteroidToOtherAsteroidsAngle.remove(optimalAsteroidToAngle.key)
        optimalAsteroidToOtherAsteroidsDistance.remove(optimalAsteroidToAngle.key)

        while (vaporizedAsteroids.size < 200) {
            val sortedByAngle = optimalAsteroidToOtherAsteroidsAngle.map { it }.sortedBy { it.value }
            val upIndex = sortedByAngle.indexOfFirst { it.value >= PI / 2 }
            val fromUp = sortedByAngle.subList(upIndex, sortedByAngle.size) + sortedByAngle.subList(0, upIndex)

            var i = 0
            while (i < fromUp.size) {
                val toRemove = fromUp[i]
                if (fromUp.count { it.value == toRemove.value } > 1) {
                    val allAsteroidsWithSameAngle = optimalAsteroidToOtherAsteroidsAngle.filter { it.value == toRemove.value }
                    val lowestDistance = optimalAsteroidToOtherAsteroidsDistance.filter { allAsteroidsWithSameAngle.containsKey(it.key) }.minBy { it.value }!!
                    vaporizedAsteroids += lowestDistance.key
                    optimalAsteroidToOtherAsteroidsAngle.remove(lowestDistance.key)
                    i += allAsteroidsWithSameAngle.size
                } else {
                    vaporizedAsteroids += toRemove.key
                    optimalAsteroidToOtherAsteroidsAngle.remove(toRemove.key)
                    i += 1
                }
            }
        }

        val twoHoundred = vaporizedAsteroids[199]

        print("Problem 2: ${twoHoundred.first * 100 + twoHoundred.second}")
    }

    private fun distance(from: Pair<Int, Int>, to: Pair<Int, Int>): Float {
        return sqrt((from.first - to.first).toFloat().pow(2) + (from.second - to.second).toFloat().pow(2))
    }

    private val data = """
.............#..#.#......##........#..#
.#...##....#........##.#......#......#.
..#.#.#...#...#...##.#...#.............
.....##.................#.....##..#.#.#
......##...#.##......#..#.......#......
......#.....#....#.#..#..##....#.......
...................##.#..#.....#.....#.
#.....#.##.....#...##....#####....#.#..
..#.#..........#..##.......#.#...#....#
...#.#..#...#......#..........###.#....
##..##...#.#.......##....#.#..#...##...
..........#.#....#.#.#......#.....#....
....#.........#..#..##..#.##........#..
........#......###..............#.#....
...##.#...#.#.#......#........#........
......##.#.....#.#.....#..#.....#.#....
..#....#.###..#...##.#..##............#
...##..#...#.##.#.#....#.#.....#...#..#
......#............#.##..#..#....##....
.#.#.......#..#...###...........#.#.##.
........##........#.#...#.#......##....
.#.#........#......#..........#....#...
...............#...#........##..#.#....
.#......#....#.......#..#......#.......
.....#...#.#...#...#..###......#.##....
.#...#..##................##.#.........
..###...#.......#.##.#....#....#....#.#
...#..#.......###.............##.#.....
#..##....###.......##........#..#...#.#
.#......#...#...#.##......#..#.........
#...#.....#......#..##.............#...
...###.........###.###.#.....###.#.#...
#......#......#.#..#....#..#.....##.#..
.##....#.....#...#.##..#.#..##.......#.
..#........#.......##.##....#......#...
##............#....#.#.....#...........
........###.............##...#........#
#.........#.....#..##.#.#.#..#....#....
..............##.#.#.#...........#.....
    """.trimIndent()
            .split("\n")

}