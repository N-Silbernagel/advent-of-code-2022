import java.util.*

fun main() {
    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    var mostCalories = 0
    var currentCalories = 0

    for (line in input) {
        if (line.isBlank()) {
            if (currentCalories > mostCalories) {
                mostCalories = currentCalories
            }
            currentCalories = 0
            continue
        }

        val lineCalories = line.toInt()
        currentCalories += lineCalories
    }

    return mostCalories
}

fun part2(input: List<String>): Int {
    val top3Calories = TreeSet<Int>()

    var currentCalories = 0
    for (line in input) {
        if (line.isBlank()) {
            top3Calories.add(currentCalories)
            if (top3Calories.size > 3) {
                top3Calories.remove(top3Calories.first())
            }

            currentCalories = 0
            continue
        }

        val lineCalories = line.toInt()
        currentCalories += lineCalories
    }

    return top3Calories.sum()
}
