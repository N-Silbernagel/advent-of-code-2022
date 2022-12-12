package test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day12Test: StringSpec ({
    val testInput = "Sabqponm\n" +
            "abcryxxl\n" +
            "accszExk\n" +
            "acctuvwj\n" +
            "abdefghi"

    "part 1 test input" {
        val output = Day12.part1(
            testInput
                .lines()
        )

        output shouldBe 31
    }

    "part 2 test input" {
        val output = Day12.part2(
            testInput
                .lines()
        )

        output shouldBe 29
    }
})
