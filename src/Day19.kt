import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d19part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d19part2() }}ms")
}

fun d19part1() {
	val input = readInput("day19.txt")
	val towels = input.split("\n\n")[0].split(", ")
	val designs = input.split("\n\n")[1].lines()

	fun testDesign(design: String): Boolean {
		if (design.isEmpty()) return true

		return towels
			.filter { design.startsWith(it) }
			.any { testDesign(design.substring(it.length)) }
	}

	designs.count(::testDesign).print()
}

fun d19part2() {
	val input = readInput("day19.txt")
	val towels = input.split("\n\n")[0].split(", ")
	val designs = input.split("\n\n")[1].lines()

	val cache = HashMap<String, Long>()
	fun countCombinations(design: String): Long {
		if (design.isEmpty()) return 1
		if (cache.contains(design)) return cache[design]!!

		return towels
			.filter { design.startsWith(it) }
			.sumOf { countCombinations(design.substring(it.length)) }
			.also { cache[design] = it }
	}

	designs.sumOf(::countCombinations).print()
}
