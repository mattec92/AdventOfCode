package se.mattec.advent2018

fun main(args: Array<String>) {
    println(Day8.problem1())
    println(Day8.problem2())
}

object Day8 {

    fun problem1(): Int {
        val entry = getEntry(data, 0)
        return sumMetadata(entry)
    }

    fun problem2(): Int {
        val entry = getEntry(data, 0)
        return value(entry)
    }

    private fun value(entry: Entry): Int {
        return if (entry.numChildren == 0) {
            entry.metadata.sum()
        } else {
            entry.metadata.map {
                val index = it - 1
                if (entry.children.size > index && index >= 0) {
                    value(entry.children[index])
                } else {
                    0
                }
            }.sum()
        }
    }

    private fun sumMetadata(entry: Entry): Int {
        return entry.metadata.sum() + entry.children.sumBy { sumMetadata(it) }
    }

    private fun getEntry(input: List<Int>, offset: Int): Entry {
        val numChildren = input[offset]
        val numMetaData = input[offset + 1]
        val children = mutableListOf<Entry>()
        val metadata = mutableListOf<Int>()

        var childOffset = offset + 2
        for (i in 0 until numChildren) {
            val child = getEntry(input, childOffset)
            children += child
            childOffset += child.lengthConsumed
        }

        for (i in 0 until numMetaData) {
            metadata += input[childOffset + i]
        }

        return Entry(numChildren, numMetaData, children, metadata, 2 + children.sumBy { it.lengthConsumed } + numMetaData)
    }

    private data class Entry(val numChildren: Int, val numMetadata: Int, val children: List<Entry>, val metadata: List<Int>, val lengthConsumed: Int)

    private val data = Day8Data.data
            .split(" ")
            .map { it.toInt() }

}