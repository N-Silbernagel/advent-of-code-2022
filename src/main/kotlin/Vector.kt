import kotlin.math.abs

data class Vector(override val x: Int, override val y: Int): Position {
    /**
     * Calculate manhattan distance between two vectors
     */
    override infix fun distanceTo(other: Vector): Int {
        val distanceX = abs(x - other.x)
        val distanceY = abs(y - other.y)
        return distanceX + distanceY
    }
}
