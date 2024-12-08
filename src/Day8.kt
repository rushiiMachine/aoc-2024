import me.carleslc.kotlinextensions.arrays.*
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d8part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d8part2() }}ms")
}

fun d8part1() {
	val input = readInput("day8.txt").lines()

	val antiNodes = matrix<Boolean>(input.size, input[0].length) { _, _ -> false }

	for (antiNodeY in antiNodes.rows) {
		for (antiNodeX in antiNodes.columns) {

			antenna@ for (antennaY in antiNodes.rows) {
				for (antennaX in antiNodes.columns) {
					if (antennaY == antiNodeY || antennaX == antiNodeX) continue
					if (input[antennaY][antennaX] in arrayOf('.', '#')) continue

					val nextAntennaY = antennaY + (antennaY - antiNodeY)
					val nextAntennaX = antennaX + (antennaX - antiNodeX)

					if ((input.getOrNull(nextAntennaY)?.getOrNull(nextAntennaX) ?: continue) == input[antennaY][antennaX]) {
						antiNodes[antiNodeY][antiNodeX] = true
						break@antenna
					}
				}
			}

		}
	}

	antiNodes.sumOf { it.count { it } }.print()
}

fun d8part2() {
	val input = readInput("day8.txt").lines()

	val antiNodes = matrix<Boolean>(input.size, input[0].length) { _, _ -> false }

	for (antennaY in antiNodes.rows) {
		for (antennaX in antiNodes.columns) {
			if (input[antennaY][antennaX] in arrayOf('.', '#')) continue

			for (nextAntennaY in antiNodes.rows) {
				for (nextAntennaX in antiNodes.columns) {
					if (input[antennaY][antennaX] != input[nextAntennaY][nextAntennaX]) continue

					for (i in -1000 until 1000) {
						val antiNodeY = antennaY + (nextAntennaY - antennaY) * i
						val antiNodeX = antennaX + (nextAntennaX - antennaX) * i

						try {
							antiNodes[antiNodeY][antiNodeX] = true
						} catch (_: Exception) {
						}
					}
				}
			}
		}
	}

	antiNodes.sumOf { it.count { it } }.print()
}
