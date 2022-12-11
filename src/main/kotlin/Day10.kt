fun main() {
    val input = readFileAsList("Day10")
    println(Day10.part1(input))
    println(Day10.part2(input))
}

object Day10 {
    fun part1(input: List<String>): Int {
        var cycles = 1
        var x = 1

        var signalStrength = 0

        for (instruction in input) {
            if (instruction.isBlank()) {
                continue
            }

            val isNoop = isNoopInstruction(instruction)

            signalStrength = probeSignalStrength(cycles, signalStrength, x)
            cycles++

            if (!isNoop) {
                signalStrength = probeSignalStrength(cycles, signalStrength, x)
                cycles++

                val v = instruction.substringAfter(" ")
                    .toInt()
                x += v
            }
        }
        return signalStrength
    }

    fun part2(input: List<String>): String {
        var cycles = 1
        var x = 1

        var output = ""

        for (instruction in input) {
            if (instruction.isBlank()) {
                continue
            }

            val isNoop = isNoopInstruction(instruction)

            output += nextChar(cycles, x)
            cycles++

            if (!isNoop) {
                output += nextChar(cycles, x)
                cycles++

                val v = instruction.substringAfter(" ")
                    .toInt()
                x += v
            }

        }

        println(output)
        return output
    }

    private fun isNoopInstruction(instruction: String): Boolean {
        var isNoop = true
        if (instruction.startsWith("addx")) {
            isNoop = false
        }
        return isNoop
    }

    private fun probeSignalStrength(cycles: Int, signalStrength: Int, x: Int): Int {
        var signalStrength1 = signalStrength
        if (cycles == 20) {
            signalStrength1 += 20 * x
        }

        if (cycles == 60) {
            signalStrength1 += 60 * x
        }

        if (cycles == 100) {
            signalStrength1 += 100 * x
        }

        if (cycles == 140) {
            signalStrength1 += 140 * x
        }

        if (cycles == 180) {
            signalStrength1 += 180 * x
        }

        if (cycles == 220) {
            signalStrength1 += 220 * x
        }
        return signalStrength1
    }

    private fun nextChar(cycles: Int, x: Int): String {
        var next = ""
        val cycles2 = cycles - 40 * ((cycles - 1) / 40)
        if (cycles % 40 == 1 && cycles != 1) {
            next += System.lineSeparator()
        }
        if (isSpritePixel(cycles2, x)) {
            next += "#"
        } else {
            next += "."
        }

        return next
    }

    private fun isSpritePixel(cycles: Int, x: Int): Boolean {
        return cycles - 1 in x - 1..x + 1
    }
}
