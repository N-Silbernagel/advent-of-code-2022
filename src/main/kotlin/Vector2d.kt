data class Vector2d(override val x: Int, override val y: Int): Position2d {
    override operator fun plus(other: Position2d): Vector2d {
        return Vector2d(x + other.x, y+other.y)
    }
}
