package test

import part1
import kotlin.test.*

internal class Day01Test {
    @Test
    fun part1() {
        val input = listOf(
            "1",
            "2",
            "",
            "4",
            "",
            "1"
        )
        val biggestSetOfValues = part1(input)
        assertEquals(4, biggestSetOfValues)
    }
}
