fun main() {
    val input = readFileAsList("Day20")
    println(Day20.part1(input))
    println(Day20.part2(input))
}

object Day20 {
    private const val decryptionKey = 811589153

    fun part1(input: List<String>): Long {
        val (numbers, copy) = parseInput(input) { index, it -> Number(it.toLong(), index) }

        mix(numbers, copy)

        return getCoordinatesFromList(copy)
    }

    private fun parseInput(input: List<String>, mapNumber: (index: Int, String) -> Number): Pair<CyclicList<Number>, CyclicList<Number>> {
        val numbers = CyclicList<Number>().apply {
            addAll(input.mapIndexed(mapNumber))
        }
        val copy = CyclicList<Number>().apply {
            addAll(numbers)
        }

        return Pair(numbers, copy)
    }

    private fun getCoordinatesFromList(numbers: CyclicList<Number>): Long {
        val first0Index = numbers.indexOfFirst { it.value == 0L }

        val (one, _) = numbers[first0Index + 1000]
        val (two, _) = numbers[first0Index + 2000]
        val (three, _) = numbers[first0Index + 3000]

        return one + two + three
    }

    fun part2(input: List<String>): Long {
        val (numbers, copy) = parseInput(input) { index, it -> Number(it.toLong() * decryptionKey, index) }

        mix(numbers, copy)
        mix(numbers, copy)
        mix(numbers, copy)
        mix(numbers, copy)
        mix(numbers, copy)
        mix(numbers, copy)
        mix(numbers, copy)
        mix(numbers, copy)
        mix(numbers, copy)
        mix(numbers, copy)

        return getCoordinatesFromList(copy)
    }

    private fun mix(numbers: CyclicList<Number>, copy: CyclicList<Number>) {
        numbers.forEach { (_, ini) ->
            val index = copy.indexOfFirst { it.initialIndex == ini }
            val removeAt = copy.removeAt(index)
            copy.add(index + removeAt.value, removeAt)
        }
    }
}

private data class Number(val value: Long, val initialIndex: Int)

private class CyclicList<E>() : MutableList<E> {
    val internalList: MutableList<E> = mutableListOf()

    override val size: Int
        get() = internalList.size

    override fun contains(element: E): Boolean {
        return internalList.contains(element)
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        return internalList.containsAll(elements)
    }

    override fun get(index: Int): E {
        return get(index.toLong())
    }

    fun get(index: Long): E {
        return internalList[getCyclicIndex(index)]
    }

    override fun indexOf(element: E): Int {
        return internalList.indexOf(element)
    }

    override fun isEmpty(): Boolean {
        return internalList.isNotEmpty()
    }

    override fun iterator(): MutableIterator<E> {
        return internalList.iterator()
    }

    override fun lastIndexOf(element: E): Int {
        return internalList.lastIndexOf(element)
    }

    override fun add(element: E): Boolean {
        return internalList.add(element)
    }

    override fun add(index: Int, element: E) {
        return add(index.toLong(), element)
    }

    fun add(index: Long, element: E) {
        return internalList.add(getCyclicIndex(index), element)
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        return internalList.addAll(index, elements)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        return internalList.addAll(elements)
    }

    override fun clear() {
        return internalList.clear()
    }

    override fun listIterator(): MutableListIterator<E> {
        return internalList.listIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<E> {
        return internalList.listIterator(index)
    }

    override fun remove(element: E): Boolean {
        return internalList.remove(element)
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        return internalList.removeAll(elements)
    }

    override fun removeAt(index: Int): E {
        return removeAt(index.toLong())
    }

    fun removeAt(index: Long): E {
        return internalList.removeAt(getCyclicIndex(index))
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        return internalList.retainAll(elements)
    }

    override fun set(index: Int, element: E): E {
        return set(index.toLong(), element)
    }

    fun set(index: Long, element: E): E {
        return internalList.set(getCyclicIndex(index), element)
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> {
        return subList(fromIndex.toLong(), toIndex.toLong())
    }

    fun subList(fromIndex: Long, toIndex: Long): MutableList<E> {
        val cyclicFrom = getCyclicIndex(fromIndex)
        val cyclicTo = getCyclicIndex(toIndex)
        return internalList.subList(cyclicFrom, cyclicTo)
    }

    override fun toString(): String {
        return internalList.toString()
    }

    private fun getCyclicIndex(index: Long): Int {
        val modulo = index % size
        if (index < 0) {
            return (size + modulo).toInt()
        }
        return modulo.toInt()
    }
}
