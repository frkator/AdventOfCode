package com.adventofcode._2019.day._9.sensorboost

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigInteger

internal class CompleteIntCodeComputerTest {


    companion object {
        @JvmStatic
        fun sources(): List<Arguments> {
            return listOf(
                    Arguments.of(
                            listOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99).map { it.toBigInteger() },
                            listOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99).map { it.toBigInteger() }
                    )/*,
                    Arguments.of(
                            listOf(1102,34915192,34915192,7,4,7,99,0).map { it.toBigInteger() },
                            listOf(1102, 34915192, 34915192, 7, 4, 7, 99, 1219070632396864).map { it.toBigInteger() }
                    ),
                    Arguments.of(
                            listOf(104,1125899906842624,99).map { it.toBigInteger() },
                            listOf(104,1125899906842624,99).map { it.toBigInteger() }
                    )*/
            )
        }
    }

    @ParameterizedTest
    @MethodSource("sources")
    fun testExecuteAll(input:List<BigInteger>,expectedResult:List<BigInteger>) {
        val actualResult = input.toMutableList()
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
        assertEquals(expectedResult,actualResult)
    }

}