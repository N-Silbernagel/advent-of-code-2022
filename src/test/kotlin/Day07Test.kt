package test

import Day07
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day07Test: StringSpec ({
    val testInput = "\$ cd /\n" +
            "\$ ls\n" +
            "dir a\n" +
            "14848514 b.txt\n" +
            "8504156 c.dat\n" +
            "dir d\n" +
            "\$ cd a\n" +
            "\$ ls\n" +
            "dir e\n" +
            "29116 f\n" +
            "2557 g\n" +
            "62596 h.lst\n" +
            "\$ cd e\n" +
            "\$ ls\n" +
            "584 i\n" +
            "\$ cd ..\n" +
            "\$ cd ..\n" +
            "\$ cd d\n" +
            "\$ ls\n" +
            "4060174 j\n" +
            "8033020 d.log\n" +
            "5626152 d.ext\n" +
            "7214296 k\n"

    "part 1 test input" {
        val output = Day07.part1(
            testInput.lines()
        )

        output shouldBe 95437
    }

    "part 2 test input" {
        val output = Day07.part2(
            testInput.lines()
        )

        output shouldBe 24933642
    }
})
