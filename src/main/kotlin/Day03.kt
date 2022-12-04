fun main() {
    fun findCommonChar(firstCompartment: CharSequence, secondCompartment: CharSequence): Char {
        for (cFirst in firstCompartment) {
            for (cSecond in secondCompartment) {
                if (cFirst == cSecond) {
                    return cFirst
                }
            }
        }

        throw RuntimeException()
    }

    fun getPriority(commonChar: Char): Int {
        val ascii = commonChar.code
        if (commonChar.isLowerCase()) {
            return ascii - 96
        }
        return ascii - 38
    }

    fun compare(firstCompartment: CharSequence, secondCompartment: CharSequence, value: Int): Int {
        val commonChar = findCommonChar(firstCompartment, secondCompartment)

        return value + getPriority(commonChar)
    }

    fun part1(input: List<String>): Int {
        var value = 0

        for (rucksack in input) {
            val compartmentSize = rucksack.length / 2
            val firstCompartment = rucksack.subSequence(0, compartmentSize)
            val secondCompartment = rucksack.subSequence(compartmentSize, rucksack.length)


            value = compare(firstCompartment, secondCompartment, value)
        }

        return value
    }

    fun calculatePriorityOfGroup(
        chunk: List<String>,
    ): Int {
        for (char in chunk[0]) {
            val secondRucksackHas = chunk[1].indexOf(char) != -1
            val thirdRucksackHas = chunk[2].indexOf(char) != -1

            if (secondRucksackHas && thirdRucksackHas) {
                return getPriority(char)
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val chunkedInput: List<List<String>> = input.chunked(3)

        var sumOfPriorities = 0

        for (chunk in chunkedInput) {
            sumOfPriorities += calculatePriorityOfGroup(chunk)
        }

        return sumOfPriorities
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))

}

