package se.mattec.advent2019

import java.lang.Math.abs
import kotlin.math.min

fun main() {
    println(Day12.problem1())
    println(Day12.problem2())
}

object Day12 {

    fun problem1(): Long {
        val moon1 = Moon(-1, 7, 3, 0, 0, 0)
        val moon2 = Moon(12, 2, -13, 0, 0, 0)
        val moon3 = Moon(14, 18, -8, 0, 0, 0)
        val moon4 = Moon(17, 4, -4, 0, 0, 0)

        val moons = listOf(moon1, moon2, moon3, moon4)

        for (step in 0 until 1000) {
            applyGravity(moon1, moon2)
            applyGravity(moon1, moon3)
            applyGravity(moon1, moon4)
            applyGravity(moon2, moon3)
            applyGravity(moon2, moon4)
            applyGravity(moon3, moon4)

            for (moon in moons) {
                moon.posX += moon.velX
                moon.posY += moon.velY
                moon.posZ += moon.velZ
            }
        }

        var energy = 0L
        moons.forEach {
            energy += ((abs(it.posX) + abs(it.posY) + abs(it.posZ)) * (abs(it.velX) + abs(it.velY) + abs(it.velZ)))
        }

        return energy
    }

    fun problem2(): Long {
        val moon1Start = Moon(-1, 7, 3, 0, 0, 0)
        val moon2Start = Moon(12, 2, -13, 0, 0, 0)
        val moon3Start = Moon(14, 18, -8, 0, 0, 0)
        val moon4Start = Moon(17, 4, -4, 0, 0, 0)

        val moon1 = Moon(-1, 7, 3, 0, 0, 0)
        val moon2 = Moon(12, 2, -13, 0, 0, 0)
        val moon3 = Moon(14, 18, -8, 0, 0, 0)
        val moon4 = Moon(17, 4, -4, 0, 0, 0)

        val moons = listOf(moon1, moon2, moon3, moon4)

        var xRepeating: Long? = null
        var yRepeating: Long? = null
        var zRepeating: Long? = null

        fun xRepeating(): Boolean {
            return moon1.posX == moon1Start.posX && moon2.posX == moon2Start.posX && moon3.posX == moon3Start.posX && moon4.posX == moon4Start.posX &&
                    moon1.velX == moon1Start.velX && moon2.velX == moon2Start.velX && moon3.velX == moon3Start.velX && moon4.velX == moon4Start.velX
        }

        fun yRepeating(): Boolean {
            return moon1.posY == moon1Start.posY && moon2.posY == moon2Start.posY && moon3.posY == moon3Start.posY && moon4.posY == moon4Start.posY &&
                    moon1.velY == moon1Start.velY && moon2.velY == moon2Start.velY && moon3.velY == moon3Start.velY && moon4.velY == moon4Start.velY
        }

        fun zRepeating(): Boolean {
            return moon1.posZ == moon1Start.posZ && moon2.posZ == moon2Start.posZ && moon3.posZ == moon3Start.posZ && moon4.posZ == moon4Start.posZ &&
                    moon1.velZ == moon1Start.velZ && moon2.velZ == moon2Start.velZ && moon3.velZ == moon3Start.velZ && moon4.velZ == moon4Start.velZ
        }

        var step = 0L
        while (xRepeating == null || yRepeating == null || zRepeating == null) {
            step++

            applyGravity(moon1, moon2)
            applyGravity(moon1, moon3)
            applyGravity(moon1, moon4)
            applyGravity(moon2, moon3)
            applyGravity(moon2, moon4)
            applyGravity(moon3, moon4)

            for (moon in moons) {
                moon.posX += moon.velX
                moon.posY += moon.velY
                moon.posZ += moon.velZ
            }

            if (xRepeating()) {
                xRepeating = step
            }

            if (yRepeating()) {
                yRepeating = step
            }

            if (zRepeating()) {
                zRepeating = step
            }
        }

        val minStep = min(xRepeating, min(yRepeating, zRepeating))

        var stepRepeating = minStep
        while (true) {
            stepRepeating += minStep
            if (stepRepeating % xRepeating == 0L && stepRepeating % yRepeating == 0L && stepRepeating % zRepeating == 0L) {
                break;
            }
        }

        return stepRepeating
    }

    private fun applyGravity(moonI: Moon, moonJ: Moon) {
        val gravityX = moonI.posX.compareTo(moonJ.posX)
        val gravityY = moonI.posY.compareTo(moonJ.posY)
        val gravityZ = moonI.posZ.compareTo(moonJ.posZ)
        moonI.velX += -gravityX
        moonJ.velX += gravityX
        moonI.velY += -gravityY
        moonJ.velY += gravityY
        moonI.velZ += -gravityZ
        moonJ.velZ += gravityZ
    }

    data class Moon(var posX: Long, var posY: Long, var posZ: Long, var velX: Long, var velY: Long, var velZ: Long)

}