package test

import Day23
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day23Test : StringSpec({
    val testInput = "..............\n" +
            "..............\n" +
            ".......#......\n" +
            ".....###.#....\n" +
            "...#...#.#....\n" +
            "....#...##....\n" +
            "...#.###......\n" +
            "...##.#.##....\n" +
            "....#..#......\n" +
            "..............\n" +
            "..............\n" +
            ".............."

    "part 1 test input" {
        val output = Day23.part1(
            testInput
                .lines(),
        )

        output shouldBe 110
    }

    "part 2 test input" {
        val output = Day23.part2(
            testInput
                .lines(),
        )

        output shouldBe 20
    }
})
