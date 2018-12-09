package se.mattec.advent2018

import java.util.*

fun main(args: Array<String>) {
    println(Day9.problem1())
    println(Day9.problem2())
}

object Day9 {

    fun problem1() = marbles(lastMarble)

    fun problem2() = marbles(lastMarbleLarge)

    private fun marbles(lastMarble: Int): Long {
        val players = mutableMapOf<Int, Long>()
        val marbles = ArrayDeque<Int>()

        for (marble in 0..lastMarble) {
            when {
                marble < 2 -> {
                    marbles.addLast(marble)
                }
                marble % 23 == 0 -> {
                    marbles.moveBy(-7)
                    val player = marble % playerCount
                    players[player] = (players[player] ?: 0) + marble + marbles.pop()
                }
                else -> {
                    marbles.moveBy(2)
                    marbles.addLast(marble)
                }
            }
        }

        return players.maxBy { it.value }!!.value
    }

    //Makes sure currentMarble is always the last index
    private fun ArrayDeque<Int>.moveBy(by: Int) {
        if (by >= 0) {
            repeat(by) { addFirst(removeLast()) }
        } else {
            repeat(-by - 1) { addLast(removeFirst()) }
        }
    }

    private val playerCount = 468
    private val lastMarble = 71010
    private val lastMarbleLarge = 71010 * 100
    private val data = "468 players; last marble is worth 71010 points"

}