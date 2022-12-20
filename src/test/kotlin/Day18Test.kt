package test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day18Test: StringSpec ({
    val testInput = "2,2,2\n" +
            "1,2,2\n" +
            "3,2,2\n" +
            "2,1,2\n" +
            "2,3,2\n" +
            "2,2,1\n" +
            "2,2,3\n" +
            "2,2,4\n" +
            "2,2,6\n" +
            "1,2,5\n" +
            "3,2,5\n" +
            "2,1,5\n" +
            "2,3,5"

    "part 1 test input" {
        val output = Day18.part1(
            testInput
                .lines(),
        )

        output shouldBe 64
    }

    "part 2 test input" {
        val output = Day18.part2(
            testInput
                .lines(),
        )

        output shouldBe 58
    }
})
