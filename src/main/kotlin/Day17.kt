fun main() {
    val input = readFileAsList("Day17")
    println(Day17.part1(input))
    //println(Day17.part2(input))
}

object Day17 {
    private const val chamberWidth = 7
    private val downMovementVector = Vector(0, -1)

    fun part1(input: List<String>): Long {
        val jetMovements = parseJetPattern(input)

        return simulate(jetMovements, 2_022, input[0].length)
    }

    fun part2(input: List<String>): Long {
        val jetMovements = parseJetPattern(input)

        return simulate(jetMovements, 1_000_000_000_000, input[0].length)
    }

    private fun simulate(jetMovements: JetMovements, numberOfRocks: Long, length: Int): Long {

        val fallenRocks = LinkedHashSet<Rock>()
        var currentRock = RockSource.next(Vector(2, 3))

        val seen = LinkedHashMap<Pair<Int, Int>, Int>()
        var currentRockStreamCombo = -1 to -1
        var jetStreams = 0
        var cylceStart: Pair<Int, Int>? = null
        seen[currentRockStreamCombo] = 0
        while (fallenRocks.size < numberOfRocks) {
            val jetDirection = jetMovements.next()
            jetStreams++

            if (fallenRocks.contains(currentRock)) {
                val currentHighestPoint = calculateHighestPoint(fallenRocks)
                currentRock = RockSource.next(Vector(2, currentHighestPoint + 3))

                currentRockStreamCombo = fallenRocks.size % 5 to jetStreams % length
                if (seen.contains(currentRockStreamCombo)) {
                    cylceStart = currentRockStreamCombo
                    break
                }
                seen[currentRockStreamCombo] = currentHighestPoint
            }

            val isCollisionVertical = checkCollisionMany(currentRock, fallenRocks, jetDirection.vector)

            if (!isCollisionVertical && jetDirection == JetDirection.LEFT && currentRock.position.x > 0) {
                currentRock.position = currentRock.position + jetDirection.vector
            } else if (!isCollisionVertical && jetDirection == JetDirection.RIGHT && (currentRock.position.x + currentRock.width) < chamberWidth) {
                currentRock.position = currentRock.position + jetDirection.vector
            }

            var isCollisionDown = checkCollision(currentRock, null, downMovementVector)
            if (!isCollisionDown) {
                isCollisionDown = checkCollisionMany(currentRock, fallenRocks, downMovementVector)
            }

            if (isCollisionDown) {
                fallenRocks.add(currentRock)
                continue
            }

            currentRock.position = currentRock.position + downMovementVector
        }

        if (cylceStart != null) {
            val indexOfCycleStart = seen.keys.indexOf(cylceStart)
            val indexOfCycleEnd = seen.keys.indexOf(seen.keys.last())
            val cycleSize = indexOfCycleEnd - indexOfCycleStart + 1

            val keyBeforeCycle = seen.keys.toList()[indexOfCycleStart - 1]
            val heightBeforeCycle = seen[keyBeforeCycle]!!

            val cycleEndHeight = seen.values.last()

            val cycleHeightDiff = cycleEndHeight - heightBeforeCycle

            val remainingRocks = numberOfRocks - fallenRocks.size

            val cyclesRemaining = remainingRocks / cycleSize
            val rest = remainingRocks % cycleSize

            val currentHeight = calculateHighestPoint(fallenRocks)
            val heightTroughCycles = cyclesRemaining * cycleHeightDiff

            val restKey = seen.keys.toList()[indexOfCycleStart + rest.toInt()]
            val restHeight = seen[restKey]!!
            val heightThroughRestRocks = restHeight - heightBeforeCycle

            val joinedHeight = currentHeight + heightTroughCycles + heightThroughRestRocks
            return joinedHeight
        }

        return calculateHighestPoint(fallenRocks).toLong()
    }

    private fun checkCollisionMany(
        currentRock: Rock,
        fallenRocks: MutableSet<Rock>,
        position: Position
    ): Boolean {
        var isCollision = false
        for (fallenRock in fallenRocks.reversed()) {
            isCollision = checkCollision(currentRock, fallenRock, position)
            if (isCollision) {
                break
            }
        }
        return isCollision
    }

    private fun calculateHighestPoint(fallenRocks: MutableSet<Rock>) =
        fallenRocks.map { it.position.y + it.height }.maxByOrNull { it } ?: 0

    private fun parseJetPattern(input: List<String>): JetMovements {
        val jetPattern = input[0]
            .split("")
            .filter { it.isNotBlank() }
            .map { JetDirection.fromString(it) }

        return JetMovements(jetPattern)
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

    abstract class Rock {
        abstract val shape: List<Vector>
        abstract val width: Int
        abstract val height: Int
        abstract var position: Position
    }

    class MinusRock(override var position: Position) : Rock() {
        override val width = 4
        override val height = 1

        override val shape = listOf(
            Vector(0, 0),
            Vector(1, 0),
            Vector(2, 0),
            Vector(3, 0),
        )
    }

    class PlusRock(override var position: Position) : Rock() {
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

    class LRock(override var position: Position) : Rock() {
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

    class IRock(override var position: Position) : Rock() {
        override val width = 1
        override val height = 4

        override val shape = listOf(
            Vector(0, 0),
            Vector(0, 1),
            Vector(0, 2),
            Vector(0, 3),
        )
    }

    class BlockRock(override var position: Position) : Rock() {
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
