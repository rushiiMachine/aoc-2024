#include <iostream>
#include <unordered_map>
#include <chrono>

uint64_t countDigits(uint64_t n)
{
	int digits = 0;
	while (n != 0)
	{
		n /= 10;
		digits++;
	}
	return digits;
}

int main()
{
	const auto p1 = std::chrono::system_clock::now();

	auto stones = std::unordered_map<uint64_t, uint64_t>(10000000);
	auto newStones = std::unordered_map<uint64_t, uint64_t>(10000000);

	auto initValues = {8435, 234, 928434, 14, 0, 7, 92446, 8992692};
	for (const auto& item: initValues)
		stones[item] += 1;

	for (int i = 0; i < 75; ++i)
	{
		for (auto& stone: stones)
		{
			uint64_t digits;

			if (stone.first == 0)
			{
				newStones[1] += stone.second;
			}
			else if ((digits = countDigits(stone.first)) % 2 == 0)
			{
				uint64_t half = pow(10, digits / 2);
				uint64_t leftHalf = stone.first / half;
				uint64_t rightHalf = stone.first - leftHalf * half;

				newStones[leftHalf] += stone.second;
				newStones[rightHalf] += stone.second;
			}
			else
			{
				newStones[stone.first * 2024] += stone.second;
			}
		}

		std::swap(stones, newStones);
		newStones.clear();
	}

	uint64_t count = 0;
	for (auto& stone: stones)
	{
		count += stone.second;
	}

	const auto p2 = std::chrono::system_clock::now();
	std::cout << count << '\n';
	std::cout << std::chrono::duration_cast<std::chrono::milliseconds>((p2 - p1)).count() << "ms\n";
}
