package test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day16Test: StringSpec ({
    val testInput = "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB\n" +
            "Valve BB has flow rate=13; tunnels lead to valves CC, AA\n" +
            "Valve CC has flow rate=2; tunnels lead to valves DD, BB\n" +
            "Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE\n" +
            "Valve EE has flow rate=3; tunnels lead to valves FF, DD\n" +
            "Valve FF has flow rate=0; tunnels lead to valves EE, GG\n" +
            "Valve GG has flow rate=0; tunnels lead to valves FF, HH\n" +
            "Valve HH has flow rate=22; tunnel leads to valve GG\n" +
            "Valve II has flow rate=0; tunnels lead to valves AA, JJ\n" +
            "Valve JJ has flow rate=21; tunnel leads to valve II"

    "part 1 test input" {
        val output = Day16.part1(
            testInput
                .lines(),
        )

        output shouldBe 1651
    }

    "part 2 test input" {
        val output = Day16.part2(
            testInput
                .lines(),
        )

        output shouldBe 1707
    }
})
