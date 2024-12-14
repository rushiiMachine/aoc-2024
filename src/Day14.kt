import me.carleslc.kotlinextensions.arrays.matrix
import java.io.File
import kotlin.system.measureTimeMillis

const val width = 101
const val height = 103

fun main() {
	println("Part 1 in ${measureTimeMillis { d14part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d14part2() }}ms")
}

data class Robot(var x: Int, var y: Int, val velX: Int, val velY: Int)

fun printMatrix(robots: List<Robot>): String {
	val strBuilder = StringBuilder()
	val map = matrix(height, width) { y, x -> robots.count { it.x == x && it.y == y } }
	for (row in map) {
		strBuilder.append(row.joinToString(separator = "") { if (it == 0) "." else it.toString() })
		strBuilder.append("\n")
	}
	return strBuilder.toString()
}

fun d14part1() {
	val input = readInput("day14.txt")
	val regex = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex()

	val robots = mutableListOf<Robot>()

	for (line in input.lines()) {
		val (x, y, velX, velY) = regex.find(line)!!.groupValues.drop(1).map { it.toInt() }
		robots += Robot(x, y, velX, velY)
	}

	for (i in 0..<100) {
		for (robot in robots) {
			robot.x += robot.velX
			robot.y += robot.velY

			if (robot.x >= width) robot.x %= width
			else if (robot.x < 0) robot.x += width

			if (robot.y >= height) robot.y %= height
			else if (robot.y < 0) robot.y += height
		}
	}

	val q1 = robots.count { it.x < width / 2 && it.y < height / 2 }
	val q2 = robots.count { it.x > width / 2 && it.y < height / 2 }
	val q3 = robots.count { it.x < width / 2 && it.y > height / 2 }
	val q4 = robots.count { it.x > width / 2 && it.y > height / 2 }

	(q1 * q2 * q3 * q4).print()
}

fun d14part2() {
	val input = readInput("day14.txt")
	val (width, height) = 101 to 103
	val regex = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex()
	val robots = mutableListOf<Robot>()

	for (line in input.lines()) {
		val (x, y, velX, velY) = regex.find(line)!!.groupValues.drop(1).map { it.toInt() }
		robots += Robot(x, y, velX, velY)
	}

	val file = File("./src/input/day14.output.txt")
	val writer = file.writer()
	for (i in 0..<10000) {
		for (robot in robots) {
			robot.x += robot.velX
			robot.y += robot.velY

			if (robot.x >= width) robot.x %= width
			else if (robot.x < 0) robot.x += width

			if (robot.y >= height) robot.y %= height
			else if (robot.y < 0) robot.y += height
		}

		println("Processing ${i + 1}th second")
		if (i > 5000) {
			writer.write("${i + 1} ->\n")
			writer.write(printMatrix(robots))
			writer.flush()
		}
	}
	writer.close()

	val reader = file.bufferedReader(bufferSize = 1024 * 1024 * 10)
	var seconds = -1

	for (line in reader.lineSequence()) {
		if (line.endsWith(" ->")) {
			seconds = line.removeSuffix(" ->").toInt()
		}
		if (line.contains("111111111111111111111")) {
			println(seconds)
			break
		}
	}
}
