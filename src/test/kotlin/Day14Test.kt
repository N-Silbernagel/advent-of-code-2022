package test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day14Test: StringSpec ({
    val testInput = "498,4 -> 498,6 -> 496,6\n" +
                    "503,4 -> 502,4 -> 502,9 -> 494,9"

    "part 1 test input" {
        val output = Day14.part1(
            testInput
                .lines()
        )

        output shouldBe 24
    }

    "part 2 test input" {
        val output = Day14.part2(
            testInput
                .lines()
        )

        output shouldBe 93
    }
})
