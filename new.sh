contents="fun main() {
	d$1part1()
	d$1part2()
}

fun d$1part1() {
	val input = readInput(\"day$1.txt\")
}

fun d$1part2() {
	val input = readInput(\"day$1.txt\")
}"

touch "./src/Day$1.kt"
touch "./src/input/day$1.txt"
echo "$contents" > "./src/Day$1.kt"
