package com.adventofcode._2019.day._10.monitoringstation

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class SlopeTest {

    companion object {
        @JvmStatic
        fun data(): List<Arguments> {
            return listOf(
                    Arguments.of(Point(0,0),Point(1,3),Point(-1,-3))
            )
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun testSlope(center:Point,first:Point,second:Point) {
        println(Slope.of(center,first))
        println(Slope.of(first,center))
        println(Slope.of(center,second))
        println(Slope.of(second,center))
        println(Slope.of(second,first))
        println(Slope.of(first,second))
    }

}