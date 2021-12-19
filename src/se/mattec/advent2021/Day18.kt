package se.mattec.advent2021

import java.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

fun main(args: Array<String>) {
    println(Day18.problem1())
    println(Day18.problem2())
}

object Day18 {

    fun problem1(): String {
        var number = data.first().copy()
        data.drop(1).forEach {
            number = number.add(it.copy())
            number.reduce()
        }
        return number.magnitude().toString()
    }

    fun problem2(): String {
        var maxMagnitude = Int.MIN_VALUE
        for (n1 in data) {
            for (n2 in data) {
                if (n1 == n2) {
                    break
                }

                val added1 = n1.copy().add(n2.copy())
                added1.reduce()

                val added2 = n2.copy().add(n1.copy())
                added2.reduce()

                maxMagnitude = max(maxMagnitude, max(added1.magnitude(), added2.magnitude()))
            }
        }
        return maxMagnitude.toString()
    }

    interface SFNumber {

        var parent: Branch?

        fun magnitude(): Int

        fun copy(parent: Branch? = null): SFNumber?

        data class Branch(var left: SFNumber? = null, var right: SFNumber? = null) : SFNumber {

            override var parent: Branch? = null

            fun explode() {
                val left = left as Leaf
                val leftParentAndSetLeft = leftNumber()
                if (leftParentAndSetLeft != null) {
                    if (leftParentAndSetLeft.second) {
                        (leftParentAndSetLeft.first.left as Leaf).number = (leftParentAndSetLeft.first.left as Leaf).number + left.number
                    } else {
                        (leftParentAndSetLeft.first.right as Leaf).number = (leftParentAndSetLeft.first.right as Leaf).number + left.number
                    }
                }

                val right = right as Leaf
                val rightParentAndSetLeft = rightNumber()
                if (rightParentAndSetLeft != null) {
                    if (rightParentAndSetLeft.second) {
                        (rightParentAndSetLeft.first.left as Leaf).number = (rightParentAndSetLeft.first.left as Leaf).number + right.number
                    } else {
                        (rightParentAndSetLeft.first.right as Leaf).number = (rightParentAndSetLeft.first.right as Leaf).number + right.number
                    }
                }

                if (parent!!.left == this) {
                    val newNumber = Leaf(0)
                    newNumber.parent = parent!!
                    parent?.left = newNumber
                } else {
                    val newNumber = Leaf(0)
                    newNumber.parent = parent!!
                    parent?.right = newNumber
                }
            }

            private fun leftNumber(): Pair<Branch, Boolean>? {
                // If parent's left value isn't this one, use that one for explosion
                if (this.parent!!.left != this && this.parent!!.left is Leaf) {
                    return this.parent!! to true
                } else {
                    // If parent's left value is a pair, find the parent of it's right most leaf
                    var parent = parent
                    var current = this
                    while (parent?.left === current && parent.parent != null) {
                        current = parent
                        parent = parent.parent
                    }

                    // Found root and we got here from the left, can't do anything
                    if (parent?.parent == null && parent?.left === current) {
                        return null
                    }

                    if (parent?.left is Leaf) {
                        return parent to true
                    } else {
                        parent = parent?.left as Branch
                    }

                    while (parent?.right !is Leaf) {
                        parent = parent?.right as Branch
                    }

                    return parent to false
                }
            }

            private fun rightNumber(): Pair<Branch, Boolean>? {
                // If parent's right value isn't this one, use that one for explosion
                if (this.parent!!.right != this && this.parent!!.right is Leaf) {
                    return this.parent!! to false
                } else {
                    // If parent's right value is a pair, find the parent of it's left most leaf
                    var parent = parent
                    var current = this
                    while (parent?.right === current && parent.parent != null) {
                        current = parent
                        parent = parent.parent
                    }

                    // Found root and we got here from the right, can't do anything
                    if (parent?.parent == null && parent?.right === current) {
                        return null
                    }

                    if (parent?.right is Leaf) {
                        return parent to false
                    } else {
                        parent = parent?.right as Branch
                    }

                    while (parent?.left !is Leaf) {
                        parent = parent?.left as Branch
                    }

                    return parent to true
                }
            }

            override fun magnitude(): Int {
                return 3 * left!!.magnitude() + 2 * right!!.magnitude()
            }

            override fun toString(): String {
                return "[${left.toString()},${right.toString()}]"
            }

            override fun copy(parent: Branch?): Branch {
                val newBranch = Branch(null, null)
                val left = left?.copy(newBranch)
                val right = right?.copy(newBranch)
                newBranch.left = left
                newBranch.right = right
                newBranch.parent = parent
                return newBranch
            }
        }

        data class Leaf(var number: Int) : SFNumber {

            override var parent: Branch? = null

            fun split() {
                val newPair = Branch(null, null)
                val left = Leaf(floor(number / 2f).toInt())
                left.parent = newPair
                val right = Leaf(ceil(number / 2f).toInt())
                right.parent = newPair
                newPair.left = left
                newPair.right = right
                newPair.parent = parent
                if (parent?.left === this) {
                    parent?.left = newPair
                } else {
                    parent?.right = newPair
                }
            }

            override fun magnitude(): Int {
                return number
            }

            override fun toString(): String {
                return number.toString()
            }

            override fun copy(parent: Branch?): Leaf {
                return Leaf(number).apply { this.parent = parent }
            }

        }

        fun add(toAdd: SFNumber): Branch {
            val newPair = Branch(null, null)
            newPair.left = this
            newPair.right = toAdd
            this.parent = newPair
            toAdd.parent = newPair
            return newPair
        }

        fun reduce() {
            do {
                val beforeReduce = toString()
                val anyReduced = mutableSetOf<Boolean>()
                reduceExplode(1, anyReduced)
                if (anyReduced.isEmpty()) {
                    reduceSplit(anyReduced)
                }
            } while (toString() != beforeReduce)
        }

        fun reduceExplode(level: Int, anyReduced: MutableSet<Boolean>) {
            if (anyReduced.isNotEmpty()) return
            if (this is Branch) {
                if (level > 4) {
                    explode()
                    anyReduced += true
                } else {
                    left?.reduceExplode(level + 1, anyReduced)
                    right?.reduceExplode(level + 1, anyReduced)
                }
            }
        }

        fun reduceSplit(anyReduced: MutableSet<Boolean>) {
            if (anyReduced.isNotEmpty()) return
            if (this is Branch) {
                left?.reduceSplit(anyReduced)
                right?.reduceSplit(anyReduced)
            } else if (this is Leaf && number >= 10) {
                split()
                anyReduced += true
            }
        }
    }

    private val data = """
[[[1,4],[1,6]],[0,[5,[6,3]]]]
[[[8,2],5],[[[9,8],[3,5]],[2,1]]]
[[[[6,2],[0,6]],[[9,8],[7,8]]],[[6,3],[[8,8],3]]]
[[[1,[2,1]],[5,7]],[[[3,1],[3,1]],[[8,4],[8,5]]]]
[[[[6,4],7],[[1,6],5]],[7,[9,5]]]
[[7,[[5,3],[0,9]]],[[[6,2],[6,8]],[5,[5,7]]]]
[9,5]
[[[[7,8],[8,0]],[[3,8],[0,7]]],[[1,[1,2]],2]]
[[[4,[5,5]],[[6,8],[4,3]]],[[9,9],[4,[3,6]]]]
[[[[2,8],7],[[6,1],[1,0]]],[[6,2],9]]
[[[8,1],3],[9,[[1,4],[4,1]]]]
[[[[0,1],[3,9]],[[4,3],6]],[[[4,8],8],[[8,3],[9,5]]]]
[[[[7,3],7],[[5,9],0]],7]
[[[5,[1,6]],3],[[3,5],9]]
[[[[2,5],[1,8]],[[6,5],[0,1]]],[[[4,1],1],[0,[9,6]]]]
[[[4,8],[[3,6],[3,8]]],[[[2,3],3],[[9,8],[7,9]]]]
[[[[5,6],0],[9,[4,4]]],[[[3,1],[3,6]],[[6,0],3]]]
[[[[4,3],4],4],[[[1,6],7],[8,[6,0]]]]
[[[0,2],1],5]
[[[[7,2],[9,0]],[8,[0,1]]],2]
[[[1,6],[[6,2],5]],[[1,[8,2]],[[9,8],7]]]
[[[8,1],9],[[[4,3],2],[[2,9],6]]]
[[[[9,4],0],[4,0]],4]
[[[5,[2,8]],[[5,3],[6,4]]],[8,3]]
[[0,5],[[[3,4],7],[[0,2],[9,1]]]]
[[[8,[7,9]],[[1,8],6]],[[4,[6,0]],0]]
[[[1,0],[[6,7],4]],[[[2,5],[9,7]],[[7,8],0]]]
[[9,[[7,1],3]],[[[9,2],[4,3]],[2,[1,8]]]]
[[[5,[9,6]],4],[1,[[9,2],[6,8]]]]
[6,[[[6,1],7],6]]
[[4,[[5,6],9]],[[9,[6,6]],[[6,1],[8,2]]]]
[[1,[9,5]],[[[5,8],9],5]]
[[[[6,6],[1,8]],6],[[[4,9],4],[8,[9,8]]]]
[[[[6,5],[4,4]],[[0,2],8]],[[[0,6],[4,5]],3]]
[[[1,[6,9]],[9,[5,8]]],[5,2]]
[[2,[[2,8],[3,3]]],[[[1,9],9],6]]
[[3,2],[9,[2,2]]]
[4,[3,[6,[2,0]]]]
[[[[1,0],4],3],[[0,9],[[9,8],[7,1]]]]
[[[2,6],[3,8]],[[5,5],[2,3]]]
[6,[[[8,8],4],[[8,1],[6,6]]]]
[[[5,9],[5,3]],7]
[[[5,[1,2]],[6,[7,2]]],[[[0,5],3],3]]
[[8,[[7,3],[9,7]]],[[2,[3,9]],[[1,7],[5,7]]]]
[8,[4,6]]
[[[4,4],[[4,5],[2,5]]],[[[9,1],0],[[2,9],1]]]
[[[2,[2,8]],9],[5,[6,9]]]
[[[[4,1],5],[6,[2,7]]],[1,2]]
[[[6,[3,5]],0],[[0,3],4]]
[[[[3,2],[8,0]],[5,1]],[[[9,7],3],[[6,5],[2,6]]]]
[[1,[0,[1,4]]],[[[8,6],[6,9]],[[4,9],8]]]
[[[[5,2],[4,3]],[0,[3,5]]],[0,[1,7]]]
[[[8,1],[3,[8,1]]],[[[7,9],[6,2]],[[0,8],2]]]
[[[2,[9,7]],[[6,6],[2,7]]],[[8,[6,4]],0]]
[[3,0],[[6,3],1]]
[[[[5,5],2],[9,7]],[[0,[3,5]],7]]
[[[[4,8],2],0],[[4,[7,9]],[6,6]]]
[[[1,0],[[9,4],[8,8]]],2]
[[[6,1],9],[5,2]]
[[[7,[0,3]],[[5,5],7]],[5,[[0,5],[5,3]]]]
[[[[8,0],4],[[5,5],[9,4]]],[[[9,0],[2,5]],[6,[8,1]]]]
[[[7,8],[0,[5,4]]],[[[7,6],[0,9]],[7,2]]]
[[[4,[0,2]],[3,[4,9]]],[[[4,7],8],3]]
[[1,[5,[7,3]]],8]
[[[[1,3],[6,8]],3],[[6,1],8]]
[[[[7,9],5],[[6,2],4]],[[5,[6,9]],1]]
[[2,[3,[9,3]]],[[6,[2,7]],[4,8]]]
[7,[[6,2],[[6,7],[5,0]]]]
[[[9,[8,6]],1],[[4,8],[[6,1],[0,1]]]]
[[[[4,6],[4,0]],[[2,4],0]],[[[0,5],[9,8]],[[3,4],[2,5]]]]
[9,[3,[[5,5],[3,1]]]]
[[[5,[7,1]],3],[[[8,2],5],[[2,8],[0,0]]]]
[[[[8,3],0],[[5,0],5]],[[3,[8,2]],[[8,2],3]]]
[[4,[[9,4],5]],[[[1,6],[0,2]],[0,8]]]
[[[0,0],[[1,8],2]],[[[1,8],1],[0,[0,8]]]]
[[[6,[1,5]],5],[[2,[0,1]],9]]
[[7,[2,[2,8]]],[4,[[1,1],5]]]
[1,[[4,[0,5]],4]]
[[3,[[3,1],[1,2]]],[[[5,3],8],[5,2]]]
[[[3,[2,0]],6],[[9,3],[[3,0],[1,6]]]]
[4,[[6,[5,9]],[[4,1],[6,6]]]]
[8,[3,0]]
[[[[5,3],[8,8]],[[5,1],4]],[[6,6],[8,2]]]
[[1,[[7,1],5]],[[[2,3],7],[[7,6],0]]]
[9,[[4,3],[[6,2],0]]]
[[[[4,0],4],[1,7]],[[[3,8],8],[[9,1],1]]]
[[[0,1],[9,9]],7]
[[[[1,7],0],[1,5]],[1,[2,2]]]
[[[[6,1],[3,3]],[6,[9,0]]],[[7,0],3]]
[[[[6,1],[9,8]],[[2,2],2]],[8,[3,6]]]
[[6,[5,0]],[7,[1,7]]]
[[4,[[6,1],6]],[[2,5],7]]
[8,[8,[[6,4],1]]]
[[[[0,2],4],[[2,6],2]],0]
[[2,[[6,1],9]],[[7,[0,5]],[5,[9,4]]]]
[3,[[8,7],[[8,9],6]]]
[[[[7,8],[1,1]],[[2,6],[3,7]]],4]
[[[[6,1],1],5],5]
[[9,[4,[6,6]]],[[5,1],[8,2]]]
[[5,[[7,3],4]],9]
    """.trimIndent()
            .split("\n")
            .map { sfNumberString ->
                val queue = ArrayDeque<SFNumber.Branch>()
                var lastPop: SFNumber.Branch? = null
                var beforeComma = true
                sfNumberString.forEach { char ->
                    when (char) {
                        '[' -> {
                            val newPair = SFNumber.Branch()
                            val previousPair = queue.peekLast()
                            newPair.parent = previousPair
                            if (beforeComma) {
                                previousPair?.left = newPair
                            } else {
                                previousPair?.right = newPair
                                beforeComma = true
                            }
                            queue.addLast(newPair)
                        }
                        ',' -> {
                            beforeComma = false
                        }
                        in '0'..'9' -> {
                            val previousPair = queue.peekLast()!!
                            val newPair = Day18.SFNumber.Leaf(char.toString().toInt())
                            newPair.parent = previousPair
                            if (beforeComma) {
                                previousPair.left = newPair
                            } else {
                                previousPair.right = newPair
                                beforeComma = true
                            }
                        }
                        ']' -> {
                            lastPop = queue.removeLast()
                        }
                    }
                }
                lastPop!!
            }
}