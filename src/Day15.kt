import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d15part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d15part2() }}ms")
}

fun List<List<Char>>.printMap(title: String) {
	println(title)
	for (row in this) {
		row.joinToString(separator = "").print()
	}
}

fun d15part1() {
	val input = readInput("day15.txt")
	val (firstHalf, secondHalf) = input.split("\n\n")

	// Parse input
	val map = firstHalf.lines().map { it.toMutableList() }.toMutableList()
	val moves = secondHalf.lines().flatMap { line ->
		line.map { char ->
			when (char) {
				'^' -> Direction.North
				'>' -> Direction.East
				'<' -> Direction.West
				'v' -> Direction.South
				else -> error("")
			}
		}
	}

	// Recursively check if at least one empty spot is available in the direction before a wall
	fun canMove(y: Int, x: Int, direction: Direction): Boolean {
		return when (val nextBox = map[y + direction.y][x + direction.x]) {
			'#' -> false
			'.' -> true
			'O' -> canMove(y + direction.y, x + direction.x, direction)
			else -> error("invalid box $nextBox at ($x,$y)")
		}
	}

	// Recursively move the 2nd last box to the last box, 3rd last to 2nd last, cursor to 1st, etc...
	// Does not overwrite the initial cursor position
	fun moveBoxes(y: Int, x: Int, direction: Direction) {
		when (val nextBox = map[y + direction.y][x + direction.x]) {
			'#' -> error("invalid box value")
			'O' -> moveBoxes(y + direction.y, x + direction.x, direction)
			'.' -> {}
			else -> error("invalid box $nextBox at ($x,$y)")
		}
		// Move this box to the next box
		map[y + direction.y][x + direction.x] = map[y][x]
	}

	// Perform each robot move
	for (move in moves) {
		val y = map.indexOfFirst { it.any { it == '@' } }
		val x = map[y].indexOfFirst { it == '@' }

		if (!canMove(y, x, move)) continue
		moveBoxes(y, x, move)
		map[y][x] = '.'
	}

	// Tally the coordinates of each box
	var total = 0
	for ((y, x, box) in map.iterator2D()) {
		if (box == 'O') total += 100 * y + x
	}

	map.printMap("Final map:")
	total.print()
}

fun d15part2() {
	val input = readInput("day15.txt")
	val (firstHalf, secondHalf) = input.split("\n\n")

	// Parse input
	val map = firstHalf.lines().map { line ->
		line.flatMap { char ->
			when (char) {
				'#' -> "##"
				'O' -> "[]"
				'.' -> ".."
				'@' -> "@."
				else -> error("invalid input")
			}.toList()
		}.toMutableList()
	}.toMutableList()

	val moves = secondHalf.lines().flatMap { line ->
		line.map { char ->
			when (char) {
				'^' -> Direction.North
				'>' -> Direction.East
				'<' -> Direction.West
				'v' -> Direction.South
				else -> error("")
			}
		}
	}

	// Recursively check if at least one empty spot is available in the direction before a wall
	fun canMove(y: Int, x: Int, direction: Direction): Boolean {
		return when (val nextBox = map[y + direction.y][x + direction.x]) {
			'.' -> true
			'#' -> false
			'[', ']' -> {
				// Check the other half of [ or ] recursively as well if in N/S direction
				// If in W/E then it functions the same as in part 1
				if (direction.vertical) {
					val xOffset = if (nextBox == '[') 1 else -1
					if (!canMove(y + direction.y, x + direction.x + xOffset, direction))
						return false
				}

				canMove(y + direction.y, x + direction.x, direction)
			}

			else -> error("invalid box $nextBox at ($x,$y)")
		}
	}

	// Recursively move the 2nd last box pair to the last box pair, 3rd last to 2nd last, etc..., cursor to entrypoint.
	// Does not overwrite the initial cursor position
	fun moveBoxes(y: Int, x: Int, direction: Direction) {
		when (val nextBox = map[y + direction.y][x + direction.x]) {
			'.' -> {}
			'[', ']' -> {
				moveBoxes(y + direction.y, x + direction.x, direction)

				// Move the other half of [ or ] recursively as well if in N/S direction
				// If in W/E then it functions the same as in part 1
				if (direction.vertical) {
					val xOffset = if (nextBox == '[') 1 else -1
					moveBoxes(y + direction.y, x + direction.x + xOffset, direction)

					// Clear the other half of [] otherwise it might not get overwritten
					map[y + direction.y][x + direction.x + xOffset] = '.'
				}
			}

			else -> error("invalid box $nextBox at ($x,$y)")
		}
		// Move this box to the next box
		map[y + direction.y][x + direction.x] = map[y][x]
	}

	// Perform each robot move
	for (move in moves) {
		// Find the position of the robot
		val y = map.indexOfFirst { it -> it.any { it == '@' } }
		val x = map[y].indexOfFirst { it == '@' }

		// Check if we can move in direction, then move the boxes in that direction
		if (!canMove(y, x, move)) continue
		moveBoxes(y, x, move)
		// Clear initial box since it was moved
		map[y][x] = '.'
	}

	// Tally the coordinates of the left side of each box
	var total = 0
	for ((y, x, box) in map.iterator2D()) {
		if (box == '[') total += 100 * y + x
	}

	map.printMap("Final map:")
	total.print()
}
