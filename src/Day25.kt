import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d25part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d25part2() }}ms")
}

fun d25part1() {
	val inputs = readInput("day25.txt").split("\n\n")
	val (locks, keys) = inputs.partition { it[0] == '#' }

	val combinations = locks.sumOf { lock ->
		keys.count { key ->
			lock.indices.none { i -> key[i] == '#' && key[i] == lock[i] }
		}
	}
	combinations.print()
}

fun d25part2() {}
