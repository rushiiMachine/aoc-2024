import java.util.*
import kotlin.system.measureTimeMillis

// I hate today's problem fuck graphs

fun main() {
	println("Part 1 in ${measureTimeMillis { d16part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d16part2() }}ms")
}

data class Point(val x: Int, val y: Int) {
	override fun toString() = "($x,$y)"
}

private var minimumDistance: Int = -1

fun d16part1() {
	val map = readInput("day16.txt").lines()
	val points = map
		.withIndex()
		.flatMap { (y, row) ->
			row.withIndex()
				.filter { (_, char) -> char != '#' }
				.map { (x, _) -> Point(x, y) }
		}
	val start = points.find { (x, y) -> map[y][x] == 'S' }!!
	val end = points.find { (x, y) -> map[y][x] == 'E' }!!

	val vertexQueue = PriorityQueue<Triple<Point, Direction, Int>>(compareBy { (_, _, distance) -> distance })
	val distances = points.associateWith { -1 }.toMutableMap()
	distances[start] = 0
	vertexQueue.add(Triple(start, Direction.East, 0))

	while (vertexQueue.isNotEmpty()) {
		val (point, direction, distance) = vertexQueue.remove()

		val directionLeft = direction.rotateLeft()
		val directionRight = direction.rotateRight()
		val neighbors = listOf(
			Triple(point.copy(x = point.x + direction.x, y = point.y + direction.y), direction, 1),
			Triple(point.copy(x = point.x + directionLeft.x, y = point.y + directionLeft.y), directionLeft, 1001),
			Triple(point.copy(x = point.x + directionRight.x, y = point.y + directionRight.y), directionRight, 1001),
		).filter { (point) ->
			map[point.y][point.x] != '#'
		}

		for ((nextPoint, nextDirection, addedDistance) in neighbors) {
			if (distances[nextPoint]!! == -1 || distances[nextPoint]!! > distance + addedDistance) {
				distances[nextPoint] = distance + addedDistance
				vertexQueue.add(Triple(nextPoint, nextDirection, distance + addedDistance))
			}
		}
	}

	minimumDistance = distances[end]!!
	distances[end].print()
}

fun printMap(width: Int, height: Int, distances: Map<Point, Int>) {
	for (y in 0..<height) {
		for (x in 0..<width) {
			val distance = distances[Point(x, y)]?.toString()?.padStart(5, ' ') ?: "     "
			print("$distance ")
		}
		println()
	}
}

fun d16part2() {}
