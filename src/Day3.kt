import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d3part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d3part2() }}ms")
}

fun d3part1() {
	val input = readInput("day3.txt")
	var total = 0

	val regex = "mul\\((\\d{1,3}),\\s*?(\\d{1,3})\\)".toRegex()

	for (result in regex.findAll(input)) {
		val (val1, val2) = result.destructured

		total += val1.toInt() * val2.toInt()
	}

	total.print()
}

fun d3part2() {
	val input = readInput("day3.txt")
	var total = 0

	// Extract a/b from mul(a,b) or match do() or don't()
	val regex = "mul\\((\\d{1,3}),\\s*?(\\d{1,3})\\)|do\\(\\)|don't\\(\\)".toRegex()

	var enabled = true
	for (result in regex.findAll(input)) {
		when (result.value.take(3)) {
			"do(" -> enabled = true
			"don" -> enabled = false
			"mul" -> {
				val (val1, val2) = result.destructured
				if (enabled) {
					total += val1.toInt() * val2.toInt()
				}
			}

			else -> error("match: ${result.value}")
		}
	}

	total.print()
}
