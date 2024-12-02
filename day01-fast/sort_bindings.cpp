#include <stdint.h>
#include "x86-simd-sort/src/xss-common-includes.h"
#include "x86-simd-sort/src/xss-common-qsort.h"
#include "x86-simd-sort/src/xss-common-argsort.h"
#include "x86-simd-sort/src/avx2-32bit-qsort.hpp"

extern "C" void simd_qsort_uint32(uint32_t *arr, size_t size)
{
    avx2_qsort(arr, size);
}
