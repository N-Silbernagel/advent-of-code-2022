import java.util.UUID
import kotlin.collections.ArrayDeque

fun main() {
    val input = readFileAsList("Day24")
    println(Day24.part1(input))
    println(Day24.part2(input))
}

object Day24 {
    private val start = Vector2d(2, 1)

    fun part1(input: List<String>): Long {
        val (maxXBlizzard, maxYBlizzard, goal) = calculateMapConstraints(input)

        val blizzardsUnique = findUniqueBlizzards(input, maxXBlizzard, maxYBlizzard)

        val bestPath = findPath(goal, blizzardsUnique, maxXBlizzard, maxYBlizzard, Path(listOf(start)))

        return bestPath.size - 1L
    }

    fun part2(input: List<String>): Long {
        val (maxXBlizzard, maxYBlizzard, goal) = calculateMapConstraints(input)

        val blizzardsUnique = findUniqueBlizzards(input, maxXBlizzard, maxYBlizzard)

        var path = findPath(goal, blizzardsUnique, maxXBlizzard, maxYBlizzard, Path(listOf(start)))

        path = findPath(start, blizzardsUnique, maxXBlizzard, maxYBlizzard, path)

        path = findPath(goal, blizzardsUnique, maxXBlizzard, maxYBlizzard, path)

        return path.size - 1L
    }

    private fun findUniqueBlizzards(
        input: List<String>,
        maxXBlizzard: Int,
        maxYBlizzard: Int
    ): MutableSet<Set<Blizzard>> {
        val blizzards = parseBlizzards(input)

        val blizzardsUnique = mutableSetOf<Set<Blizzard>>(blizzards)
        while (true) {
            val next = blizzardsUnique.last()
                .map { it.next(maxXBlizzard, maxYBlizzard) }
                .toSet()
            if (blizzardsUnique.contains(next)) {
                break
            }
            blizzardsUnique.add(next)
        }
        return blizzardsUnique
    }

    private fun findPath(
        goal: Vector2d,
        blizzardsUnique: MutableSet<Set<Blizzard>>,
        maxXBlizzard: Int,
        maxYBlizzard: Int,
        startPath: Path
    ): Path {
        val queue = ArrayDeque<Path>()
        queue.add(startPath)
        val seenOne = mutableSetOf<Pair<Position2d, Set<Blizzard>>>()
        while (queue.isNotEmpty()) {
            val path = queue.removeFirst()

            val currentPosition = path.positions.last()
            if (currentPosition == goal) {
                return path
            }
            val currentBlizzards = getNextBlizzardPositions(path, blizzardsUnique)

            val positionBlizzardsPair = currentPosition to currentBlizzards
            if (seenOne.contains(positionBlizzardsPair)) {
                continue
            }
            seenOne.add(positionBlizzardsPair)

            val currentBlizzardPositions = currentBlizzards.map { it.position }.toSet()

            val up = currentPosition + Direction.UP.vector
            val right = currentPosition + Direction.RIGHT.vector
            val down = currentPosition + Direction.DOWN.vector
            val left = currentPosition + Direction.LEFT.vector

            val currentIsBlizzard = currentBlizzardPositions.contains(currentPosition)
            val rightIsBlizzard = currentBlizzardPositions.contains(right)
            val downIsBlizzard = currentBlizzardPositions.contains(down)
            val upIsBlizzard = currentBlizzardPositions.contains(up)
            val leftIsBlizzard = currentBlizzardPositions.contains(left)

            val rightIsValid = (!rightIsBlizzard && isValidPosition(right, maxXBlizzard, maxYBlizzard, goal))
            val downIsValid = (!downIsBlizzard && isValidPosition(down, maxXBlizzard, maxYBlizzard, goal))
            val upIsValid = (!upIsBlizzard && isValidPosition(up, maxXBlizzard, maxYBlizzard, goal))
            val leftIsValid = (!leftIsBlizzard && isValidPosition(left, maxXBlizzard, maxYBlizzard, goal))

            if (downIsValid) {
                val newPath = Path(path.positions + down)
                queue.add(newPath)
            }
            if (rightIsValid) {
                val newPath = Path(path.positions + right)
                queue.add(newPath)
            }
            if (upIsValid) {
                val newPath = Path(path.positions + up)
                queue.add(newPath)
            }
            if (leftIsValid) {
                val newPath = Path(path.positions + left)
                queue.add(newPath)
            }
            if (!currentIsBlizzard) {
                queue.add(Path(path.positions + currentPosition))
            }
        }
        throw RuntimeException("Could not find path")
    }

    private fun calculateMapConstraints(input: List<String>): Triple<Int, Int, Vector2d> {
        val maxXBlizzard = input[0].length - 1
        val maxYBlizzard = input.size - 1
        val goal = Vector2d(maxXBlizzard, maxYBlizzard + 1)
        return Triple(maxXBlizzard, maxYBlizzard, goal)
    }

    private fun getNextBlizzardPositions(
        path: Path,
        blizzardsUnique: MutableSet<Set<Blizzard>>
    ): Set<Blizzard> {
        val blizzardsIndex = path.positions.size % blizzardsUnique.size
        return blizzardsUnique.elementAt(blizzardsIndex)
    }

    private fun isValidPosition(position: Position2d, maxXBlizzard: Int, maxYBlizzard: Int, goal: Position2d) =
        isInConstraints(position, maxXBlizzard, maxYBlizzard) || position == goal

    private fun isInConstraints(position: Position2d, maxX: Int, maxY: Int): Boolean {
        return position.x in 2..maxX && position.y in 2..maxY
    }

    private fun parseBlizzards(input: List<String>): MutableSet<Blizzard> {
        val blizzards = mutableSetOf<Blizzard>()
        for ((y, line) in input.withIndex()) {
            for ((x, c) in line.withIndex()) {
                val position = Vector2d(x + 1, y + 1)
                if (c == '^') {
                    val blizzard = Blizzard(position, Direction.UP)
                    blizzards.add(blizzard)
                }
                if (c == '>') {
                    val blizzard = Blizzard(position, Direction.RIGHT)
                    blizzards.add(blizzard)
                }
                if (c == 'v') {
                    val blizzard = Blizzard(position, Direction.DOWN)
                    blizzards.add(blizzard)
                }
                if (c == '<') {
                    val blizzard = Blizzard(position, Direction.LEFT)
                    blizzards.add(blizzard)
                }
            }
        }
        return blizzards
    }

    data class Blizzard(val position: Position2d, val direction: Direction, val id: UUID = UUID.randomUUID()) : Position2d by position {
        fun next(maxXBlizzard: Int, maxYBlizzard: Int): Blizzard {
            var nextPosition = position + direction.vector

            if (nextPosition.x > maxXBlizzard) {
                nextPosition = Vector2d(2, nextPosition.y)
            }
            if (nextPosition.x < 2) {
                nextPosition = Vector2d(maxXBlizzard, nextPosition.y)
            }
            if (nextPosition.y > maxYBlizzard) {
                nextPosition = Vector2d(nextPosition.x, 2)
            }
            if (nextPosition.y < 2) {
                nextPosition = Vector2d(nextPosition.x, maxYBlizzard)
            }

            return this.copy(position = nextPosition)
        }
    }

    enum class Direction(val vector: Vector2d) {
        UP(Vector2d(0, -1)),
        RIGHT(Vector2d(1, 0)),
        DOWN(Vector2d(0, 1)),
        LEFT(Vector2d(-1, 0))
    }

    data class Path(val positions: List<Position2d>): List<Position2d> by positions
}
