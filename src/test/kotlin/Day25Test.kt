package test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day25Test: StringSpec ({
    val testInput = "1=-0-2\n" +
                    "12111\n" +
                    "2=0=\n" +
                    "21\n" +
                    "2=01\n" +
                    "111\n" +
                    "20012\n" +
                    "112\n" +
                    "1=-1=\n" +
                    "1-12\n" +
                    "12\n" +
                    "1=\n" +
                    "122"

    "part 1 test input" {
        val output = Day25.part1(
            testInput
                .lines(),
        )

        output shouldBe "2=-1=0"
    }

    "part 2 test input" {
        val output = Day25.part2(
            testInput
                .lines(),
        )

        output shouldBe 54
    }
})
