package se.mattec.advent2018

fun main(args: Array<String>) {
    println(Day13.problem1())
    println(Day13.problem2())
}

object Day13 {

    fun problem1(): String {
        val movesAndCarts = getMovesAndCarts(grid)
        val availableMoves = movesAndCarts.first
        val carts = movesAndCarts.second.map { Cart(it.key, it.value, null) }

        while (true) {
            val sortedCarts = carts.sortedWith(Comparator { o1, o2 ->
                o1.coordinates.second.compareTo(o2.coordinates.second).takeIf { it != 0 }
                        ?: o1.coordinates.first.compareTo(o2.coordinates.first)
            })

            for (cart in sortedCarts) {
                val availableMovesForCart = availableMoves[cart.coordinates]!!

                var lastMove: Int? = null //How the cart acted at an intersection
                val nextMove: Int = if (availableMovesForCart.size > 2) { //The actual move to make
                    //This is an intersection
                    lastMove = nextMove(cart.lastMove)
                    when (lastMove) {
                        STRAIGHT -> cart.direction
                        LEFT -> left(cart.direction)
                        RIGHT -> right(cart.direction)
                        else -> throw IllegalArgumentException()
                    }
                } else {
                    //If this is not an intersection, pick the first move that is not opposite to current direction
                    availableMovesForCart.filterNot { it == opposite(cart.direction) }.first()
                }

                var x = cart.coordinates.first
                var y = cart.coordinates.second
                when (nextMove) {
                    LEFT -> x--
                    RIGHT -> x++
                    UP -> y--
                    DOWN -> y++
                }

                cart.coordinates = x to y
                cart.direction = nextMove
                cart.lastMove = lastMove ?: cart.lastMove

                val collisions = carts.groupBy { it.coordinates }.filter { it.value.size > 1 }
                if (!collisions.isEmpty()) {
                    val coordinate = collisions.iterator().next().key
                    return "${coordinate.first},${coordinate.second}"
                }
            }
        }
    }

    private fun getMovesAndCarts(grid: List<String>): Pair<MutableMap<Pair<Int, Int>, List<Int>>, MutableMap<Pair<Int, Int>, Int>> {
        val availableMoves = mutableMapOf<Pair<Int, Int>, List<Int>>()
        val carts = mutableMapOf<Pair<Int, Int>, Int>()

        grid.forEachIndexed { lineIndex, line ->
            line.forEachIndexed { charIndex, char ->
                when (char) {
                //Rails
                    '/' -> {
                        //Need to check if this is a top or a bottom most corner
                        //If there is a horizontal rail to the right, this is a top most corner
                        if (line.length > charIndex + 1 && "-+<>".contains(grid[lineIndex][charIndex + 1])) {
                            availableMoves[Pair(charIndex, lineIndex)] = listOf(RIGHT, DOWN)
                        } else {
                            availableMoves[Pair(charIndex, lineIndex)] = listOf(LEFT, UP)
                        }
                    }
                    '\\' -> {
                        //If there is a horizontal rail to the right, this is a bottom most corner
                        if (line.length > charIndex + 1 && "-+<>".contains(grid[lineIndex][charIndex + 1])) {
                            availableMoves[Pair(charIndex, lineIndex)] = listOf(RIGHT, UP)
                        } else {
                            availableMoves[Pair(charIndex, lineIndex)] = listOf(LEFT, DOWN)
                        }
                    }
                    '|' -> {
                        availableMoves[Pair(charIndex, lineIndex)] = listOf(UP, DOWN)
                    }
                    '-' -> {
                        availableMoves[Pair(charIndex, lineIndex)] = listOf(LEFT, RIGHT)
                    }
                    '+' -> {
                        availableMoves[Pair(charIndex, lineIndex)] = listOf(LEFT, RIGHT, UP, DOWN)
                    }
                //Carts
                //Visual inspection shows that no carts start in intersections or corners
                    '<' -> {
                        availableMoves[Pair(charIndex, lineIndex)] = listOf(LEFT, RIGHT)
                        carts[Pair(charIndex, lineIndex)] = LEFT
                    }
                    '>' -> {
                        availableMoves[Pair(charIndex, lineIndex)] = listOf(LEFT, RIGHT)
                        carts[Pair(charIndex, lineIndex)] = RIGHT
                    }
                    '^' -> {
                        availableMoves[Pair(charIndex, lineIndex)] = listOf(UP, DOWN)
                        carts[Pair(charIndex, lineIndex)] = UP
                    }
                    'v' -> {
                        availableMoves[Pair(charIndex, lineIndex)] = listOf(UP, DOWN)
                        carts[Pair(charIndex, lineIndex)] = DOWN
                    }
                }
            }
        }

        return availableMoves to carts
    }

    private fun nextMove(lastMove: Int?): Int {
        return when (lastMove) {
            LEFT -> STRAIGHT
            STRAIGHT -> RIGHT
            RIGHT, null -> LEFT
            else -> throw IllegalArgumentException()
        }
    }

    private fun opposite(direction: Int): Int {
        return when (direction) {
            LEFT -> RIGHT
            RIGHT -> LEFT
            UP -> DOWN
            DOWN -> UP
            else -> throw  IllegalArgumentException()
        }
    }

    private fun left(direction: Int): Int {
        return when (direction) {
            UP -> LEFT
            DOWN -> RIGHT
            LEFT -> DOWN
            RIGHT -> UP
            else -> throw IllegalArgumentException()
        }
    }

    private fun right(direction: Int): Int {
        return when (direction) {
            UP -> RIGHT
            DOWN -> LEFT
            LEFT -> UP
            RIGHT -> DOWN
            else -> throw IllegalArgumentException()
        }
    }

    private fun printGrid(availableMoves: Map<Pair<Int, Int>, List<Int>>, carts: Map<Pair<Int, Int>, Int>) {
        var grid = ""
        for (y in 0 until 150) {
            for (x in 0 until 150) {
                val moves = availableMoves[Pair(x, y)]
                val cartDirection = carts[Pair(x, y)]
                if (cartDirection != null) {
                    grid += when (cartDirection) {
                        LEFT -> "<"
                        RIGHT -> ">"
                        UP -> "^"
                        DOWN -> "v"
                        else -> throw IllegalArgumentException()
                    }
                } else {
                    grid += when (moves) {
                        listOf(RIGHT, DOWN), listOf(LEFT, UP) -> "/"
                        listOf(RIGHT, UP), listOf(LEFT, DOWN) -> "\\"
                        listOf(UP, DOWN) -> "|"
                        listOf(LEFT, RIGHT) -> "-"
                        listOf(LEFT, RIGHT, UP, DOWN) -> "+"
                        else -> " "
                    }
                }
            }
            grid += "\n"
        }
        println(grid)
    }

    //A lot of duplication, pretty much only checking when we're done changed, but meh.
    fun problem2(): String {
        val movesAndCarts = getMovesAndCarts(grid)
        val availableMoves = movesAndCarts.first
        val carts = movesAndCarts.second.map { Cart(it.key, it.value, null) }.toMutableList()

        while (true) {
            val sortedCarts = carts.sortedWith(Comparator { o1, o2 ->
                o1.coordinates.second.compareTo(o2.coordinates.second).takeIf { it != 0 }
                        ?: o1.coordinates.first.compareTo(o2.coordinates.first)
            })

            for (cart in sortedCarts) {
                val availableMovesForCart = availableMoves[cart.coordinates]!!

                var lastMove: Int? = null //How the cart acted at an intersection
                val nextMove: Int = if (availableMovesForCart.size > 2) { //The actual move to make
                    //This is an intersection
                    lastMove = nextMove(cart.lastMove)
                    when (lastMove) {
                        STRAIGHT -> cart.direction
                        LEFT -> left(cart.direction)
                        RIGHT -> right(cart.direction)
                        else -> throw IllegalArgumentException()
                    }
                } else {
                    //If this is not an intersection, pick the first move that is not opposite to current direction
                    availableMovesForCart.filterNot { it == opposite(cart.direction) }.first()
                }

                var x = cart.coordinates.first
                var y = cart.coordinates.second
                when (nextMove) {
                    LEFT -> x--
                    RIGHT -> x++
                    UP -> y--
                    DOWN -> y++
                }

                cart.coordinates = x to y
                cart.direction = nextMove
                cart.lastMove = lastMove ?: cart.lastMove

                val collisions = carts.groupBy { it.coordinates }.filter { it.value.size > 1 }
                if (!collisions.isEmpty()) {
                    val coordinate = collisions.iterator().next().key
                    carts.removeIf { it.coordinates == coordinate }
                }
            }

            if (carts.size == 1) {
                val finalCart = carts[0]
                return "${finalCart.coordinates.first},${finalCart.coordinates.second}"
            }
        }
    }

    private data class Cart(var coordinates: Pair<Int, Int>, var direction: Int, var lastMove: Int?)

    private const val LEFT = 0
    private const val RIGHT = 1
    private const val UP = 2
    private const val DOWN = 3
    private const val STRAIGHT = 4

    private val grid = """
   /----------------------------------------------------------------------------------------------\
   |        /-----------------------------------------------------\                               |
   |     /--+-----------------------------------------------------+-------------------------------+--\                            /---------\
 /-+-----+--+-------------------------------------------------\   |                               |  |                            |         |
 | |     |  |     /-------------------------------------------+---+--------------------------\    |  |                            |         |
 | |     |  |    /+---------------\                           |   |            /-------------+----+--+----------------------------+-------\ |
 | |     |  |    ||               |                      /----+---+------------+\            |    |  |                            |       | |
 | |     |  |    ||               |                      |    |   |        /---++------------+----+--+-----------------\          |       | |
 | |     |  |    ||               |                      |    |   |        |   ||  /---------+----+--+-----------------+---------\|       | |
 | |     |  |    ||               |                      |   /+---+--------+---++--+---------+----+--+----------\      |         ||       | |
 | |     |  |    ||        /------+----------------------+-\ ||   |        |   ||  |         |    |  |          |      |         ||       | |
 | |     |  |    ||        |      |                      | | ||  /+--------+---++--+---------+--\ |  |          |      |         ||       | |
 | |     |  |    ||        |     /+----------------------+-+-++--++--------+---++--+---------+--+-+--+----------+------+-----\   ||       | |
 | |     |  |    ||       /+-----++----------------------+-+-++--++--------+---++--+---------+--+-+--+------\   |      |     |   ||       | |
 | |     |  |    ||       ||     ||                      | | ||  ||        |   ||  |         |  | |  |    /-+---+------+-----+---++\      | |
 | |  /--+--+----++--<----++-----++------------\         | | ||  ||        |   ||  |         |  | |  |    | |  /+------+-----+---+++---\  | |
 | |  |  |  |   /++-------++-----++--\         |      /--+-+-++--++--------+---++--+---------+--+-+--+----+-+--++------+-----+---+++---+--+-+--\
 | |  |  |  |   |||  /----++-----++--+---------+----\ |  | | ||  ||        \---++--+---------+--+-+--+----+-+--++------/     |   |||   |  | |  |
 | \--+--+--+---+++--+----++-----++--+---------+----+-+--+-+-++--++------------++--+---------+--+-/  |    | |  ||            |   |||   |  | |  |
 |    |  |  |   |||  |    ||     ||  |       /-+----+-+--+-+-++--++------------++--+---------+--+----+----+-+--++------------+---+++-\ |  | |  |
 |  /-+--+--+---+++--+----++----\||  |       | |    | |  | | ||  ||            ||  |         |  |    |    | |  ||            |   ||| | |  | |  |
 |/-+-+--+--+---+++--+----++----+++--+-------+-+----+-+--+-+-++--++------------++\ |         |  |    |    | |  ||            |   |\+-+-+--+-/  |
 || | |  |  |   |||  |    ||  /-+++--+-------+-+----+-+--+-+-++--++------------+++-+---------+--+----+----+-+--++------------+-<\| | | |  |    |
 || | |  |  |   |||  |    ||  | |||  |       |/+----+-+--+-+-++--++------------+++-+---------+--+----+---\| |  ||            |  || | | |  |    |
 || | |  |  |   |||  |    ||  | |||  |       |||/---+-+--+-+-++--++--------\ /-+++-+---------+--+----+---++-+--++------------+--++-+-+-+--+----+-\
 || | |  |  |/--+++--+----++--+-+++--+-------++++---+-+--+-+-++--++--------+-+-+++-+---------+--+----+---++-+--++\           |  || | | |  |    | |
 || | |  |  ||  |||  |    ||  | |||  |       ||||   | | /+-+-++--++--------+-+-+++-+---------+--+----+---++-+--+++--------\  |  || | | |  |    | |
 || | |  |  ||  |||  |    ||  | |||  |       ||||/--+-+-++-+-++--++--------+-+-+++-+---------+--+----+---++-+--+++--------+\ |  || | | |  |    | |
 || | |  |  ||  ||| /+----++--+-+++--+-------+++++--+-+-++\| ||  ||        | | \++-+---------+--+----+---++-+--+++--------++-+--++-+-+-+--/    | |
 || | |  |  ||  ||| || /--++--+-+++--+-------+++++\ | | |||| ||  ||       /+-+--++-+---------+--+----+---++-+--+++--------++-+--++-+-+-+-----\ | |
 || | |  |  ||  ||| || |  ||  | |||  |       |||||| | | |||| || /++-------++-+--++-+----\/---+--+----+---++-+--+++--------++-+--++-+-+-+\    | | |
 || | \--+--++--+++-++-+--++--+-+++--+-------++/||| | | ||||/++-+++-------++-+--++-+----++---+--+----+---++-+--+++--------++-+--++-+-+-++----+-+-+\
 || |    |  ||  ||| || |  ||  | |||  |    /--++-+++-+-+-+++++++-+++-------++-+--++\|    ||   |  |  /-+---++-+--+++--------++-+--++-+-+-++----+-+-++\
 || |    |  ||  ||| || |  ||  | |||  |/---+--++-+++-+-+-+++++++-+++-------++-+--++++----++---+\ |  | |   || |  |||        || |  |^ | | ||    | | |||
 || |    |  || /+++-++-+--++--+-+++--++---+--++-+++-+-+-+++++++-+++-------++-+--++++----++---++-+--+-+---++-+--+++--\     || |  || | | ||    | | |||
 || |    | /++-++++-++-+--++--+-+++--++---+--++-+++-+-+-+++++++-+++-------++-+--++++-\  ||   || |  | |/->++-+--+++--+--\  || |  || | | v|    | | |||
 || |    |/+++-++++-++-+--++\ | |||  ||   |  || ||| | | |||||\+-+++-------++-+--++++-+--++---++-+--+-++--++-+--+/|  |  |  || |  || | | ||    | | |||
 || |    |||\+-++++-++-+--+++-+-+++--++---+--++-+++-+-+-+++++-+-++/ /-----++-+--++++-+--++---++-+\ | ||  || |  \-+--+--+--++-+--++-+-+-/|    | | |||
 || |    ||| | |||| || |  ||| | |||  ||   |  || |||/+-+-+++++-+-++--+-----++-+--++++-+--++---++-++-+-++--++-+\   |  | /+--++-+--++-+\|  |    | | |||
 || |    ||| | |||| || |  ||| | |||  ||   |  || ||||| | ||||| | ||  |     || |  |||| |  ||   || || | ||  || ||   |  | ||  || |  || |||  |    | | |||
 || |    ||| | |||| || |  ||| | |||  || /-+--++-+++++-+-+++++-+-++--+-----++-+--++++-+--++---++-++-+-++--++-++\  |  | ||  || |  || |||  |    | | |||
 |\-+----+++-+-++++-++-+--+++-+-+++--++-+-+--++-+++++-+-+++++-+-++--+-----++-+--+/|| |  ||   || || | ||  || |||  |  | ||  || |  || |||  |    | | |||
 |  |    ||| | |||| || |  ||| | |||  || | |  || ||||| | ||||| | ||  |     || |  | || |  ||   || || | ||  || |||  |  | ||  || |  || |||  |    | | |||
 |  |    ||| | |||| || |  ||| | |||  || | |  || ||||| | ||||| | ||  |     || |  | || |  ||   || || | ||  || |||  |  | ||  || |  || |||  |    | | |||
 |  |    ||| | |||| || |  ||| | |||/-++-+-+--++-+++++-+-+++++-+-++--+-----++-+--+-++-+--++---++-++-+-++--++-+++--+--+-++--++-+\ || |||  |    | | |||
 |  |    ||| | |||| || |  ||| | |||| || | |  || ||||| |/+++++-+-++--+-----++-+--+-++-+--++\  || || | ||  ||/+++--+--+-++--++-++-++-+++--+\   | | |||
 |  |    ||| | |||| || |  ||| | |||| || | |  || ||||| ||||||| |/++--+-----++-+--+-++-+--+++--++-++-+-++--++++++--+--+-++--++\|| || |||  ||   | | |||
 |  |   /+++-+-++++-++-+--+++-+-++++-++-+-+--++-+++++-+++++++-++++--+-----++-+--+-++-+--+++--++-++-+-++--++++++\ |  | ||  ||||| || |||  ||   | | |||
 |  |   |||| | |||| || |  ||| | |||| || | |  || ||||| ||||||| ||||  |     || |  | || |  |||  || || | ||  ||||||| |  | ||  ||||| || |||  ||   | | |||
 |  |   |||| | |||| || |  ||| | |||| |\-+-+--++-+++++-+++++++-++++--+-----++-+--+-++-+--+++--+/ || |/++--+++++++-+--+-++--+++++-++\|||  ||   | | |||
 |  |   |||| | |||| || \--+++-+-++++-+--+-+--++-++/|| ||||||| ||||  |     || |  | || |  |||  |  || |||\--+++++++-+--+-+/  ||||| ||||||  ||   | | |||
 |  |   |||| | |||| ||    ||| | |||| |/-+-+--++-++-++-+++++++-++++--+-----++-+--+-++-+--+++--+--++-+++--\||||||| |  | |   ||||| ||||||  ||   | | |||
 |  |   |||| | |||| ||    ||| |/++++-++-+-+\ || || || ||\++++-++++--+-----++-+--+-++-+--+++--+--++-+++--++++++++-+--+-+---/|||| ||||||  ||   | | |||
 |  |   |||| | \+++-++----+++-++++++-++-+-++-++-++-++-++-++++-++++--+-----++-+--+-++-+--+++--+--++-+++--++++++++-+--/ |    |||| ||||||  ||   | | |||
/+--+---++++-+--+++-++----+++-++++++-++-+-++-++-++-++-++-++++-++++--+---\ || | /+-++-+--+++--+--++-+++--++++++++-+----+----++++-++++++--++-\ | | |||
||  |   |||| |  ||| ||    ||| |||||| || | ||/++-++-++-++-++++-++++--+---+-++-+-++-++-+--+++--+--++-+++--++++++++-+--\ |    |||| ||||||  || | | | |||
||  |   |||| v  ||| ||    ||| |||||| || | ||||| || || || |||| |||| /+---+-++-+-++-++-+--+++--+--++-+++--++++++++-+--+-+----++++-++++++--++-+-+-+-+++\
||  |   |||| |  ||\-++----+++-++++++-++-+-+++++-++-++-++-++++-++++-++---+-++-+-++-++-+--+++--/  || |||  |||||||| |  | |    |||| ||||||  || | | | ||||
||  |   |||| |  ||  |\----+++-++++++-++-+-+++++-++-+/ || |||| |||| |\---+-++-+-++-++-+--+++-----+/ |||  |||||||| |  | |    |||| ||||||  || | | | ||||
||  |   |||| |  ||  | /---+++-++++++-++-+-+++++-++-+--++-++++-++++-+----+-++-+-++-++-+--+++-----+--+++--++++++++-+-\| |    |||| ||||||  || | | | ||||
||  |   |||| |  ||  | |   ||| |||||| ||/+-+++++-++-+--++-++++-++++-+----+-++-+-++-++-+--+++---<-+\ |||  |||||||| | || |    |||| ||||||  || | | | ||||
||  |   |||| |  ||  | |   ||| |||||| |||| \++++-++-+--++-++++-++++-+----+-++-+-++-/| | /+++-----++-+++--++++++++-+-++-+----++++-++++++--++\| | | ||||
||  |   \+++-+--++--+-+---+++-++++++-++++--++++-++-+--++-++++-++++-+----+-++-+-++--+-+-++++-----++-+++--+++++++/ | || |    |||| ||||||  |||| | | ||||
||  |/---+++-+--++--+-+---+++-++++++-++++--++++-++-+--++-++++-++++\|    | || | ||  | | ||||     || |||  |||||||  | || |    |||| ||||||  |||| | | ||||
||  ||   ||| |  ||  | |   ||| |||||| |||| /++++-++-+--++-++++-++++++----+-++\|/++--+-+-++++---\ || |||  ||||||| /+-++-+----++++-++++++\ |||| | | ||||
||  ||   ||| |  ||  | |   ||| |\++++-++++-+/||| || |  || |||| ||||||    | |||||||  | | ||||   | || |||  ||||||| || || |    |||| ||||||| |||| | | ||||
||  ||   |||/+--++--+\|   ||| | |||| |||| | ||| || |  || |||| |||\++----+-+++++++--+-+-++++---+-/| \++--+++++++-++-++-+----++++-+++++++-++++-+-+-++/|
||  ||   |||||  ||  |||   ||| | |||| |||| | ||| |\-+--++-++++-+++-++----+-+++++++--+-+-++++---+--+--++--+++++++-++-++-+----/||| ||||||| |||| | | || |
||  ||   |||||  ||  |||   ||| | |||| |||| | ||| |  |  |\-++++-+++-++----+-+++++++--+-+-+++/   |  |  ||  ||||||| || || |     ||| ||||||| |||| | | || |
||  ||   |||||  ||  |||   ||| | |||| |||| | ||| |  |  |  |||| ||| ||    | |||\+++--+-+-+++----+--+--++--+++++++-++-++-+-----+++-+++++++-++++-+-+-/| |
||  ||   |||||  ||  |||   ||| | |||| |||| | |||/+--+--+--++++-+++-++----+-+++-+++--+-+-+++----+--+--++--+++++++-++-++-+-----+++-+++++++-++++-+-+--+-+\
||  ||   |||||  ||  |||  /+++-+-++++-++++-+-+++++--+--+--++++-+++-++----+-+++-+++->+-+-+++----+--+--++--+++++++-++-++-+-----+++\||||||| |||| | |  | ||
||  ||   |||||  ||  ||\--++++-+-++++-++++-+-+++++--+--+--++++-+++-++----+-+++-+++--+-+-+++----+--+--++--+++++++-++-/| |     ||||||||||| |||| | |  | ||
||  ||   |||||  ||  ||   |||| | |||| |||| | |\+++--+--+--++++-+++-++----+-+++-+++--+-+>+++----+--+--++--+++++++-++--+-+-----+++++++++/| |||| | |  | ||
||  ||   |||||  ||  ||   |||| |/++++-++++-+-+-+++--+--+--++++\||| || /--+-+++-+++--+-+-+++----+--+--++--+++++++-++-\| |     ||||||||| | |||| | |  | ||
||  ||   |||||  ||  ||   |\++-++++++-++++-+-+-+++--+--+--++++++++-++-+--+-+++-+++--+-+-+++----+--+--++--++++/|| || || |     ||||||||| | |||| | |  | ||
||  ||   |||||  || /++---+-++-++++++\|||| | | |||  | /+--++++++++-++-+--+-+++-+++--+-+-+++----+--+--++--++++-++-++-++-+-----+++++++++-+-++++-+-+--+\||
||  ||   |||||  || |||   | || ||||||||||| | | |||  | ||  \+++++++-++-+--+-+++-++/  | | |||    |  |  ||  |||| || || || |     ||||||||| | |||| | |  ||||
|\--++---+++++--++-+++---+-++-+++++++++++-+-+-+++--+-++---++++/|| || |  | ||| ||   | | |||    |  |  ||  |||| || ^| || |     ||||||||| | |||| | |  ||||
|   ||   |||||  || |||   | || ||||||||||| \-+-+++--+-++---++++-++-++-+--+-++/ ||   | | |||    |  |  ||  |||| || || || |     ||||||||| | |||| | |  ||||
|   ||   |||||  || |||   | || |||||||||||  /+-+++--+-++---++++-++-++-+--+-++--++---+-+-+++----+--+--++--++++-++-++-++-+-----+++++++++-+-++++-+-+\ ||||
|   ||   |||||  || |||  /+-++-+++++++++++--++-+++--+-++---++++-++-++-+--+-++--++\  | | |||    |  |  ||  |||| || || || |     ||||||||| | |||| | || ||||
|   ||   |||||  || |||  || || |||||||||||  || |||  | ||   |||| || || |  | ||  |||  | | |||    |  |  ||  |||| || || || |     ||||||||| | |||| | || ||||
|   ||   |||||  || |||  || || |||||||||||  || |||  | |\---++++-++-++-+--+-++--+++--+-+-+++----+--+--++--++++-++-++-++-+-----+++++++++-+-++++-+-/| ||||
|   ||   |||||  || |||  || || |||||||||||  || |||  | |    |||| || || |  | ||  |||  | | |||    |  |  ||  ||\+-++-++-++-+-----+++++++/| | |||| |  | ||||
|   ||   |||||  || |||  || || |||||||||||  || |||  | |    |||| || || |  | ||  |||  | | |||    |  |  ||  || | || || || |     ||||||| | | |||| |  | ||||
|   ||   |||||  || |||  || || |||||||||||  || |||  | |    |||| || || |  | ||  |||  | | ||\----+--+--++--++-+-++-++-++-+-----+++++++-+-+-/||| |  | ||||
|   ||   |||||  || |||  || || \++++++++++--++-+++--+-+----++++-++-++-+--+-++--+++--+-+-++-----+--+--++--++-+-++-++-++-+-----++++/|| | |  ||| |  | ||||
|   ||   |||||  || |||  || ||  ||||||||||  || |||  | |    |||| || |\-+--+-++--+++--+-+-++-----+--+--++--++-+-++-++-++-+-----++++-++-+-+--+++-+--+-++/|
|   ||   |||||  |\-+++--++-++--+++/||||||  || |||  | |    |||| || |  |  | ||  |\+--+-+-++-----+--+--++--++-+-++-++-++-+-----++++-++-+-+--++/ |  | || |
|   |\---+++++--+--+++--++-++--+++-++++++--++-+++--+-+----++++-++-/  |  | ||  | |  | | || /---+--+--++--++-+-++-++-++-+--\  |||| || | |  ||  |  | || |
|   |    |||||  |  |||  || ||  ||| ||||\+--++-+++--+-+----++++-++----+--+-++--+-+--+-+-++-+---+--/  ||  || | || || || |  |  |||| || | |  ||  |  | || |
|   |  /-+++++->+--+++--++-++--+++-++++-+--++-+++--+-+----++++-++----+--+-++--+-+--+-+-++-+---+-----++--++-+-++\|| || |  |  |||| || | |  ||  |  | || |
| /-+--+-+++++--+--+++--++-++--+++-++++-+--++-+++--+-+----++++-++----+--+-++--+-+--+-+-++-+---+-----++--++-+\||||| || |  |  |||| || | |  ||  |  | || |
| | |  | |||||  |  |||  || ||  ||| |||| |  || |||  | |    |||| ||    |  | ||  | |  | | || |   |     ||  || ||||||| || |  |  |||| |v | |  ||  |  | || |
| | |  | |||||  |  |||  || ||  ||| |||| |  || |||  | |  />++++-++----+--+-++--+-+\ \-+-++-+---+-----++--++-+++++++-++-+--+--++++-/| | |  ||  |  | || |
| | |  | |||||  |  |||  || ||  ||| |||| |  || |||  | |  | |||| ||/---+--+-++--+-++---+-++-+---+-----++--++\||||||| || |  |  ||||  | | |  ||  |  | || |
| | |  | ||||\--+--+++--++-++--+++-++++-+--++-+++--+-+--+-++++-+++---+--+-++--+-++---+-++-+---+-----++--+++++++++/ || |  |  ||||  | | |  ||  |  | || |
| | |  | ||||   |  |||  || ||  ||| |||| |  || |||  | |  | |||| |||   |  | ||  | ||   | ||/+---+-----++\ |||||||||  || |  |  ||||  | | |  ||  |  | || |
| | |  | ||||   |  |||  || ||  ||| |||| |  || |||  | |  | |||| |||   |  | ||  | ||   |/++++---+----\||| |||||||||  || |  |  ||||  | | |  ||  |  | || |
| | |  | ||||   |  |||  || ||  ||| |||| |  || |||  | |/-+-++++-+++---+--+-++--+-++---++++++---+----++++-+++++++++--++-+--+--++++--+-+-+--++--+\ | || |
| | |  | ||||   |  |||  || ||/-+++-++++-+--++-+++--+-++-+-++++-+++---+--+-++--+-++---++++++\  |    |||| |||||||||  || |  |  ||||  | | |  ||  || | || |
| | |/-+-++++---+--+++--++-+++-+++-++++-+--++-+++--+-++-+-++++-+++---+--+-++--+-++---+++++++--+--\ |||| |||||||||  || |  |  ||||  | | |  ||  || | || |
| | || |/++++---+--+++--++-+++-+++-++++-+-\|| |||  | || | |||| \++---+--+-++--+-++---+++++++--+--+-++++-+++++++++--++-+--+--/|||  | | |  ||  || | || |
| | || ||||\+---+--+++--++-+++-+++-++++-+-+++-+++--+-++-+-++++--++---+--+-++--+-++---/||||||  | /+-++++-+++++++++--++\|  |   |||  | | |  ||  || | || |
| | || |||| |/--+--+++-\|| ||| ||| |||| | ||\-+++<-+-++-+-++++--++---+--+-++--+-++----++++++--+-++-++++-+++++++++--+/||  |   |||  | | |  ||  || | || |
| | || |||| ||  |  ||| ||| ||| |||/++++-+-++--+++-\| || | ||\+--++---+--+-++--+-++----++++++--+-++-++++-+++++++++--+-++--+---+++--+-+-+--++--++-+-/| |
| | \+-++++-++--+--+++-+++-+++-+/|||||| | ||  ||| || || \-++-+--++---+--+-++--+-+/ /--++++++\ | || |||| |||||||||  | ||  |   |||  | | |  ||  || |  | |
| |  | ||\+-++--+--+++-+++-+++-+-++++++-+-++--+++-++-++---++-+--++---+--+-++--+-+--+--+++++++-+-++-++/| ||||||||\--+-++--+---+++--+-+-/  ||  || |  | |
|/+--+-++-+-++--+--+++-+++-+++-+-++++++-+-++--+++-++-++---++-+--++---+--+-++--+-+--+--+++++++-+-++-++\| ||||||||   | ||  |   |||  | |    ||  || |  | |
|||  | || | ||  \--+++-+++-+++-+-++++/| \-++--+++-++-++---++-+--++---+--+-++--+-+--+--+++++++-+-++-++++-++++++/|   | ||  |   |||  | |    ||  || |  | |
|||  | || | ||     ||| ||| ||| | |||| |   ||  ||| || ||   || |  \+---+--+-++--+-+--+--++/|||| | || |||| |||||| |   | ||  |   |||  | |    ||  || |  | |
|||/-+-++-+-++-----+++-+++-+++-+-++++-+---++--+++-++-++---++\|   |   |  | ||  | |  |  || |||| | || |||| |||||| |   | ||  |   |||  | |    ||  || |  | |
|||| | || | ||     ||| ||| ||| | \+++-+---++--+++-++-++---++++---+---+--+-++--+-+--+--++-++++-+-++-++++-++++++-+---+-++--+---/||  | |    ||  || |  | |
|||| | || | ||     ||| ||| ||| |  ||| |   ||  ||\-++-++---++++---+---+--+-+/  | |  |  || |||| | || |||| |||||| |   | ||  |    || /+-+----++--++-+--+\|
|||| | || | ||     ||| ||| ||| \--+++-+---++--++--++-++---+++/   |   |  | |   | |  |  || |||| | || |||| |||||| |   | ||  |    || || |    ||  || |  |||
|||| | || \-++-----+++-+++-+/|    ||| |   ||  ||  || ||   |||    |/--+--+-+--\| |  |  \+-++++-+-++-/||| |||\++-+---+-++--+----++-++-+----/|  || |  |||
|||| | ||   ||     ||| ||| \-+----+++-+---++--++--++-++---+/|    ||  |  | |  || |  |   |/++++-+-++--+++-+++-++-+---+-++--+---\|| || |     |  || |  |||
|||| | ||   ||     ||| |||   |    \++-+---++--++--/| \+---+-+----++--+--+-+--++-+--+---++++++-+-++--+++-+++-++-+---+-++--+---+++-++-+-----+--++-+--/||
|||| | ||   ||     ||| |||   |     || |   ||  ||   |  |   | |    ||  \--+-+--++-+--+---++++++-+-++--+++-+++-++-+---/ ||  |   ||| || |     |  || |   ||
|||| | \+---++-----+++-+++---+-----++-+---++--++---+--+---+-+----++-----+-+--++-+--+---++++++-+-++--+++-+++-++-/     ||  |   ||| || |     |  || |   ||
|||| |  |   ||     ||| |||   |     || |   ||  ||   |  |   | |    ||     | |  || |  |   |||||| | \+--+++-+++-++-------/|  |   ||| || |     |  || |   ||
|||| |  |   ||     ||| |||   |     || |   ||  |\---+--+---+-+----++-----+-+--++-+--+---++++++-+--+--+++-+++-++--------+--+---+++-++-+-----+--++-+---+/
|||| |  |   \+-----++/ |||   |     || |   ||  \----+--+---+-+----++-----+-+--++-+--+---++++++-+--+--+++-+/| ||        |  |   ||| || |     |  || |   |
|||| |  \----+-----++--+++---+-----++-+---/|       \--+---+-+----++-----+-+--++-+--+---++++++-+--+--+++-+-+-+/        \--+---+++-++-/     |  || |   |
|||| |       |     ||  |||   |     |v |    |    /-----+---+-+----++-----+-+--++-+--+---++++++-+--+--+++-+-+-+----\       |   ||| ||       |  || |   |
|||| |       |     ||  ||\---+-----++-+----+----+-----+---+-+----++-----+-+--++-+--+---++++++-+--+--+++-+-+-+--<-+-------+---++/ ||       |  || |   |
|||| |       |     ||  ||    |     || |    |    |     |   | |    ||     | \--++-+--+---++++++-+--+--+++-+-+-+----+-------+---++--++-------+--/| |   |
|||| |  /----+-----++--++--\ |     || |    |    |     |   | |    ||     |    || |  |   |\++++-+--+--+++-+-+-+----+-------+---/|  ||       |   | |   |
|||| |  |    |     ||  ||  | |     || |   /+----+-----+---+-+--\ ||     |    || |  |   | |\++-+--+--+++-+-+-+----+-------/    |  ||       |   | |   |
|||| |  |    |     ||  ||  | |     || |   ||    |     |   | |  | ||     |    || |  |   \-+-++-+--+--+++-+-+-+----+------------+--++-------/   | |   |
||\+-+--+----+-----++--++--+-+-----++-+-->++----+-----+---+-+--+-++-----+----++-+--+-----+-++-+--+--+++-+-+-/    |            |  ||           | |   |
|| | \--+----+-----++--++--+-+-----++-+---++----+-----+---+-+--+-++-----+----++-+--+-----+-++-+--/  |||/+-+------+---\        |  ||           | |   |
|| |    |    |     ||  |\--+-+-----++-+---++----+-----+---+-+--+-++-----+----++-/  |     | || |     ||||| |      |   |        |  ||           | |   |
|| |    |    |     ||  |   | \-----++-+---++----+-----+---+-+--+-++-----+----++----+-----+-/| |     ||||| |      |   |        |  ||           | |   |
|| |    |    |     |\--+---+-------++-+---++----+-----+---/ |  | ||     |    ||    |     |  | |     ||||| |      |   |        |  ||           | |   |
|| |    |    \-----+---/   |       || |   ||    |     |     |  | ||     |    ||    |     |  | |     \++++-+------+---+--------+--+/           | |   |
|| |    |          \-------+-------+/ |   ||    |     \-----+--+-++-----+----++----+-----+--+-+------++++-+------+---+--------+--+------------/ |   |
|\-+----+------------------+-------+--+---++----+-----------+--+-++-----+----++----+-----+--+-+------/||| |      |   |        |  |              |   |
|  |    |                  |       |  \---++----+-----------+--+-++-----+----++----+-----+--+-+-------++/ |      |   |        |  |              |   |
|  |    |                  |       |      \+----+-----------+--/ |\-----+----/|    |     \--+-+-------/|  |      |   |        |  |              |   |
|  |    |                  |       |       |    \-----------+----+------+-----+----+--------+-+--------+--+------/   |        |  |              |   |
|  |    |                  |       |       |                |    |      |     |    |        | |        |  |          |        |  |              |   |
|  |    \------------------/       \-------+----------------+----+------+-----+----+--------+-+--------+--+----------+--------/  |              |   |
\--+---------------------------------------+----------------+----+------/     |    \--------/ |        \--+----------/           |              |   |
   |                                       |                |    |            |               |           |                      |              |   |
   |                                       \----------------+----+------------+---------------+-----------+----------------------+--------------/   |
   |                                                        |    |            |               |           |                      \------------------/
   \--------------------------------------------------------/    \------------+---------------+-----------/
                                                                              \---------------/
    """.trimIndent()
            .split("\n")

}