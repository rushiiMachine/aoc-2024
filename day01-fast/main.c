#include "main.h"
#include "sort_bindings.h"
#include <immintrin.h>
#include <stddef.h>
#include <stdint.h>
#include <stdio.h>
#include <windows.h>

// 1000 lines of input + 128 to be safe when copying into __m128i
#define AOC_INPUT_LENGTH ((size_t)(14 * 1000 - 1))
char AOC_INPUT[AOC_INPUT_LENGTH + 128] __attribute__((__aligned__(32)));

// Parsed left & right lists
uint32_t left[1000] __attribute__((__aligned__(32)));
uint32_t right[1000] __attribute__((__aligned__(32)));

int main()
{
	FILE* fp;
	if (fopen_s(&fp, "input.txt", "rb") != 0)
	{
		fprintf_s(stderr, "Failed to open input.txt for AOC input\n");
		return 1;
	}
	if (AOC_INPUT_LENGTH > fread(AOC_INPUT, sizeof(char), AOC_INPUT_LENGTH, fp))
	{
		fprintf_s(stderr, "AOC input is insufficient length\n");
		return 1;
	}
	fclose(fp);

	// Bind to core 2
	SetProcessAffinityMask(GetCurrentProcess(), 1 << 17);

	LARGE_INTEGER start, end, frequency;
	double duration1 = 0.0, duration2 = 0.0;
	uint64_t result;

	QueryPerformanceFrequency(&frequency);

	const size_t BENCHMARK_RUNS = 100000;

	for (size_t i = 0; i < BENCHMARK_RUNS; i++)
	{
		QueryPerformanceCounter(&start);
		result = part1();
		QueryPerformanceCounter(&end);
		duration1 += (end.QuadPart - start.QuadPart) * 1000000.0 / frequency.QuadPart;
	}
	printf_s("Part 1 result: %u\n", result);

	for (size_t i = 0; i < BENCHMARK_RUNS; i++)
	{
		QueryPerformanceCounter(&start);
		result = part2();
		QueryPerformanceCounter(&end);
		duration2 += (end.QuadPart - start.QuadPart) * 1000000.0 / frequency.QuadPart;
	}
	printf_s("Part 2 result: %u\n", result);

	printf_s("Part 1 ran for %.4f us on average\n", duration1 / BENCHMARK_RUNS);
	printf_s("Part 2 ran for %.4f us on average\n", duration2 / BENCHMARK_RUNS);

	return 0;
}

void parseInput()
{
	__m256i mulMask = _mm256_setr_epi32(10000, 1000, 100, 10, 10000, 1000, 100, 10);
	__m128i subMask = _mm_setr_epi8('0', '0', '0', '0', '0', 0, 0, 0, '0', '0', '0', '0', '0', 0, 0, 0);
	__m128i subtracted128, intermediate128;
	__m256i intermediate256;

	for (uint32_t i = 0; i < 1000; ++i)
	{
		// Load from inside buffer unaligned & subtract ascii '0' to get integer value
		intermediate128 = _mm_loadu_si128((__m128i_u*)(AOC_INPUT + (14 * i)));
		subtracted128 = _mm_sub_epi8(intermediate128, subMask);

		// Pack 4 most significant bytes from left & right into a single register,
		// then expand each uint8_t into uint32_t, filling up the entire register.
		// _mm256_permute2x128_si256 explanation: https://www.cs.virginia.edu/~cr4bd/3330/F2018/simdref.html
		intermediate256 = _mm256_permute2x128_si256(
			_mm256_cvtepu8_epi32(subtracted128),					// Left side in first 128-bits
			_mm256_cvtepu8_epi32(_mm_srli_si128(subtracted128, 8)), // Right side in first 128-bits
			0b00100100												// Select first half of a, first half of b
		);

		// Apply multiplication to increase each number to it's proper base-10 value
		intermediate256 = _mm256_mullo_epi32(intermediate256, mulMask);

		// Partial sum each half of this register.
		// _mm256_srli_si256 shifts each 128-bit lane separately, which is perfect (left & right are packed side by side).
		// This saves us ~0.25us compared to 4 dependent adds (ie, largest[0] + largest[1] + largest[2] + largest[3])
		// Based on https://stackoverflow.com/a/10589306/13964629
		intermediate256 = _mm256_add_epi32(intermediate256, _mm256_srli_si256(intermediate256, 4));
		intermediate256 = _mm256_add_epi32(intermediate256, _mm256_srli_si256(intermediate256, 8));
		intermediate256 = _mm256_add_epi32(intermediate256, _mm256_srli_si256(intermediate256, 16));
		intermediate256 = _mm256_add_epi32(intermediate256, _mm256_srli_si256(intermediate256, 32));

		// Combine left & right back into the most significant part + single digit that was left out earlier
		left[i] = _mm256_extract_epi32(intermediate256, 0) + _mm_extract_epi8(subtracted128, 4);
		right[i] = _mm256_extract_epi32(intermediate256, 4) + _mm_extract_epi8(subtracted128, 12);
	}
}

uint64_t part1()
{
	parseInput();

	// Sort both sides ascending
	simd_qsort_uint32(left, 1000);
	simd_qsort_uint32(right, 1000);

	uint64_t total = 0;
	for (size_t i = 0; i < 1000 / (256 / 32); i++)
	{
		// These are already aligned to 32 bytes
		__m256i* dataLeft = (__m256i*)left + i;
		__m256i* dataRight = (__m256i*)right + i;

		// Subtract 8 signed integers and get the absolute values
		__m256i absDiff = _mm256_abs_epi32(_mm256_sub_epi32(*dataLeft, *dataRight));

		// Sum all uint32_t values in register
		// Partial sum trick doesn't work here, slows down by 0.05us
		uint32_t* data = (uint32_t*)&absDiff;
		total += data[0] + data[1] + data[2] + data[3] + data[4] + data[5] + data[6] + data[7];
	}
	return total;
}

inline uint32_t countConsecutive(uint32_t* arr, uint32_t* end)
{
	uint32_t value = (arr++)[0],
			 total = 1;

	while (arr < end && value == *(arr++))
		++total;

	return total;
}

uint64_t part2()
{
	uint64_t total = 0;
	size_t leftOffset = 0, rightOffset = 0;

	while (leftOffset < 1000)
	{
		uint32_t leftValue = left[leftOffset];
		uint32_t leftValueCount = countConsecutive((uint32_t*)(left + leftOffset), (uint32_t*)(left + 1000));

		while (right[rightOffset] < leftValue && rightOffset < 1000)
			rightOffset++;

		if (rightOffset == 1000 || right[rightOffset] != leftValue)
		{
			++leftOffset;
			continue;
		}

		uint32_t rightValueCount = countConsecutive((uint32_t*)(right + rightOffset), (uint32_t*)(right + 1000));

		leftOffset += leftValueCount;
		rightOffset += rightValueCount;
		total += leftValue * rightValueCount * leftValueCount;
	}

	return total;
}
