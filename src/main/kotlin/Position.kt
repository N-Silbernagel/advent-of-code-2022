interface Position {
    val x: Int
    val y: Int

    infix fun distanceTo(other: Vector): Int
}
