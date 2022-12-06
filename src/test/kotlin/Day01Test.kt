package test

import Day01
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day01Test: StringSpec ({
    val testInput = "1000\n" +
            "2000\n" +
            "3000\n" +
            "\n" +
            "4000\n" +
            "\n" +
            "5000\n" +
            "6000\n" +
            "\n" +
            "7000\n" +
            "8000\n" +
            "9000\n" +
            "\n" +
            "10000"

    "part 1 test input" {
        val output = Day01.part1(
            testInput.lines()
        )

        output shouldBe 24000
    }
})
