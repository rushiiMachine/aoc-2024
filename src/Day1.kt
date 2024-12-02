import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d1part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d1part2() }}ms")
}

fun d1part1() {
	val input = readInput("day1.txt")
	val lines = input.lines()

	val left = mutableListOf<Int>()
	val right = mutableListOf<Int>()

	for (line in lines) {
		val (val1, val2) = line.split("   ")
		left += val1.toInt()
		right += val2.toInt()
	}

	left.sort()
	right.sort()

	val sum = left
		.zip(right) { a, b -> abs(a - b) }
		.sum()

	sum.print()
}

fun d1part2() {
	val input = readInput("day1.txt")
	val lines = input.lines()

	val left = mutableListOf<Int>()
	val right = mutableListOf<Int>()

	for (line in lines) {
		val (val1, val2) = line.split("   ")
		left += val1.toInt()
		right += val2.toInt()
	}

	var sum = 0
	for (num in left) {
		sum += num * right.count { it == num }
	}
	sum.print()
}
