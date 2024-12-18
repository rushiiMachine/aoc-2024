import me.carleslc.kotlinextensions.arrays.*
import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d18part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d18part2() }}ms")
}

private const val height = 71
private const val width = 71
private val start = Point(0, 0)
private val end = Point(width - 1, height - 1)

// Calculate distances for every free point on the map using Dijkstra
fun simulate(walls: Matrix<Boolean>): Map<Point, Int> {
	val vertexQueue = PriorityQueue<Pair<Point, Int>>(compareBy { (_, distance) -> distance })
	val distances = walls.flatMapIndexed { y: Int, row: Array<Boolean> ->
		row.mapIndexedNotNull { x, corrupted ->
			if (corrupted) null
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
		).filter { (x, y) ->
			x in walls.rows && y in walls.columns && !walls[y][x]
		}

		for (nextPoint in neighbors) {
			val nextPointDistance = distances[nextPoint]!!
			if (nextPointDistance == -1 || nextPointDistance > distance + 1) {
				distances[nextPoint] = distance + 1
				vertexQueue.add(nextPoint to distance + 1)
			}
		}
	}
	return distances
}

fun d18part1() {
	val input = readInput("day18.txt")
	val walls = matrix(width, height) { _, _ -> false }

	for (line in input.lines().take(1024)) {
		val (x, y) = line.extractIntSeparated()
		walls[y][x] = true
	}

	simulate(walls)[end].print()
}

fun d18part2() {
	val input = readInput("day18.txt")
	val walls = matrix(width, height) { _, _ -> false }

	for (line in input.lines().skip(1024)) {
		val (x, y) = line.extractIntSeparated()
		walls[y][x] = true

		if (simulate(walls)[end] == -1) {
			println("$x,$y")
			break
		}
	}
}
