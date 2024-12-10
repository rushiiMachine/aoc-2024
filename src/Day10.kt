import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d10part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d10part2() }}ms")
}

fun d10part1() {
	val input = readInput("day10.txt")
	var total = 0

	val map = input.lines().map { it.map { it.digitToIntOrNull() ?: -2 } }

	fun score(y: Int, x: Int, prevLevel: Int, visitedPeaks: MutableList<Pair<Int, Int>>): Int {
		if (y < 0 || y > map.lastIndex) return 0
		if (x < 0 || x > map[0].lastIndex) return 0

		if (map[y][x] != 0 && prevLevel == -1) return 0
		if (map[y][x] - prevLevel != 1) return 0
		if (map[y][x] == 9) {
			if (visitedPeaks.contains(y to x)) {
				return 0
			} else {
				visitedPeaks.add(y to x)
				return 1
			}
		}

		return score(y - 1, x, map[y][x], visitedPeaks) +
				score(y + 1, x, map[y][x], visitedPeaks) +
				score(y, x - 1, map[y][x], visitedPeaks) +
				score(y, x + 1, map[y][x], visitedPeaks)
	}

	for (y in map.indices) {
		for (x in map[0].indices) {
			val visitedPeaks = mutableListOf<Pair<Int, Int>>()
			total += score(y, x, -1, visitedPeaks)
		}
	}

	total.print()
}

fun d10part2() {
	val input = readInput("day10.txt")
	var total = 0

	val map = input.lines().map { it.map { it.digitToIntOrNull() ?: -2 } }

	fun score(y: Int, x: Int, prevLevel: Int): Int {
		if (y < 0 || y > map.lastIndex) return 0
		if (x < 0 || x > map[0].lastIndex) return 0

		if (map[y][x] != 0 && prevLevel == -1) return 0
		if (map[y][x] - prevLevel != 1) return 0
		if (map[y][x] == 9) return 1

		return score(y - 1, x, map[y][x]) +
				score(y + 1, x, map[y][x]) +
				score(y, x - 1, map[y][x]) +
				score(y, x + 1, map[y][x])
	}

	for (y in map.indices) {
		for (x in map[0].indices) {
			total += score(y, x, -1)
		}
	}

	total.print()
}
