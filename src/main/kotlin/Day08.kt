fun main() {
    val input = readFileAsList("Day08")
    println(Day08.part1(input))
    println(Day08.part2(input))
}

object Day08 {
    fun part1(input: List<String>): Int {
        var visibleItems = 0

        for ((rowIndex, row) in input.withIndex()) {
            for ((itemIndex, tree) in row.withIndex()) {
                val isVisible = checkVisibility(itemIndex, rowIndex, input, row, tree.digitToInt())

                if (isVisible) {
                    visibleItems++
                }
            }
        }

        return visibleItems
    }

    fun part2(input: List<String>): Int {
        var bestScenicScore = 0

        for ((rowIndex, row) in input.withIndex()) {
            for ((treeIndex, tree) in row.withIndex()) {
                val itemIsOnEdge = treeIndex == 0 || rowIndex == 0 || rowIndex == input.size - 1 || treeIndex == row.length - 1
                if (itemIsOnEdge) {
                    // 0 scenic score because of multiplication -> not gonna be the biggest
                    continue
                }

                val scenicScore = calculateScenicScore(treeIndex, row, tree.digitToInt(), rowIndex, input)

                if (scenicScore > bestScenicScore) {
                    bestScenicScore = scenicScore
                }
            }
        }

        return bestScenicScore
    }

    private fun checkVisibility(
        itemIndex: Int,
        rowIndex: Int,
        input: List<String>,
        row: String,
        item: Int
    ): Boolean {
        val itemIsOnEdge = itemIndex == 0 || rowIndex == 0 || rowIndex == input.size - 1 || itemIndex == row.length - 1
        if (itemIsOnEdge) {
            return true
        }

        var visibleFromRight = true
        var visibleFromLeft = true
        for ((otherRowItemIndex, otherRowItem) in row.withIndex()) {
            if (otherRowItem.digitToInt() < item) continue

            if (otherRowItemIndex < itemIndex) {
                visibleFromLeft = false
            }

            if (otherRowItemIndex > itemIndex) {
                visibleFromRight = false
            }

            if (!visibleFromLeft && !visibleFromRight) {
                break
            }
        }

        var visibleFromTop = true
        var visibleFromBottom = true
        for ((otherColItemIndex, innerRow) in input.withIndex()) {
            val otherColItem = innerRow[itemIndex]

            if (otherColItem.digitToInt() < item) continue

            if (otherColItemIndex < rowIndex) {
                visibleFromTop = false
            }

            if (otherColItemIndex > rowIndex) {
                visibleFromBottom = false
            }

            if (!visibleFromTop && !visibleFromBottom) {
                break
            }
        }

        return visibleFromTop || visibleFromRight || visibleFromBottom || visibleFromLeft
    }

    private fun calculateScenicScore(
        treeIndex: Int,
        row: String,
        tree: Int,
        rowIndex: Int,
        input: List<String>
    ): Int {
        var scenicScoreLeft = 0
        for (otherRowItemIndex in (treeIndex - 1) downTo 0) {
            val otherRowItem = row[otherRowItemIndex]

            scenicScoreLeft++
            if (otherRowItem.digitToInt() >= tree) {
                break
            }
        }

        var scenicScoreRight = 0
        for (otherRowItemIndex in (treeIndex + 1) until row.length) {
            val otherRowItem = row[otherRowItemIndex]

            scenicScoreRight++
            if (otherRowItem.digitToInt() >= tree) {
                break
            }
        }

        var scenicScoreTop = 0
        for (otherColItemIndex in (rowIndex - 1) downTo 0) {
            val otherColItem = input[otherColItemIndex][treeIndex]

            scenicScoreTop++
            if (otherColItem.digitToInt() >= tree) {
                break
            }
        }

        var scenicScoreBottom = 0
        for (otherColItemIndex in (rowIndex + 1) until input.size) {
            val otherColItem = input[otherColItemIndex][treeIndex]

            scenicScoreBottom++
            if (otherColItem.digitToInt() >= tree) {
                break
            }
        }

        return scenicScoreTop * scenicScoreRight * scenicScoreBottom * scenicScoreLeft
    }
}
