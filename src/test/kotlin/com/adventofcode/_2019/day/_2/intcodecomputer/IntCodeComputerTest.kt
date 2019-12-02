package com.adventofcode._2019.day._2.intcodecomputer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class IntCodeComputerTest {

    companion object {
        @JvmStatic
        fun sourceCode() = listOf(
                Arguments.of(
                    arrayOf(1,9,10,3,2,3,11,0,99,30,40,50),
                    arrayOf(3500,9,10,70,2,3,11,0,99,30,40,50)
                ),
                Arguments.of(
                        arrayOf(1,0,0,0,99),
                        arrayOf(2,0,0,0,99)
                ),
                Arguments.of(
                        arrayOf(2,3,0,3,99),
                        arrayOf(2,3,0,6,99)
                ),
                Arguments.of(
                        arrayOf(2,4,4,5,99,0),
                        arrayOf(2,4,4,5,99,9801)
                ),
                Arguments.of(
                        arrayOf(1,1,1,4,99,5,6,0,99),
                        arrayOf(30,1,1,4,2,5,6,0,99)
                )
        )
    }

    @ParameterizedTest
    @MethodSource("sourceCode")
    fun verify(code:Array<Int>, expectedResult:Array<Int>)  {
        IntCodeComputer(code).execute()
        assertArrayEquals(code,expectedResult)
    }



}