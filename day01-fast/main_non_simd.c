#include <math.h>
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <windows.h>
#include "main.h"

uint32_t parse5DigitInt(const char *buf);

int main()
{
    const double BENCHMARK_RUNS = 100000;

    FILE *fp;
    char buf[13 * 1000];
    fopen_s(&fp, "input.txt", "rb");
    fread(buf, 13 * 1000, 1, fp);
    fclose(fp);

    HANDLE proc = GetCurrentProcess();
    DWORD_PTR affinityMask = 1 << 14;
    SetProcessAffinityMask(proc, affinityMask);

    LARGE_INTEGER start, end, frequency;
    double duration1 = 0.0, duration2 = 0.0;
    uint64_t result;

    QueryPerformanceFrequency(&frequency);

    for (size_t i = 0; i < BENCHMARK_RUNS; i++)
    {
        QueryPerformanceCounter(&start);
        result = part1(buf);
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
    printf_s("Part 2 ran for %.4f us\n", duration2 / BENCHMARK_RUNS);

    return 0;
}

inline uint32_t parse5DigitInt(const char *buf)
{
    const uint8_t LENGTH = 5;
    uint32_t value = 0;

#pragma clang loop unroll(full)
    for (uint8_t i = 0; i < LENGTH; i++)
    {
        value += pow(10, LENGTH - 1 - i) * (buf[i] - '0');
    }

    return value;
}

struct minData
{
    uint32_t value;
    size_t index;
};

struct minData minAt(uint32_t *arr, size_t length)
{
    struct minData min = {
        .value = arr[0],
        .index = 0,
    };

    for (size_t i = 0; i < length; i++)
    {
        uint32_t value = arr[i];
        min.index = value < min.value ? i : min.index;
        min.value = value < min.value ? value : min.value;
    }

    return min;
}

uint32_t left[1000], right[1000];

uint64_t part1(const char *buf)
{
    uint32_t total = 0;
    size_t offset = 0;

    for (uint32_t i = 0; i < 1000; i++)
    {
        left[i] = parse5DigitInt(buf + (14 * i));
        right[i] = parse5DigitInt(buf + 8 + (14 * i));
    }

    for (uint32_t i = 0; i < 1000; i++)
    {
        struct minData leftMinData = minAt((uint32_t *)(left + offset), 1000 - offset);
        struct minData rightMinData = minAt((uint32_t *)(right + offset), 1000 - offset);

        left[leftMinData.index + offset] = left[offset];
        left[offset] = leftMinData.value;

        right[rightMinData.index += offset] = right[offset];
        right[offset] = rightMinData.value;

        total += abs((int32_t)leftMinData.value - (int32_t)rightMinData.value);
        offset++;
    }

    return total;
}

inline uint32_t countConsecutive(uint32_t *arr, uint32_t *end)
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
        uint32_t leftValueCount = countConsecutive((uint32_t *)(left + leftOffset), (uint32_t *)(left + 1000));

        while (right[rightOffset] < leftValue && rightOffset < 1000)
            rightOffset++;

        if (rightOffset == 1000 || right[rightOffset] != leftValue)
        {
            ++leftOffset;
            continue;
        }

        uint32_t rightValueCount = countConsecutive((uint32_t *)(right + rightOffset), (uint32_t *)(right + 1000));

        leftOffset += leftValueCount;
        rightOffset += rightValueCount;
        total += leftValue * rightValueCount * leftValueCount;
    }

    return total;
}
