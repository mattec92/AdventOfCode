package se.mattec.advent2018

fun main() {
    println(Day14.problem1())
    println(Day14.problem2())
}

object Day14 {

    fun problem1(): String {
        val startingFrom = 157901
        val recipes = mutableListOf(3, 7)

        var elfA = 0
        var elfB = 1

        for (i in 0 until startingFrom + 10) {
            val newRecipe = recipes[elfA] + recipes[elfB]
            recipes += newRecipe.toString().toCharArray().map { "$it".toInt() }
            elfA = (elfA + 1 + recipes[elfA]) % recipes.size
            elfB = (elfB + 1 + recipes[elfB]) % recipes.size
        }

        return recipes.subList(startingFrom, startingFrom + 10).joinToString("")
    }

    fun problem2(): Int {
        val toFind = "157901"
        val recipes = mutableListOf(3, 7)

        var elfA = 0
        var elfB = 1

        while (true) {
            val newRecipe = recipes[elfA] + recipes[elfB]
            recipes += newRecipe.toString().toCharArray().map { "$it".toInt() }
            elfA = (elfA + 1 + recipes[elfA]) % recipes.size
            elfB = (elfB + 1 + recipes[elfB]) % recipes.size

            if (recipes.size >= toFind.length + 2 && recipes.subList(recipes.size - toFind.length - 2, recipes.size).joinToString("").contains(toFind)) {
                return recipes.size - toFind.length - 2 + recipes.subList(recipes.size - toFind.length - 2, recipes.size).joinToString("").indexOf(toFind)
            }
        }
    }

}