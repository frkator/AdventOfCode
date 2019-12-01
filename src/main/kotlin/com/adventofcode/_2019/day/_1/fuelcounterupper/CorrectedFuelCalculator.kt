package com.adventofcode._2019.day._1.fuelcounterupper

class CorrectedFuelCalculator : FuelCalculator() {

    override fun calculateFuelAmountForGivenMass(mass: Int): Int {
        var nextMass = super.calculateFuelAmountForGivenMass(mass)
        var massSum = nextMass
        do {
            nextMass = super.calculateFuelAmountForGivenMass(nextMass)
            massSum += if (nextMass > 0) { nextMass } else { 0 }
        }
        while (nextMass > 0)
        return massSum
    }

}