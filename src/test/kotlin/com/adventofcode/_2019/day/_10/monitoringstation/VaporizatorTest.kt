package com.adventofcode._2019.day._10.monitoringstation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


internal class VaporizatorTest {

    companion object {
        @JvmStatic
        fun maps(): List<Arguments> {
            return listOf(
                    Arguments.of(
                            ".#..##.###...#######\n" +
                                    "##.############..##.\n" +
                                    ".#.######.########.#\n" +
                                    ".###.#######.####.#.\n" +
                                    "#####.##.#.##.###.##\n" +
                                    "..#####..#.#########\n" +
                                    "####################\n" +
                                    "#.####....###.#.#.##\n" +
                                    "##.#################\n" +
                                    "#####.##.###..####..\n" +
                                    "..######..##.#######\n" +
                                    "####.##.####...##..#\n" +
                                    ".#####..#.######.###\n" +
                                    "##...#.##########...\n" +
                                    "#.##########.#######\n" +
                                    ".####.#.###.###.#.##\n" +
                                    "....##.##.###..#####\n" +
                                    ".#.#.###########.###\n" +
                                    "#.#.#.#####.####.###\n" +
                                    "###.##.####.##.#..##",
                            Point(11, 13),
                            mapOf(
                                    1 to Point(11, 12),
                                    2 to Point(12, 1),
                                    3 to Point(12, 2),
                                    10 to Point(12, 8),
                                    20 to Point(16, 0),
                                    50 to Point(16, 9),
                                    100 to Point(10, 16),
                                    199 to Point(9, 6),
                                    200 to Point(8, 2),
                                    201 to Point(10, 9),
                                    299 to Point(11, 1)
                            )
                    ),
                    Arguments.of(
                            ".#....#####...#..\n" +
                            "##...##.#####..##\n" +
                            "##...#...#.#####.\n" +
                            "..#.....X...###..\n" +
                            "..#.#.....#....##\n" +
                            ".................\n" +
                            ".................\n" +
                            ".................\n" +
                            ".................\n" +
                            ".................\n" +
                            ".................\n" +
                            ".................\n" +
                            ".................\n" +
                            ".................\n" +
                            ".................\n" +
                            ".................\n" +
                            ".................\n"

                    )
            )
        }


        @ParameterizedTest
        @MethodSource("maps")
        fun testVaporize(map: String, expectedResult: Point, expectedOrder: Map<Int, Point> = mapOf()) {
            if (expectedOrder.isNotEmpty()) {
                val monitoringStationSurveyor = MonitoringStationSurveyor(map)
                val actualResult = monitoringStationSurveyor.vaporizeAsteroids(expectedResult, true)

                assertEquals(expectedOrder, actualResult
                        .mapIndexed { idx, pint -> idx + 1 to pint }
                        .filter { it.first in expectedOrder.keys }
                        .toMap()
                )
            }
        }
    }
}