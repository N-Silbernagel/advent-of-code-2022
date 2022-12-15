import java.util.*
import kotlin.collections.HashSet
import kotlin.math.abs

fun main() {
    val input = readFileAsList("Day15")
    println(Day15.part2(input, 4_000_000))
}

object Day15 {
    fun part1(input: List<String>, lookupY: Int): Int {
        val sensors = parseSensors(input)

        val coveredXsAtY = coveredXsAtYBySensors(sensors, lookupY)

        return coveredXsAtY.size - 1
    }

    fun part2(input: List<String>, searchLength: Int): Long {
        val sensors = parseSensors(input)

        val searchRange = 0..searchLength
        for (lookupY in searchRange) {
            var coveredXsAtY = HashSet<Int>()
            for (sensor in sensors) {
                val radiusAtY = sensor.coveredDistance - abs(lookupY - sensor.y)
                for(i in sensor.x - radiusAtY..sensor.x + radiusAtY) {
                    if (i in searchRange) {
                        coveredXsAtY.add(i)
                    }
                }
            }

            if (coveredXsAtY.size <= searchLength) {
                for ((index, coveredX) in coveredXsAtY.withIndex()) {
                    if (index != coveredX) {
                        return 4_000_000L * index + lookupY
                    }
                }
            }

            println(lookupY)
        }



        return 0
    }

    private fun coveredXsAtYBySensors(
        sensors: MutableSet<Sensor>,
        lookupY: Int
    ): TreeSet<Int> {
        val coveredXsAtY = TreeSet<Int>()
        for (sensor in sensors) {
            val radiusAtY = sensor.coveredDistance - abs(lookupY - sensor.y)
            coveredXsAtY.addAll((sensor.x - radiusAtY)..sensor.x + radiusAtY)
        }
        return coveredXsAtY
    }

    private fun parseSensors(input: List<String>): MutableSet<Sensor> {
        val sensors = mutableSetOf<Sensor>()

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

            val sensorPosition = Vector(sensorX, sensorY)
            val sensor = Sensor(sensorPosition, manhattanDistance)
            sensors.add(sensor)
        }
        return sensors
    }

    data class Sensor(val position: Vector, val coveredDistance: Int) {
        val x
            get() = position.x

        val y
            get() = position.y
    }
}
