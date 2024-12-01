import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d1part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d1part2() }}ms")
}

fun d1part1() {
	val input = readInput("day1.txt")
	val lines = input.lines()

	val list1 = mutableListOf<Int>()
	val list2 = mutableListOf<Int>()

	for (line in lines) {
		val (val1, val2) = line.split("   ")
		list1 += val1.toInt()
		list2 += val2.toInt()
	}

	list1.sort()
	list2.sort()

	var sum = 0
	for (i in 0..list1.lastIndex) {
		sum += abs(list1[i] - list2[i])
	}

	sum.print()
}

fun d1part2() {
	val input = readInput("day1.txt")
	val lines = input.lines()

	val list1 = mutableListOf<Int>()
	val list2 = mutableListOf<Int>()

	for (line in lines) {
		val (val1, val2) = line.split("   ")
		list1 += val1.toInt()
		list2 += val2.toInt()
	}

	var sum = 0
	for (num in list1) {
		sum += num * list2.count { it == num }
	}
	sum.print()
}
