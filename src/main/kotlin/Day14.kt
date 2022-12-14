import java.util.TreeMap

fun main() {
    val input = readFileAsList("Day14")
    println(Day14.part1(input))
    println(Day14.part2(input))
}

object Day14 {
    fun part1(input: List<String>): Int {
        val field = Field.from(input)

        return simulateSand(field)
    }

    fun part2(input: List<String>): Int {
        val field = Field.from(input)

        val floorDescription = "${field.xStart - field.height},${field.yEnd + 2} -> ${field.xEnd + field.height},${field.yEnd + 2}"

        val fieldWithFloor = Field.from(input + floorDescription)

        return simulateSand(fieldWithFloor)
    }


    private fun simulateSand(field: Field): Int {
        field.getOrAddSand(Sand(500, 0))
        while (true) {
            if (field.currentSand.y > field.yEnd) {
                return field.sand.size - 1
            }

            if (field.currentSand.hasFallen) {
                if (field.currentSand.x == 500 && field.currentSand.y == 0) {
                    return field.sand.size
                }
                field.getOrAddSand(Sand(500, 0))
                continue
            }

            if (field.isAir(field.currentSand.x, field.currentSand.y + 1)) {
                field.moveCurrentSand(field.currentSand.x, field.currentSand.y + 1)
                continue
            }

            if (field.isAir(field.currentSand.x - 1, field.currentSand.y + 1)) {
                field.moveCurrentSand(field.currentSand.x - 1, field.currentSand.y + 1)
                continue
            }

            if (field.isAir(field.currentSand.x + 1, field.currentSand.y + 1)) {
                field.moveCurrentSand(field.currentSand.x + 1, field.currentSand.y + 1)
                continue
            }

            field.currentSand.hasFallen = true
        }
    }

    class Field {
        private val map: TreeMap<Int, TreeMap<Int, Thing>> = TreeMap()

        private var _yEnd = 0
        private var _locked = false

        val sand = mutableListOf<Sand>()

        val currentSand: Sand
            get() = sand.last()

        val yEnd: Int
            get() = _yEnd

        val xStart: Int
            get() = map.firstKey()

        val xEnd: Int
            get() = map.lastKey()

        val height: Int
            get() = _yEnd

        companion object {
            fun from(input: List<String>): Field {
                val field = Field()

                for (line in input) {
                    val splitLine = line.split(" -> ")
                    val pathCoordinates = splitLine.map { splitLinePart ->
                        splitLinePart.split(",").map {
                            it.toInt()
                        }
                    }

                    for ((pathIndex, coordinates) in pathCoordinates.withIndex()) {
                        val (fromX, fromY) = coordinates
                        val (toX, toY) = pathCoordinates.getOrNull(pathIndex + 1) ?: continue

                        for (x in fromX..toX) {
                            field.getOrAddThing(x, fromY, Rock())
                        }

                        for (x in toX..fromX) {
                            field.getOrAddThing(x, fromY, Rock())
                        }

                        for (y in fromY..toY) {
                            field.getOrAddThing(fromX, y, Rock())
                        }

                        for (y in toY..fromY) {
                            field.getOrAddThing(fromX, y, Rock())
                        }
                    }
                }

                field.lock()

                return field
            }
        }

        fun getOrAddThing(x: Int, y: Int, thing: Thing): Thing {
            if (!_locked && y > _yEnd) {
                _yEnd = y
            }
            val row = map.getOrPut(x) { TreeMap() }
            return row.getOrPut(y) { thing }
        }

        fun getOrAddSand(sand: Sand): Thing {
            this.sand.add(sand)
            return getOrAddThing(sand.x, sand.y, sand)
        }

        fun isAir(x: Int, y: Int): Boolean {
            return map[x]?.get(y) == null
        }

        fun moveCurrentSand(newX: Int, newY: Int) {
            map[currentSand.x]!!.remove(currentSand.y)
            currentSand.x = newX
            currentSand.y = newY
            getOrAddThing(newX, newY, currentSand)
        }

        fun lock() {
            this._locked = true
        }
    }

    interface Thing
    private class Rock: Thing
    data class Sand(var x: Int, var y: Int, var hasFallen: Boolean = false): Thing
}
