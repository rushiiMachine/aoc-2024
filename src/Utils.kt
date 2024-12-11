@file:Suppress("unused")

import me.carleslc.kotlinextensions.arrays.*
import me.carleslc.kotlinextensions.number.log10
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.abs

// TODO: take inspiration from eagely's AOC utils
//       and this: https://github.com/mdekaste/AdventOfCode2024/blob/master/src/year2024/day4/Day4.kt

fun readInput(name: String) = File("./src/input", name)
	.readText().replace("\r\n", "\n").trim()

fun String.extractIntSeparated(): List<Int> = split(Regex("\\D+")).filter { it.isNotBlank() }.map { it.toInt() }
fun String.extractLongSeparated(): List<Long> = split(Regex("\\D+")).filter { it.isNotBlank() }.map { it.toLong() }

fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
	.toString(16)
	.padStart(32, '0')

fun Any?.print() = println(this)

/**
 * Alias for [List.drop] functionality.
 */
fun <T> List<T>.skip(n: Int) = subList(n, size)

/**
 * Write this matrix to the console like:
 * ```
 * [0, 1, 2]
 * [3, 4, 5]
 * [6, 7, 8]
 * ```
 */
fun <T> Matrix<T>.printMatrix() {
	for (row in this) {
		println(row.contentToString())
	}
}

/**
 * Returns a new matrix that has the [Summed-area Table](https://en.wikipedia.org/wiki/Summed-area_table)
 * algorithm applied. For use with [summedAreaTableLargestSubarea] and [summedAreaTableSumOf].
 */
fun Matrix<Int>.summedAreaTable(): Matrix<Int> {
	val matrix = this.copy()

	for ((y, row) in matrix.withIndex()) {
		for ((x, value) in row.withIndex()) {
			var sum = value
			if (x > 0) sum += matrix[y][x - 1]
			if (y > 0) sum += matrix[y - 1][x]
			if (x > 0 && y > 0) sum -= matrix[y - 1][x - 1]

			matrix[y][x] = sum
		}
	}

	return matrix
}

/**
 * Find the sum of the region specified by [x1],[y1] (top left corner) and [x2] [y2] (bottom right corner)
 * @receiver A [Matrix] that has had the summed area table algorithm applied ([summedAreaTable]).
 * @return The sum of the subarea specified by the points.
 */
fun Matrix<Int>.summedAreaTableSumOf(x1: Int, y1: Int, x2: Int, y2: Int): Int {
	require(y1 >= 0)
	require(x1 >= 0)
	require(x1 <= x2)
	require(y1 <= y2)

	var sum = this[y2][x2]
	if (y1 > 0 && x1 > 0) sum += this[y1 - 1][x1 - 1]
	if (x1 > 0) sum -= this[y2][x1 - 1]
	if (y1 > 0) sum -= this[y1 - 1][x2]

	return sum
}

/**
 * Find the sum of the largest sub-region in the matrix.
 * @receiver A [Matrix] that has had the summed area table algorithm applied ([summedAreaTable]).
 * @return The sum of the largest subarea with the specified dimensions.
 */
fun Matrix<Int>.summedAreaTableLargestSubarea(width: Int, height: Int): Int {
	require(width > 0)
	require(height > 0)
	require(rowSize >= width)
	require(columnSize >= height)

	var max = -1
	for (y in rows) {
		for (x in columns) {
			var sum = this[y][x]
			sum += this.getOrNull(y - height)?.getOrNull(x - width) ?: continue
			sum -= this[y].getOrNull(x - width) ?: continue
			sum -= this.getOrNull(y - height)?.get(x) ?: continue

			if (sum > max) {
				max = sum
			}
		}
	}
	return max
}

/**
 * Matrix for testing.
 */
val SAMPLE_MATRIX: Matrix<Int>
	get() = array2dOf<Int>(
		arrayOf(3, 1, 4, 1, 5, 9),
		arrayOf(2, 6, 5, 3, 5, 8),
		arrayOf(9, 7, 9, 3, 2, 3),
		arrayOf(8, 4, 6, 2, 6, 4),
		arrayOf(3, 3, 8, 3, 2, 7),
		arrayOf(9, 5, 0, 2, 8, 8),
	)

/**
 * Count the amount of digits in this number. Negative sign is disregarded.
 */
fun Int.length(): Int = when(this) {
	0 -> 1
	else -> abs(toDouble()).log10().toInt() + 1
}

/**
 * Count the amount of digits in this number. Negative sign is disregarded.
 */
fun Long.length(): Int = when(this) {
	0L -> 1
	else -> abs(toDouble()).log10().toInt() + 1
}
