package se.mattec.advent2021

fun main(args: Array<String>) {
    println(Day20.problem1())
    println(Day20.problem2())
}

object Day20 {

    fun problem1(): String {
        return enhance(2).toString()
    }

    fun problem2(): String {
        return enhance(50).toString()
    }

    private fun enhance(steps: Int): Int {
        var input = image
        var edge = 0

        for (step in 0 until steps) {
            input = enlargeCanvas(input, edge)
            input = enhanceImage(input, edge)
            edge = input[0][0]
        }

        return input.flatten().sum()
    }

    private fun emptyCanvas(image: Array<Array<Int>>, enlargeBy: Int = 0, edge: Int): Array<Array<Int>> {
        return Array(image.size + enlargeBy) { Array(image[0].size + enlargeBy) { edge } }
    }

    private fun enlargeCanvas(image: Array<Array<Int>>, edge: Int): Array<Array<Int>> {
        val output = emptyCanvas(image, enlargeBy = 2, edge = edge)

        image.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, lit ->
                output[rowIndex + 1][colIndex + 1] = lit
            }
        }

        return output
    }

    private fun enhanceImage(image: Array<Array<Int>>, edge: Int): Array<Array<Int>> {
        val output = emptyCanvas(image, edge = edge)

        image.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, _ ->
                output[rowIndex][colIndex] = enhancePixel(image, rowIndex, colIndex, edge)
            }
        }

        return output
    }

    private fun enhancePixel(image: Array<Array<Int>>, row: Int, col: Int, edge: Int): Int {
        val keyIndex = listOf(
                image.getOrNull(row - 1)?.getOrNull(col - 1) ?: edge,
                image.getOrNull(row - 1)?.getOrNull(col) ?: edge,
                image.getOrNull(row - 1)?.getOrNull(col + 1) ?: edge,
                image.getOrNull(row)?.getOrNull(col - 1) ?: edge,
                image.getOrNull(row)?.getOrNull(col) ?: edge,
                image.getOrNull(row)?.getOrNull(col + 1) ?: edge,
                image.getOrNull(row + 1)?.getOrNull(col - 1) ?: edge,
                image.getOrNull(row + 1)?.getOrNull(col) ?: edge,
                image.getOrNull(row + 1)?.getOrNull(col + 1) ?: edge
        ).joinToString("").toInt(2)

        return enhancementKey[keyIndex]
    }

    private fun print(image: Array<Array<Int>>) {
        image.forEach { row ->
            row.forEach { lit ->
                print(if (lit == 1) '#' else '.')
            }
            println()
        }
        println()
    }

    private val data = """
####....##..####..##.#.#.##..##...##.####....####.##..#.######.#####..##.#..#..#..###.#..###........#...########...#.#........#...####...#...##...####..###..#..#..##..#####...##.######...###.##.##..#...##....#.#...#.######.##.##.#..#.#..#..####.#####....#.##.####..#.##..#...##.#.#####.#..##......###.#..###.##.#.#..##...###..#..#..#.###...##.#....#.....##...##..#.##.#.#.....###..#.#...####..#..##.####...###...##...###..#....####..####..#.###.###.##.#.#.#.###.##.#.#.#..#.#...##...#.#.##..#..###.##..##.###.##.

#.#....#...#..#.....###.....#...##.#####.#.#.##.#.###.###.##.##.........########......##...#....##..
..#.#..#...#.###...#..#..#.....###.#.#.##.#.##.#..###.###.#..#.##.#.##.#.#.#...####.#.......#..##...
##..##...#.#########.###..#####..###.###.####....###.#.#.###.#.#..#..#.#...####..###.#.##.#####.##..
.#.##....##..#.###..#....#.#..##..####.#..#.#####.#.#...#.....#.##.#.#.#...#..#......#.#.#.##..#.#.#
##..#.##.#.#.#.##....####.#.#.....#.....#....#....#...###..###..######.#...#.#.....#.#.##...##..##..
###..#......##.....#..##.#.#...#...#.##..##..###.####...###.#...##.#.#.#.##.#....###...######...#.##
...##.#..#.#.#.####.####..#.#.#.##.#...#.....#.#..#.#####..###.#...#..#...####...#....##..##..#....#
.#.#.....##...#.##..#.##..##.#######.##..#...#..#.######.#.##..#..#......###.#..##.#.#...###.##..#..
.##.#######.##..#...#..###.###...##.##..#.#...#..#.#..###.##.#..#.##.#.#.##.####..#..###.###.##.#.#.
#..###...#.####.#...##.###...##..#.######..#.#........##.#...#..#.##.#....#####...#..#.#.##.#....###
#..####.#.#.##..#.#.##...#...###....#...#.....###..##...##.#.##.#....##..##.##.#####.#.#.##.###..##.
####.####..#.#..###.#..#####......#...####.##.#..##.##..#...#..#.#####...#...#...###.##.##........##
#.....###..#.###.#...##.#####...###...###.#.##.###.###.##.####..#..#.....#...##.#.....#.##..#..#....
.##..#.######..#.#.#.....#.#..###.#.####.##.#...##..#..#.#.##..#.....#.####.#.#.#...##.##..#......##
#.##...###.##.##.#...####.#..#...#.#..#.###.#.#.##.##..###.##.##.#..##..#.#.#.#..#.###.#...###..#.##
###...#.#.##.#.#.#.##.#.###.##..#.#.##..#.##..####.##..#...##.#.###....###..###..###.#..#.##..#.#.##
.......###...#####.#####.#.####..#....##.##.###.#.#.#.#.#...#.#.###.####.#..###.####.#.#..#..#######
#.#........###..###..#...####..##.#..####....#.#...##.#....#..###..##......#..###....###.#.###...#.#
...#.#....#...##..###.##.####.....####.........#..#..#.#.#.###.#..##.####........###.###.#..#.#...#.
##.######...###.....#.##..#..##..#.#.#..#...#.#...##.#.##...######.#.#...###..#.########.#.##.###...
##..#..#..#.##...#.##.........##..#####.#.##.#.####...#.#.#...#...##.####..#...######..#.....##..#..
...#..#.###..#.##.###...#....#..#...#.###..####.#..######.##..#....##...####...##..#.#.#.###.###....
.##.###..####.##....#....######...#..####.##...###.##.#.#.##..##.....####.#..#.###.##...#....####.##
####.######.#..#...###.#.#.#.....####....#.#..#....##...##.#######.#..#..#.######.##......#...##...#
.#####..#..##...###.###.#.#....###.###.##...#.#...#..#..#..#.........#.##..#.....#....####..####.###
#..##.#.###.#......##...#.###..##.##.#.#..#.#.##..###.#..........#..#...#.###.###..###.#.#..#..#....
......#.####..#.####..#....##..####.##..#....##.#.#........#.#####..###....###..###.#..#.#.#.#.##.##
#..#.....####.#.##.#..#..#...#.#..##..##.#.#.####..#...#..#.#.#.#....#.###.###..##.#.#...###.###.#..
.##..#..####.##.#...####...#.#.#.#.#.#..#.#...##..#....#.#..#..#.#.##..#.#..##..#.#.#...##..###.##..
..#.#..#.....##.###.##.#####.......###..##.##..#..###..##..#.....##.#.#.....##.#########.####.#.####
##....####..#....#.########...##.###....#......#..###..###.#.####.##.#######.#############...#.##.#.
##..##....#..#..#...#.#.#.##..##..#.###....##..#..##.#.#...#.....#....#..#....#.###.#...#...#.##..##
##.#..##..#..###.##.....#..#.##...##....#.#..##.#.....#.#...##......##########.....#....#.####......
.###.#....##..#..##..#####.###.#.##.#.####..###.#.###..#.##....####..#.#...#.#.#..#####...####.##..#
....#..####..#.......#.#.......##...##.#.###..##.........##..##...##.#..###..#.#....#.####.#...#.###
#.#.#.####.#.#.#.#....##.#...#..##.#.##..##.#.##..#.#..#.#.#..#####..###.#..#..#.#..##.#...#..#..#.#
.#..##....##.#...#.......###.#####.#........#.###.###.###..##.###...#.#....#...####.###.##..###.##..
.#.##.##.#.#..####.#..##.###..##..##.##.#.#####..#..#.#..#.##.#.####.#.##.#...##....#.....#.####.##.
##.##.#.#########.#....##.##.#####....#..#..###.#..##.#..##.#.#.#######.##.#......##.##..#.####...#.
#.....#.##...##...##.#.#...###.#####.#.##.####.##.#..#..##.#####.##..###.......#.#...##...#..###.#.#
.#.#####.##.##.#.#..##...#..#.#####...#.##.####.##.#.#.##.#.##.#..#....#.....###..##.##.##.#.#.#.###
##.##.#.#.#..#..#..##.#####.#...##.#...##..#.#..#..#.#...#.###..###.#...#..##...###.......#...#.#.#.
.#.....#..##......#.#..###.#..#.##..##.#.....##.##...#....#.##.#......#.#..#..###..#.#..####.##..###
...###.....#.##..####.##.#.#...#.......##..###.###..##.#.#....#...#.#..#.###.####..###...#....#...#.
..#.....#...###.##.##.#....#.##..#.#####..###.####.##..#.##.##.##..#.#.###.#.#.#...##....###.#.##...
.#########...#...##..###.#.###.#...#...#.###.###..###....###..#.##...##.#.#...#.#.####..#..#...#####
##..##..#..#....#....#####....###...#.##.###.#.#.#.....#..##..###.#.#..##.#.#..##....#.###.##..#..##
.....#.##...##......#..#.####....#.##.#####...#..#..#.###..####..#.####.##..#.####.#.#.....###..#..#
###....##.#..##....#####..#.#####.####....#....##.......#####.#...#.....#..#.###...##.#..#.###..#.##
#.##..#...##...##.#.###.###.#.#...#...#..#####.#..#.###.#.##.###.####...#.#.##..##.#.######.###..#..
.#.####...##.###.#.....#.##########.###.#####...###.#...###.#....#..##..##....##.##....#.##...#.##.#
###..#.#######.##.#####..######......#.#..#.#..#....######.##.###.###.#.##.#.##..#..##..###..##..###
....###.######.###.....##...#..#...##..###..########.#.##...####....##.###.#.##..##.#.....##.#.###..
#...#....#..##.#.#.#..#..#.#...#.#..#.###.######.##.####....#.##....##.###.##.##.......#.##..#.##...
###.#.#.#...#.#.#.#.#..#.####..##..##.##..##.#.##..##.###.####.#####..#.####...##.....#.#.##...#.##.
###.....#####.#...#.#....##...#..####...##.##.#..#.....#.##.#....######.##.##..#.#..#####.#...##...#
.#..##.###.###..#######.#.#.###...##.###.#.#.###.#.#####.##......#.##.....#..####.#.##.##.....#..###
##....##.#.##.#..#.##.###.##.####..#.##...#.#.##..##.#.#...##.#.#.##....#.#.#.....##....######.#.#.#
...#.##.#.#.#....#.###.#.###...##....#..#..##...#.##.#.....##.#.###...###...#.#...###.#.##.##..##.##
.#.#..#..#....##....#######..#...##.##.....#..###.##.#..#....#..##.##.#...#####.#....##....######.##
#.#.##..##..###.....#.##.##.###...###.##.#.#...#....####.......#....#..##.##...##....#...#.####..###
.#.##..#.....######.##..##.###....#####..#...#.####..##..#.#.###...####....#.##....###.##..######...
..###.######..###.....#...#.#..##....##..#...######.#.#.#...###.##....#.#..####.#######..##.....#..#
#...#.#####...#.###..#....#.#....#..#...##.#...#.#..#.##..##..#..##..#.#.###.#.#.##.#.##.......#####
###..#.##.#.#.#.....##.##.###..####....##..#..#..#.###..##.....#.....######.....#.###.#####.##.#####
.##..#.#.###.##.#.#.#..#......##...#..##.###..#.###.#.#.#...##.###...#.....#.##.#.#..##.##.#....##..
##.#..#.#####.##..#.#.##.###....###.......###......#..#...###.##.#.##########.##.##.##.###.#.##.....
.###.#..#####..##..#.####..#.#..######....####.###..#.##.####..#..#...##.#.#.####.####.#.##..####...
###.###.#.#.#..##..###..#....##.#..##.###.##.#.#......###....####.#......#.#.#...#.....##......####.
###.#.####.######..###..##.#.#....#####.#..#....#.##.##..#####.#......#...##....#..#.#.##..####.####
#..#...####..####.####.#..#.##..#.#.#.####..#.####...##....#..####.#.#........#.##......#.....##.#.#
..#..#.#####.#...#.###..##.#..#..#..#...#...#...###.#####.#.##..#.######...##.###....#######....#.##
.#.#........#.##.#.######.###.######..#..##.##.#...#.###.#..####.#.##.######.###..###..........#..##
..###.#.#..#.####...#.##...#....##############.#.#..####.###.#..##...#####.#.##.#..#####.#..#...###.
.#####.######.##...#.#.#..###.##..###.#.##.##....####..#.###...#.#.#...#..#..#.#..##......#..##.#...
.#.#..####.#.###.##..#.#.#.##..##.#..#.########..#..###...##....###.##.#...#.#.#.##.#..#.#..#..#..#.
.##...#.#.#####......##.####.####...#.#######..#..#...#..#####.#.........#####..#.###.#.#.##...##..#
#..#.#..#.#..##.#.##.###.#..##.#..#..####.###.##.########.#.##..#.....#..#....#.##..#..######..#####
.#.#...####..##..####....#.#.###.#.#...##.#.#..####.######..#.#.#.#...#..##...#....#####....####.##.
###...#.#..##..#.#..###.###...##.#..#.###..#.###..#.#...#.#..#.#..##...##..###.#.#....####..#....#.#
##....#.###.####..#.###.#..#######.###.....###...#.##....######.#.##..#####...###..#....##...#..###.
###....####......####.##.#..#...###.##..#...##..#.##..####..###.###..#..#..###..#.###...#.####..#..#
#...#...##..#...###...#..#####...#####.##...#..#.#####.....#####.......##..####....##.....##..#..##.
####..####.##..#.#..#####...#.#..##.#..##.###..#..###..#.....##.##..#..#.####.#..#..###.##..##..##..
####..#......##.#.#.#....#..###..####.##..##.###.##.....#......#.###...#...###.#..#..#.###.##.###...
..##.####..#..........###.#..##.####..#..##.##..##.####.#.##.####.....#.#.#..####.#####.##...#..####
..#....#..#.....#.##.#####.####...##.#.#.##..###..#.######.#.....##.######..#..#.##.##.#...#####.###
........#.#.....#.#.###...#######.#........#.#####.#.####...##.#.##..###...#......#.##.#.#.#..###.##
....#..##.#..#.###.....#...#..###.####.###.##..##...#.#..#....#.#.###.##.#..#...##.##..##..####..#.#
.#.#.##...#...#...#.#.#.####.####.#...#.#....#....##..#......#.#####.#.##.##.##.###...#.#####...#..#
###..##....#......########.##.##.##.##.#....####.###..#..##.##...#..##.#...##..##.####.####.##.#..##
#......###.###.##.....#.........#....###.#...##.###......##.#.....##..###.....##.##.###....##.#.#..#
.##.#.#.##.....#..#.#....#####.#.#...#.##..###.#####.###..###.##.....#.#...#..###..##..#...#.#..##..
########.#.#.##.#.##..#..#.#.#..#.###.#.##.######..#.####...##.##....###..#....#...#..#.#...##...#..
#....#.#####.#....######...##.#.###.#..####.##.###.###...####..#.#.##...#...##...##....####.#.......
..###..#..####.##.#.#####...###.#...###...#.#..##..##...##.##.#.##.#.#...##.....#..#.#.######.####.#
#..###.#..#.#..#.#.....##.#.#.#.#....#.#..#..#.###.#.##...#...##..###.#...#...#######..####..#.....#
#..####.#####....####.#.#.#.#.##.#.#...#####...##.###..###.#.#............###.####..##..##.###..###.
...#..#..#...#.#######.#.##..##..#..#.#..###.###..#.#####....#..#...####.#..##..###.##..######.##.##
##.#..#....#.#..###...#.#########.#####..#...#.####..#.#..#######.#....#.#.#.#..###.#.#..#...#.####.
    """.trimIndent()
            .split("\n\n")

    private val enhancementKey = data[0]
            .replace("\n", "")
            .map { if (it == '#') 1 else 0 }

    private val image = data[1].split("\n")
            .map { it.map { if (it == '#') 1 else 0 }.toTypedArray() }
            .toTypedArray()

}