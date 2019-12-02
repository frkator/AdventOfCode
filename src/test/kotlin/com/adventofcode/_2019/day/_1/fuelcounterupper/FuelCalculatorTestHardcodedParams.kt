package com.adventofcode._2019.day._1.fuelcounterupper

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

internal class FuelCalculatorTestHardcodedParams {

    val fuelCalculator = FuelCalculator()

    companion object {
        @JvmStatic
        fun requiredFuelPerMass() = listOf(
                Arguments.of(12, 2),
                Arguments.of(14, 2),
                Arguments.of(1969,654),
                Arguments.of(100756,33583)
        )
    }

    @ParameterizedTest
    @MethodSource("requiredFuelPerMass")
    fun calculateFuelAmountForGivenMass(mass:Int, fuel:Int)  {
        val actualResult = fuelCalculator.calculateFuelAmountForGivenMass(mass)
        assertEquals(fuel,actualResult)
    }

    @Test
    fun calculateSumOfFuelAmountsForGivenMasses() {
        val expectedSumOfFuelAmounts = 34241

        val actualSum = fuelCalculator.calculateSumOfFuelAmountsForGivenMasses(
                requiredFuelPerMass().map {
                    it.get()[0]!!.toString().toInt()
                }
        )
        assertEquals(expectedSumOfFuelAmounts,actualSum)
    }

}