import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val input = readFileAsList("Day23")
    println(Day23.part1(input))
    println(Day23.part2(input))
}

object Day23 {
    private val directions: List<Direction> = listOf(Direction.N, Direction.S, Direction.W, Direction.E)

    fun part1(input: List<String>): Int {
        val grove = mapInput(input)

        for (i in 0 until 10) {
            val newElves = move(grove, i)

            grove.elves = newElves
        }

        val elvesWidth = grove.maxElfX - grove.minElfX + 1
        val elvesHeight = grove.maxElfY - grove.minElfY + 1

        val elvesRect = elvesWidth * elvesHeight

        return elvesRect - grove.elves.count()
    }

    fun part2(input: List<String>): Int {
        val grove = mapInput(input)

        var i = 0
        while (true) {
            val newElves = move(grove, i)

            i++

            if (newElves == grove.elves) {
                return i
            }

            grove.elves = newElves
        }
    }

    private fun mapInput(input: List<String>): Grove {
        val elves = input.flatMapIndexed { y, line ->
            line.mapIndexedNotNull() { x, c ->
                if (c == '.') {
                    return@mapIndexedNotNull null
                }
                Vector2d(x, y)
            }
        }.toSet()

        return Grove(input.size, elves)
    }

    private fun move(grove: Grove, i: Int): MutableSet<Vector2d> {
        val roundDirections = ArrayList(directions)
        Collections.rotate(roundDirections, -1 * i)
        val elfProposals = mutableMapOf<Vector2d, Vector2d>()
        grove.elves.forEach { elf ->
            val noElvesAround = Direction.values().map {
                elf + it.offset
            }.all {
                !grove.elves.contains(it)
            }

            if (noElvesAround) {
                elfProposals[elf] = elf
                return@forEach
            }

            val roundDir = roundDirections.firstOrNull { potentialDirection ->
                val next = dirGroups[potentialDirection]!!.map { groupDir -> elf + groupDir.offset }
                next.all { !grove.elves.contains(it) }
            }

            if (roundDir == null) {
                // no possible moves, stay where you are
                elfProposals[elf] = elf
                return@forEach
            }

            val proposal = elf + roundDir.offset
            if (elfProposals.contains(proposal)) {
                // another elf wants to go to that position, both stay where they are
                elfProposals[elf] = elf
                val originalFirstProposingElf = elfProposals[proposal]!!
                elfProposals[originalFirstProposingElf] = originalFirstProposingElf
                elfProposals.remove(proposal)
            } else {
                // propose to move to new position
                elfProposals[proposal] = elf
            }
        }

        return elfProposals.keys
    }

    private data class Grove(val size: Int, var elves: Set<Vector2d>) {
        val minElfX: Int
            get() = elves.minOf { it.x }
        val maxElfX: Int
            get() = elves.maxOf { it.x }
        val minElfY: Int
            get() = elves.minOf { it.y }
        val maxElfY: Int
            get() = elves.maxOf { it.y }
    }

    private enum class Direction(val offset: Vector2d) {
        N(Vector2d(0, -1)),
        NE(Vector2d(1, -1)),
        NW(Vector2d(-1, -1)),
        S(Vector2d(0, 1)),
        SE(Vector2d(1, 1)),
        SW(Vector2d(-1, 1)),
        W(Vector2d(-1, 0)),
        E(Vector2d(1, 0)),
    }

    private val dirGroups: Map<Direction, List<Direction>> = mapOf(
        Direction.N to listOf(Direction.N, Direction.NW, Direction.NE),
        Direction.S to listOf(Direction.S, Direction.SW, Direction.SE),
        Direction.W to listOf(Direction.W, Direction.NW, Direction.SW),
        Direction.E to listOf(Direction.E, Direction.SE, Direction.NE),
    )
}
