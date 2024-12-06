import me.carleslc.kotlinextensions.arrays.array2d
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d6part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d6part2() }}ms")
}

data class Position(
	var visited: Boolean = false,
	var obstacle: Boolean = false,
	var startPos: Boolean = false,
)

fun d6part1() {
	val input = readInput("day6.txt")
	val lines = input.lines()

	// y,x
	val board = array2d(lines.size) { Array(lines[0].length) { Position() } }

	// parse
	for ((y, line) in input.lines().withIndex()) {
		for ((x, char) in line.toCharArray().withIndex()) {
			board[y][x] = Position(
				visited = char == '^',
				obstacle = char == '#',
				startPos = char == '^',
			)
		}
	}

	var posY = board.indexOfFirst { it.any { it.startPos } }
	var posX = board[posY].indexOfFirst { it.startPos }
	var direction = "north"

	while (true) {
		// Coordinate difference to next square in direction
		val (xDiff, yDiff) = when (direction) {
			"north" -> 0 to -1
			"south" -> 0 to 1
			"east" -> 1 to 0
			"west" -> -1 to 0
			else -> error("")
		}

		// If next square is an obstacle, change direction
		if (board.getOrNull(posY + yDiff)?.getOrNull(posX + xDiff)?.obstacle ?: break) {
			direction = when (direction) {
				"north" -> "east"
				"east" -> "south"
				"south" -> "west"
				"west" -> "north"
				else -> error("")
			}
		}
		// No obstacle in next square, move to it and mark it visited
		else {
			posY += yDiff
			posX += xDiff
			board.getOrNull(posY)?.getOrNull(posX)?.visited = true
		}
	}

	board.sumOf { it.count { it.visited } }.print()
}

fun d6part2() {
	val input = readInput("day6.txt")
	val lines = input.lines()
	var total = 0

	// y,x
	val board = array2d(lines.size) { Array(lines[0].length) { Position() } }

	// parse
	for ((y, line) in input.lines().withIndex()) {
		for ((x, char) in line.toCharArray().withIndex()) {
			board[y][x] = Position(
				visited = char == '^',
				obstacle = char == '#',
				startPos = char == '^',
			)
		}
	}

	fun stuckInLoop(): Boolean {
		var posY = board.indexOfFirst { it.any { it.startPos } }
		var posX = board[posY].indexOfFirst { it.startPos }
		var direction = "north"

		data class Move(val x: Int, val y: Int, val direction: String)

		val moves = mutableSetOf<Move>()

		while (true) {
			// If exact position and orientation has been seen before, loop detected
			if (!moves.add(Move(posX, posY, direction)))
				return true

			// Coordinate difference to next square in direction
			val (xDiff, yDiff) = when (direction) {
				"north" -> 0 to -1
				"south" -> 0 to 1
				"east" -> 1 to 0
				"west" -> -1 to 0
				else -> error("")
			}

			// If next square is an obstacle, change direction
			if (board.getOrNull(posY + yDiff)?.getOrNull(posX + xDiff)?.obstacle ?: break) {
				direction = when (direction) {
					"north" -> "east"
					"east" -> "south"
					"south" -> "west"
					"west" -> "north"
					else -> error("")
				}
			}
			// No obstacle in next square, move to it and mark it visited
			else {
				posY += yDiff
				posX += xDiff
				board.getOrNull(posY)?.getOrNull(posX)?.visited = true
			}
		}
		return false
	}

	// Set obstacles where they don't exist and see if it ends up in a loop
	for (y in lines.indices) {
		for (x in lines[0].indices) {
			if (board[y][x].obstacle) continue

			board[y][x].obstacle = true
			if (stuckInLoop())
				total += 1
			board[y][x].obstacle = false
		}
	}

	total.print()
}
