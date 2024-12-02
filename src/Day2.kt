import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d2part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d2part2() }}ms")
}

fun d2part1() {
	val input = readInput("day2.txt")
	var total = 0

	nextLine@
	for (line in input.lines()) {
		val reports = line.extractNumbersSeparated()
		var prev = reports[0]
		val increasing = reports[1] > reports[0]

		for (report in reports.skip(1)) {
			if (increasing != report > prev || abs(report - prev) !in 1..3)
				continue@nextLine

			prev = report
		}

		total++
	}

	total.print()
}

fun d2part2() {
	val input = readInput("day2.txt")
	var total = 0

	nextLine@
	for (line in input.lines()) {
		val reports = line.extractNumbersSeparated()
		var prev = reports[0]
		val increasing = reports[1] > reports[0]
		var badDetected = false

		nextReport@
		for (report in reports.skip(1)) {
			if (increasing != report > prev || abs(report - prev) !in 1..3) {
				if (!badDetected) {
					badDetected = true
					prev = report
					continue@nextReport
				}
				continue@nextLine
			}
			prev = report
		}

		total++
	}

	total.print()
}
