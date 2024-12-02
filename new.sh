contents="import kotlin.system.measureTimeMillis

fun main() {
	println(\"Part 1 in \${measureTimeMillis { d$1part1() }}ms\")
	println(\"Part 2 in \${measureTimeMillis { d$1part2() }}ms\")
}

fun d$1part1() {
	val input = readInput(\"day$1.txt\")
}

fun d$1part2() {
}"

touch "./src/Day$1.kt"
touch "./src/input/day$1.txt"
echo "$contents" > "./src/Day$1.kt"
