import kotlin.math.absoluteValue

data class Vector3d(val x: Int, val y: Int, val z: Int) {
    infix fun diffAbs(other: Vector3d): Vector3d {
        val diffVector = this diff other
        return Vector3d(diffVector.x.absoluteValue, diffVector.y.absoluteValue, diffVector.z.absoluteValue)
    }

    infix fun diff(other: Vector3d): Vector3d {
        val xDiff = x - other.x
        val yDiff = y - other.y
        val zDiff = z - other.z
        return Vector3d(xDiff, yDiff, zDiff)
    }

    fun neighbours(): Set<Vector3d> =
        setOf(
            copy(x = x - 1),
            copy(x = x + 1),
            copy(y = y - 1),
            copy(y = y + 1),
            copy(z = z - 1),
            copy(z = z + 1)
        )
}
