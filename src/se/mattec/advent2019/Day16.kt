package se.mattec.advent2019

import java.lang.Math.abs

fun main() {
    val problem1Output = Day16.problem1()
    println(problem1Output)
    println(Day16.problem2(problem1Output.toInt()))
}

object Day16 {

    fun problem1(): String {
        var output = data

        for (round in 0 until 100) {
            output = calculate(output)
        }

        return output.take(8).joinToString("")
    }

    fun problem2(problem1Output: Int): String {
        var output : List<Int> = ArrayList<Int>(data.size * 10000)
        repeat(10000) { (output as MutableList).addAll(data)}

        for (round in 0 until 100) {
            output = calculate(output)
        }



        return output.take(8).joinToString("")
    }

    private fun calculate(input: List<Int>): List<Int> {
        val output = ArrayList<Int>(input.size)

        input.forEachIndexed { index, _ ->
            val pattern = pattern(index, input.size)

            var temp = 0
            input.forEachIndexed { innerIndex, element ->
                temp += (pattern[innerIndex] * element)
            }

            output += abs(temp % 10)
        }

        return output
    }

    private fun pattern(position: Int, length: Int): List<Int> {
        val basePattern = mutableListOf<Int>()
        repeat(position + 1) {
            basePattern += 0
        }
        repeat(position + 1) {
            basePattern += 1
        }
        repeat(position + 1) {
            basePattern += 0
        }
        repeat(position + 1) {
            basePattern += -1
        }
        val output = ArrayList<Int>(length)
        while (output.size < (length + 1)) {
            output += basePattern
        }
        return output.subList(1, output.size)
    }

    private val data = """
59755896917240436883590128801944128314960209697748772345812613779993681653921392130717892227131006192013685880745266526841332344702777305618883690373009336723473576156891364433286347884341961199051928996407043083548530093856815242033836083385939123450194798886212218010265373470007419214532232070451413688761272161702869979111131739824016812416524959294631126604590525290614379571194343492489744116326306020911208862544356883420805148475867290136336455908593094711599372850605375386612760951870928631855149794159903638892258493374678363533942710253713596745816693277358122032544598918296670821584532099850685820371134731741105889842092969953797293495"""
            .trimIndent()
            .split("")
            .toMutableList()
            .apply {
                removeAt(lastIndex)
                removeAt(0)
            }
            .map { it.toInt() }

}