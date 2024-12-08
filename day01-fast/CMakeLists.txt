cmake_minimum_required(VERSION 3.29)
project(simd_test C CXX)

set(SIMD_FLAGS "-mavx -mavx2 -msse -msse2 -msse3 -mssse3 -msse4.1 -msse4.2")

set(CMAKE_BUILD_TYPE Release)
set(CMAKE_C_STANDARD 23)
set(CMAKE_C_FLAGS "-Wall -Wextra ${SIMD_FLAGS}")
set(CMAKE_C_FLAGS_DEBUG "-g")
set(CMAKE_C_FLAGS_RELEASE "-O3 -fno-stack-protector")
set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_FLAGS "-Wall -Wextra ${SIMD_FLAGS}")
set(CMAKE_CXX_FLAGS_DEBUG "-g")
set(CMAKE_CXX_FLAGS_RELEASE "-O3 -fno-stack-protector")

add_executable(simd_test main.c sort_bindings.cpp)
