package se.mattec.advent2018

import java.util.regex.Pattern

fun main() {
    println(Day10.problem1And2())
}

object Day10 {

    fun problem1And2(): String {
        var smallestX = Int.MAX_VALUE
        var smallestY = Int.MAX_VALUE
        var smallestIndex = 0

        for (i in 0..Int.MAX_VALUE) {
            val coordinates = data.map { it.x + it.velX * i to it.y + it.velY * i }
            val maxX = coordinates.map { it.first }.max()!!
            val maxY = coordinates.map { it.second }.max()!!
            val minX = coordinates.map { it.first }.min()!!
            val minY = coordinates.map { it.second }.min()!!

            if (maxX - minX < smallestX) {
                smallestX = maxX - minX
                smallestIndex = i
            } else {
                break
            }
            if (maxY - minY < smallestY) {
                smallestY = maxY - minY
                smallestIndex = i
            } else {
                break
            }
        }

        val coordinates = data.map { it.x + it.velX * smallestIndex to it.y + it.velY * smallestIndex }
        val maxX = coordinates.map { it.first }.max()!!
        val maxY = coordinates.map { it.second }.max()!!
        val minX = coordinates.map { it.first }.min()!!
        val minY = coordinates.map { it.second }.min()!!

        val grid = Array(maxY - minY + 1) { IntArray(maxX - minX + 1) }
        for (coordinate in coordinates) {
            grid[coordinate.second - minY][coordinate.first - minX] = 1
        }

        for (line in grid) {
            println(line.toList().map { if (it > 0) "#" else " " })
        }

        return "It took $smallestIndex seconds."
    }

    private val pattern = Pattern.compile("position=<(.)(\\d*), (.)(\\d*)> velocity=<(.)(\\d*), (.)(\\d*)>")

    private fun toEntry(input: String): Entry {
        val matcher = pattern.matcher(input)
        matcher.matches()
        val x = matcher.group(2).toInt().let { if (matcher.group(1) == "-") it.unaryMinus() else it }
        val y = matcher.group(4).toInt().let { if (matcher.group(3) == "-") it.unaryMinus() else it }
        val velX = matcher.group(6).toInt().let { if (matcher.group(5) == "-") it.unaryMinus() else it }
        val velY = matcher.group(8).toInt().let { if (matcher.group(7) == "-") it.unaryMinus() else it }
        return Entry(x, y, velX, velY)
    }

    private data class Entry(val x: Int, val y: Int, val velX: Int, val velY: Int)

    private val data = """
position=<-20515,  20790> velocity=< 2, -2>
position=< 10476,  31130> velocity=<-1, -3>
position=< 51833,  41457> velocity=<-5, -4>
position=< 51808, -30871> velocity=<-5,  3>
position=< 20845,  41465> velocity=<-2, -4>
position=<-30848, -10200> velocity=< 3,  1>
position=< 31159, -20533> velocity=<-3,  2>
position=< 10497,  41464> velocity=<-1, -4>
position=<-51511, -10200> velocity=< 5,  1>
position=<-41170, -20542> velocity=< 4,  2>
position=< 10463, -41204> velocity=<-1,  4>
position=<-30867, -10203> velocity=< 3,  1>
position=<-41205,  41459> velocity=< 4, -4>
position=<-20534,  41459> velocity=< 2, -4>
position=< 20825,  31125> velocity=<-2, -3>
position=<-51527,  10466> velocity=< 5, -1>
position=< 51840, -51534> velocity=<-5,  5>
position=< 41459,  51798> velocity=<-4, -5>
position=<-41157, -51532> velocity=< 4,  5>
position=< 51789, -20535> velocity=<-5,  2>
position=< 41451,  10466> velocity=<-4, -1>
position=< 20785, -51538> velocity=<-2,  5>
position=< 20798, -10206> velocity=<-2,  1>
position=< 51816, -51538> velocity=<-5,  5>
position=<-10161,  20792> velocity=< 1, -2>
position=< 10457,  20794> velocity=<-1, -2>
position=<-30848, -30870> velocity=< 3,  3>
position=< 20825,  20791> velocity=<-2, -2>
position=< 51789, -10202> velocity=<-5,  1>
position=< 31126, -51540> velocity=<-3,  5>
position=<-51522, -51540> velocity=< 5,  5>
position=<-51506, -51534> velocity=< 5,  5>
position=< 41507,  31127> velocity=<-4, -3>
position=< 31174, -51536> velocity=<-3,  5>
position=< 51824,  10459> velocity=<-5, -1>
position=< 10457,  41460> velocity=<-1, -4>
position=< 41471, -41205> velocity=<-4,  4>
position=<-30880, -20533> velocity=< 3,  2>
position=< 41499,  41460> velocity=<-4, -4>
position=< 41464,  20795> velocity=<-4, -2>
position=<-30848,  41460> velocity=< 3, -4>
position=< 41459, -51532> velocity=<-4,  5>
position=<-20542,  41459> velocity=< 2, -4>
position=<-51498,  20794> velocity=< 5, -2>
position=< 31163, -10201> velocity=<-3,  1>
position=<-41203,  31127> velocity=< 4, -3>
position=< 10469, -20542> velocity=<-1,  2>
position=< 20821, -41199> velocity=<-2,  4>
position=< 41511, -30866> velocity=<-4,  3>
position=< 51785, -41203> velocity=<-5,  4>
position=< 10476, -30871> velocity=<-1,  3>
position=< 31175, -51532> velocity=<-3,  5>
position=<-10166, -20536> velocity=< 1,  2>
position=< 41485,  20799> velocity=<-4, -2>
position=< 10465,  51797> velocity=<-1, -5>
position=<-51541, -51539> velocity=< 5,  5>
position=< 31126,  31127> velocity=<-3, -3>
position=< 20801,  41464> velocity=<-2, -4>
position=<-30838,  10466> velocity=< 3, -1>
position=< 20835, -20537> velocity=<-2,  2>
position=<-30872, -51533> velocity=< 3,  5>
position=< 10484, -20541> velocity=<-1,  2>
position=< 31135, -51541> velocity=<-3,  5>
position=<-51546,  41465> velocity=< 5, -4>
position=<-51536, -41204> velocity=< 5,  4>
position=< 41511,  41465> velocity=<-4, -4>
position=<-30819, -51532> velocity=< 3,  5>
position=< 10476, -20541> velocity=<-1,  2>
position=<-20542, -51536> velocity=< 2,  5>
position=<-20495, -10208> velocity=< 2,  1>
position=< 10510,  51798> velocity=<-1, -5>
position=<-41194, -41204> velocity=< 4,  4>
position=<-20542, -51534> velocity=< 2,  5>
position=< 10508, -30871> velocity=<-1,  3>
position=<-20527, -20533> velocity=< 2,  2>
position=<-51544, -30875> velocity=< 5,  3>
position=< 31171, -41201> velocity=<-3,  4>
position=<-51522,  31125> velocity=< 5, -3>
position=< 20818,  51798> velocity=<-2, -5>
position=<-41189,  51795> velocity=< 4, -5>
position=< 20817,  31125> velocity=<-2, -3>
position=<-30827, -51535> velocity=< 3,  5>
position=< 10489,  41465> velocity=<-1, -4>
position=<-30867, -41203> velocity=< 3,  4>
position=<-41189, -20542> velocity=< 4,  2>
position=<-10209,  20799> velocity=< 1, -2>
position=< 41472,  20790> velocity=<-4, -2>
position=< 10470,  51794> velocity=<-1, -5>
position=< 31119, -51540> velocity=<-3,  5>
position=<-30832,  51791> velocity=< 3, -5>
position=< 51811,  41465> velocity=<-5, -4>
position=<-20523,  51792> velocity=< 2, -5>
position=< 10500,  41459> velocity=<-1, -4>
position=< 10476, -30866> velocity=<-1,  3>
position=<-20507, -51538> velocity=< 2,  5>
position=< 20790,  51791> velocity=<-2, -5>
position=< 41491, -20538> velocity=<-4,  2>
position=<-51496,  20795> velocity=< 5, -2>
position=<-41160,  10461> velocity=< 4, -1>
position=< 20806, -10209> velocity=<-2,  1>
position=< 31137, -30875> velocity=<-3,  3>
position=<-51546, -20540> velocity=< 5,  2>
position=< 10472, -30872> velocity=<-1,  3>
position=<-20522,  20799> velocity=< 2, -2>
position=< 51788,  41461> velocity=<-5, -4>
position=< 31126, -20542> velocity=<-3,  2>
position=<-30856, -10200> velocity=< 3,  1>
position=< 10510, -30866> velocity=<-1,  3>
position=<-51541,  51795> velocity=< 5, -5>
position=<-30853, -10200> velocity=< 3,  1>
position=<-10201, -30867> velocity=< 1,  3>
position=< 10508, -10200> velocity=<-1,  1>
position=<-10156, -20533> velocity=< 1,  2>
position=< 31143,  31132> velocity=<-3, -3>
position=<-41173, -51536> velocity=< 4,  5>
position=<-51490, -10206> velocity=< 5,  1>
position=<-41157,  10457> velocity=< 4, -1>
position=< 10484, -30868> velocity=<-1,  3>
position=< 41451, -20537> velocity=<-4,  2>
position=< 20838,  20792> velocity=<-2, -2>
position=< 10492, -51537> velocity=<-1,  5>
position=< 31121, -30875> velocity=<-3,  3>
position=<-30824, -30872> velocity=< 3,  3>
position=< 51837, -30868> velocity=<-5,  3>
position=<-41165,  20799> velocity=< 4, -2>
position=< 41491,  20791> velocity=<-4, -2>
position=< 51792,  51793> velocity=<-5, -5>
position=<-20531, -30875> velocity=< 2,  3>
position=<-41162,  10462> velocity=< 4, -1>
position=<-10201,  10461> velocity=< 1, -1>
position=< 10452,  20797> velocity=<-1, -2>
position=<-20539,  31129> velocity=< 2, -3>
position=<-20494, -30870> velocity=< 2,  3>
position=<-20522,  51798> velocity=< 2, -5>
position=<-10165,  10458> velocity=< 1, -1>
position=< 31160, -10209> velocity=<-3,  1>
position=< 10493, -20542> velocity=<-1,  2>
position=<-51525,  10459> velocity=< 5, -1>
position=<-20494, -41202> velocity=< 2,  4>
position=< 10508, -10207> velocity=<-1,  1>
position=< 31142,  20791> velocity=<-3, -2>
position=< 31118, -30867> velocity=<-3,  3>
position=< 31118, -51534> velocity=<-3,  5>
position=<-51490, -51540> velocity=< 5,  5>
position=< 10476,  51795> velocity=<-1, -5>
position=<-20543,  41457> velocity=< 2, -4>
position=<-30848,  41459> velocity=< 3, -4>
position=<-51490, -30874> velocity=< 5,  3>
position=<-51490,  31131> velocity=< 5, -3>
position=< 41483, -10200> velocity=<-4,  1>
position=< 20841,  20797> velocity=<-2, -2>
position=<-10214,  10463> velocity=< 1, -1>
position=<-20502, -10208> velocity=< 2,  1>
position=< 10495,  41465> velocity=<-1, -4>
position=<-30835, -20541> velocity=< 3,  2>
position=<-30872,  31131> velocity=< 3, -3>
position=<-51490, -30872> velocity=< 5,  3>
position=<-41196, -51532> velocity=< 4,  5>
position=< 20785,  10459> velocity=<-2, -1>
position=< 31162, -51541> velocity=<-3,  5>
position=<-41181, -10204> velocity=< 4,  1>
position=< 31119,  10458> velocity=<-3, -1>
position=< 51828,  51789> velocity=<-5, -5>
position=< 41502,  20790> velocity=<-4, -2>
position=<-51504,  31123> velocity=< 5, -3>
position=<-20542,  31126> velocity=< 2, -3>
position=< 31174,  41462> velocity=<-3, -4>
position=< 20836, -51541> velocity=<-2,  5>
position=< 51789,  20793> velocity=<-5, -2>
position=<-10198, -41208> velocity=< 1,  4>
position=<-41196,  41462> velocity=< 4, -4>
position=< 31121,  31123> velocity=<-3, -3>
position=< 41469, -10200> velocity=<-4,  1>
position=< 41459,  31125> velocity=<-4, -3>
position=< 10508,  41461> velocity=<-1, -4>
position=<-51522,  51798> velocity=< 5, -5>
position=<-51506,  51794> velocity=< 5, -5>
position=< 41504,  10461> velocity=<-4, -1>
position=<-51541,  20798> velocity=< 5, -2>
position=<-41180,  10466> velocity=< 4, -1>
position=< 20788,  31128> velocity=<-2, -3>
position=< 41499,  51796> velocity=<-4, -5>
position=< 31179, -20533> velocity=<-3,  2>
position=< 51813,  20799> velocity=<-5, -2>
position=<-41210,  10457> velocity=< 4, -1>
position=< 41456,  20798> velocity=<-4, -2>
position=< 31120,  10462> velocity=<-3, -1>
position=<-10196,  51798> velocity=< 1, -5>
position=< 51784, -51534> velocity=<-5,  5>
position=< 10476,  51794> velocity=<-1, -5>
position=< 20793,  31126> velocity=<-2, -3>
position=< 10505, -51535> velocity=<-1,  5>
position=< 20817, -30875> velocity=<-2,  3>
position=< 41491,  51797> velocity=<-4, -5>
position=<-10174, -41201> velocity=< 1,  4>
position=<-41157,  51790> velocity=< 4, -5>
position=< 41491,  51793> velocity=<-4, -5>
position=<-10209,  10459> velocity=< 1, -1>
position=<-30880, -51535> velocity=< 3,  5>
position=<-10162,  10462> velocity=< 1, -1>
position=<-10210, -10204> velocity=< 1,  1>
position=< 31166,  10462> velocity=<-3, -1>
position=< 51801,  41462> velocity=<-5, -4>
position=<-30856,  51797> velocity=< 3, -5>
position=< 10473,  20799> velocity=<-1, -2>
position=< 10471, -20542> velocity=<-1,  2>
position=<-51542,  51790> velocity=< 5, -5>
position=<-30872,  41461> velocity=< 3, -4>
position=< 31137,  31127> velocity=<-3, -3>
position=< 41483, -10200> velocity=<-4,  1>
position=<-41197, -41199> velocity=< 4,  4>
position=< 51793,  20794> velocity=<-5, -2>
position=< 31131,  41465> velocity=<-3, -4>
position=<-20515,  20798> velocity=< 2, -2>
position=<-30860, -41208> velocity=< 3,  4>
position=<-20543, -30874> velocity=< 2,  3>
position=<-41181, -41201> velocity=< 4,  4>
position=<-51497, -30870> velocity=< 5,  3>
position=< 51828,  41465> velocity=<-5, -4>
position=< 31119,  10462> velocity=<-3, -1>
position=<-51506,  51795> velocity=< 5, -5>
position=< 41475,  51792> velocity=<-4, -5>
position=<-10166, -10204> velocity=< 1,  1>
position=< 51843,  10466> velocity=<-5, -1>
position=<-41181, -10204> velocity=< 4,  1>
position=<-51514,  41460> velocity=< 5, -4>
position=<-41165, -10201> velocity=< 4,  1>
position=< 20828,  20790> velocity=<-2, -2>
position=<-10181, -41199> velocity=< 1,  4>
position=<-41205, -41201> velocity=< 4,  4>
position=<-30840, -41202> velocity=< 3,  4>
position=<-41205,  10463> velocity=< 4, -1>
position=< 51816,  41462> velocity=<-5, -4>
position=< 20793, -10209> velocity=<-2,  1>
position=<-10201,  10458> velocity=< 1, -1>
position=<-30821,  31132> velocity=< 3, -3>
position=< 41492, -20542> velocity=<-4,  2>
position=< 10453,  51794> velocity=<-1, -5>
position=<-51536,  10461> velocity=< 5, -1>
position=<-30880, -20537> velocity=< 3,  2>
position=< 41459,  20794> velocity=<-4, -2>
position=< 41502,  51789> velocity=<-4, -5>
position=< 20841, -51532> velocity=<-2,  5>
position=<-20515,  51792> velocity=< 2, -5>
position=< 10470,  41456> velocity=<-1, -4>
position=<-20507,  10465> velocity=< 2, -1>
position=< 51816,  10459> velocity=<-5, -1>
position=<-41185, -51532> velocity=< 4,  5>
position=<-51541,  41462> velocity=< 5, -4>
position=< 31137, -30871> velocity=<-3,  3>
position=<-30819, -41199> velocity=< 3,  4>
position=<-30877,  41461> velocity=< 3, -4>
position=<-30840,  41458> velocity=< 3, -4>
position=<-10170,  31132> velocity=< 1, -3>
position=<-30848,  51789> velocity=< 3, -5>
position=<-20534,  10457> velocity=< 2, -1>
position=< 31134, -20535> velocity=<-3,  2>
position=<-20507,  20796> velocity=< 2, -2>
position=<-30848, -41201> velocity=< 3,  4>
position=< 31126, -20540> velocity=<-3,  2>
position=< 31168, -20537> velocity=<-3,  2>
position=< 20789, -10204> velocity=<-2,  1>
position=< 20793,  20790> velocity=<-2, -2>
position=< 51792, -51538> velocity=<-5,  5>
position=<-41208, -30870> velocity=< 4,  3>
position=< 20788, -10204> velocity=<-2,  1>
position=<-41173,  51792> velocity=< 4, -5>
position=<-51514, -20534> velocity=< 5,  2>
position=<-20499, -30871> velocity=< 2,  3>
position=< 31139, -20542> velocity=<-3,  2>
position=< 10464,  41460> velocity=<-1, -4>
position=< 10511, -51532> velocity=<-1,  5>
position=< 10497, -30867> velocity=<-1,  3>
position=< 20833,  10459> velocity=<-2, -1>
position=< 31131, -30875> velocity=<-3,  3>
position=<-20534,  10465> velocity=< 2, -1>
position=<-51546, -10205> velocity=< 5,  1>
position=<-10161, -41200> velocity=< 1,  4>
position=<-30864,  41464> velocity=< 3, -4>
position=<-41165,  51797> velocity=< 4, -5>
position=<-30840,  41463> velocity=< 3, -4>
position=<-30827, -30868> velocity=< 3,  3>
position=< 20841, -20536> velocity=<-2,  2>
position=< 10479,  41465> velocity=<-1, -4>
position=<-41169,  41456> velocity=< 4, -4>
position=< 20826,  51789> velocity=<-2, -5>
position=<-30867,  51789> velocity=< 3, -5>
position=< 31167, -20541> velocity=<-3,  2>
position=< 10473, -30866> velocity=<-1,  3>
position=< 10505,  51792> velocity=<-1, -5>
position=<-51525,  31124> velocity=< 5, -3>
position=<-10182, -10207> velocity=< 1,  1>
position=<-41189,  20793> velocity=< 4, -2>
position=<-10198,  10465> velocity=< 1, -1>
position=<-20528, -51541> velocity=< 2,  5>
position=<-30860, -41208> velocity=< 3,  4>
position=< 51840,  41462> velocity=<-5, -4>
position=<-10158, -41200> velocity=< 1,  4>
position=<-30867,  20797> velocity=< 3, -2>
position=<-10162,  10462> velocity=< 1, -1>
position=<-30827, -10205> velocity=< 3,  1>
position=<-30844,  20799> velocity=< 3, -2>
position=<-51527,  51798> velocity=< 5, -5>
position=< 41501, -41208> velocity=<-4,  4>
position=< 10505, -10200> velocity=<-1,  1>
position=< 10452, -20536> velocity=<-1,  2>
position=< 31146, -51532> velocity=<-3,  5>
position=<-30867, -41206> velocity=< 3,  4>
position=< 51824, -41207> velocity=<-5,  4>
position=< 10460, -51536> velocity=<-1,  5>
position=< 31170, -51536> velocity=<-3,  5>
position=<-20521, -41199> velocity=< 2,  4>
position=< 10457, -10204> velocity=<-1,  1>
position=<-41165, -10203> velocity=< 4,  1>
    """.trimIndent()
            .split("\n")
            .map { toEntry(it) }

}