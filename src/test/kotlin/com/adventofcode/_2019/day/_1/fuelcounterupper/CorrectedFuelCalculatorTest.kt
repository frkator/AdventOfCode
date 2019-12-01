package com.adventofcode._2019.day._1.fuelcounterupper

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class CorrectedFuelCalculatorTest {

    val fuelCalculator = CorrectedFuelCalculator()

    companion object {
        @JvmStatic
        fun requiredFuelPerMass() = listOf(
                Arguments.of(14, 2),
                Arguments.of(1969,966),
                Arguments.of(100756,50346)
        )
    }

    @ParameterizedTest
    @MethodSource("requiredFuelPerMass")
    fun calculateFuelAmountForGivenMass(mass:Int, fuel:Int)  {
        val actualResult = fuelCalculator.calculateFuelAmountForGivenMass(mass)
        kotlin.test.assertEquals(fuel, actualResult)
    }

    @Test
    fun calculateSumOfFuelAmountsForGivenMasses() {
        val expectedSumOfFuelAmounts = 51314

        val actualSum = fuelCalculator.calculateSumOfFuelAmountsForGivenMasses(
                requiredFuelPerMass().map {
                    it.get()[0]!!.toString().toInt()
                }
        )
        kotlin.test.assertEquals(expectedSumOfFuelAmounts, actualSum)
    }
}