import java.util.TreeMap
import java.util.TreeSet
import kotlin.math.abs

fun main() {
    val input = readFileAsList("Day15")
    println(Day15.part1(input, 2000000))
    println(Day15.part2(input))
}

object Day15 {
    fun part1(input: List<String>, lookupY: Int): Int {
        val coveredPoints = TreeMap<Int, TreeSet<Int>>()

        for (inputLine in input) {
            val (sensorDesc, beaconDesc) = inputLine.split(": ")

            val sensorX = sensorDesc.substringAfter("x=")
                .substringBefore(",")
                .toInt()
            val sensorY = sensorDesc.substringAfter("y=")
                .substringBefore(",")
                .toInt()

            val beaconX = beaconDesc.substringAfter("x=")
                .substringBefore(",")
                .toInt()
            val beaconY = beaconDesc.substringAfter("y=")
                .substringBefore(",")
                .toInt()

            val distanceX = abs(sensorX - beaconX)
            val distanceY = abs(sensorY - beaconY)
            val manhattanDistance = distanceX + distanceY

            val leftMostCoveredX = sensorX - manhattanDistance
            val rightMostCoveredX = sensorX + manhattanDistance
            for (x in leftMostCoveredX..rightMostCoveredX) {
                val heightAtX = height(x - sensorX, manhattanDistance)

                for (y in -heightAtX..heightAtX) {
                    val row = coveredPoints.computeIfAbsent(y) { TreeSet() }
                    row.add(x)
                }
            }
        }

        val lookupRow = coveredPoints.getOrDefault(lookupY, TreeSet())

        return lookupRow.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    fun height(x: Int, manhattenDistance: Int): Int {
        if (x < 0) {
            return manhattenDistance + x
        }

        return manhattenDistance - x
    }

    data class Sensor(val vector: Vector, val coveredDistance: Int)
}
