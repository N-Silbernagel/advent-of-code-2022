package test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day17Test: StringSpec ({
    val testInput = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"

    "part 1 test input" {
        val output = Day17.part1(
            testInput
                .lines(),
        )

        output shouldBe 3068
    }

    "part 2 test input" {
        val output = Day17.part2(
            testInput
                .lines(),
        )

        output shouldBe 1514285714288
    }
})
