package se.mattec.advent2021

import kotlin.math.max

fun main(args: Array<String>) {
    println(Day17.problem1())
    println(Day17.problem2())
}

object Day17 {

    fun problem1(): String {
        val withinTargetArea = mutableListOf<State>()

        for (velocityX in 1..100) {
            for (velocityY in 1..100) {
                var state = State(0, 0, velocityX, velocityY)
                while (state.x <= data.maxX && state.y >= data.minY) {
                    state = state.next()
                    if (state.isWithinTargetArea()) {
                        withinTargetArea += state
                    }
                }
            }
        }

        return withinTargetArea.maxBy { it.maxY }!!.maxY.toString()
    }

    fun problem2(): String {
        val withinTargetArea = mutableListOf<State>()

        for (velocityX in -100..1000) {
            for (velocityY in -100..1000) {
                var state = State(0, 0, velocityX, velocityY)
                while (state.x <= data.maxX && state.y >= data.minY) {
                    state = state.next()
                    if (state.isWithinTargetArea()) {
                        withinTargetArea += state
                        break
                    }
                }
            }
        }

        return withinTargetArea.size.toString()
    }

    private data class State(val x: Int, val y: Int, val velocityX: Int, val velocityY: Int, val maxY: Int = y) {

        fun next(): State {
            return State(
                    x = x + velocityX,
                    y = y + velocityY,
                    velocityX = when {
                        velocityX > 0 -> velocityX - 1
                        velocityX < 0 -> velocityX + 1
                        velocityX == 0 -> 0
                        else -> error("Can't be anything else.")
                    },
                    velocityY = velocityY - 1,
                    maxY = max(maxY, y + velocityY)
            )
        }

        fun isWithinTargetArea(): Boolean {
            return x in data.minX..data.maxX && y in data.minY..data.maxY
        }
    }

    private val data = """
target area: x=137..171, y=-98..-73
    """.trimIndent()
            .replace("target area: ", "")
            .split(", ")
            .let {
                val xs = it[0].replace("x=", "").split("..").map { it.toInt() }
                val ys = it[1].replace("y=", "").split("..").map { it.toInt() }
                TargetArea(xs[0], xs[1], ys[0], ys[1])
            }

    data class TargetArea(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int)

}