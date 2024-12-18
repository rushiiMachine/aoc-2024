import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d17part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d17part2() }}ms")
}

fun runVM(instructions: Array<Int>, aReg: Long): List<Long> {
	val registers = arrayOf(aReg, 0L, 0L)
	val output = ArrayList<Long>(instructions.size)
	var ptr = 0
	var loopDetector = Long.MAX_VALUE

	while (ptr < instructions.size - 1) {
		val opcode = instructions[ptr]
		val literalOperand = instructions[ptr + 1]
		val comboOperand = when (literalOperand) {
			0, 1, 2, 3 -> literalOperand.toLong()
			4, 5, 6 -> registers[literalOperand - 4]
			else -> null
		}

		when (opcode) {
			0 -> registers[0] /= pow(2L, comboOperand!!) // adv (div)
			1 -> registers[1] = registers[1] xor literalOperand.toLong() // bxl
			2 -> registers[1] = comboOperand!!.mod(8).toLong() // bst

			3 -> { // jnz
				if (registers[0] == loopDetector) {
					break
				} else if (registers[0] != 0L) {
					ptr = literalOperand
					loopDetector = registers[0]
				} else {
					ptr++
				}
			}

			4 -> registers[1] = registers[1] xor registers[2] // bxc
			5 -> output.add(comboOperand!!.mod(8).toLong()) // out
			6 -> registers[1] = registers[0] / pow(2, comboOperand!!) // bdv
			7 -> registers[2] = registers[0] / pow(2, comboOperand!!) // cdv
		}

		if (opcode != 3) {
			ptr += 2
			loopDetector = Long.MAX_VALUE
		}
	}
	return output
}

fun d17part1() {
	val lines = readInput("day17.txt").lines()
	val aReg = lines.first().extractLongSeparated().single()
	val instructions = lines.skip(4).first().extractIntSeparated().toTypedArray()

	runVM(instructions, aReg)
		.joinToString(",")
		.print()
}

fun d17part2() {
	val lines = readInput("day17.txt").lines()
	val instructions = lines.skip(4).first().extractIntSeparated().toTypedArray()
	val initialRange = pow(8L, instructions.size.toLong() - 1L)..<pow(8L, instructions.size.toLong())

	fun findMatches(aRegRange: LongRange, xIdx: Int): Sequence<LongRange> = sequence {
		val stepSize = pow(8L, xIdx.toLong())

		for (aReg in aRegRange step stepSize) {
			if (runVM(instructions, aReg)[xIdx] != instructions[xIdx].toLong())
				continue

			yield(aReg..<(aReg + stepSize))
		}
	}

	// Queue of A register ranges to test at a specific xIdx, sorted by the lowest minimum value
	val searchRanges = PriorityQueue<Pair<LongRange, Int>>(compareByDescending { (range) -> range.first })
	searchRanges.add(initialRange to instructions.lastIndex)

	while (searchRanges.isNotEmpty()) {
		val (range, xIdx) = searchRanges.remove()

		if (xIdx == -1) {
			println("Found candidate A register: ${range.single()}")
			continue
		}

		// Queue up newly found possible ranges within the larger range
		for (newRange in findMatches(range, xIdx)) {
			println("found new search range at xIdx=${xIdx - 1}: $newRange")
			searchRanges.add(newRange to xIdx - 1)
		}
	}
}
