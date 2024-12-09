import me.carleslc.kotlinextensions.number.even
import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {
	println("Part 1 in ${measureTimeMillis { d9part1() }}ms")
	println("Part 2 in ${measureTimeMillis { d9part2() }}ms")
}

fun d9part1() {
	val input = readInput("day9.txt")
	val ids = mutableListOf<Int>()

	for ((index, value) in input.withIndex()) {
		if (index.even()) {
			val id = index / 2
			for (i in 0 until value.digitToInt()) {
				ids += id
			}
		} else {
			for (i in 0 until value.digitToInt()) {
				ids += -1
			}
		}
	}

	for (i in ids.indices.reversed()) {
		val id = ids[i]
		if (id == -1) continue

		val firstEmptyIdx = ids.indexOf(-1)
		if (firstEmptyIdx < 0 || i <= firstEmptyIdx) break

		ids[firstEmptyIdx] = id
		ids[i] = -1
	}

	val total = ids.withIndex().sumOf { (idx, value) -> idx.toLong() * max(0, value) }

	total.print()
}

fun d9part2() {
	data class Page(var blocks: Int, val id: Int)

	val input = readInput("day9.txt")
	val pages = mutableListOf<Page>()

	for ((index, value) in input.withIndex()) {
		pages += Page(value.digitToInt(), if (index.even()) index / 2 else -1)
	}

	var i = pages.lastIndex
	while (i >= 0) {
		val page = pages[i]
		if (page.id == -1) {
			i--
			continue
		}

		val firstFittingPageIdx = pages.indexOfFirst { it.id == -1 && it.blocks >= page.blocks }
		if (firstFittingPageIdx < 0 || firstFittingPageIdx >= i) {
			i--
			continue
		}

		pages[firstFittingPageIdx].blocks -= page.blocks
		if (pages[firstFittingPageIdx].blocks == 0) {
			pages.removeAt(firstFittingPageIdx)
			i--
		}

		pages.removeAt(i)
		pages.add(i, Page(page.blocks, -1))
		pages.add(firstFittingPageIdx, page)
		i--
	}

	val ids = pages.flatMap { page -> List(page.blocks) { page.id } }
	val total = ids.withIndex().sumOf { (idx, value) -> idx.toLong() * max(0, value) }

	total.print()
}