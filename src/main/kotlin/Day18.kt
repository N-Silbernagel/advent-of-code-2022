import kotlin.math.absoluteValue

fun main() {
    val input = readFileAsList("Day18")
    println(Day18.part1(input))
    println(Day18.part2(input))
}

object Day18 {
    fun part1(input: List<String>): Long {
        val vectors = parseLava(input)

        var uncoveredSidesTotal = 0
        for ((index, vector) in vectors.withIndex()) {
            var uncoveredSides = 6
            for (vector2 in vectors.filterIndexed { i, _ -> i != index }) {
                val (xDiff, yDiff, zDiff) = vector diffAbs vector2
                val isXNeighbor = xDiff == 0 && yDiff == 0 && zDiff.absoluteValue == 1
                val isYNeighbor = xDiff == 0 && zDiff == 0 && yDiff.absoluteValue == 1
                val isZNeighbor = yDiff == 0 && zDiff == 0 && xDiff.absoluteValue == 1
                if (isXNeighbor || isYNeighbor || isZNeighbor) {
                    uncoveredSides--
                }
            }
            uncoveredSidesTotal += uncoveredSides
        }

        return uncoveredSidesTotal.toLong()
    }

    fun part2(input: List<String>): Long {
        val vectors = parseLava(input)

        var uncoveredSidesTotal = 0
        for ((index, vector) in vectors.withIndex()) {
            var uncoveredSides = 6
            for (vector2 in vectors.filterIndexed { i, _ -> i != index }) {
                val (xDiff, yDiff, zDiff) = vector diffAbs vector2
                val isXNeighbor = xDiff == 0 && yDiff == 0 && zDiff.absoluteValue != 0
                val isYNeighbor = xDiff == 0 && zDiff == 0 && yDiff.absoluteValue != 0
                val isZNeighbor = yDiff == 0 && zDiff == 0 && xDiff.absoluteValue != 0
                if (isXNeighbor || isYNeighbor || isZNeighbor) {
                    uncoveredSides--
                }
            }
            uncoveredSidesTotal += uncoveredSides
        }

        return uncoveredSidesTotal.toLong()
    }

    private fun parseLava(input: List<String>): Set<Vector3d> {
        val vectors = input.map {
            val (x, y, z) = it.split(",")
                .map { numeric -> numeric.toInt() }
            Vector3d(x, y, z)
        }
            .toSet()
        return vectors
    }
}
