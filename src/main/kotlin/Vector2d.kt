data class Vector2d(override val x: Int, override val y: Int): Position2d {
    override operator fun plus(other: Position2d): Vector2d {
        return Vector2d(x + other.x, y+other.y)
    }

    override fun compareTo(other: Position2d): Int {
        val yComparison = this.y.compareTo(other.y)
        if (yComparison != 0) {
            return yComparison
        }
        return this.x.compareTo(other.x);
    }
}
