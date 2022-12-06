fun main() {
    fun part1(input: List<String>): Int {
        val stream = input[0]

        val buffer = ArrayList<Char>()

        for ((index, char) in stream.withIndex()) {
            buffer.add(char)

            if (index < 3) {
                continue
            }

            val uniqueChars = HashSet<Char>()
            uniqueChars.addAll(buffer)
            if (uniqueChars.size == 4) {
                return index + 1
            }

            buffer.remove(buffer.first())
        }

        return -1
    }

    fun part2(input: List<String>): Int {
        val stream = input[0]

        val buffer = ArrayList<Char>()

        for ((index, char) in stream.withIndex()) {
            buffer.add(char)

            if (index < 13) {
                continue
            }

            val uniqueChars = HashSet<Char>()
            uniqueChars.addAll(buffer)
            if (uniqueChars.size == 14) {
                return index + 1
            }

            buffer.remove(buffer.first())
        }

        return -1

    }

    val input = readFileAsList("Day06")
    println(part1(input))
    println(part2(input))

}
