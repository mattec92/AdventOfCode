package se.mattec.advent2021

fun main(args: Array<String>) {
    println(Day21.problem1())
    println(Day21.problem2())
}

object Day21 {

    fun problem1(): String {
        val positions = data.toTypedArray()
        val scores = Array(positions.size) { 0 }

        var dieValue = 1
        var numberOfThrows = 0

        fun throwDie(): Int {
            numberOfThrows++
            return (dieValue++ % 100 - 1) + 1
        }

        while (scores.none { it >= 1000 }) {
            for (playerIndex in 0 until positions.size) {
                val moves = throwDie() + throwDie() + throwDie()
                val newPosition = ((positions[playerIndex] + moves - 1) % 10) + 1
                positions[playerIndex] = newPosition
                val newScore = scores[playerIndex] + newPosition
                scores[playerIndex] = newScore
                if (newScore >= 1000) {
                    break
                }
            }
        }

        return (numberOfThrows * scores.min()!!).toString()
    }

    fun problem2(): String {
        val game = Game(players = data.map { Player(position = it, score = 0) }, multiplier = 1)
        val wins = mutableMapOf<Int, Long>()

        // Outcome of dice throws to how many instances of that outcome
        val throwSums = (1..3).flatMap { first -> (1..3).flatMap { second -> (1..3).map { third -> first + second + third } } }
                .groupBy { it }
                .map { it.key to it.value.size }

        fun roll(game: Game, playerIndex: Int) {
            val player = game.players[playerIndex]

            val newGames = throwSums.map { thrown ->
                val newPosition = ((player.position + thrown.first - 1) % 10) + 1
                val newScore = player.score + newPosition
                val newMultiplier = game.multiplier * thrown.second
                val newPlayers = game.players.toMutableList()
                newPlayers[playerIndex] = Player(newPosition, newScore)
                Game(players = newPlayers, multiplier = newMultiplier)
            }

            val wonToUnwon = newGames.partition { it.players[playerIndex].score >= 21 }
            val won = wonToUnwon.first
            val unwon = wonToUnwon.second

            won.forEach { game ->
                wins[playerIndex] = wins[playerIndex]?.plus(game.multiplier) ?: game.multiplier.toLong()
            }

            unwon.forEach { game ->
                roll(game, (playerIndex + 1) % game.players.size)
            }
        }

        roll(game, 0)

        return wins.maxBy { it.value }!!.value.toString()
    }

    private data class Game(val players: List<Player>, val multiplier: Long)

    private data class Player(val position: Int, val score: Int)

    private val data = """
Player 1 starting position: 4
Player 2 starting position: 7
    """.trimIndent()
            .split("\n")
            .map { it.split(": ")[1].toInt() }

}
