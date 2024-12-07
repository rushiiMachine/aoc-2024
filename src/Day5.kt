import com.google.common.collect.ArrayListMultimap
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d5part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d5part2() }}ms")
}

fun d5part1() {
	val input = readInput("day5.txt")
	val halves = input.split("\n\n")

	val orderingRules = ArrayListMultimap.create<Int, Int>()
	var total = 0

	// Parse sorting rules
	for (line in halves[0].lines()) {
		val (left, right) = line.split("|").map { it.toInt() }
		orderingRules.put(left, right)
	}

	// Parse updates
	line@
	for (line in halves[1].lines()) {
		val updateIds = line.extractIntSeparated()

		for ((index, updateId) in updateIds.withIndex()) {
			val previousIds = updateIds.slice(0..<index)

			for (after in orderingRules.get(updateId)) {
				if (previousIds.contains(after))
					continue@line
			}
		}

		total += updateIds[updateIds.size / 2]
	}

	total.print()
}

fun d5part2() {
	val input = readInput("day5.txt")
	val halves = input.split("\n\n")

	val orderingRules = ArrayListMultimap.create<Int, Int>()
	var total = 0

	// Parse sorting rules
	for (line in halves[0].lines()) {
		val (left, right) = line.split("|").map { it.toInt() }
		orderingRules.put(left, right)
	}

	// Parse updates
	line@
	for (line in halves[1].lines()) {
		val updateIds = line.extractIntSeparated().toMutableList()

		var badUpdate = false
		var updateIndex = 0
		while (updateIndex < updateIds.size) {
			val updateId = updateIds[updateIndex]

			for (after in orderingRules.get(updateId)) {
				val afterIdx = updateIds.slice(0..<updateIndex).indexOf(after)

				if (afterIdx >= 0) {
					badUpdate = true
					updateIds[afterIdx] = updateId
					updateIds[updateIndex] = after
					updateIndex = afterIdx
				}
			}

			updateIndex++
		}
		if (!badUpdate) continue@line

		total += updateIds[updateIds.size / 2]
	}

	total.print()
}
