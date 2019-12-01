package com.adventofcode._2019.day._1.fuelcounterupper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource


internal class FuelCalculatorTestFromFile{

    val fuelCalculator = FuelCalculator()

    @ParameterizedTest
    @CsvFileSource(resources = ["/fuel-calculator-test-data.csv"])
    fun calculateFuelAmountForGivenMass(mass:Int, fuel:Int)  {
        val actualResult = fuelCalculator.calculateFuelAmountForGivenMass(mass)
        assertEquals(fuel,actualResult)
    }

}