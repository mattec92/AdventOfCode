package se.mattec.advent2018

import java.util.regex.Pattern

fun main(args: Array<String>) {
    println(Day7.problem1())
    println(Day7.problem2())
}

object Day7 {

    fun problem1(): String {
        var order = ""

        val requirements = data.toMutableList()

        //Get all available instructions
        val allInstructions = (requirements.map { it.first } + requirements.map { it.second }).distinct()

        val availableInstructions = allInstructions.toMutableList()

        //As long as we don't have all instructions in the result list, go on
        while (order.length != allInstructions.size) {
            val instructionsForMove = mutableListOf<String>()

            //Find any instruction that don't have any requirement
            for (instruction in availableInstructions) {
                if (!requirements.any { it.second == instruction }) {
                    instructionsForMove += instruction
                }
            }

            //Sort alphabetically
            instructionsForMove.sortBy { it }

            order += instructionsForMove[0]
            availableInstructions.remove(instructionsForMove[0])

            //Remove all requirements that are already fulfilled by executing current instruction
            requirements.removeAll { it.first == instructionsForMove[0] }
        }

        return order
    }

    private const val NUM_WORKERS = 5
    private const val DEBUG = false

    fun problem2(): Int {
        var workers = Array(NUM_WORKERS) { 0 }
        val tasks = Array(NUM_WORKERS) { "" }

        var totalTime = 0

        val requirements = data.toMutableList()

        //Get all available instructions
        val allInstructions = (requirements.map { it.first } + requirements.map { it.second }).distinct()

        val availableInstructions = allInstructions.toMutableList()

        //As long as we don't have all instructions in the result list, go on
        while (availableInstructions.size != 0) {
            //Decrement time left until workers are done
            workers = workers.map { Math.max(0, it - 1) }.toTypedArray()

            //Work remove finished work from available work
            workers.forEachIndexed { index, i ->
                if (i == 0) {
                    //Remove all requirements that are already fulfilled by executing current instruction
                    requirements.removeAll { it.first == tasks[index] }

                    availableInstructions.remove(tasks[index])

                    tasks[index] = ""
                }
            }

            val instructionsForMove = mutableListOf<String>()

            //Find any instruction that don't have any requirement
            for (instruction in availableInstructions) {
                if (!requirements.any { it.second == instruction }) {
                    instructionsForMove += instruction
                }
            }

            //Sort alphabetically
            instructionsForMove.sortBy { it }

            //Remove instructions that are ongoing
            instructionsForMove.removeIf { tasks.contains(it) }

            //Assign work to workers
            while (!instructionsForMove.isEmpty()) {
                val worker = workers.indexOf(0)
                if (worker != -1) {
                    val instruction = instructionsForMove[0]
                    workers[worker] = 60 + instruction[0].toInt() - 64
                    tasks[worker] = instruction

                    instructionsForMove.remove(instruction)
                } else {
                    break
                }
            }

            totalTime++

            if (DEBUG) {
                println(workers.mapIndexed { index, i -> "${tasks[index]}: $i" })
            }
        }

        return totalTime - 1 //We count the last cycle where work is already finished
    }

    private val pattern = Pattern.compile("Step (\\w).* step (\\w).*")

    private fun toPair(input: String): Pair<String, String> {
        val matcher = pattern.matcher(input)
        matcher.matches()
        return matcher.group(1) to matcher.group(2)
    }

    private val data = """
Step J must be finished before step E can begin.
Step X must be finished before step G can begin.
Step D must be finished before step A can begin.
Step K must be finished before step M can begin.
Step P must be finished before step Z can begin.
Step F must be finished before step O can begin.
Step B must be finished before step I can begin.
Step U must be finished before step W can begin.
Step A must be finished before step R can begin.
Step E must be finished before step R can begin.
Step H must be finished before step C can begin.
Step O must be finished before step S can begin.
Step Q must be finished before step Y can begin.
Step V must be finished before step W can begin.
Step T must be finished before step N can begin.
Step S must be finished before step I can begin.
Step Y must be finished before step W can begin.
Step Z must be finished before step C can begin.
Step M must be finished before step L can begin.
Step L must be finished before step W can begin.
Step N must be finished before step I can begin.
Step I must be finished before step G can begin.
Step C must be finished before step G can begin.
Step G must be finished before step R can begin.
Step R must be finished before step W can begin.
Step Z must be finished before step R can begin.
Step Z must be finished before step N can begin.
Step G must be finished before step W can begin.
Step L must be finished before step G can begin.
Step Y must be finished before step R can begin.
Step P must be finished before step I can begin.
Step C must be finished before step W can begin.
Step T must be finished before step G can begin.
Step T must be finished before step R can begin.
Step V must be finished before step Z can begin.
Step L must be finished before step C can begin.
Step K must be finished before step I can begin.
Step J must be finished before step I can begin.
Step Q must be finished before step C can begin.
Step F must be finished before step A can begin.
Step H must be finished before step Y can begin.
Step M must be finished before step N can begin.
Step P must be finished before step H can begin.
Step M must be finished before step C can begin.
Step V must be finished before step Y can begin.
Step O must be finished before step V can begin.
Step O must be finished before step Q can begin.
Step A must be finished before step G can begin.
Step T must be finished before step Z can begin.
Step K must be finished before step R can begin.
Step H must be finished before step O can begin.
Step O must be finished before step Y can begin.
Step O must be finished before step C can begin.
Step K must be finished before step P can begin.
Step P must be finished before step F can begin.
Step E must be finished before step M can begin.
Step M must be finished before step I can begin.
Step T must be finished before step W can begin.
Step P must be finished before step L can begin.
Step A must be finished before step O can begin.
Step X must be finished before step V can begin.
Step S must be finished before step G can begin.
Step A must be finished before step Y can begin.
Step J must be finished before step R can begin.
Step K must be finished before step F can begin.
Step J must be finished before step A can begin.
Step P must be finished before step C can begin.
Step E must be finished before step N can begin.
Step F must be finished before step Y can begin.
Step J must be finished before step D can begin.
Step H must be finished before step Z can begin.
Step U must be finished before step H can begin.
Step J must be finished before step T can begin.
Step V must be finished before step G can begin.
Step Z must be finished before step I can begin.
Step H must be finished before step W can begin.
Step B must be finished before step R can begin.
Step F must be finished before step B can begin.
Step X must be finished before step C can begin.
Step L must be finished before step R can begin.
Step F must be finished before step U can begin.
Step D must be finished before step N can begin.
Step P must be finished before step O can begin.
Step B must be finished before step O can begin.
Step F must be finished before step C can begin.
Step H must be finished before step L can begin.
Step O must be finished before step N can begin.
Step J must be finished before step Y can begin.
Step H must be finished before step N can begin.
Step O must be finished before step L can begin.
Step I must be finished before step W can begin.
Step J must be finished before step H can begin.
Step D must be finished before step Z can begin.
Step F must be finished before step W can begin.
Step X must be finished before step W can begin.
Step Y must be finished before step M can begin.
Step T must be finished before step M can begin.
Step U must be finished before step G can begin.
Step L must be finished before step I can begin.
Step N must be finished before step W can begin.
Step E must be finished before step C can begin.
    """.trimIndent()
            .split("\n")
            .map { toPair(it) }

}