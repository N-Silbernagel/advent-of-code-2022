import kotlin.math.abs

interface Position {
    val x: Int
    val y: Int

    operator fun plus(other: Position): Position {
        return Vector(x + other.x, y+other.y)
    }

    /**
     * Calculate manhattan distance between two vectors
     */
    infix fun distanceTo(other: Position): Int {
        val diff = this diff other
        return diff.x + diff.y
    }

    infix fun diff(other: Position): Position {
        val distanceX = abs(x - other.x)
        val distanceY = abs(y - other.y)
        return Vector(distanceX, distanceY)
    }
}
