fun main() {
    val ROCK_ENEMY = 'A'
    val PAPER_ENEMY = 'B'
    val SCISSORS_ENEMY = 'C'

    val enemyChoiceMap = mapOf(
        Pair(ROCK_ENEMY, Choice.ROCK),
        Pair(PAPER_ENEMY, Choice.PAPER),
        Pair(SCISSORS_ENEMY, Choice.SCISSORS)
    )

    val choiceScores = mapOf(
        Pair(Choice.ROCK, 1),
        Pair(Choice.PAPER, 2),
        Pair(Choice.SCISSORS, 3)
    )

    val looseMap = mapOf(
        Pair(Choice.ROCK, Choice.SCISSORS),
        Pair(Choice.PAPER, Choice.ROCK),
        Pair(Choice.SCISSORS, Choice.PAPER),
    )

    val winMap = mapOf(
        Pair(Choice.SCISSORS, Choice.ROCK),
        Pair(Choice.ROCK, Choice.PAPER),
        Pair(Choice.PAPER, Choice.SCISSORS),
    )

    val resultScoreMap = mapOf(
        Pair(Result.LOOSE, 0),
        Pair(Result.DRAW, 3),
        Pair(Result.WIN, 6)
    )

    fun part1(input: List<String>): Int {
        val ROCK_ME = 'X'
        val PAPER_ME = 'Y'
        val SCISSORS_ME = 'Z'

        val myChoiceMap = mapOf(
            Pair(ROCK_ME, Choice.ROCK),
            Pair(PAPER_ME, Choice.PAPER),
            Pair(SCISSORS_ME, Choice.SCISSORS)
        )

        var score = 0

        for (line in input) {
            val splitLine = line.split(' ')
            val enemyChoice = enemyChoiceMap.get(splitLine[0].get(0))
            val myChoice = myChoiceMap.get(splitLine[1].get(0))

            score += choiceScores.getOrDefault(myChoice, 0)

            if (enemyChoice == myChoice) {
                score += resultScoreMap.getOrDefault(Result.DRAW, 0)
                continue
            }

            val myLooseChoice = looseMap.getOrDefault(enemyChoice, Choice.ROCK)

            if (myChoice == myLooseChoice) {
                continue
            }

            val myWinChoice = winMap.getOrDefault(enemyChoice, Choice.ROCK)

            if (myChoice == myWinChoice) {
                score += 6
                continue
            }

        }

        return score
    }

    fun part2(input: List<String>): Int {
        val LOOSE = 'X'
        val DRAW = 'Y'
        val WIN = 'Z'

        val resultMap = mapOf(
            Pair(LOOSE, Result.LOOSE),
            Pair(DRAW, Result.DRAW),
            Pair(WIN, Result.WIN)
        )

        var score = 0

        for (line in input) {
            val splitLine = line.split(' ')
            val enemyChoice = enemyChoiceMap.getOrDefault(splitLine[0].get(0), Choice.ROCK)
            val wantedResult = resultMap.get(splitLine[1].get(0))

            var myChoice = Choice.ROCK

            if (wantedResult == Result.DRAW) {
                myChoice = enemyChoice
            }

            if (wantedResult == Result.LOOSE) {
                myChoice = looseMap.getOrDefault(enemyChoice, Choice.ROCK)
            }

            if (wantedResult == Result.WIN) {
                myChoice = winMap.getOrDefault(enemyChoice, Choice.ROCK)
            }

            score += choiceScores.getOrDefault(myChoice, 0)
            score += resultScoreMap.getOrDefault(wantedResult, 0)
        }

        return score
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

enum class Choice {
    ROCK,
    PAPER,
    SCISSORS
}

enum class Result {
    WIN,
    LOOSE,
    DRAW
}
