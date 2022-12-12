package se.mattec.advent2022

fun main(args: Array<String>) {
    println(Day12.problem1())
    println(Day12.problem2())
}

object Day12 {

    fun problem1(): String {
        val startX = data.find { it.contains('S') }!!.indexOf('S')
        val startY = data.indexOfFirst { it.contains('S') }

        val endX = data.find { it.contains('E') }!!.indexOf('E')
        val endY = data.indexOfFirst { it.contains('E') }

        val data = data.map {
            it.map {
                when (it) {
                    'S' -> 'a'
                    'E' -> 'z'
                    else -> it
                }
            }
        }

        val maxX = data[0].size
        val maxY = data.size
        val pathLengths = Array(maxY) { Array(maxX) { Int.MAX_VALUE } }
        val visited = mutableSetOf<Pair<Int, Int>>()
        val unvisited = mutableSetOf<Pair<Int, Int>>()

        pathLengths[startY][startX] = 0
        unvisited += startX to startY

        while (unvisited.isNotEmpty()) {
            val point = unvisited.minBy { pathLengths[it.second][it.first] }
            val x = point.first
            val y = point.second

            val currentLength = pathLengths[y][x]

            fun tryVisit(x: Int, y: Int, pathLength: Int) {
                if (visited.contains(x to y)) {
                    return // Never consider visited nodes again
                }

                val newPathLength = pathLength + 1
                if (newPathLength < pathLengths[y][x]) {
                    pathLengths[y][x] = newPathLength
                }
                unvisited += x to y
            }

            fun safeCoordinates(x: Int, y: Int): Pair<Int, Int>? {
                return if (x in 0 until maxX && y in 0 until maxY && data[y][x] - data[point.second][point.first] <= 1) {
                    x to y
                } else {
                    null
                }
            }

            safeCoordinates(x, y + 1)?.let { tryVisit(it.first, it.second, currentLength) }
            safeCoordinates(x, y - 1)?.let { tryVisit(it.first, it.second, currentLength) }
            safeCoordinates(x + 1, y)?.let { tryVisit(it.first, it.second, currentLength) }
            safeCoordinates(x - 1, y)?.let { tryVisit(it.first, it.second, currentLength) }

            visited += x to y
            unvisited.remove(x to y)
        }

        return pathLengths[endY][endX].toString()
    }

    fun problem2(): String {
        val endX = data.find { it.contains('E') }!!.indexOf('E')
        val endY = data.indexOfFirst { it.contains('E') }

        var shortestPath = Int.MAX_VALUE

        val data = data.map {
            it.map {
                when (it) {
                    'S' -> 'a'
                    'E' -> 'z'
                    else -> it
                }
            }
        }

        data.forEachIndexed { startY, chars ->
            chars.forEachIndexed { startX, c ->
                if (c != 'a') return@forEachIndexed

                val maxX = data[0].size
                val maxY = data.size
                val pathLengths = Array(maxY) { Array(maxX) { Int.MAX_VALUE } }
                val visited = mutableSetOf<Pair<Int, Int>>()
                val unvisited = mutableSetOf<Pair<Int, Int>>()

                pathLengths[startY][startX] = 0
                unvisited += startX to startY

                while (unvisited.isNotEmpty()) {
                    val point = unvisited.minBy { pathLengths[it.second][it.first] }
                    val x = point.first
                    val y = point.second

                    val currentLength = pathLengths[y][x]

                    fun tryVisit(x: Int, y: Int, pathLength: Int) {
                        if (visited.contains(x to y)) {
                            return // Never consider visited nodes again
                        }

                        val newPathLength = pathLength + 1
                        if (newPathLength < pathLengths[y][x]) {
                            pathLengths[y][x] = newPathLength
                        }
                        unvisited += x to y
                    }

                    fun safeCoordinates(x: Int, y: Int): Pair<Int, Int>? {
                        return if (x in 0 until maxX && y in 0 until maxY && data[y][x] - data[point.second][point.first] <= 1) {
                            x to y
                        } else {
                            null
                        }
                    }

                    safeCoordinates(x, y + 1)?.let { tryVisit(it.first, it.second, currentLength) }
                    safeCoordinates(x, y - 1)?.let { tryVisit(it.first, it.second, currentLength) }
                    safeCoordinates(x + 1, y)?.let { tryVisit(it.first, it.second, currentLength) }
                    safeCoordinates(x - 1, y)?.let { tryVisit(it.first, it.second, currentLength) }

                    visited += x to y
                    unvisited.remove(x to y)
                }

                if (pathLengths[endY][endX] < shortestPath) {
                    shortestPath = pathLengths[endY][endX]
                }
            }
        }

        return shortestPath.toString()
    }

    private val data = """
abcccccccccccccccccccccccccccccccaaaaaaaaaaaaaaaaccaaaaaaaaccccccccccccccccccccccccccccccccccccaaaaaa
abcccccccccccccccccccccccccccccccaaaaaaaaaaaaaaaaaccaaaaaaccccccccccccccccccccccccccccccccccccccaaaaa
abcccccccccccccccccccccccccccccccccaaaaaaaacccaaaaccaaaaaaccccccccccccccccccccaaaccccccccccccccccaaaa
abcccccccccccccccccccccccccccccccccccaaaaaaaccaaccccaaaaaaccccccccccccccccccccaaaccccccccccccccccaaaa
abcccccccccccccccccccccccccccccaaacccaaaaaaaacccccccaaccaaccccccccccccccccccccaaaccccccccccccccccaaac
abcccccccccccccccccccccccccccccaaaaaaaaacaaaacccccccccccccccaccaaccccccccccccciiaaccaaaccccccccccaacc
abccccccccaaccccccccccccccccccaaaaaaaaaaccaaacccccccccccccccaaaaaccccccccacaiiiiijjaaaacccccccccccccc
abacccaaccaacccccccccccccccccaaaaaaaaaaccccacccccaaaaccccccccaaaaacccccccaaiiiiijjjjaaaccccccaacccccc
abacccaaaaaacccccccccccccccccaaaaaaaaccccccccccccaaaacccccccaaaaaacccccccaiiiioojjjjjacccaaaaaacccccc
abcccccaaaaaaacccccccccccccccccaaaaaaccccaaccccccaaaacccccccaaaaccccccccciiinnoooojjjjcccaaaaaaaccccc
abccccccaaaaaccccccccccccccccccaaaaaacccaaaaccccccaaacccccccccaaaccccccchiinnnooooojjjjcccaaaaaaacccc
abcccccaaaaacccccccccccccccccccaacccccccaaaaccccccccccccccccccccccccccchhiinnnuuoooojjjjkcaaaaaaacccc
abccccaaacaaccccccccccccccccccccccccccccaaaaccccccccccccccccccaaacccchhhhhnnntuuuoooojjkkkkaaaacccccc
abccccccccaacccccccccccccccccccccccccccccccccccccccccccccccccccaacchhhhhhnnnnttuuuuoookkkkkkkaacccccc
abcccccccccccccccccccaacaaccccccccccccccccccccccccccccccccccaacaahhhhhhnnnnntttxuuuoopppppkkkkacccccc
abcccccccccccccccccccaaaaacccccccccaccccccccccccccccccccccccaaaaahhhhmnnnnntttxxxuuupppppppkkkccccccc
abccccccccccccccccccccaaaaacccccaaaacccccccccccccccccccccccccaaaghhhmmmmttttttxxxxuuuuuupppkkkccccccc
abcccccccccccccccccccaaaaaaaccccaaaaaaccccccccccccccccccccccccaagggmmmmtttttttxxxxuuuuuuvppkkkccccccc
abcccccccccccccccccccaaaaaaaaaaacaaaaacccccccccccccccccccccccaaagggmmmttttxxxxxxxyyyyyvvvppkkkccccccc
abccccccccccccccccccccaaaaaaaaaaaaaaaccccccccccccccccccccaacaaaagggmmmtttxxxxxxxyyyyyyvvppplllccccccc
SbcccccccccccccccccccaaaaaaaaaacaccaaccccccccccccccccccccaaaaaccgggmmmsssxxxxEzzzyyyyvvvpplllcccccccc
abcccccccccccccccccccccaaaaaaccccccccccccccaacaaccccccccaaaaaccccgggmmmsssxxxxyyyyyvvvvqqplllcccccccc
abccccccccccccccccccccccaaaaaacccccccccccccaaaacccccccccaaaaaacccgggmmmmsssswwyyyyyvvvqqqlllccccccccc
abcccccccccccccccccccccaaaaaaaccccccccccccaaaaacccccccccccaaaaccccgggmmmmsswwyyyyyyyvvqqllllccccccccc
abcccccccccccccccccccccaaaccaaacccccccccccaaaaaaccccccccccaccccccccgggooosswwwywwyyyvvqqlllcccccccccc
abccccccccccccccccccccccacccccccccccccccccacaaaacccccccccccccccccccfffooosswwwwwwwwvvvqqqllcccccccccc
abccccccccccccccccccccccccccccccccccccccccccaacccccccccccccccccccccfffooosswwwwwrwwvvvqqqllcccccccccc
abccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccffooossswwwrrrwvvvqqqmmcccccccccc
abccccaaacccccccccccccccccccccccccccccccccccccccccccccccccccccccccccffooosssrrrrrrrrqqqqmmmcccccccccc
abccccaaacaacccccaaccccaaaacccccccccccccccccccccccccccccccccccccccccffooossrrrrrnrrrqqqqmmmcccaaacccc
abcccccaaaaaccaaaaacccaaaaacccccccccccccccccccccccccccccccccccccccccfffoooorrnnnnnnmqqmmmmmcccaaacccc
abccaaaaaaaacccaaaaaccaaaaaaccccccccccccccccccccccccccccccccccccccccfffooonnnnnnnnnmmmmmmmcccaaaccccc
abcccaaaaacccccaaaaaccaaaaaaccccccaacccccccccccccccccccccccccccccccccfffoonnnnneddnmmmmmmccccaaaccccc
abccccaaaaacccaaaaacccaaaaaacccccaaaaaaccccccccccccccccccccaaccccccccffeeeeeeeeeddddddddccccaaaaccccc
abccccaacaaacccccaacccccaacccccccaaaaaaaccccccccccccccccaaaaaccccccccceeeeeeeeeedddddddddccaccaaccccc
abccccaacccccccccccccccccccccccccaaaaaaaccaaaccccccccccccaaaaaccccccccceeeeeeeeaaaaddddddcccccccccccc
abcccccccccccaaccccccccccccccccccccccaaaaaaaaacccccccccccaaaaacccccccccccccaaaacaaaacccccccccccccccaa
abccccccccaacaaacccccccccccccccccccccaaaaaaaacccccccccccaaaaaccccccccccccccaaaccaaaaccccccccccccccaaa
abccccccccaaaaacccccccccccccccccccccacaaaaaaccccccccccccccaaacccccccccccccccaccccaaacccccccccccacaaaa
abcccccccccaaaaaaccccccccccccccccaaaaaaaaaaacccccccccccccccccccccccccccccccccccccccacccccccccccaaaaaa
abcccccccaaaaaaaaccccccccccccccccaaaaaaaaaaaaacccccccccccccccccccccccccccccccccccccccccccccccccaaaaaa
    """.trimIndent()
        .split("\n")
        .map { it.toList() }

}