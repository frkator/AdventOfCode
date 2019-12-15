package com.adventofcode._2019.day._10.monitoringstation

import junit.framework.Assert.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class MonitoringStationSurveyorTest {

    companion object {
        @JvmStatic
        fun maps(): List<Arguments> {
            return listOf(
                    Arguments.of(
                            ".#..#\n" +
                            ".....\n" +
                            "#####\n" +
                            "....#\n" +
                            "...##",
                            Point(3, 4)
                    )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("maps")
    fun calculate(map: String, expectedResult: Point) {
        val actualResult = MonitoringStationSurveyor(map).findBestLocation(true)
        assertEquals(expectedResult, actualResult)
    }
}