import kotlin.math.pow

fun main() {
    val input = readFileAsList("Day25")
    println(Day25.part1(input))
    println(Day25.part2(input))
}

object Day25 {
    val charNumberMap = mapOf(
        '=' to -2,
        '-' to -1,
        '0' to 0,
        '1' to 1,
        '2' to 2,
    )

    fun part1(input: List<String>): String {
        val snafuNumbers = mutableListOf<Long>()
        for (s in input) {
            snafuNumbers.add(snafuToDecimal(s))
        }
        val sum = snafuNumbers.sum()

        return decimalToSnafu(sum)
    }

    fun part2(input: List<String>): String {
        return ""
    }

    private fun snafuToDecimal(snafu: String): Long {
        val test = snafu.reversed()
            .map { charNumberMap[it]!! }
        var sum = 0L
        for ((index, i) in test.withIndex()) {
            sum += i * 5.toDouble().pow(index).toLong()
        }
        return sum
    }

    private fun decimalToSnafu(decimal: Long): String {
        return generateSequence(decimal) { (it + 2) / 5 }
            .takeWhile { it != 0L }
            .map { "012=-"[(it % 5).toInt()] }
            .joinToString("")
            .reversed()
    }
}
