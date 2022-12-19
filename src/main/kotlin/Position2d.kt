import kotlin.math.abs

interface Position2d {
    val x: Int
    val y: Int

    operator fun plus(other: Position2d): Position2d {
        return Vector2d(x + other.x, y+other.y)
    }

    /**
     * Calculate manhattan distance between two vectors
     */
    infix fun distanceTo(other: Position2d): Int {
        val diff = this diff other
        return diff.x + diff.y
    }

    infix fun diff(other: Position2d): Position2d {
        val distanceX = abs(x - other.x)
        val distanceY = abs(y - other.y)
        return Vector2d(distanceX, distanceY)
    }
}
