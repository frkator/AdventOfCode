package com.adventofcode._2019.day._9.sensorboost

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class CompleteIntCodeComputerTest {

    companion object {
        @JvmStatic
        fun sources(): List<Arguments> {
            return listOf(
                    Arguments.of(
                            listOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99),
                            listOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99)
                    )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("sources")
    fun testExecuteAll(input:List<Int>,expectedResult:List<Int>) {
        val actualResult = input.map { it.toBigInteger() }.toMutableList()
        CompleteIntCodeComputer(actualResult).executeAll()
        assert(actualResult.size >= expectedResult.size)
        if (actualResult.size > expectedResult.size) {
            val tail = actualResult.subList(expectedResult.size + 1, actualResult.size)
            if (tail.count { it == 0.toBigInteger() } == tail.size) {
                while (actualResult.size > expectedResult.size) {
                    actualResult.removeAt(actualResult.lastIndex)
                }
            }
        }
        assertEquals(expectedResult.map { it.toBigInteger() }, actualResult)
    }

}