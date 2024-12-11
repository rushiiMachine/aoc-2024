import arrow.core.fold
import com.google.common.math.IntMath.pow
import me.carleslc.kotlinextensions.number.even
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d11part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d11part2() }}ms")
}

fun d11part1() {
	val input = readInput("day11.txt")
	val lines = input.lines()

	val stones = lines[0].extractLongSeparated().toMutableList()

	repeat(25) {
		var i = 0
		while (i <= stones.lastIndex) {
			val num = stones[i]

			when {
				num == 0L -> stones[i] = 1
				num.length().even() -> {
					val str = num.toString()
					stones[i] = str.substring(0, str.length / 2).toLong()
					stones.add(i + 1, str.substring(str.length / 2, str.length).toLong())

					i++
				}

				else -> {
					stones[i] *= 2024L
				}
			}
			i++
		}
	}

	val total = stones.size
	total.print()
}

fun d11part2() {
	val input = readInput("day11.txt")

	var stones = HashMap<Long, Long>(1000000)
	var newStones = HashMap<Long, Long>(1000000)
	for (num in input.extractLongSeparated()) {
		stones[num] = 1
	}

	repeat(75) {
		for ((stoneValue, stoneCount) in stones) {
			when {
				stoneValue == 0L -> newStones.compute(1) { _, value ->
					stoneCount + (value ?: 0)
				}

				stoneValue.length().even() -> {
					val digits = stoneValue.length()
					val half = pow(10, digits / 2)
					val leftHalf = stoneValue / half
					val rightHalf = stoneValue - leftHalf * half

					newStones.compute(leftHalf) { _, value -> stoneCount + (value ?: 0) }
					newStones.compute(rightHalf) { _, value -> stoneCount + (value ?: 0) }
				}

				else -> newStones.compute(stoneValue * 2024) { _, value ->
					stoneCount + (value ?: 0)
				}
			}
		}

		val tmp = stones
		stones = newStones
		newStones = tmp
		newStones.clear()
	}

	val total = stones.fold(0L) { a, b -> a + b.value }
	total.print()
}
