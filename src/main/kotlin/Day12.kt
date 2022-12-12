
fun main() {
    val input = readFileAsList("Day12")
    println(Day12.part1(input))
    println(Day12.part2(input))
}

typealias Elevation = Char

object Day12 {
    fun part1(input: List<String>): Int {
        return findBestPath(input, 'S')
    }

    fun part2(input: List<String>): Int {
        return findBestPath(input, 'a')
    }

    private fun findBestPath(input: List<String>, target: Elevation): Int {
        val grid = input.flatMapIndexed { y, row -> row.mapIndexed { x, char -> Position(x, y) to char } }
            .toMap()

        val startingPoint = grid.entries.first { it.value == 'E' }.key
        val distances = grid.keys.associateWith { Int.MAX_VALUE }.toMutableMap()
        distances[startingPoint] = 0

        val toVisit = mutableListOf(startingPoint)

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            current.validNeighbours(grid)
                .forEach { neighbour ->
                    val newDistance = distances[current]!! + 1

                    if (grid[neighbour] == target) return newDistance

                    if (newDistance < distances[neighbour]!!) {
                        distances[neighbour] = newDistance
                        toVisit.add(neighbour)
                    }
                }
        }

        error("")
    }

    data class Position(
        val x: Int,
        val y: Int,
    ) {
        fun validNeighbours(grid: Map<Position, Elevation>) = neighbours().filter { neighbour ->
            neighbour in grid && grid[neighbour]!!.value - grid[this]!!.value >= -1
        }

        private fun neighbours(): List<Position> {
            return arrayOf((-1 to 0), (1 to 0), (0 to -1), (0 to 1))
                .map { (dx, dy) -> Position(x + dx, y + dy) }
        }
    }

    private val Elevation.value: Int
        get() = when (this) {
            'S' -> 'a'.code
            'E' -> 'z'.code
            else -> this.code
        }
}
