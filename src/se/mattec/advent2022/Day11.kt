package se.mattec.advent2022

fun main(args: Array<String>) {
    println(Day11.problem1())
    println(Day11.problem2())
}

object Day11 {

    fun problem1(): String {
        val monkeys = data.map { it.copy(items = it.items.toMutableList()) }

        (0 until 20).map { round ->
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    val newWorryLevel = performOperation(monkey.operationRow, item) / 3L
                    val testResult = newWorryLevel % monkey.testDivisibleBy == 0L
                    if (testResult) {
                        monkeys[monkey.testTrueToMonkey].items += newWorryLevel
                    } else {
                        monkeys[monkey.testFalseToMonkey].items += newWorryLevel
                    }
                    monkey.inspectionCounter++
                }
            }
        }

        return monkeys.sortedByDescending { it.inspectionCounter }
            .let { it[0].inspectionCounter * it[1].inspectionCounter }.toString()
    }

    private fun performOperation(operation: List<String>, worryLevel: Long): Long {
        val left = when (operation[0]) {
            "old" -> worryLevel
            else -> operation[0].toLong()
        }
        val right = when (operation[2]) {
            "old" -> worryLevel
            else -> operation[2].toLong()
        }
        return when (operation[1]) {
            "+" -> left + right
            "*" -> left * right
            else -> throw IllegalStateException()
        }.also { if (it < worryLevel) throw IllegalStateException("Overflow") }
    }

    fun problem2(): String {
        val monkeys = data.map { it.copy(items = it.items.toMutableList()) }

        val commonDenominator = monkeys.fold(1) { acc, m -> acc * m.testDivisibleBy }

        (0 until 10000).map { round ->
            monkeys.forEachIndexed { index, monkey ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    val newWorryLevel = performOperation(monkey.operationRow, item) % commonDenominator
                    val testResult = newWorryLevel % monkey.testDivisibleBy == 0L
                    if (testResult) {
                        monkeys[monkey.testTrueToMonkey].items += newWorryLevel
                    } else {
                        monkeys[monkey.testFalseToMonkey].items += newWorryLevel
                    }
                    monkey.inspectionCounter++
                }
            }
        }

        return monkeys.sortedByDescending { it.inspectionCounter }
            .let { it[0].inspectionCounter * it[1].inspectionCounter }.toString()
    }

    private val data = """
Monkey 0:
  Starting items: 83, 88, 96, 79, 86, 88, 70
  Operation: new = old * 5
  Test: divisible by 11
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 59, 63, 98, 85, 68, 72
  Operation: new = old * 11
  Test: divisible by 5
    If true: throw to monkey 4
    If false: throw to monkey 0

Monkey 2:
  Starting items: 90, 79, 97, 52, 90, 94, 71, 70
  Operation: new = old + 2
  Test: divisible by 19
    If true: throw to monkey 5
    If false: throw to monkey 6

Monkey 3:
  Starting items: 97, 55, 62
  Operation: new = old + 5
  Test: divisible by 13
    If true: throw to monkey 2
    If false: throw to monkey 6

Monkey 4:
  Starting items: 74, 54, 94, 76
  Operation: new = old * old
  Test: divisible by 7
    If true: throw to monkey 0
    If false: throw to monkey 3

Monkey 5:
  Starting items: 58
  Operation: new = old + 4
  Test: divisible by 17
    If true: throw to monkey 7
    If false: throw to monkey 1

Monkey 6:
  Starting items: 66, 63
  Operation: new = old + 6
  Test: divisible by 2
    If true: throw to monkey 7
    If false: throw to monkey 5

Monkey 7:
  Starting items: 56, 56, 90, 96, 68
  Operation: new = old + 7
  Test: divisible by 3
    If true: throw to monkey 4
    If false: throw to monkey 1
    """.trimIndent()
        .split("\n\n")
        .mapIndexed { index, it ->
            val rows = it.split("\n")
            val startingItems = rows[1].split(": ")[1].split(", ").map { it.toLong() }.toMutableList()
            val operationRow = rows[2].split("= ")[1].split(" ")
            val testDivisibleBy = rows[3].split("by ")[1].toInt()
            val testTrueToMonkey = rows[4].split("monkey ")[1].toInt()
            val testFalseToMonkey = rows[5].split("monkey ")[1].toInt()
            Monkey(index, startingItems, operationRow, testDivisibleBy, testTrueToMonkey, testFalseToMonkey)
        }

    private data class Monkey(
        val index: Int,
        val items: MutableList<Long>,
        val operationRow: List<String>,
        val testDivisibleBy: Int,
        val testTrueToMonkey: Int,
        val testFalseToMonkey: Int,
        var inspectionCounter: Long = 0L
    )

}