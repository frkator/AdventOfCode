package com.adventofcode._2019.day._10.monitoringstation

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

internal class SlopeTest {

    companion object {
        @JvmStatic
        fun data(): List<Arguments> {
            return listOf(
                    Arguments.of(Point(0,0),Point(0,2),Point(0,-3)),
                    Arguments.of(Point(0,0),Point(2,0),Point(-2,0)),
                    Arguments.of(Point(0,0),Point(1,3),Point(-1,-3)),
                    Arguments.of(Point(0,0),Point(-1,3),Point(1,-3)),
                    Arguments.of(Point(1,1),Point(2,4),Point(0,-2)),
                    Arguments.of(Point(1,1),Point(0,4),Point(2,-2))
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
    fun testSlopeCalculation(center:Point, first:Point, second:Point) {
        assertEquals(Slope.of(center,first),Slope.of(first,center))
        assertEquals(Slope.of(center,second),Slope.of(second,center))
        assertEquals(Slope.of(second,first),Slope.of(first,second))
        assertEquals(Slope.of(second,first),Slope.of(first,center))
        assertEquals(Slope.of(second,first),Slope.of(second,center))
        assertEquals(Slope.of(second,center),Slope.of(first,center))
    }

    @ParameterizedTest
    @MethodSource("data")
    fun testOppositeCalculation(center:Point, first:Point, second:Point) {
        val slope = Slope.of(center, first)
        assert(first.opposite(slope,second,center))
    }

    @ParameterizedTest
    @MethodSource("nonOpposite")
    fun testNonOppositeCalculation(center:Point, first:Point, second:Point) {
        val slope = Slope.of(center, first)
        assert(!first.opposite(slope,second,center))
    }

}