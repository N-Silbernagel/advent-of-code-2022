package test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day21Test: StringSpec ({
    val testInput = "root: pppw + sjmn\n" +
            "dbpl: 5\n" +
            "cczh: sllz + lgvd\n" +
            "zczc: 2\n" +
            "ptdq: humn - dvpt\n" +
            "dvpt: 3\n" +
            "lfqf: 4\n" +
            "humn: 5\n" +
            "ljgn: 2\n" +
            "sjmn: drzm * dbpl\n" +
            "sllz: 4\n" +
            "pppw: cczh / lfqf\n" +
            "lgvd: ljgn * ptdq\n" +
            "drzm: hmdt - zczc\n" +
            "hmdt: 32"

    "part 1 test input" {
        val output = Day21.part1(
            testInput
                .lines(),
        )

        output shouldBe 152
    }

    "part 2 test input" {
        val output = Day21.part2(
            testInput
                .lines(),
        )

        output shouldBe 301
    }
})
