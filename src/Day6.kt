import me.carleslc.kotlinextensions.arrays.Matrix
import me.carleslc.kotlinextensions.arrays.matrix
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d6part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d6part2() }}ms")
}

data class Square(
	var visited: Boolean = false,
	var obstacle: Boolean = false,
	var startPos: Boolean = false,
)

data class Move(val x: Int, val y: Int, val direction: Direction)

// parse into y,x
fun parseInput(input: String): Matrix<Square> {
	val lines = input.lines()

	return matrix(lines.size, lines[0].length) { y, x ->
		Square(
			visited = lines[y][x] == '^',
			obstacle = lines[y][x] == '#',
			startPos = lines[y][x] == '^',
		)
	}
}

fun getStartPos(matrix: Matrix<Square>): Pair<Int, Int> { // y, x
	for ((y, row) in matrix.withIndex()) {
		for ((x, square) in row.withIndex()) {
			if (square.startPos) return y to x
		}
	}
	error("unreachable")
}

fun d6part1() {
	val input = readInput("day6.txt")
	val board = parseInput(input)

	var (posY, posX) = getStartPos(board)
	var direction = Direction.North

	while (true) {
		// If next square is an obstacle, change direction
		if (board
				.getOrNull(posY + direction.matrixY)
				?.getOrNull(posX + direction.matrixX)
				?.obstacle ?: break
		) {
			direction = direction.rotateRight()
		}
		// No obstacle in next square, move to it and mark it visited
		else {
			posY += direction.matrixY
			posX += direction.matrixX
			board.getOrNull(posY)?.getOrNull(posX)?.visited = true
		}
	}

	// Count up all square visited
	board.sumOf { it.count { it.visited } }.print()
}

fun d6part2() {
	val input = readInput("day6.txt")
	val board = parseInput(input)
	var total = 0

	fun stuckInLoop(): Boolean {
		val moves = mutableSetOf<Move>()
		var (posY, posX) = getStartPos(board)
		var direction = Direction.North

		while (true) {
			// If exact position and orientation has been seen before, loop detected
			if (!moves.add(Move(posX, posY, direction)))
				return true

			// If next square is an obstacle, change direction
			if (board
					.getOrNull(posY + direction.matrixY)
					?.getOrNull(posX + direction.matrixX)
					?.obstacle ?: break
			) {
				direction = direction.rotateRight()
			}
			// No obstacle in next square, move to it and mark it visited
			else {
				posY += direction.matrixY
				posX += direction.matrixX
				board.getOrNull(posY)?.getOrNull(posX)?.visited = true
			}
		}
		return false
	}

	// Set obstacles where they don't exist and see if it ends up in a loop
	for (row in board) {
		for (square in row) {
			if (square.obstacle) continue

			square.obstacle = true
			if (stuckInLoop()) total++
			square.obstacle = false
		}
	}

	total.print()
}
