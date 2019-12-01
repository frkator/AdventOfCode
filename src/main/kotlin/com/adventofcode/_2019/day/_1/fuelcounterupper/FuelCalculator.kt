package com.adventofcode._2019.day._1.fuelcounterupper

open class FuelCalculator {

    open fun calculateFuelAmountForGivenMass(mass:Int) = mass.div(3).minus(2)

    fun calculateSumOfFuelAmountsForGivenMasses(masses:List<Int>):Int = masses.map(this::calculateFuelAmountForGivenMass).sum()

    fun parseMasses(data: String) = data.split(Regex(System.getProperty("line.separator"))).map { it.toInt() }

}

fun main(args:Array<String>) {
    val input = FuelCalculator::class.java.getResource("/com/adventofcode/_2019/day/_1/fuelcounterupper/masses.txt")
    val fuelCalculator = FuelCalculator()
    val masses = fuelCalculator.parseMasses(input.readText())
    println (fuelCalculator.calculateSumOfFuelAmountsForGivenMasses(masses))
    val correctedFuelCalculator = CorrectedFuelCalculator()
    println (correctedFuelCalculator.calculateSumOfFuelAmountsForGivenMasses(masses))
}