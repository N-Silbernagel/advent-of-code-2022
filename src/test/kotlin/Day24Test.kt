package test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day24Test: StringSpec ({
    val testInput = "#.######\n" +
                    "#>>.<^<#\n" +
                    "#.<..<<#\n" +
                    "#>v.><>#\n" +
                    "#<^v^^>#\n" +
                    "######.#"

    "part 1 test input" {
        val output = Day24.part1(
            testInput
                .lines(),
        )

        output shouldBe 18
    }

    "part 2 test input" {
        val output = Day24.part2(
            testInput
                .lines(),
        )

        output shouldBe 301
    }
})
