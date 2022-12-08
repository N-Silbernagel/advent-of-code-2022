fun main() {
    val input = readFileAsList("Day08")
    println(Day08.part1(input))
    println(Day08.part2(input))
}

object Day08 {
    fun part1(input: List<String>): Int {
        var visibleItems = 0

        for ((rowIndex, row) in input.withIndex()) {
            for ((itemIndex, item) in row.withIndex()) {
                val itemIsOnEdge = itemIndex == 0 || rowIndex == 0 || rowIndex == input.size - 1 || itemIndex == row.length - 1
                if (itemIsOnEdge) {
                    visibleItems++
                    continue
                }

                var visibleFromTop = true
                var visibleFromRight = true
                var visibleFromBottom = true
                var visibleFromLeft = true

                for ((otherRowItemIndex, otherRowItem) in row.withIndex()) {
                    if (otherRowItem.digitToInt() < item.digitToInt()) continue

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

                for ((otherColItemIndex, innerRow) in input.withIndex()) {
                    val otherColItem = innerRow[itemIndex]

                    if (otherColItem.digitToInt() < item.digitToInt()) continue

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

                if (visibleFromTop || visibleFromRight || visibleFromBottom || visibleFromLeft) {
                    visibleItems++
                }
            }
        }

        return visibleItems
    }

    fun part2(input: List<String>): Int {
        var bestScenicScore = 0

        for ((rowIndex, row) in input.withIndex()) {
            for ((itemIndex, item) in row.withIndex()) {
                val itemIsOnEdge = itemIndex == 0 || rowIndex == 0 || rowIndex == input.size - 1 || itemIndex == row.length - 1
                if (itemIsOnEdge) {
                    // 0 scenic score -> not gonna be the biggest
                    continue
                }

                var scenicScoreLeft = 0
                for (otherRowItemIndex in 0 until itemIndex) {
                    val otherRowItem = row[otherRowItemIndex]
                    if (otherRowItem.digitToInt() >= item.digitToInt()) {
                        scenicScoreLeft = 1
                    }

                    if (otherRowItem.digitToInt() < item.digitToInt()) {
                        scenicScoreLeft++
                    }
                }

                var scenicScoreRight = 0
                for (otherRowItemIndex in (itemIndex+1) until row.length) {
                    val otherRowItem = row[otherRowItemIndex]

                    if (otherRowItem.digitToInt() >= item.digitToInt()) {
                        scenicScoreRight++
                        break
                    }

                    if (otherRowItem.digitToInt() < item.digitToInt()) {
                        scenicScoreRight++
                    }
                }

                var scenicScoreTop = 0
                for (otherColItemIndex in 0 until rowIndex) {
                    val otherColItem = input[otherColItemIndex][itemIndex]
                    if (otherColItem.digitToInt() >= item.digitToInt()) {
                        scenicScoreTop = 1
                    }

                    if (otherColItem.digitToInt() < item.digitToInt()) {
                        scenicScoreTop++
                    }
                }

                var scenicScoreBottom = 0
                for (otherColItemIndex in (rowIndex+1) until input.size) {
                    val otherColItem = input[otherColItemIndex][itemIndex]

                    if (otherColItem.digitToInt() >= item.digitToInt()) {
                        scenicScoreBottom++
                        break
                    }

                    if (otherColItem.digitToInt() < item.digitToInt()) {
                        scenicScoreBottom++
                    }
                }

                val scenicScore = scenicScoreTop * scenicScoreRight * scenicScoreBottom * scenicScoreLeft

                if (scenicScore > bestScenicScore) {
                    bestScenicScore = scenicScore
                }
            }
        }

        return bestScenicScore
    }
}
