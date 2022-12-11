import java.util.*

fun main() {
    val input = readFileAsList("Day11")
    println(Day11.part1(input))
    println(Day11.part2(input))
}

object Day11 {
    fun part1(input: List<String>): Long {
        val monkeys = parseMonkeys(input)

        repeat(20) {
            for (monkey in monkeys) {
                for (item in monkey.items) {
                    inspectItem(monkey, item, monkeys) { it / 3 }
                }
                monkey.items.clear()
            }
        }

        return calculateMonkeyBusiness(monkeys)
    }

    fun part2(input: List<String>): Long {
        val monkeys = parseMonkeys(input)

        val modulus = monkeys.map { it.testDivisor }.reduce(Int::times)

        repeat(10_000) {
            for (monkey in monkeys) {
                for (item in monkey.items) {
                    inspectItem(monkey, item, monkeys) { it % modulus }
                }
                monkey.items.clear()
            }
        }

        return calculateMonkeyBusiness(monkeys)
    }

    private fun inspectItem(
        monkey: Monkey,
        item: Long,
        monkeys: MutableList<Monkey>,
        manageWorryLevel: (Long) -> Long
    ) {
        val worryLevelAfterOperation = monkey.operation(item)
        val worryLevelOfItem = manageWorryLevel(worryLevelAfterOperation)
        val test = worryLevelOfItem % monkey.testDivisor == 0L
        val throwToMonkey = if (test) monkey.trueMonkey else monkey.falseMonkey
        monkeys[throwToMonkey].items.add(worryLevelOfItem)
        monkey.inspections++
    }

    private fun calculateMonkeyBusiness(monkeys: MutableList<Monkey>): Long {
        val mostActiveMonkeysMonkeyBusiness = TreeSet<Long>()

        for (monkey in monkeys) {
            mostActiveMonkeysMonkeyBusiness.add(monkey.inspections)
            if (mostActiveMonkeysMonkeyBusiness.size > 2) {
                mostActiveMonkeysMonkeyBusiness.remove(mostActiveMonkeysMonkeyBusiness.first())
            }
        }

        return mostActiveMonkeysMonkeyBusiness.first() * mostActiveMonkeysMonkeyBusiness.last()
    }

    private fun parseMonkeys(input: List<String>): MutableList<Monkey> {
        val monkeys = mutableListOf<Monkey>()

        val monkeyDescriptions = input.chunked(7)
        for (monkeyDescription in monkeyDescriptions) {
            var startingItems: MutableList<Long>? = null
            var operation: ((Long) -> Long)? = null
            var testDivisor: Int? = null
            var trueMonkey: Int? = null
            var falseMonkey: Int? = null

            for (inputLine in monkeyDescription) {

                if (inputLine.isBlank()) {
                    continue
                }

                if (inputLine.startsWith("Monkey")) {
                    continue
                }

                if (inputLine.startsWith("  Starting items")) {
                    val startingItemsString = inputLine.substringAfter("  Starting items: ")
                    startingItems = startingItemsString.split(", ")
                        .map { it.toLong() }
                        .toMutableList()
                }

                if (inputLine.startsWith("  Operation")) {
                    val operationString = inputLine.substringAfter("  Operation: new = old ")
                    val mathOperation = operationString[0]
                    val operantString = operationString.substring(2)

                    if (operantString == "old") {
                        operation = { it * it }
                        continue
                    }

                    val operant = operantString.toInt()
                    if (mathOperation == '+') {
                        operation = { it + operant }
                    }

                    if (mathOperation == '*') {
                        operation = { it * operant }
                    }
                }

                if (inputLine.startsWith("  Test")) {
                    val operationString = inputLine.substringAfter("  Test: divisible by ")
                    val divisor = operationString.toInt()

                    testDivisor = divisor
                }

                if (inputLine.startsWith("    If true")) {
                    val otherMonkey = inputLine.substringAfter("    If true: throw to monkey ")
                        .toInt()

                    trueMonkey = otherMonkey
                }

                if (inputLine.startsWith("    If false")) {
                    val otherMonkey = inputLine.substringAfter("    If false: throw to monkey ")
                        .toInt()

                    falseMonkey = otherMonkey
                }

            }

            val monkey = Monkey(startingItems!!, operation!!, 0, testDivisor!!, trueMonkey!!, falseMonkey!!)
            monkeys.add(monkey)
        }
        return monkeys
    }


    data class Monkey(
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        var inspections: Long,
        val testDivisor: Int,
        val trueMonkey: Int,
        val falseMonkey: Int,
    )
}
