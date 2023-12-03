package se.mattec.advent2021

import sumByLong

fun main() {
    println(Day14.problem1())
    println(Day14.problem2())
}

object Day14 {

    fun problem1(): String {
        var chain = chain

        for (step in 0 until 10) {
            var i = 0
            while (i != chain.length - 1) {
                val pair = chain.substring(i, i + 2)
                if (formulas[pair] != null) {
                    chain = chain.substring(0, i + 1) + formulas[pair] + chain.substring(i + 1)
                    i++
                }
                i++
            }
        }

        val charactersToCount = chain.groupBy { it }.mapValues { it.value.size }
        val minCharacterCount = charactersToCount.minBy { it.value }!!.value
        val maxCharacterCount = charactersToCount.maxBy { it.value }!!.value

        return (maxCharacterCount - minCharacterCount).toString()
    }

    fun problem2(): String {
        val formulas = formulas.mapKeys { it.key[0] to it.key[1] }

        var pairs = mutableMapOf<Pair<Char, Char>, Long>()

        chain.forEachIndexed { index, _ ->
            if (index != chain.length - 1) {
                val pair = chain[index] to chain[index + 1]
                pairs[pair] = pairs[pair]?.plus(1) ?: 1
            }
        }

        for (step in 0 until 40) {
            val newPairs = mutableMapOf<Pair<Char, Char>, Long>()
            pairs.forEach { (pair, count) ->
                val between = formulas[pair]?.first()
                if (between != null) {
                    val newPair1 = pair.first to between
                    newPairs[newPair1] = newPairs[newPair1]?.plus(count) ?: count
                    val newPair2 = between to pair.second
                    newPairs[newPair2] = newPairs[newPair2]?.plus(count) ?: count
                }
            }
            pairs = newPairs
        }

        val charactersToCount = pairs.flatMap { listOf(it.key.first to it.value, it.key.second to it.value) }
                .groupBy { it.first }
                .map { it.key to it.value.sumByLong { it.second } }
                .toMap()
        val minCharacterCount = charactersToCount.minBy { it.value }!!.value
        val maxCharacterCount = charactersToCount.maxBy { it.value }!!.value

        return ((maxCharacterCount - minCharacterCount) / 2).toString()
    }

    private val data = """
NNSOFOCNHBVVNOBSBHCB

HN -> S
FK -> N
CH -> P
VP -> P
VV -> C
PB -> H
CP -> F
KO -> P
KN -> V
NO -> K
NF -> N
CO -> P
HO -> H
VH -> V
OV -> C
VS -> F
PK -> H
OS -> S
BF -> S
SN -> P
NK -> N
SV -> O
KB -> O
ON -> O
FN -> H
FO -> N
KV -> S
CS -> C
VO -> O
SP -> O
VK -> H
KP -> S
SK -> N
NC -> B
PN -> N
HV -> O
HS -> C
CN -> N
OO -> V
FF -> B
VC -> V
HK -> K
CC -> H
BO -> H
SC -> O
HH -> C
BV -> P
OB -> O
FC -> H
PO -> C
FV -> C
BK -> F
HB -> B
NH -> P
KF -> N
BP -> H
KK -> O
OH -> K
CB -> H
CK -> C
OK -> H
NN -> F
VF -> N
SO -> K
OP -> F
NP -> B
FS -> S
SH -> O
FP -> O
SF -> V
HF -> N
KC -> K
SB -> V
FH -> N
SS -> C
BB -> C
NV -> K
OC -> S
CV -> N
HC -> P
BC -> N
OF -> K
BH -> N
NS -> K
BN -> F
PC -> C
CF -> N
HP -> F
BS -> O
PF -> S
PV -> B
KH -> K
VN -> V
NB -> N
PH -> V
KS -> B
PP -> V
PS -> C
VB -> N
FB -> N
    """.trimIndent()

    private val chain = data.split("\n\n")[0]

    private val formulas = data.split("\n\n")[1]
            .split("\n")
            .map { it.split(" -> ") }
            .map { it[0] to it[1] }
            .toMap()

}