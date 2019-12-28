package com.adventofcode._2019.day._10.monitoringstation

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

internal class SlopeTest {

    companion object {
        @JvmStatic
        fun data(): List<Arguments> {
            return listOf(
                    Arguments.of(Point(0,0),Point(0,2),Point(0,-3), Slope(0,2)),
                    Arguments.of(Point(0,0),Point(2,0),Point(-2,0), Slope(2,0)),
                    Arguments.of(Point(0,0),Point(1,3),Point(-1,-3), Slope(1,3)),
                    Arguments.of(Point(0,0),Point(-1,3),Point(1,-3), Slope(1,-3)),
                    Arguments.of(Point(1,1),Point(2,4),Point(0,-2), Slope(1,3)),
                    Arguments.of(Point(1,1),Point(0,4),Point(2,-2), Slope(1,-3)),
                    Arguments.of(Point(1,1),Point(1,4),Point(1,-7), Slope(0,3)),
                    Arguments.of(Point(1,1),Point(1,-4),Point(1,5), Slope(0,3))
            )
        }

        @JvmStatic
        fun nonOpposite(): List<Arguments> {
            return listOf(
                    Arguments.of(Point(0,0),Point(2,3),Point(-1,-3)),
                    Arguments.of(Point(0,0),Point(0,3),Point(1,-3)),
                    Arguments.of(Point(1,1),Point(3,4),Point(0,-2)),
                    Arguments.of(Point(1,1),Point(1,4),Point(2,-2))
            )
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun testSlopeEquality(center:Point, first:Point, second:Point) {
        assertEquals(Slope.of(center,first),Slope.of(first,center),"cf == fc")
        assertEquals(Slope.of(center,second),Slope.of(second,center),"cs == sc")
        assertEquals(Slope.of(second,first),Slope.of(first,second),"sf == fs")
        assertEquals(Slope.of(second,first),Slope.of(first,center),"sf == fc")
        assertEquals(Slope.of(second,first),Slope.of(second,center),"sf == sc")
        assertEquals(Slope.of(second,center),Slope.of(first,center),"sc == fc")
    }

    @ParameterizedTest
    @MethodSource("data")
    fun testSlopeInitialization(center:Point, first:Point, second:Point, expectedSlope:Slope) {
        val actualSlope = Slope.of(center, first)
        assertEquals(expectedSlope, actualSlope)
    }

    @ParameterizedTest
    @MethodSource("data")
    fun testOppositePointsWithRegardToCenterOnSlope(center:Point, first:Point, second:Point) {
        val slope = Slope.of(center, first)
        assert(first.opposite(slope,second,center))
    }

    @ParameterizedTest
    @MethodSource("nonOpposite")
    fun testNonOppositeCalculation(center:Point, first:Point, second:Point) {
        val slope = Slope.of(center, first)
        assert(!first.opposite(slope,second,center))
    }

    @Test
    fun testTangens() {
        val center = Point (0,0)
        assert(Double.POSITIVE_INFINITY > Double.MAX_VALUE)
        assert(Double.NEGATIVE_INFINITY < (-1)*Double.MAX_VALUE)
        //println ("${Double.MAX_VALUE} ${-1 * Double.MAX_VALUE}")
        assert (Point(0,1).tanForCenter(center) == Double.POSITIVE_INFINITY)
        assert (Point(0,-1).tanForCenter(center) == Double.NEGATIVE_INFINITY)
        //assert(Slope.of(center,0,1).tanForCenter() > Slope.of(center,1, 1000).tanForCenter())
    }

    @ParameterizedTest
    @MethodSource("data")
    fun testSlopeInitialization() {

    }

}