package com.adventofcode._2019.day._10.monitoringstation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class LineSegmentTest {

    companion object {
        @JvmStatic
        fun lines(): List<Arguments> {
            return listOf(
                    Arguments.of(
                            Point(0,0),
                            Point(3,3),
                            linkedSetOf(
                                    Point(0,0),
                                    Point(1,1),
                                    Point(2,2),
                                    Point(3,3)
                            )
                    ),
                    Arguments.of(
                            Point(0,0),
                            Point(-3,3),
                            linkedSetOf(
                                    Point(0,0),
                                    Point(-1,1),
                                    Point(-2,2),
                                    Point(-3,3)
                            )
                    ),
                    Arguments.of(
                            Point(0,0),
                            Point(3,-3),
                            linkedSetOf(
                                    Point(0,0),
                                    Point(1,-1),
                                    Point(2,-2),
                                    Point(3,-3)
                            )
                    ),
                    Arguments.of(
                            Point(0,0),
                            Point(-3,-3),
                            linkedSetOf(
                                    Point(0,0),
                                    Point(-1,-1),
                                    Point(-2,-2),
                                    Point(-3,-3)
                            )
                    ),
                    Arguments.of(
                            Point(0,0),
                            Point(-2,-6),
                            linkedSetOf(
                                    Point(0,0),
                                    Point(-1,-3),
                                    Point(-2,-6)
                            )
                    )

            )
        }
    }

    @ParameterizedTest
    @MethodSource("lines")
    fun calculate(start:Point, end:Point, expectedResult: LinkedHashSet<Point>) {
        val actualResult = LineSegment(start, end).value
        assertEquals(expectedResult, actualResult)
    }
}