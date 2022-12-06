fun main() {
    fun part1(input: List<String>): Int {
        var fullOverlaps = 0

        for (pairLine in input) {
            val pairs = pairLine.split(',')
            val firstElf = pairs[0]
            val secondElf = pairs[1]

            val firstElfRange = firstElf.split('-')
            val firstElfStart = firstElfRange[0].toInt()
            val firstElfEnd = firstElfRange[1].toInt()

            val secondElfRange = secondElf.split('-')
            val secondElfStart = secondElfRange[0].toInt()
            val secondElfEnd = secondElfRange[1].toInt()

            val firstContainedInSecond = firstElfStart >= secondElfStart && firstElfEnd <= secondElfEnd
            val secondContainedInFirst = firstElfStart <= secondElfStart && firstElfEnd >= secondElfEnd
            if (firstContainedInSecond || secondContainedInFirst) {
                fullOverlaps++
            }
        }

        return fullOverlaps
    }

    fun part2(input: List<String>): Int {
        var overlaps = 0

        for (pairLine in input) {
            val pairs = pairLine.split(',')
            val firstElf = pairs[0]
            val secondElf = pairs[1]

            val firstElfRange = firstElf.split('-')
            val firstElfStart = firstElfRange[0].toInt()
            val firstElfEnd = firstElfRange[1].toInt()

            val secondElfRange = secondElf.split('-')
            val secondElfStart = secondElfRange[0].toInt()
            val secondElfEnd = secondElfRange[1].toInt()

            val firstStartIsInSecondRange = firstElfStart in secondElfStart..secondElfEnd
            val firstEndIsInSecondRange = firstElfEnd in secondElfStart..secondElfEnd
            val secondStartIsInSecondRange = secondElfStart in firstElfStart..firstElfEnd
            val secondEndIsInSecondRange = secondElfEnd in firstElfStart..firstElfEnd


            if (firstStartIsInSecondRange || firstEndIsInSecondRange || secondStartIsInSecondRange || secondEndIsInSecondRange) {
                overlaps++
            }
        }

        return overlaps

    }

    val input = readFileAsList("Day04")
    println(part1(input))
    println(part2(input))

}

