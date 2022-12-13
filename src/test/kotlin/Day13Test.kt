package test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day13Test: StringSpec ({
    val testInput = "[1,1,3,1,1]\n" +
            "[1,1,5,1,1]\n" +
            "\n" +
            "[[1],[2,3,4]]\n" +
            "[[1],4]\n" +
            "\n" +
            "[9]\n" +
            "[[8,7,6]]\n" +
            "\n" +
            "[[4,4],4,4]\n" +
            "[[4,4],4,4,4]\n" +
            "\n" +
            "[7,7,7,7]\n" +
            "[7,7,7]\n" +
            "\n" +
            "[]\n" +
            "[3]\n" +
            "\n" +
            "[[[]]]\n" +
            "[[]]\n" +
            "\n" +
            "[1,[2,[3,[4,[5,6,7]]]],8,9]\n" +
            "[1,[2,[3,[4,[5,6,0]]]],8,9]"

    "part 1 test input" {
        val output = Day13.part1(
            testInput
                .lines()
        )

        output shouldBe 13
    }

    "part 2 test input" {
        val output = Day13.part2(
            testInput
                .lines()
        )

        output shouldBe 140
    }
})
