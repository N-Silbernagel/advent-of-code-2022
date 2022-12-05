import java.util.*

fun main() {
    fun parseCrates(
        stackChunks: List<String>,
        stacks: TreeMap<Int, Stack<Char>>
    ): Boolean {
        for ((index, stackChunk) in stackChunks.withIndex()) {
            if (stackChunk.trim() == "1") {
                return true
            }

            if (stackChunk.isBlank()) {
                continue
            }

            if (!stacks.containsKey(index + 1)) {
                stacks[index + 1] = Stack<Char>()
            }

            val stackChar = stackChunk.trim('[', ']', ' ')[0]

            stacks[index + 1]?.add(0, stackChar)
        }

        return false
    }

    fun part1(input: List<String>): String {
        var isInstructionsPart = false

        val stacks = TreeMap<Int, Stack<Char>>()
        for (line in input) {
            if (line.isBlank()) {
                continue
            }

            val stackChunks = line.chunked(4)

            if (!isInstructionsPart) {
                isInstructionsPart = parseCrates(stackChunks, stacks)
            } else {
                val instructionRegex = Regex("move (\\d+) from (\\d+) to (\\d+)")
                val match = instructionRegex.find(line) ?: continue
                val crateCount = match.groupValues[1].toInt()
                val originStack = match.groupValues[2].toInt()
                val destinationStack = match.groupValues[3].toInt()

                for (i in 1..crateCount) {
                    val poppedCrate = stacks[originStack]!!.pop()
                    stacks[destinationStack]!!.add(poppedCrate)
                }
            }
        }

        var answer = ""
        for (stack in stacks.values) {
            answer += stack.pop()
        }

        return answer
    }

    fun part2(input: List<String>): String {
        var isInstructionsPart = false

        val stacks = TreeMap<Int, Stack<Char>>()
        for (line in input) {
            if (line.isBlank()) {
                continue
            }

            val stackChunks = line.chunked(4)

            if (!isInstructionsPart) {
                isInstructionsPart = parseCrates(stackChunks, stacks)
            } else {
                val instructionRegex = Regex("move (\\d+) from (\\d+) to (\\d+)")
                val match = instructionRegex.find(line) ?: continue
                val crateCount = match.groupValues[1].toInt()
                val originStackIndex = match.groupValues[2].toInt()
                val destinationStackIndex = match.groupValues[3].toInt()

                val crates = Stack<Char>()

                val originStack = stacks[originStackIndex]!!

                for (i in 1..crateCount) {
                    crates.add(0, originStack.pop())
                }

                stacks[destinationStackIndex]!!.addAll(crates)
            }
        }

        var answer = ""
        for (stack in stacks.values) {
            answer += stack.pop()
        }

        return answer
    }

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))

}
