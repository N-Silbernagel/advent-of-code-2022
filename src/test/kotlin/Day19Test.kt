package test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day19Test: StringSpec ({
    val testInput = "Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.\n" +
            "Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian."

    "part 1 test input" {
        val output = Day19.part1(
            testInput
                .lines(),
        )

        output shouldBe 33
    }

    "part 2 test input" {
        val output = Day19.part2(
            testInput
                .lines(),
        )

        output shouldBe 3472
    }
})
