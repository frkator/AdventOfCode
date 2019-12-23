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
                            Point(3, 4), mapOf<Int,Point>()
                    ),
                    Arguments.of(
                            ".....\n" +
                            ".###.\n" +
                            ".###.\n" +
                            ".###.\n" +
                            ".....",
                            Point(2, 2), mapOf<Int,Point>()
                    ),
                    Arguments.of(
                            "###..#########.#####.\n" +
                            ".####.#####..####.#.#\n" +
                            ".###.#.#.#####.##..##\n" +
                            "##.####.#.###########\n" +
                            "###...#.####.#.#.####\n" +
                            "#.##..###.########...\n" +
                            "#.#######.##.#######.\n" +
                            ".#..#.#..###...####.#\n" +
                            "#######.##.##.###..##\n" +
                            "#.#......#....#.#.#..\n" +
                            "######.###.#.#.##...#\n" +
                            "####.#...#.#######.#.\n" +
                            ".######.#####.#######\n" +
                            "##.##.##.#####.##.#.#\n" +
                            "###.#######..##.#....\n" +
                            "###.##.##..##.#####.#\n" +
                            "##.########.#.#.#####\n" +
                            ".##....##..###.#...#.\n" +
                            "#..#.####.######..###\n" +
                            "..#.####.############\n" +
                            "..##...###..#########",
                            Point(11,11), mapOf<Int,Point>()
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
        _dump(sortedEdge,monitoringStationSurveyor.xMax,monitoringStationSurveyor.yMax,actualResult)
        _dump(sortedEdge,monitoringStationSurveyor.xMax,monitoringStationSurveyor.yMax,actualResult,false)
    }

    fun _dump(points: List<Point>, xMax: Int, yMax: Int, center: Point,quadrant:Boolean = true) {
        println ("$center ${points.size} \n$points\n${points.groupBy { it.quadrant(center) }.map { "${it.key}=${it.value}" }.joinToString (separator = "\n")}")
        val pointsSet = LinkedHashSet(points)
        val map = mutableMapOf<Point,Char>()
        for (x in 0..xMax) {
            for (y in 0..yMax) {
                val point = Point(x,y)
                if (point !in pointsSet) {
                    map[point] = ".".toCharArray()[0]
                }
                else {
                    map[point] = if (quadrant) {
                        point.quadrant(center).toString().toCharArray().last()
                    }
                    else {
                        pointsSet.indexOf(point).toString().toCharArray().last()
                    }
                }
                if (point==center) {
                    map[center] = "x".toCharArray().first()
                }
            }
        }
        dump(map.toMap(), xMax, yMax)
    }


    

}
