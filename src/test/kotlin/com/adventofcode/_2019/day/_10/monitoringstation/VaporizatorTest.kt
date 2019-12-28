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
                            ,
                            Point(8, 3),
                            mapOf(
                                    1 to Point(8, 1),
                                    2 to Point(9, 0),
                                    3 to Point(9, 1),
                                    4 to Point(10, 0),
                                    5 to Point(9, 2),
                                    6 to Point(11, 1),
                                    7 to Point(12, 1),
                                    8 to Point(11, 2),
                                    9 to Point(15, 1),//
                                    10 to Point(12, 2),
                                    11 to Point(13, 2),
                                    12 to Point(14, 2),
                                    13 to Point(15, 2),
                                    14 to Point(12, 3),
                                    15 to Point(16, 4),
                                    16 to Point(15, 4),
                                    17 to Point(10, 4),
                                    18 to Point(4, 4),//
                                    19 to Point(2, 4),
                                    20 to Point(2, 3),
                                    21 to Point(0, 2),
                                    22 to Point(1, 2),
                                    23 to Point(0, 1),
                                    24 to Point(1, 1),
                                    25 to Point(5, 2),
                                    26 to Point(1, 0),
                                    27 to Point(5, 1),//
                                    28 to Point(6, 1),
                                    29 to Point(6, 0),
                                    30 to Point(7, 0),
                                    31 to Point(8, 0),
                                    32 to Point(10, 1),
                                    33 to Point(14, 0),
                                    34 to Point(16, 1),
                                    35 to Point(13, 3),
                                    36 to Point(14, 3)//
                            )
                    )
            )
        }


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