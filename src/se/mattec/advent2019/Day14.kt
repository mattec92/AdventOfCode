package se.mattec.advent2019

import kotlin.math.ceil
import kotlin.math.roundToInt

fun main() {
    println(Day14.problem1(mutableListOf()))
    println(Day14.problem2())
}

object Day14 {

    fun problem1(leftoverIngredients: MutableList<Ingredient>): Int {
        var requiredIngredients = mutableListOf<Ingredient>()
        val leftoverIngredients = leftoverIngredients

        requiredIngredients.addAll(data.find { it.to.name == "FUEL" }!!.from)

        oreLoop@ while (requiredIngredients.any { it.name != "ORE" }) {
            var to = requiredIngredients[0]

            if (to.name == "ORE") {
                requiredIngredients.add(to)
                requiredIngredients.removeAt(0)
            } else {
                val leftoverIngredient = leftoverIngredients.find { it.name == to.name }
                if (leftoverIngredient != null) {
                    val newTo = to.copy(quantity = to.quantity - leftoverIngredient.quantity)
                    when {
                        newTo.quantity == 0 -> {
                            leftoverIngredients.remove(leftoverIngredient)
                            requiredIngredients.removeAt(0)
                            continue@oreLoop
                        }
                        newTo.quantity < 0 -> {
                            val newLeftoverIngredient = leftoverIngredient.copy(quantity = leftoverIngredient.quantity - to.quantity)
                            leftoverIngredients.remove(leftoverIngredient)
                            leftoverIngredients.add(newLeftoverIngredient)
                            requiredIngredients.removeAt(0)
                            continue@oreLoop
                        }
                        else -> {
                            to = newTo
                            leftoverIngredients.remove(leftoverIngredient)
                        }
                    }
                }

                val ingredients = recipe(to)
                requiredIngredients.addAll(ingredients.required)
                requiredIngredients.removeAt(0)
                if (ingredients.leftover.quantity > 0) {
                    leftoverIngredients.add(ingredients.leftover)
                }
            }

            requiredIngredients = requiredIngredients.groupBy { it.name }.map { Ingredient(it.value.sumBy { it.quantity }, it.key) }.toMutableList()
        }

        return requiredIngredients.sumBy { it.quantity }
    }

    private fun recipe(to: Ingredient): Ingredients {
        val recipe = data.find { it.to.name == to.name }!!
        val quantity = to.quantity / recipe.to.quantity.toFloat()
        val quantityCeil = ceil(quantity)
        val leftOverQuantity = quantityCeil - quantity
        val required = recipe.from.map { it.copy(quantity = (it.quantity * quantityCeil).roundToInt()) }
        val leftover = recipe.to.copy(quantity = (recipe.to.quantity * leftOverQuantity).roundToInt())
        return Ingredients(required, leftover)
    }

    fun problem2(): Long {
        var ore = 0L
        var fuel = 0L
        val leftovers = mutableListOf<Ingredient>()

        while (true) {
            val oreThisRound = problem1(leftovers)
            if ((ore + oreThisRound) > 1000000000000L) {
                break
            }
            ore += oreThisRound
            fuel += 1
        }

        return fuel
    }

    private val data = """
118 ORE => 7 GTPZ
6 RNQJN, 4 NQKVW => 4 DTQRC
2 GBXJL => 3 XHBR
4 BPZM => 9 LVDRH
131 ORE => 3 RHBL
2 LFZS => 2 FPRJW
6 GTPZ => 4 VTBTK
8 GPMP, 2 BPNFJ, 3 LFZS => 2 SFGCR
3 GPMP => 4 SPRCM
16 XCDZP, 1 NQKSL => 4 NQKVW
2 BXGD, 3 VJHSV, 1 MGNCW => 8 MGLH
1 XLNTJ => 1 KXBGP
9 PJQWR, 19 NQKVW, 10 GJHWN => 7 ZBGDF
3 VTBTK => 6 CJNQ
12 PJQWR => 1 JNHBR
16 BPZM => 9 MVCH
1 KWPXQ, 1 LVDRH => 6 LFZS
6 VTBTK => 6 XCDZP
1 PZFG, 2 LFZS, 2 CJNQ, 2 FPRJW, 17 MVCH, 7 MGNCW, 26 KXBGP => 6 TBTL
2 DTQRC, 7 NBNLC => 8 BPZM
102 ORE => 3 WNTQ
1 WNTQ => 9 NQKSL
5 XZMH, 1 LPLMR, 13 BXGD => 8 JPFL
1 NQKSL, 6 XCDZP, 2 FCDVQ => 9 GJHWN
6 XZMH => 4 GLDL
23 ZTWR, 4 BPZM => 2 MGNCW
11 GPMP, 19 ZBGDF => 2 XZMH
2 MGNCW, 4 XCDZP, 17 KQLT => 4 VJHSV
1 CJNQ => 7 QHPH
1 RHBL => 8 GBXJL
2 MVCH, 3 KDNT, 6 NBNLC, 26 QHPH, 2 KRKB, 1 MCPDH, 4 XZMH, 6 XHBR => 1 HZMWJ
9 XDLZ => 1 QSXKS
4 GLDL => 6 WJNP
5 MVCH => 3 MCPDH
14 TKGM => 5 LPLMR
1 WVQN => 2 PJQWR
4 KWPXQ => 6 FCDVQ
10 DTQRC, 27 TBTL, 9 HZMWJ, 41 XVGP, 2 TPZFL, 54 WNTQ, 85 RHBL, 5 WCZK, 2 QVSB, 28 SPRCM => 1 FUEL
15 RNQJN, 1 PJQWR, 2 NBNLC => 4 TKGM
126 ORE => 5 WVQN
10 NBNLC => 3 BWMD
2 SFGCR, 1 NQKSL, 1 KRKB => 1 WGQTF
2 MLWN => 5 ZTWR
12 DTQRC, 3 NQKVW, 9 NBNLC => 8 BPNFJ
10 SFGCR, 1 PZFG, 2 ZVFVH, 12 WJNP, 14 WGQTF, 1 JNHBR, 8 FPRJW => 3 QVSB
2 MCPDH => 8 XVGP
19 JPFL => 4 TPZFL
5 GBXJL => 6 MLWN
9 TKGM => 5 KDNT
1 NQKVW, 15 PJQWR => 9 XDLZ
2 QHPH, 2 JNHBR => 1 ZVFVH
189 ORE => 6 KWPXQ
5 KRKB, 3 MGLH => 6 WCZK
3 NBNLC, 8 BWMD => 7 KRKB
1 ZBGDF, 6 XDLZ => 4 GPMP
11 XDLZ, 1 QSXKS => 2 BXGD
2 KRKB, 1 GJHWN => 1 XLNTJ
3 ZTWR => 4 RNQJN
15 FCDVQ, 3 MLWN => 4 NBNLC
1 KDNT, 1 XZMH, 8 BXGD => 1 KQLT
2 WJNP => 3 PZFG
    """.trimIndent()
            .split("\n")
            .map {
                val fromAndTo = it.split(" => ")
                val to = fromAndTo[1].split(" ").let { Ingredient(it[0].toInt(), it[1]) }
                val from = fromAndTo[0].split(", ").map { it.split(" ").let { Ingredient(it[0].toInt(), it[1]) } }
                Recipe(from, to)
            }

    data class Recipe(val from: List<Ingredient>, val to: Ingredient)

    data class Ingredient(val quantity: Int, val name: String)

    data class Ingredients(val required: List<Ingredient>, val leftover: Ingredient)

}