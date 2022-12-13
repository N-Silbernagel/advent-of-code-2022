import java.util.TreeSet

fun main() {
    val input = readFileAsList("Day13")
    println(Day13.part1(input))
    println(Day13.part2(input))
}

object Day13 {
    private fun makePairs(input: List<String>): List<Pair<Packet, Packet>> {
        return input.chunked(3)
            .map { (left, right) -> Packet.from(left) to Packet.from(right) }
    }

    fun part1(input: List<String>): Int {
        val pairs = makePairs(input)

        return pairs.mapIndexed { i, (left, right) -> if (left compareTo right < 0) i + 1 else 0 }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val pairs = makePairs(input)

        val firstDivider = Packet.from("[[2]]")
        val secondDivider = Packet.from("[[6]]")


        val sortedPackets = TreeSet(Packet::compareTo)
        sortedPackets.addAll(pairs.flatMap { it.toList() })
        sortedPackets.add(firstDivider)
        sortedPackets.add(secondDivider)
        return (sortedPackets.indexOf(firstDivider) + 1) * (sortedPackets.indexOf(secondDivider) + 1)
    }

    private sealed class Value {
        abstract infix fun compareTo(other: Value): Int

        companion object {
            fun from(definition: String): Value {
                return when (val value = definition.toIntOrNull()) {
                    null -> Packet.from(definition)
                    else -> Number(value)
                }
            }
        }
    }

    private data class Number(val value: Int) : Value() {
        override infix fun compareTo(other: Value): Int {
            return when (other) {
                is Number -> when {
                    value < other.value -> -1
                    value > other.value -> 1
                    else -> 0
                }
                is Packet -> Packet(listOf(this)) compareTo other
            }
        }
    }

    private data class Packet(val children: List<Value>) : Value() {
        override infix fun compareTo(other: Value): Int {
            return when (other) {
                is Number -> this compareTo Packet(listOf(other))
                is Packet -> {
                    children.zip(other.children)
                        .map { it.first compareTo it.second }
                        .filterNot { it == 0 }
                        .firstOrNull() ?: (children.size compareTo other.children.size)
                }
            }
        }

        companion object {
            fun from(definition: String): Packet {
                val inside = definition.drop(1)
                    .dropLast(1)
                if (inside.isEmpty()) return Packet(emptyList())

                val children = buildList {
                    var current = ""
                    var brackets = 0
                    for (c in inside) {
                        if (c == '[') brackets++
                        if (c == ']') brackets--
                        if (c == ',' && brackets == 0) {
                            add(Value.from(current))
                            current = ""
                            continue
                        }
                        current += c
                    }
                    add(Value.from(current))
                }

                return Packet(children)
            }
        }
    }
}
