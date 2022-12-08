package test

import Day08
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day08Test: StringSpec ({
    val testInput = "30373\n" +
                    "25512\n" +
                    "65332\n" +
                    "33549\n" +
                    "35390"

    "same size counts as not visible" {
        val input = "111\n" +
                "111\n" +
                "111"

        val output = Day08.part1(input.lines())

        output shouldBe 8
    }

    "part 1 test input" {
        val output = Day08.part1(
            testInput.lines()
        )

        output shouldBe 21
    }

    "part 2 test input" {
        val output = Day08.part2(
            testInput.lines()
        )

        output shouldBe 8
    }
})
