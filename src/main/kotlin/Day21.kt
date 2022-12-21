
fun main() {
    val input = readFileAsList("Day21")
    println(Day21.part1(input))
    println(Day21.part2(input))
}

object Day21 {
    fun part1(input: List<String>): Long {
        val monkeys = parseMonkeys(input)

        val values = mutableMapOf<String, Long>()
        val queue = ArrayDeque<Monkey>()
        queue.addAll(monkeys.values)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current is NumberMonkey) {
                values[current.name] = current.number
                continue
            }
            if (current is MathMonkey) {
                val (firstName, secondName) = current.dependencies
                val firstValue = values[firstName]
                val secondValue = values[secondName]
                if (firstValue == null || secondValue == null) {
                    queue.add(current)
                    continue
                }
                val currentValue = current.operation.intBinaryOperator.invoke(firstValue, secondValue)
                values[current.name] = currentValue
            }
        }

        return values["root"]!!
    }

    fun part2(input: List<String>): Long {
        val monkeys = parseMonkeys(input)

        val me = monkeys["humn"]!!
        monkeys["humn"] = Human(me.name)
        val root = monkeys["root"]!!
        if (root !is MathMonkey) {
            throw RuntimeException("Root is not a math monkey")
        }
        monkeys["root"] = root.copy(operation = MathOperation.EQUATE)

        val values = mutableMapOf<String, Long>()
        val queue = ArrayDeque<Monkey>()
        queue.addAll(monkeys.values)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current is NumberMonkey) {
                values[current.name] = current.number
                continue
            }
            if (current is MathMonkey) {
                val (firstName, secondName) = current.dependencies
                val firstValue = values[firstName]
                val secondValue = values[secondName]
                if (firstValue == null || secondValue == null) {
                    if (current.operation == MathOperation.EQUATE) {
                        if (firstValue != null) {
                            values[secondName] = firstValue
                            continue
                        }

                        if (secondValue != null) {
                            values[firstName] = secondValue
                            continue
                        }
                    }

                    val currentValue = values[current.name]
                    if (currentValue != null) {
                        if (firstValue != null) {
                            val inverseResult = current.operation.inverse.invoke(firstValue, if (current.operation == MathOperation.SUBTRACT) -currentValue else currentValue)
                            values[secondName] = inverseResult
                            continue
                        }

                        if (secondValue != null) {
                            val inverseResult = current.operation.inverse.invoke(secondValue, currentValue)
                            values[firstName] = inverseResult
                            continue
                        }
                    }

                    queue.add(current)

                    continue
                }
                val currentValue = current.operation.intBinaryOperator.invoke(firstValue, secondValue)
                values[current.name] = currentValue
            }
        }

        return values["humn"]!!
    }

    private fun parseMonkeys(input: List<String>): MutableMap<String, Monkey> {
        return input.map { line ->
            val monkeyName = line.substringBefore(": ")
            val job = line.substringAfter(": ")
            val jobNumeric = job.toLongOrNull()
            if (jobNumeric != null) {
                return@map monkeyName to NumberMonkey(monkeyName, jobNumeric)
            }
            val firstMonkeyName = job.substringBefore(" ")
            val secondMonkeyName = job.substringAfterLast(" ")
            val mathOperator = job.substringAfter(" ")
                .substringBefore(" ")[0]

            val mathOperation = MathOperation.fromOperator(mathOperator)
            monkeyName to MathMonkey(monkeyName, mathOperation, firstMonkeyName to secondMonkeyName)
        }.toMap().toMutableMap()
    }

    interface Monkey {
        val name: String
    }

    data class NumberMonkey(override val name: String, val number: Long): Monkey

    data class MathMonkey(override val name: String, val operation: MathOperation, val dependencies: Pair<String, String>): Monkey

    data class Human(override val name: String): Monkey

    enum class MathOperation(val intBinaryOperator: (Long, Long) -> Long, val inverse: (Long, Long) -> Long) {
        ADD({first, second -> first + second}, {first, result -> result - first}),
        SUBTRACT({first, second -> first - second}, {first, result -> first + result}),
        DIVIDE({first, second -> first / second}, {first, result -> first * result}),
        MULTIPLY({first, second -> first * second}, {first, result -> result / first}),
        EQUATE({first, second -> if (first == second) 1 else 0}, {first, second -> if (first == second) 1 else 0});

        companion object {
            fun fromOperator(operator: Char): MathOperation {
                if (operator == '+') return ADD
                if (operator == '-') return SUBTRACT
                if (operator == '*') return MULTIPLY
                if (operator == '/') return DIVIDE
                throw RuntimeException("Unknown math operator!")
            }
        }
    }
}
