fun main() {
    val input = readFileAsList("Day22")
    println(Day22.part1(input))
    println(Day22.part2(input))
}

object Day22 {
    fun part1(input: List<String>): Int {
        val (grid, instructions) = parseInput(input)

        return followInstructions(instructions, grid) { p,d -> wrapFlat(grid, p, d) }
    }

    fun part2(input: List<String>): Int {
        val (grid, instructions) = parseInput(input)

        return followInstructions(instructions, grid) { p,d -> wrapCube(p, d) }
    }

    private fun parseInput(input: List<String>): Pair<Map<Vector2d, Char>, List<Instruction>> {
        val grid = input.dropLast(2)
            .flatMapIndexed { y, line ->
                line.mapIndexedNotNull { x, c ->
                    if (c == ' ') null else Vector2d(
                        x,
                        y
                    ) to c
                }
            }
            .associate { it }
        val instructions = Instruction.allFrom(input.last())
        return Pair(grid, instructions)
    }

    private sealed class Instruction {
        companion object {
            val pattern = """\d+|[LR]""".toRegex()

            fun allFrom(line: String): List<Instruction> {
                return pattern.findAll(line).map {
                    when (it.value) {
                        "L" -> Left
                        "R" -> Right
                        else -> Move(it.value.toInt())
                    }
                }.toList()
            }
        }

        object Left : Instruction()
        object Right : Instruction()
        data class Move(val steps: Int) : Instruction()
    }

    private fun followInstructions(
        instructions: List<Instruction>,
        grid: Map<Vector2d, Char>,
        wrap: (Vector2d, Dir) -> Pair<Vector2d, Dir>
    ): Int {
        var position = Vector2d(grid.keys.filter { it.y == 0 }.minOf { it.x }, 0)
        var dir = Dir.EAST

        instructions.forEach { instruction ->
            when (instruction) {
                is Instruction.Left -> dir = dir.left()
                is Instruction.Right -> dir = dir.right()
                is Instruction.Move -> generateSequence(position to dir) { (p, d) ->
                    val next = p + d.offset
                    when {
                        next in grid && grid[next] == '#' -> p to d
                        next !in grid -> {
                            val (wrapped, wrappedDir) = wrap(p, d)
                            if (grid[wrapped] == '.') wrapped to wrappedDir else p to d
                        }
                        else -> next to d
                    }
                }.take(instruction.steps + 1).last().let { (p, d) -> position = p; dir = d }
            }
        }

        return 1000 * (position.y + 1) + 4 * (position.x + 1) + dir.score
    }

    private enum class Dir(
        val left: () -> Dir,
        val right: () -> Dir,
        val offset: Vector2d,
        val score: Int
    ) {
        NORTH(left = { WEST }, right = { EAST }, offset = Vector2d(0, -1), score = 3),
        EAST(left = { NORTH }, right = { SOUTH }, offset = Vector2d(1, 0), score = 0),
        SOUTH(left = { EAST }, right = { WEST }, offset = Vector2d(0, 1), score = 1),
        WEST(left = { SOUTH }, right = { NORTH }, offset = Vector2d(-1, 0), score = 2)
    }

    private fun wrapFlat(grid: Map<Vector2d, Char>, position: Vector2d, dir: Dir): Pair<Vector2d, Dir> {
        val rotatedDir = dir.right().right()
        return generateSequence(position) { it + rotatedDir.offset }.takeWhile { it in grid }.last() to dir
    }

    private fun wrapCube(position: Vector2d, dir: Dir): Pair<Vector2d, Dir> {
        return when (Triple(dir, position.x / 50, position.y / 50)) {
            Triple(Dir.NORTH, 1, 0) -> Vector2d(0, 100 + position.x) to Dir.EAST // 1 -> N
            Triple(Dir.WEST, 1, 0) -> Vector2d(0, 149 - position.y) to Dir.EAST // 1 -> W
            Triple(Dir.NORTH, 2, 0) -> Vector2d(position.x - 100, 199) to Dir.NORTH // 2 -> N
            Triple(Dir.EAST, 2, 0) -> Vector2d(99, 149 - position.y) to Dir.WEST // 2 -> E
            Triple(Dir.SOUTH, 2, 0) -> Vector2d(99, -50 + position.x) to Dir.WEST // 2 -> S
            Triple(Dir.EAST, 1, 1) -> Vector2d(50 + position.y, 49) to Dir.NORTH // 3 -> E
            Triple(Dir.WEST, 1, 1) -> Vector2d(position.y - 50, 100) to Dir.SOUTH // 3 -> W
            Triple(Dir.NORTH, 0, 2) -> Vector2d(50, position.x + 50) to Dir.EAST // 4 -> N
            Triple(Dir.WEST, 0, 2) -> Vector2d(50, 149 - position.y) to Dir.EAST // 4 -> W
            Triple(Dir.EAST, 1, 2) -> Vector2d(149, 149 - position.y) to Dir.WEST // 5 -> E
            Triple(Dir.SOUTH, 1, 2) -> Vector2d(49, 100 + position.x) to Dir.WEST // 5 -> S
            Triple(Dir.EAST, 0, 3) -> Vector2d(position.y - 100, 149) to Dir.NORTH // 6 -> E
            Triple(Dir.SOUTH, 0, 3) -> Vector2d(position.x + 100, 0) to Dir.SOUTH // 6 -> S
            Triple(Dir.WEST, 0, 3) -> Vector2d(position.y - 100, 0) to Dir.SOUTH // 6 -> W
            else -> error("Invalid state")
        }
    }
}

