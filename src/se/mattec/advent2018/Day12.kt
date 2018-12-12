package se.mattec.advent2018

import java.util.regex.Pattern

fun main(args: Array<String>) {
    println(Day12.problem1())
    println(Day12.problem2())
}

object Day12 {

    fun problem1(): Int {
        val state = state(initialState, 20, false)
        return sum(state)
    }

    private fun state(initialState: String, nrOfGenerations: Int, print: Boolean): String {
        var state = initialState

        if (print) {
            println(state)
        }

        for (generation in 0 until nrOfGenerations) {
            val matchesPerRule = mutableMapOf<Pair<String, Char>, List<Int>>()
            for (rule in rules) {
                val matches = mutableListOf<Int>()
                val matcher = Pattern.compile(rule.first).matcher(state)
                while (matcher.find(matches.lastOrNull()?.let { it + 1 } ?: 0)) {
                    matches += matcher.start()
                }
                matchesPerRule[rule] = matches
            }

            val stateArray = state.toCharArray()
            for (ruleMatch in matchesPerRule) {
                val rule = ruleMatch.key
                val matches = ruleMatch.value
                for (match in matches) {
                    stateArray[match + 2] = rule.second
                }
            }
            state = String(stateArray)

            if (print) {
                println(state)
            }
        }

        return state
    }

    private fun sum(input: String): Int {
        return input
                .mapIndexed { index, c -> if (c == '#') index - 5 else 0 } //5 is the number of leading empty pots added
                .sum()
    }

    //After ~200 generations the pattern will start repeating
    //causing sum to increase by a set amount per generation
    fun problem2(): Long {
        val state = state(initialState, 200, false)
        val state2 = state(initialState, 201, false)
        val sum = sum(state)
        val sum2 = sum(state2)
        val delta = sum2 - sum
        return sum + (50000000000 - 200) * delta
    }

    //Description is quite unclear but there are an infinite amount of empty pot in all directions
    //I added enough empty pots in each direction so adding more will not affect the sum in early generationa
    private val initialState = ".".repeat(5) + "..#..###...#####.#.#...####.#..####..###.##.#.#.##.#....#....#.####...#....###.###..##.#....#######" + ".".repeat(1000)
    private val rules = """
..### => .
.##.# => #
#..#. => .
#.#.# => #
###.. => #
.#..# => .
##..# => #
.###. => #
..#.. => .
..... => .
##### => .
.#... => #
...#. => #
#...# => #
####. => .
.#### => .
##.## => #
...## => .
..##. => .
#.##. => .
#.... => .
.#.#. => .
..#.# => #
#.#.. => #
##... => #
##.#. => .
#..## => .
.##.. => .
#.### => .
....# => .
.#.## => #
    """.trimIndent()
            .split("\n")
            .map {
                val split = it.split(" => ")
                split[0].replace(".", "\\.") to split[1].toCharArray()[0]
            }

}