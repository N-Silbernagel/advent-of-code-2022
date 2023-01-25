package test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day20Test: StringSpec ({
    val testInput = "1\n" +
            "2\n" +
            "-3\n" +
            "3\n" +
            "-2\n" +
            "0\n" +
            "4"

    "part 1 test input" {
        val output = Day20.part1(
            testInput
                .lines(),
        )

        output shouldBe 3
    }

    "part 2 test input" {
        val output = Day20.part2(
            testInput
                .lines(),
        )

        output shouldBe 1623178306
    }
})
