import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d7part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d7part2() }}ms")
}

fun d7part1() {
	val input = readInput("day7.txt")
	var total = 0L

	val operators: Array<(Long, Long) -> Long> = arrayOf(
		{ a, b -> a + b },
		{ a, b -> a * b },
	)

	for (line in input.lines()) {
		val numbers = line.extractLongSeparated()
		val testValue = numbers[0]
		val startValue = numbers[1]

		var attempts = 0
		while (attempts++ < 1000000) {
			var sum = startValue

			for (i in 2..<numbers.size) {
				sum = operators.random()(sum, numbers[i])
			}

			if (sum == testValue) {
				total += testValue
				break
			}
		}
	}

	total.print()
}

fun d7part2() {
	val input = readInput("day7.txt")
	var total = 0L

	val operators: Array<(Long, Long) -> Long> = arrayOf(
		{ a, b -> a + b },
		{ a, b -> a * b },
		{ a, b -> "$a$b".toLong() },
	)

	for (line in input.lines()) {
		val numbers = line.extractLongSeparated()
		val testValue = numbers[0]
		val startValue = numbers[1]

		var attempts = 0
		while (attempts++ < 1000000) {
			var sum = startValue

			for (i in 2..<numbers.size) {
				sum = operators.random()(sum, numbers[i])
			}

			if (sum == testValue) {
				total += testValue
				break
			}
		}
	}

	total.print()
}
