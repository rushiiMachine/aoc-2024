import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d4part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d4part2() }}ms")
}

fun d4part1() {
	val input = readInput("day4.txt")
	val lines = input.lines()
	val lineLength = lines[0].length
	var total = 0

	for (i in 0..<(input.length - 3)) {
		if (input.substring(i, i + 4) in arrayOf("XMAS", "SAMX"))
			total++
	}

	for (y in 0..(lines.lastIndex - 3)) {
		// down
		for (x in 0..<lineLength) {
			if (lines[y][x] == 'X' && lines[y + 1][x] == 'M' && lines[y + 2][x] == 'A' && lines[y + 3][x] == 'S') {
				total += 1
			}
		}

		// down right
		for (x in 0..<(lineLength - 3)) {
			if (lines[y][x] == 'X' && lines[y + 1][x + 1] == 'M' && lines[y + 2][x + 2] == 'A' && lines[y + 3][x + 3] == 'S') {
				total += 1
			}
		}

		// down left
		for (x in 3..<(lineLength)) {
			if (lines[y][x] == 'X' && lines[y + 1][x - 1] == 'M' && lines[y + 2][x - 2] == 'A' && lines[y + 3][x - 3] == 'S') {
				total += 1
			}
		}
	}

	for (y in 3..(lines.lastIndex)) {
		// up
		for (x in 0..<lineLength) {
			if (lines[y][x] == 'X' && lines[y - 1][x] == 'M' && lines[y - 2][x] == 'A' && lines[y - 3][x] == 'S') {
				total += 1
			}
		}

		// up right
		for (x in 0..<(lineLength - 3)) {
			if (lines[y][x] == 'X' && lines[y - 1][x + 1] == 'M' && lines[y - 2][x + 2] == 'A' && lines[y - 3][x + 3] == 'S') {
				total += 1
			}
		}

		// up left
		for (x in 3..<(lineLength)) {
			if (lines[y][x] == 'X' && lines[y - 1][x - 1] == 'M' && lines[y - 2][x - 2] == 'A' && lines[y - 3][x - 3] == 'S') {
				total += 1
			}
		}
	}

	total.print()
}

fun d4part2() {
	val input = readInput("day4.txt")
	val lines = input.lines()
	val lineLength = lines[0].length
	var total = 0

	for (y in 0..<(lines.size - 2)) {
		for (x in 0..<(lineLength - 2)) {
			if (lines[y + 1][x + 1] == 'A'
				&& ((lines[y][x] == 'M' && lines[y + 2][x + 2] == 'S') || (lines[y][x] == 'S' && lines[y + 2][x + 2] == 'M'))
				&& ((lines[y][x + 2] == 'M' && lines[y + 2][x] == 'S') || (lines[y][x + 2] == 'S' && lines[y + 2][x] == 'M'))
			) {
				total++
			}
		}
	}

	total.print()
}
