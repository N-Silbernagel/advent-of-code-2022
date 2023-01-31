import kotlin.reflect.KClass

fun main() {
    val input = readFileAsList("Day17")
    println(Day17.part1(input))
    println(Day17.part2(input))
}

object Day17 {
    private const val chamberWidth = 7
    private val downMovementVector = Vector2d(0, -1)

    fun part1(input: List<String>): Long {
        val jetMovements = parseJetPattern(input)

        return simulate(jetMovements, 2_022)
    }

    fun part2(input: List<String>): Long {
        val jetMovements = parseJetPattern(input)

        return simulate(jetMovements, 1_000_000_000_000)
    }

    private fun simulate(jetMovements: JetMovements, numberOfRocks: Long): Long {
        val rockSource = RockSource()
        val fallenRocks = LinkedHashSet<Rock>()
        var currentRock = rockSource.next(Vector2d(2, 3))

        val seen = LinkedHashMap<State, Pair<Int, Int>>()
        var currentState = State(MinusRock::class, 0, listOf())
        var cycleStart: State? = null
        seen[currentState] = 0 to 0
        while (fallenRocks.size < numberOfRocks) {
            val (jetDirection, jetIndex) = jetMovements.next()

            if (fallenRocks.contains(currentRock)) {
                val currentHighestPoint = calculateHighestPoint(fallenRocks)
                currentRock = rockSource.next(Vector2d(2, currentHighestPoint + 3))

                val groupBy = fallenRocks.groupBy { it.position.x }
                val sortedByDescending = groupBy
                    .entries
                    .sortedBy { it.key }
                val map = sortedByDescending
                    .map { pointList -> pointList.value.maxBy { point -> point.position.y + point.height } }
                val ceiling = map
                    .let {
                        it.map { point -> currentHighestPoint - (point.position.y + point.height) }
                    }

                currentState = State(currentRock::class, jetIndex, ceiling)
                if (seen.contains(currentState)) {
                    cycleStart = currentState
                    break
                }
                seen[currentState] = fallenRocks.size to currentHighestPoint
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

        if (cycleStart != null) {
            val (rockCountCycleStart, heightBeforeCycle) = seen[cycleStart]!!
            val (rockCountCycleEnd, cycleEndHeight) = seen.values.last()

            val cycleSize = rockCountCycleEnd - rockCountCycleStart + 1

            val cycleHeightDiff = cycleEndHeight - heightBeforeCycle

            val remainingRocks = numberOfRocks - fallenRocks.size

            val cyclesRemaining = remainingRocks / cycleSize
            val remainder = remainingRocks % cycleSize

            val heightTroughCycles = cyclesRemaining * cycleHeightDiff

            val remainderKey = seen.keys.toList()[rockCountCycleStart + remainder.toInt()]
            val (_, remainderHeight) = seen[remainderKey]!!
            val heightThroughRemainderRocks = remainderHeight - heightBeforeCycle

            return cycleEndHeight + heightTroughCycles + heightThroughRemainderRocks
        }

        return calculateHighestPoint(fallenRocks).toLong()
    }

    private fun checkCollisionMany(
        currentRock: Rock,
        fallenRocks: MutableSet<Rock>,
        position: Position2d
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

    private fun calculateHighestPoint(fallenRocks: MutableSet<Rock>): Int {
        val maxByOrNull = fallenRocks.maxByOrNull { it.position.y + it.height } ?: return 0
        return maxByOrNull.position.y + maxByOrNull.height
    }

    private fun parseJetPattern(input: List<String>): JetMovements {
        val jetPattern = input[0]
            .split("")
            .filter { it.isNotBlank() }
            .map { JetDirection.fromString(it) }

        return JetMovements(jetPattern)
    }

    private fun checkCollision(rock: Rock, otherRock: Rock?, movementVector: Position2d): Boolean {
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
        abstract val shape: List<Vector2d>
        abstract val width: Int
        abstract val height: Int
        abstract var position: Position2d
    }

    class MinusRock(override var position: Position2d) : Rock() {
        override val width = 4
        override val height = 1

        override val shape = listOf(
            Vector2d(0, 0),
            Vector2d(1, 0),
            Vector2d(2, 0),
            Vector2d(3, 0),
        )
    }

    class PlusRock(override var position: Position2d) : Rock() {
        override val width = 3
        override val height = 3

        override val shape = listOf(
            Vector2d(1, 0),
            Vector2d(0, 1),
            Vector2d(1, 1),
            Vector2d(2, 1),
            Vector2d(1, 2),
        )
    }

    class LRock(override var position: Position2d) : Rock() {
        override val width = 3
        override val height = 3

        override val shape = listOf(
            Vector2d(0, 0),
            Vector2d(1, 0),
            Vector2d(2, 0),
            Vector2d(2, 1),
            Vector2d(2, 2),
        )
    }

    class IRock(override var position: Position2d) : Rock() {
        override val width = 1
        override val height = 4

        override val shape = listOf(
            Vector2d(0, 0),
            Vector2d(0, 1),
            Vector2d(0, 2),
            Vector2d(0, 3),
        )
    }

    class BlockRock(override var position: Position2d) : Rock() {
        override val width = 2
        override val height = 2

        override val shape = listOf(
            Vector2d(0, 0),
            Vector2d(0, 1),
            Vector2d(1, 0),
            Vector2d(1, 1),
        )
    }

    enum class JetDirection(val vector: Vector2d) {
        LEFT(Vector2d(-1, 0)),
        RIGHT(Vector2d(1, 0));

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

    class RockSource {
        private var index = 0

        fun next(startingPosition: Position2d): Rock {
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

    class JetMovements(private val pattern: List<JetDirection>) : Iterator<JetMovement> {
        private var index = 0

        override fun hasNext(): Boolean {
            return true
        }

        override fun next(): JetMovement {
            val jetDirection = pattern[index]
            index++
            if (index !in pattern.indices) {
                index = 0
            }
            return JetMovement(jetDirection, index)
        }
    }

    data class JetMovement(val direction: JetDirection, val index: Int)

    data class State(val rock: KClass<out Rock>, val jetIndex: Int, val ceiling: List<Int>)
}
