import kotlin.math.abs
import kotlin.math.sign

fun main() {
    val input = readFileAsList("Day09")
    println(Day09.part1(input))
    println(Day09.part2(input))
}

object Day09 {
    fun part1(input: List<String>): Int {
        return solve(input, 2)
    }

    fun part2(input: List<String>): Int {
        return solve(input, 10)
    }

    private fun solve(input: List<String>, numberOfKnots: Int): Int {
        val directions = mapOf(
            "R" to Vector(1, 0),
            "U" to Vector(0, 1),
            "L" to Vector(-1, 0),
            "D" to Vector(0, -1),
        )

        val visited = HashSet<Vector>()
        val knots = MutableList(numberOfKnots) { Vector(0, 0) }

        visited.add(Vector(0, 0))

        for (instruction in input) {
            if (instruction.isBlank()) {
                continue
            }
            val splitInstruction = instruction.split(" ")
            val direction = splitInstruction[0]
            val distance = splitInstruction[1].toInt()

            val directionVector = directions[direction] ?: continue

            repeat(distance) {
                knots[0] += directionVector
                val headAndTailPairs = knots.indices.windowed(2)
                for ((headIndex, tailIndex) in headAndTailPairs) {
                    if (!(knots[tailIndex] touches knots[headIndex])) {
                        knots[tailIndex] = knots[tailIndex] moveBy knots[headIndex]
                    }
                }
                visited.add(knots.last())
            }
        }

        return visited.size
    }

    data class Vector(var x: Int, var y: Int) {
        infix fun touches(other: Vector): Boolean {
            return abs(this.x - other.x) < 2 && abs(this.y - other.y) < 2
        }

        operator fun plus(other: Vector): Vector {
            return Vector(x + other.x, y + other.y)
        }

        infix fun moveBy(other: Vector): Vector {
            return this + Vector((other.x - x).sign, (other.y - y).sign)
        }
    }
}
