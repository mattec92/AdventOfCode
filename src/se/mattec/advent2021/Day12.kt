package se.mattec.advent2021

fun main() {
    println(Day12.problem1())
    println(Day12.problem2())
}

object Day12 {

    fun problem1(): String {
        fun isAllowedSmallVisit(small: String, steps: List<String>): Boolean {
            return !steps.contains(small)
        }

        return findNumberOfPaths(::isAllowedSmallVisit).toString()
    }

    fun problem2(): String {
        fun isAllowedSmallVisit(small: String, steps: List<String>): Boolean {
            // Allowed if steps doesn't contain it, or there are no small caves with 2 visits
            return !steps.contains(small) || steps.filter { it.isSmall() }.groupBy { it }.none { it.value.size == 2 }
        }

        return findNumberOfPaths(::isAllowedSmallVisit).toString()
    }

    private fun findNumberOfPaths(isAllowedSmallVisit: (small: String, steps: List<String>) -> Boolean): Int {
        val roomsToNext = (data + data.map { listOf(it[1], it[0]) }) // Can go both directions
                .groupBy { it[0] }
                .mapValues { it.value.map { it[1] } }

        fun findPath(next: String): List<List<String>> {
            val paths = mutableListOf<List<String>>()

            fun step(next: String, previous: String, steps: MutableList<String>) {
                if (next == "end") {
                    steps += next
                    paths += steps
                } else if (next == "start") {
                    return // Not allowed to re-visit start
                } else if (next.isSmall() && !isAllowedSmallVisit(next, steps)) {
                    return // Invalid path
                } else if (next != previous) {
                    steps += next
                    roomsToNext[next]!!.map {
                        step(it, next, steps.toMutableList()) // Continue with new branches, copy list to not overwrite
                    }
                }
            }

            step(next, "start", mutableListOf("start"))

            return paths
        }

        val paths = roomsToNext["start"]!!.flatMap { findPath(it) }

        return paths.size
    }

    private fun String.isSmall(): Boolean = this != "start" && this.toLowerCase() == this

    private val data = """
HF-qu
end-CF
CF-ae
vi-HF
vt-HF
qu-CF
hu-vt
CF-pk
CF-vi
qu-ae
ae-hu
HF-start
vt-end
ae-HF
end-vi
vi-vt
hu-start
start-ae
CS-hu
CF-vt
    """.trimIndent()
            .split("\n")
            .map { it.split("-") }

}