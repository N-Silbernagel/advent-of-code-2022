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

        val xRange = rangeOf(vectors) { it.x }
        val yRange = rangeOf(vectors) { it.y }
        val zRange = rangeOf(vectors) { it.z }

        val queue = ArrayDeque<Vector3d>().apply {
            add(Vector3d(xRange.first, yRange.first, zRange.first))
        }
        val seen = mutableSetOf<Vector3d>()
        var sidesFound = 0
        queue.forEach { lookNext ->
            if (lookNext !in seen) {
                lookNext.neighbours()
                    .filter { it !in seen && it.x in xRange && it.y in yRange && it.z in zRange }
                    .forEach { neighbor ->
                        seen += lookNext
                        if (neighbor in vectors) sidesFound++
                        else queue.add(neighbor)
                    }
            }
        }
        return sidesFound.toLong()
    }

    private fun rangeOf(lava: Set<Vector3d>, function: (Vector3d) -> Int): IntRange =
        lava.minOf(function) - 1..lava.maxOf(function) + 1

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
