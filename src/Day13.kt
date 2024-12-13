import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.get
import kotlin.math.roundToLong
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d13part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d13part2() }}ms")
}

// This would've been neat but too computationally expensive

//fun d13part1() {
//	val input = readInput("day13.txt")
//	var total = 0
//
//	val regex = """Button A: X\+(\d+), Y\+(\d+)\nButton B: X\+(\d+), Y\+(\d+)\nPrize: X=(\d+), Y=(\d+)""".toRegex()
//
//	for (machine in input.split("\n\n")) {
//		val (buttonAX, buttonAY, buttonBX, buttonBY, prizeX, prizeY) = regex.find(machine)!!.groupValues.drop(1).map { it.toInt() }
//
//		fun testInputs(btnInputs: List<Char>): Boolean {
//			var x = 0
//			var y = 0
//
//			for (btn in btnInputs) {
//				when (btn) {
//					'A' -> {
//						x += buttonAX
//						y += buttonAY
//					}
//					'B' -> {
//						x += buttonBX
//						y += buttonBY
//					}
//				}
//			}
//
//			return x == prizeX && y == prizeY
//		}
//
//		val matching = (1..1000).asSequence()
//			.flatMap { println("$it"); listOf('A', 'B').cartesianProduct(repeat = it).filter(::testInputs) }
//			.first()
//
//		matching.print()
//		total += matching.count { it == 'A' } * 3 + matching.count { it == 'B' }
//	}
//}
fun d13part1() {
	val input = readInput("day13.txt")
	val regex = """Button A: X\+(\d+), Y\+(\d+)\nButton B: X\+(\d+), Y\+(\d+)\nPrize: X=(\d+), Y=(\d+)""".toRegex()
	var total = 0

	for (machine in input.split("\n\n")) {
		val (buttonAX, buttonAY, buttonBX, buttonBY, prizeX, prizeY) = regex.find(machine)!!.groupValues.drop(1).map { it.toInt() }

		var minTokens = Int.MAX_VALUE
		for (aIdx in 0..101) {
			for (bIdx in 0..101) {
				val ySum = aIdx * buttonAY + bIdx * buttonBY
				val xSum = aIdx * buttonAX + bIdx * buttonBX
				if (ySum != prizeY || xSum != prizeX) continue

				val tokens = aIdx * 3 + bIdx
				if (tokens < minTokens) {
					minTokens = tokens
				}
			}
		}

		if (minTokens == Int.MAX_VALUE) continue
		total += minTokens
	}

	total.print()
}

private operator fun <E> List<E>.component6(): E {
	return this[5]
}

fun d13part2() {
	val input = readInput("day13.txt")
	val regex = """Button A: X\+(\d+), Y\+(\d+)\nButton B: X\+(\d+), Y\+(\d+)\nPrize: X=(\d+), Y=(\d+)""".toRegex()
	var total = 0L

	for (machine in input.split("\n\n")) {
		var (buttonAX, buttonAY, buttonBX, buttonBY, prizeX, prizeY) = regex.find(machine)!!
			.groupValues
			.drop(1)
			.map { it.toLong() }

		prizeX += 10000000000000L
		prizeY += 10000000000000L

		val a = mk.ndarray(mk[mk[buttonAX, buttonBX], mk[buttonAY, buttonBY]])
		val b = mk.ndarray(mk[prizeX, prizeY])

		val out = mk.linalg.solve(a, b)
		val outD1 = out.asD1Array()

		// fuck floating points
		if (outD1[0].rem(1.0) in 1e-4..(1 - 1e-4) || outD1[1].rem(1.0) in 1e-4..(1 - 1e-4)) continue
		total += (outD1[0].roundToLong() * 3 + outD1[1].roundToLong())
	}

	total.print()
}
