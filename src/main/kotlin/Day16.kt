fun main() {
    val input = readFileAsList("Day16")
    println(Day16.part1(input))
    println(Day16.part2(input))
}

object Day16 {
    fun part1(input: List<String>): Int {
        val valves = parseValves(input)
        val usefulValves = valves.values
            .filter { it.flowRate > 0 }
            .toSet()

        val distances = computeDistances(valves)

        return calculateScore(30, valves.getValue("AA"), usefulValves, distances, valves = valves)
    }

    fun part2(input: List<String>): Int {
        val valves = parseValves(input)
        val usefulValves = valves.values
            .filter { it.flowRate > 0 }
            .toSet()

        val distances = computeDistances(valves)

        return calculateScore(26, valves.getValue("AA"), usefulValves, distances, myTurn = true, valves = valves)

    }

    private fun calculateScore(
        minutes: Int,
        current: Valve,
        remaining: Set<Valve>,
        distances: Map<Valve, Map<Valve, Int>>,
        cache: MutableMap<State, Int> = mutableMapOf(),
        myTurn: Boolean = false,
        valves: Map<String, Valve>
    ): Int {
        val currentScore = minutes * current.flowRate
        val currentState = State(current, minutes, remaining)

        return currentScore + cache.getOrPut(currentState) {
            val maxCurrent = remaining
                .filter { next -> distances[current]!![next]!! < minutes }
                .takeIf { it.isNotEmpty() }
                ?.maxOf { next ->
                    val remainingMinutes = minutes - 1 - distances[current]!![next]!!
                    calculateScore(remainingMinutes, next, remaining - next, distances, cache, myTurn, valves)
                }
                ?: 0
            maxOf(maxCurrent, if (myTurn) calculateScore(26, valves.getValue("AA"), remaining, distances, valves = valves) else 0)
        }
    }

    private fun computeDistances(valves: Map<String, Valve>): Map<Valve, Map<Valve, Int>> {
        return valves.values.map { valve ->
            computeDistance(valve, valves)
        }.associateBy { it.keys.first() }
    }

    private fun computeDistance(
        valve: Valve,
        valves: Map<String, Valve>
    ): MutableMap<Valve, Int> {
        val distances = mutableMapOf<Valve, Int>().withDefault { Int.MAX_VALUE }.apply { put(valve, 0) }
        val toVisit = mutableListOf(valve)
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            valves[current.id]!!.tunnelsTo.forEach { neighbour ->
                val newDistance = distances[current]!! + 1
                if (newDistance < distances.getValue(neighbour)) {
                    distances[neighbour] = newDistance
                    toVisit.add(neighbour)
                }
            }
        }
        return distances
    }

    private fun parseValves(input: List<String>): LinkedHashMap<String, Valve> {
        val valves = LinkedHashMap<String, Valve>()

        for (line in input) {
            val id = line.substringAfter("Valve ")
                .substringBefore(" has")
            val flowRate = line.substringAfter("rate=")
                .substringBefore(";")
                .toInt()

            val valve = Valve(id, flowRate)
            valves[id] = valve
        }

        for (line in input) {
            val id = line.substringAfter("Valve ")
                .substringBefore(" has")

            val tunnelsText = line.substringAfter("valve")
                .substringAfter(" ")

            val tunnelsToValves = tunnelsText
                .split(", ")
                .map { valves.getValue(it) }

            valves.getValue(id).tunnelsTo = tunnelsToValves
        }
        return valves
    }


    private data class State(val current: Valve, val minutes: Int, val opened: Set<Valve>)

    data class Valve(
        val id: String,
        val flowRate: Int,
        var tunnelsTo: List<Valve> = listOf(),
        var isOpen: Boolean = false
    ) {
        override fun hashCode(): Int {
            return id.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Valve

            if (id != other.id) return false

            return true
        }

        override fun toString(): String {
            return id
        }
    }}
