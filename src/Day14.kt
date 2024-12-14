import me.carleslc.kotlinextensions.arrays.matrix
import java.io.File
import kotlin.system.measureTimeMillis

const val WIDTH = 101
const val HEIGHT = 103

fun main() {
	println("Part 1 in ${measureTimeMillis { d14part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d14part2() }}ms")
}

data class Robot(var x: Int, var y: Int, val velX: Int, val velY: Int)

fun printMatrix(robots: List<Robot>): String {
	val map = matrix(HEIGHT, WIDTH) { y, x -> robots.count { it.x == x && it.y == y } }

	val strBuilder = StringBuilder()
	for (row in map) {
		strBuilder.append(row.joinToString(separator = "") { if (it == 0) "." else it.toString() })
		strBuilder.append("\n")
	}
	return strBuilder.toString()
}

fun d14part1() {
	val input = readInput("day14.txt")
	val regex = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex()

	val robots = input.lines()
		.map { line -> regex.find(line)!!.groupValues.drop(1).map { it.toInt() } }
		.map { (x, y, velX, velY) -> Robot(x, y, velX, velY) }
		.toMutableList()

	for (i in 0..<100) {
		for (robot in robots) {
			// Add velocity and wrap values
			robot.x = (robot.x + robot.velX).mod(WIDTH)
			robot.y = (robot.y + robot.velY).mod(HEIGHT)
		}
	}

	val q1 = robots.count { it.x < WIDTH / 2 && it.y < HEIGHT / 2 }
	val q2 = robots.count { it.x > WIDTH / 2 && it.y < HEIGHT / 2 }
	val q3 = robots.count { it.x < WIDTH / 2 && it.y > HEIGHT / 2 }
	val q4 = robots.count { it.x > WIDTH / 2 && it.y > HEIGHT / 2 }

	(q1 * q2 * q3 * q4).print()
}

fun d14part2() {
	val input = readInput("day14.txt")
	val regex = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex()
	val outputFile = File("./src/input/day14.output.txt")

	val robots = input.lines()
		.map { line -> regex.find(line)!!.groupValues.drop(1).map { it.toInt() } }
		.map { (x, y, velX, velY) -> Robot(x, y, velX, velY) }
		.toMutableList()

	val writer = outputFile.writer()
	for (i in 0..<10000) {
		for (robot in robots) {
			// Add velocity and wrap values
			robot.x = (robot.x + robot.velX).mod(WIDTH)
			robot.y = (robot.y + robot.velY).mod(HEIGHT)
		}

		println("Processing ${i + 1}th second")
		if (i > 5000) {
			writer.write("${i + 1} ->\n")
			writer.write(printMatrix(robots))
			writer.flush()
		}
	}
	writer.close()

	val reader = outputFile.bufferedReader()
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
