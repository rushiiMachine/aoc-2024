import me.carleslc.kotlinextensions.arrays.*
import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d20part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d20part2() }}ms")
}

fun d20part1() {
	val input = readInput("day20.txt")
	val map = input.lines()
	val track = map
		.withIndex()
		.flatMap { (y, row) ->
			row.withIndex()
				.filter { (_, char) -> char != '#' }
				.map { (x, _) -> Point(x, y) }
		}
	val start = track.find { (x, y) -> map[y][x] == 'S' }!!
	val end = track.find { (x, y) -> map[y][x] == 'E' }!!

	val walls = matrix(map.size, map[0].length) { y, x -> map[y][x] == '#' }

	fun simulate(walls: Matrix<Boolean>): Map<Point, Int> {
		val vertexQueue = PriorityQueue<Pair<Point, Int>>(compareBy { (_, distance) -> distance })
		val distances = walls.flatMapIndexed { y: Int, row: Array<Boolean> ->
			row.mapIndexedNotNull { x, isWall ->
				if (isWall) null
				else Point(x, y)
			}
		}.associateWith { -1 }.toMutableMap()
		distances[start] = 0
		vertexQueue.add(start to 0)

		while (vertexQueue.isNotEmpty()) {
			val (point, distance) = vertexQueue.remove()

			val neighbors = arrayOf(
				point.copy(x = point.x + 1),
				point.copy(x = point.x - 1),
				point.copy(y = point.y + 1),
				point.copy(y = point.y - 1),
			)

			for (neighbor in neighbors) {
				if (walls[neighbor.y][neighbor.x]) continue

				val nextPointDistance = distances[neighbor]!!
				if (nextPointDistance == -1 || nextPointDistance > distance + 1) {
					distances[neighbor] = distance + 1
					vertexQueue.add(neighbor to distance + 1)
				}
			}
		}
		return distances
	}

	val baseDistance = input.count { it == '.' } + 1
	println("Base distance: $baseDistance")

	val cheats = mutableMapOf<Int, Int>()
	for (y in 1..<walls.lastRowIndex) {
		for (x in 1..<walls.lastColumnIndex) {
			if (!walls[y][x]) continue

			walls[y][x] = false
			val newDistance = simulate(walls)[end]!!
			walls[y][x] = true

			if (baseDistance == newDistance) continue
			if (baseDistance < newDistance) error("not possible")

			cheats.compute(baseDistance - newDistance) { _, count -> (count ?: 0) + 1 }
		}
	}

	cheats.filter { (savedTime) -> savedTime >= 100 }
		.values
		.sum().print()
}

fun d20part2() {}
