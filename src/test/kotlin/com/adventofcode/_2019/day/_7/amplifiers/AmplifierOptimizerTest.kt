package com.adventofcode._2019.day._7.amplifiers

import com.adventofcode._2019.day._5.extendedintcodecomputer.ExtendedIntCodeComputer
import com.adventofcode._2019.day._5.extendedintcodecomputer.IO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class AmplifierOptimizerTest {

    companion object {
        @JvmStatic
        fun sourceCode() = listOf(
                Arguments.of(
                        arrayOf(3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5),
                        arrayOf(9,8,7,6,5),
                        139629729
                )
        )
    }

    @ParameterizedTest
    @MethodSource("sourceCode")
    fun test(source:Array<Int>,expectedPhaseSetting:Array<Int>,expectedSignal:Int) {
        IO.printOutput = true
        val actualSignal = AmplifierOptimizer(source, true).optimize()
        assertEquals(expectedSignal,actualSignal)
    }
}