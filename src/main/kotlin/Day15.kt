import java.util.*
import kotlin.math.abs

fun main() {
    val input = readFileAsList("Day15")
    println(Day15.part1(input, 10))
    println(Day15.part2(input, 4000000))
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
            var lookupX = 0
            while (lookupX < searchLength) {
                val coveringSensor = sensors.firstOrNull { sensor ->
                    val vector = Vector2d(lookupX, lookupY)
                    val distanceToSensor = vector distanceTo sensor.position
                    distanceToSensor <= sensor.coveredDistance
                }

                if (coveringSensor == null) {
                    return return 4_000_000L * lookupX + lookupY
                }

                val rangeAtY = coveringSensor.coveredDistance - abs(lookupY - coveringSensor.y)
                lookupX = coveringSensor.x + rangeAtY + 1
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

            val sensorPosition = Vector2d(sensorX, sensorY)
            val beaconPosition = Vector2d(beaconX, beaconY)
            val sensor = Sensor(sensorPosition, sensorPosition distanceTo beaconPosition)
            sensors.add(sensor)
        }
        return sensors
    }

    data class Sensor(val position: Vector2d, val coveredDistance: Int): Position2d by position
}
