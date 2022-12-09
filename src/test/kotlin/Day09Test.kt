package test

import Day09
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day09Test: StringSpec ({
    "part 1 test input" {
        val output = Day09.part1(
            ("R 4\n" +
                    "U 4\n" +
                    "L 3\n" +
                    "D 1\n" +
                    "R 4\n" +
                    "D 1\n" +
                    "L 5\n" +
                    "R 2")
                .lines()
        )

        output shouldBe 13
    }

    "part 2 test input" {
        val output = Day09.part2(
            ("R 5\n" +
                    "U 8\n" +
                    "L 8\n" +
                    "D 3\n" +
                    "R 17\n" +
                    "D 10\n" +
                    "L 25\n" +
                    "U 20")
                .lines()
        )

        output shouldBe 36
    }
})
