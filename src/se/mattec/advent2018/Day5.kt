package se.mattec.advent2018

fun main(args: Array<String>) {
    measured { println(Day5.problem1(Day5::reduce)) }
    measured { println(Day5.problem1(Day5::reduce2)) }
    measured { println(Day5.problem1(Day5::splitNReduce)) }
    measured { println(Day5.problem1(Day5::splitNReduce2)) }
    measured { println(Day5.problem2(Day5::reduce)) }
    measured { println(Day5.problem2(Day5::reduce2)) }
    measured { println(Day5.problem2(Day5::splitNReduce)) }
    measured { println(Day5.problem2(Day5::splitNReduce2)) }
}

fun measured(block: () -> Unit) {
    val before = System.currentTimeMillis()
    block()
    println("Time: ${System.currentTimeMillis() - before}")
}

object Day5 {

    fun problem1(reduceFunc: (String) -> String): Int {
        return reduceFunc(data).length
    }

    fun problem2(reduceFunc: (String) -> String): Int {
        val chars = "abcdefghijklmnopqrstuvwxyz"

        val reduced = reduceFunc(data)

        return chars
                .map { reduced.replace(it.toString(), "").replace(it.toUpperCase().toString(), "") }
                .map { reduceFunc(it).length }
                .min()!!
    }

    //Most efficient
    fun reduce2(input: String): String {
        val chars = "abcdefghijklmnopqrstuvwxyz"
        val opposites = mutableMapOf<Char, Char>().apply {
            for (char in chars) {
                put(char, char.toUpperCase())
                put(char.toUpperCase(), char)
            }
        }

        var prev = ""
        var reduced = input
        while (reduced != prev) {
            prev = reduced
            for (i in 0..reduced.length) {
                try {
                    val char = reduced[i]
                    val next = reduced[i + 1]
                    if (next == opposites[char]) {
                        reduced = reduced.substring(0, i) + reduced.substring(i + 2, reduced.length)
                        break
                    }
                } catch (e: IndexOutOfBoundsException) {
                    //Ignore for last item
                }
            }
        }

        return reduced
    }

    fun reduce(input: String): String {
        val chars = "abcdefghijklmnopqrstuvwxyz"

        val regex = mutableListOf<Regex>()
        chars.forEach {
            val unit = "$it${it.toUpperCase()}"
            val oppositeUnit = unit.reversed()
            regex += Regex(unit)
            regex += Regex(oppositeUnit)
        }

        var prev = ""
        var reduced = input
        while (reduced != prev) {
            prev = reduced
            regex.forEach {
                reduced = reduced.replaceFirst(it, "")
            }
        }

        return reduced
    }

    fun splitNReduce(input: String): String {
        val parts = input.chunked(100)
                .map { reduce(it) }
                .fold("") { acc, part -> acc + part }
        return reduce(parts)
    }

    fun splitNReduce2(input: String): String {
        val parts = input.chunked(100)
                .map { reduce2(it) }
                .fold("") { acc, part -> acc + part }
        return reduce(parts)
    }

    private const val data = Day5Data.data

}