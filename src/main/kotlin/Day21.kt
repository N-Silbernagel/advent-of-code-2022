
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
                if (currentValue !is OperationResult) {
                    throw RuntimeException("Should not happen")
                }
                values[current.name] = currentValue.result
            }
        }

        return values["root"]!!
    }

    fun part2(input: List<String>): Long {
        val monkeys = parseMonkeys(input)

        val me = monkeys["humn"]!!
        monkeys["humn"] = Human(me.name)

        return 0
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

    enum class MathOperation(val intBinaryOperator: (Long, Long) -> MathResult) {
        ADD({first, second -> OperationResult(first + second)}),
        SUBTRACT({first, second -> OperationResult(first - second)}),
        DIVIDE({first, second -> OperationResult(first / second)}),
        MULTIPLY({first, second -> OperationResult(first * second)}),
        EQUATE({first, second -> EquationResult(first == second)});

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

    interface MathResult

    data class EquationResult(val result: Boolean): MathResult

    data class OperationResult(val result: Long): MathResult
}
