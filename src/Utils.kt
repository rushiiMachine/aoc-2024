@file:Suppress("unused")

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun readInput(name: String) = File("./src/input", name)
	.readText().replace("\r\n", "\n").trim()

fun String.extractNumbersSeparated() = split(Regex("\\D+")).filter { it.isNotBlank() }.map { it.toInt() }

fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
	.toString(16)
	.padStart(32, '0')

fun Any?.print() = println(this)

fun <T> List<T>.skip(n: Int) = subList(n, size)
