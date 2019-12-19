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

        @JvmStatic
        fun gcds(): List<Arguments> {
            return listOf(
                    Arguments.of(2,5,1),
                    Arguments.of(5,2,1),
                    Arguments.of(5,0,5),
                    Arguments.of(0,5,5),
                    Arguments.of(0,0,0),
                    Arguments.of(30,10,10),
                    Arguments.of(10,30,10),
                    Arguments.of(42,35,7),
                    Arguments.of(35,42,7)

            )
        }
    }


    @ParameterizedTest
    @MethodSource("maps")
    fun calculate(map: String, expectedResult: Point) {
        val actualResult = MonitoringStationSurveyor(map).findBestLocation(true)
        assertEquals(expectedResult, actualResult)
    }

    @ParameterizedTest
    @MethodSource("gcds")
    fun gcd(a:Int,b:Int,expectedResult: Int) {
        val actualResult = greatestCommonDenominator(a,b)
        assertEquals(expectedResult, actualResult)
    }

    @ParameterizedTest
    @MethodSource("maps")
    fun testSort(map: String, expectedResult: Point) {
        val monitoringStationSurveyor = MonitoringStationSurveyor(map)
        val actualResult = monitoringStationSurveyor.findBestLocation(true)
        val unsortedEdge = monitoringStationSurveyor.generateMapEdgePoints(actualResult)
        val sortedEdge = monitoringStationSurveyor.sortToEdgeRectangleBasedOnCenterPoint(actualResult)
        _dump(sortedEdge,4,4,actualResult)
    }

    fun _dump(points: List<Point>, xMax: Int, yMax: Int, center: Point) {
        val map = points.withIndex().map { it.value to it.index.toString().toCharArray().last()}.toMap().toMutableMap()
        for (x in 0..xMax) {
            for (y in 0..yMax) {
                val point = Point(x,y)
                if (!map.containsKey(point)) {
                    map[point] = "0".toCharArray()[0]
                }
                if (point==center) {
                    map[center] = "x".toCharArray().first()
                }
            }
        }
        dump(map.toMap(), xMax, yMax)
    }
}
