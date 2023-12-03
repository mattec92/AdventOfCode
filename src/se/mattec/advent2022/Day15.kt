package se.mattec.advent2022

import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    println(Day15.problem1())
    println(Day15.problem2())
}

object Day15 {

    fun problem1(): String {
        val minX = (data.map { it.sensorX }).min()
        val maxX = (data.map { it.sensorX }).max()

        val manhattanDistances =
            data.associateWith { manhattanDistance(it.sensorX, it.sensorY, it.beaconX, it.beaconY) }

        val maxManhattan = manhattanDistances.maxBy { it.value }.value

        val countSafe = (minX - maxManhattan until maxX + maxManhattan).count { x ->
            manhattanDistances.any {
                manhattanDistance(it.key.sensorX, it.key.sensorY, x, 2_000_000) <= it.value
            }
        } - data.distinctBy { it.beaconX to it.beaconY }.count { it.beaconY == 2_000_000 }

        return countSafe.toString()
    }

    private fun manhattanDistance(fromX: Int, fromY: Int, toX: Int, toY: Int): Int {
        return abs(toX - fromX) + abs(toY - fromY)
    }

    private const val MIN_COORDINATE = 0
    private const val MAX_COORDINATE = 4_000_000

    fun problem2(): String {
        val manhattanDistances =
            data.associateWith { manhattanDistance(it.sensorX, it.sensorY, it.beaconX, it.beaconY) }

        manhattanDistances
            .asSequence()
            .flatMap { (sensor, distance) ->
                (0..distance).map {
                    listOf(
                        (max(sensor.sensorX - (distance - it), MIN_COORDINATE)..min(
                            sensor.sensorX + (distance - it),
                            MAX_COORDINATE
                        ) to (sensor.sensorY - it)),
                        (max(sensor.sensorX - (distance - it), MIN_COORDINATE)..min(
                            sensor.sensorX + (distance - it),
                            MAX_COORDINATE
                        ) to (sensor.sensorY + it))
                    )
                }.flatten()
            }
            .filter { it.second in MIN_COORDINATE..MAX_COORDINATE } // Within Y bounds
            .groupBy { it.second }
            .mapValues { it.value.map { it.first } }
            .forEach { (y, ranges) ->
                val mergedRanges = mergeRanges(ranges)
                if (mergedRanges.size != 1) {
                    val gapX = findGap(mergedRanges).toLong()
                    return (gapX * 4_000_000 + y).toString()
                }
            }

        throw IllegalStateException("No solution")
    }

    private fun overlaps(r1: IntRange, r2: IntRange): Boolean {
        return r1.first <= r2.last && r1.last >= r2.first || abs(r1.first - r2.last) == 1 || abs(r2.first - r1.last) == 1
    }

    private fun mergeRanges(inputRanges: List<IntRange>): List<IntRange> {
        val ranges = inputRanges.distinct().toMutableList()

        var rangeToAdd: IntRange?
        var rangesToRemove: MutableList<IntRange>
        do {
            rangeToAdd = null
            rangesToRemove = mutableListOf()
            outer@ for (i in 0 until ranges.size) {
                for (j in 0 until ranges.size) {
                    if (i == j) continue
                    if (overlaps(ranges[i], ranges[j])) {
                        rangeToAdd = min(ranges[i].first, ranges[j].first)..max(ranges[i].last, ranges[j].last)
                        rangesToRemove += ranges[i]
                        rangesToRemove += ranges[j]
                        break@outer
                    }
                }
            }
            ranges.removeAll(rangesToRemove)
            if (rangeToAdd != null) {
                ranges += rangeToAdd
            }
        } while (rangeToAdd != null)

        return ranges
    }

    private fun findGap(ranges: List<IntRange>): Int {
        if (ranges.size != 2) throw IllegalStateException("Can only find gap between 2 ranges.")
        val leadingRange = ranges.minBy { it.last }
        val trailingRange = ranges.maxBy { it.first }
        val gapFromLeadingEdge = leadingRange.last + 1
        val gapFromTrailingEdge = trailingRange.first - 1
        if (gapFromLeadingEdge != gapFromTrailingEdge) throw IllegalStateException("Can only find gaps of size 1.")
        return gapFromLeadingEdge
    }

    private fun printGrid(ranges: Map<Int, List<Pair<IntRange, Int>>>) {
        val minY = ranges.keys.min()
        val maxY = ranges.keys.max()
        val minX = ranges.values.minBy { it.minBy { it.first.first }.first.first }.minBy { it.first.first }.first.first
        val maxX = ranges.values.maxBy { it.maxBy { it.first.last }.first.last }.maxBy { it.first.last }.first.last

        for (y in minY..maxY) {
            val rangesForY = ranges[y]
            for (x in minX..maxX) {
                var hadBeacon = false

                val xRanges = rangesForY?.map { it.first }

                if (xRanges != null) {
                    for (range in xRanges) {
                        if (x in range) {
                            print("#")
                            hadBeacon = true
                            break
                        }
                    }
                }

                if (!hadBeacon) {
                    print(".")
                }
            }
            println()
        }
    }

    private val data = """
Sensor at x=3289936, y=2240812: closest beacon is at x=3232809, y=2000000
Sensor at x=30408, y=622853: closest beacon is at x=-669401, y=844810
Sensor at x=3983196, y=3966332: closest beacon is at x=3232807, y=4625568
Sensor at x=929672, y=476353: closest beacon is at x=-669401, y=844810
Sensor at x=1485689, y=3597734: closest beacon is at x=1951675, y=3073734
Sensor at x=69493, y=1886070: closest beacon is at x=-669401, y=844810
Sensor at x=2146060, y=3999371: closest beacon is at x=2300657, y=4128792
Sensor at x=3228558, y=3890086: closest beacon is at x=3232807, y=4625568
Sensor at x=3031444, y=2295853: closest beacon is at x=2928827, y=2611422
Sensor at x=374444, y=3977240: closest beacon is at x=-888612, y=4039783
Sensor at x=1207660, y=2710720: closest beacon is at x=1951675, y=3073734
Sensor at x=3851310, y=61626: closest beacon is at x=4807592, y=976495
Sensor at x=3195193, y=3022787: closest beacon is at x=2928827, y=2611422
Sensor at x=1784895, y=2111901: closest beacon is at x=1951675, y=3073734
Sensor at x=2894075, y=2427030: closest beacon is at x=2928827, y=2611422
Sensor at x=3301867, y=803327: closest beacon is at x=3232809, y=2000000
Sensor at x=2506616, y=3673347: closest beacon is at x=2300657, y=4128792
Sensor at x=2628426, y=3054377: closest beacon is at x=1951675, y=3073734
Sensor at x=2521975, y=1407505: closest beacon is at x=3232809, y=2000000
Sensor at x=2825447, y=2045173: closest beacon is at x=3232809, y=2000000
Sensor at x=2261212, y=2535886: closest beacon is at x=2928827, y=2611422
Sensor at x=3956000, y=1616443: closest beacon is at x=3232809, y=2000000
Sensor at x=3870784, y=2872668: closest beacon is at x=2928827, y=2611422
    """.trimIndent()
        .split("\n")
        .map { row ->
            val pattern =
                Pattern.compile("Sensor at x=(-?\\d*), y=(-?\\d*): closest beacon is at x=(-?\\d*), y=(-?\\d*)")
            val matcher = pattern.matcher(row)
            matcher.matches()
            val result = matcher.toMatchResult()
            Sensor(result.group(1).toInt(), result.group(2).toInt(), result.group(3).toInt(), result.group(4).toInt())
        }

    private data class Sensor(val sensorX: Int, val sensorY: Int, val beaconX: Int, val beaconY: Int)

}