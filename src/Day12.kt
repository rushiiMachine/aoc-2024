import me.carleslc.kotlinextensions.arrays.matrix
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d12part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d12part2() }}ms")
}

fun d12part1() {
	val input = readInput("day12.txt")
	val map = input.lines()

	val visited = matrix(map.size, map[0].length) { a, b -> false }

	fun handleSquare(y: Int, x: Int, type: Char): Pair<Int, Int> { // perimeter, area
		if (y !in map.indices || x !in map[0].indices || map[y][x] != type)
			return 1 to 0
		if (visited[y][x])
			return 0 to 0
		visited[y][x] = true

		val neighbors = listOf(
			handleSquare(y, x + 1, type),
			handleSquare(y, x - 1, type),
			handleSquare(y + 1, x, type),
			handleSquare(y - 1, x, type),
		)
		val perimeter = neighbors.fold(0) { a, b -> a + b.first }
		val area = neighbors.fold(0) { a, b -> a + b.second }
		return perimeter to area + 1
	}

	var total = 0
	for ((y, row) in map.withIndex()) {
		for ((x, _) in row.withIndex()) {
			if (visited[y][x]) continue
			total += handleSquare(y, x, map[y][x]).let { (perim, area) -> perim * area }
		}
	}
	total.print()
}

fun d12part2() {
	val input = readInput("day12.txt")
	val map = input.lines()

	val visited = matrix(map.size, map[0].length) { a, b -> false }
	var visitedThisTime = matrix(map.size, map[0].length) { a, b -> false }
	val fenceDirections = mutableSetOf<Pair<Direction, Int>>()

	fun handleSquare(y: Int, x: Int, type: Char): Int { // area
		if (y !in map.indices || x !in map[0].indices) return 0
		if (map[y][x] != type || visited[y][x]) return 0
		visited[y][x] = true
		visitedThisTime[y][x] = true

		if (map.getOrNull(y)?.getOrNull(x + 1) != type) fenceDirections += Direction.East to x
		if (map.getOrNull(y)?.getOrNull(x - 1) != type) fenceDirections += Direction.West to x
		if (map.getOrNull(y + 1)?.getOrNull(x) != type) fenceDirections += Direction.South to y
		if (map.getOrNull(y - 1)?.getOrNull(x) != type) fenceDirections += Direction.North to y

		return listOf(
			handleSquare(y, x + 1, type),
			handleSquare(y, x - 1, type),
			handleSquare(y + 1, x, type),
			handleSquare(y - 1, x, type),
		).sum() + 1
	}

	fun calcSides(type: Char, fenceDirection: Pair<Direction, Int>): Int {
		val (direction, coord) = fenceDirection
		var sides = 0

		when (direction) {
			Direction.East, Direction.West -> {
				for (y in map.indices) {
					if (map[y][coord] != type || !visitedThisTime[y][coord]) continue
					if (map[y].getOrNull(coord + direction.matrixX) == type) continue
					if (map.getOrNull(y - 1)?.getOrNull(coord) == type
						&& map.getOrNull(y - 1)?.getOrNull(coord + direction.matrixX) != type
					) continue

					sides++
					println("found edge $direction at ($coord, $y)")
				}
			}

			Direction.North, Direction.South -> {
				for (x in map.indices) {
					if (map[coord][x] != type || !visitedThisTime[coord][x]) continue
					if (map.getOrNull(coord + direction.matrixY)?.getOrNull(x) == type) continue
					if (map.getOrNull(coord)?.getOrNull(x - 1) == type
						&& map.getOrNull(coord + direction.matrixY)?.getOrNull(x - 1) != type
					) continue

					sides++
					println("found edge $direction at ($x,$coord)")
				}
			}
		}
		return sides
	}

	var total = 0
	for ((y, row) in map.withIndex()) {
		for ((x, _) in row.withIndex()) {
			if (visited[y][x]) continue

			visitedThisTime = matrix(map.size, map[0].length) { a, b -> false }
			fenceDirections.clear()
			val testChar = map[y][x]
			val area = handleSquare(y, x, testChar)
			val sides = fenceDirections.sumOf { calcSides(testChar, it) }

			total += area * sides
			println("${map[y][x]} -> sides: $sides -> $fenceDirections")
		}
	}
	total.print()
}
