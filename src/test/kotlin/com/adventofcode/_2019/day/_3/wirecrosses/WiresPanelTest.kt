package com.adventofcode._2019.day._3.wirecrosses

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File

internal class WiresPanelTest {

    companion object {
        @JvmStatic
        fun lineConfigurations() = listOf(
                Arguments.of(
                        listOf(
                            listOf("R8","U5","L5","D3"),
                            listOf("U7","R6","D4","L4")
                        ),
                        6
                ),
                Arguments.of(
                        listOf(
                                listOf("R75","D30","R83","U83","L12","D49","R71","U7","L72"),
                                listOf("U62","R66","U55","R34","D71","R55","D58","R83")
                        ),
                        159
                ),
                Arguments.of(
                        listOf(
                                listOf("R98","U47","R26","D63","R33","U87","L62","D20","R33","U53","R51"),
                                listOf("U98","R91","D20","R16","D67","R40","U7","R15","U6","R7")
                        ),
                        135
                )
        )
    }


/*
    @Test
    fun placeWiresOnPanel() {
    }

    @Test
    fun findClosestCrossing() {
    }
*/
    @ParameterizedTest
    @MethodSource("lineConfigurations")
    fun findDistanceToClosestCrossing(encodedLines:List<List<String>>, expectedResult:Int) {
        val actualResult = WiresPanel(encodedLines).findDistanceToClosestCrossing()
        println(WiresPanel(encodedLines).printPanel())
        assertEquals(expectedResult, actualResult)
    }
}

