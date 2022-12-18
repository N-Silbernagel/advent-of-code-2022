fun main() {
    val input = readFileAsList("Day17")
    println(Day17.part1(input))
    println(Day17.part2(input))
}

object Day17 {
    private const val chamberWidth = 7
    private val downMovementVector = Vector(0, -1)

    fun part1(input: List<String>): Int {
        val jetMovements = parseJetPattern(input)

        val fallenRocks = LinkedHashSet<Rock>()
        var currentRock = RockSource.next(Vector(2, 3))
        while (fallenRocks.size < 2022) {
            if (fallenRocks.contains(currentRock)) {
                val currentHighestPoint = calculateHighestPoint(fallenRocks)
                currentRock = RockSource.next(Vector(2, currentHighestPoint + 3))
                println(currentRock.javaClass)
            }

            val jetDirection = jetMovements.next()
            val isCollisionVertical = checkCollisionMany(fallenRocks, currentRock, jetDirection.vector)

            if (!isCollisionVertical && jetDirection == JetDirection.LEFT && currentRock.position.x > 0) {
                currentRock.position = currentRock.position + jetDirection.vector
            }
            else if (!isCollisionVertical && jetDirection == JetDirection.RIGHT && (currentRock.position.x + currentRock.width) < chamberWidth) {
                currentRock.position = currentRock.position + jetDirection.vector
            }

            var isCollisionDown = checkCollision(currentRock, null, downMovementVector)
            if (!isCollisionDown) {
                isCollisionDown = checkCollisionMany(fallenRocks, currentRock, downMovementVector)
            }

            if (!isCollisionDown) {
                currentRock.position = currentRock.position + downMovementVector
            } else {
                fallenRocks.add(currentRock)
            }
        }

        return calculateHighestPoint(fallenRocks)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    private fun checkCollisionMany(
        fallenRocks: LinkedHashSet<Rock>,
        currentRock: Rock,
        position: Position
    ): Boolean {
        var isCollision = false
        for (fallenRock in fallenRocks) {
            isCollision = checkCollision(currentRock, fallenRock, position)
            if (isCollision) {
                break
            }
        }
        return isCollision
    }

    private fun calculateHighestPoint(fallenRocks: LinkedHashSet<Rock>) =
        fallenRocks.map { it.position.y + it.height }.maxByOrNull { it } ?: 0

    private fun parseJetPattern(input: List<String>): JetMovements {
        val jetPattern = input[0]
            .split("")
            .filter { it.isNotBlank() }
            .map { JetDirection.fromString(it) }

        val jetMovements = JetMovements(jetPattern)
        return jetMovements
    }

    private fun checkCollision(rock: Rock, otherRock: Rock?, movementVector: Position): Boolean {
        val nextRockVector = rock.position + movementVector

        if (nextRockVector.y < 0) {
            return true
        }

        if (otherRock == null) {
            return false
        }

        for (rockShapeVector in rock.shape) {
            for (otherRockShapeVector in otherRock.shape) {
                val absoluteRockShapeVector = nextRockVector + rockShapeVector
                val absoluteOtherRockShapeVector = otherRock.position + otherRockShapeVector

                if (absoluteRockShapeVector distanceTo absoluteOtherRockShapeVector == 0) {
                    return true
                }
            }
        }

        return false
    }

    interface Rock {
        val shape: List<Vector>
        val width: Int
        val height: Int
        var position: Position
    }

    class MinusRock(override var position: Position) : Rock {
        override val width = 4
        override val height = 1

        override val shape = listOf(
            Vector(0, 0),
            Vector(1, 0),
            Vector(2, 0),
            Vector(3, 0),
        )
    }

    class PlusRock(override var position: Position) : Rock {
        override val width = 3
        override val height = 3

        override val shape = listOf(
            Vector(1, 0),
            Vector(0, 1),
            Vector(1, 1),
            Vector(2, 1),
            Vector(1, 2),
        )
    }

    class LRock(override var position: Position) : Rock {
        override val width = 3
        override val height = 3

        override val shape = listOf(
            Vector(0, 0),
            Vector(1, 0),
            Vector(2, 0),
            Vector(2, 1),
            Vector(2, 2),
        )
    }

    class IRock(override var position: Position) : Rock {
        override val width = 1
        override val height = 4

        override val shape = listOf(
            Vector(0, 0),
            Vector(0, 1),
            Vector(0, 2),
            Vector(0, 3),
        )
    }

    class BlockRock(override var position: Position) : Rock {
        override val width = 2
        override val height = 2

        override val shape = listOf(
            Vector(0, 0),
            Vector(0, 1),
            Vector(1, 0),
            Vector(1, 1),
        )
    }

    enum class JetDirection(val vector: Vector) {
        LEFT(Vector(-1, 0)),
        RIGHT(Vector(1, 0));

        companion object {
            fun fromString(s: String): JetDirection {
                if (s == "<") {
                    return LEFT
                }

                if (s == ">") {
                    return RIGHT
                }

                throw RuntimeException()
            }
        }
    }

    object RockSource {
        private var index = 0

        fun next(startingPosition: Position): Rock {
            index++
            if (index % 5 == 1) {
                return MinusRock(startingPosition)
            }

            if (index % 5 == 2) {
                return PlusRock(startingPosition)
            }

            if (index % 5 == 3) {
                return LRock(startingPosition)
            }

            if (index % 5 == 4) {
                return IRock(startingPosition)
            }

            if (index % 5 == 0) {
                return BlockRock(startingPosition)
            }

            throw RuntimeException()
        }
    }

    class JetMovements(private val pattern: List<JetDirection>) : Iterator<JetDirection> {
        private var index = 0

        override fun hasNext(): Boolean {
            return true
        }

        override fun next(): JetDirection {
            val jetDirection = pattern[index]
            index++
            if (index !in pattern.indices) {
                index = 0
            }
            return jetDirection
        }
    }
}
