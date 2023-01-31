package test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day22Test: StringSpec ({
    val testInput =
            "        ...#\n" +
            "        .#..\n" +
            "        #...\n" +
            "        ....\n" +
            "...#.......#\n" +
            "........#...\n" +
            "..#....#....\n" +
            "..........#.\n" +
            "        ...#....\n" +
            "        .....#..\n" +
            "        .#......\n" +
            "        ......#.\n" +
            "\n" +
            "10R5L5R10L4R5L5"

    "part 1 test input" {
        val output = Day22.part1(
            testInput
                .lines(),
        )

        output shouldBe 6032
    }

    "part 2 test input" {
        val output = Day22.part2(
            testInput
                .lines(),
        )

        output shouldBe 301
    }
})
