package com.adventofcode._2019.day._7.amplifiers

import com.adventofcode._2019.day._5.extendedintcodecomputer.ExtendedIntCodeComputer
import com.adventofcode._2019.day._5.extendedintcodecomputer.IO
import com.marcinmoskala.math.permutations
import java.lang.IllegalStateException

class InstrumentationDataStore(val input:MutableList<Int> = mutableListOf(), val output: MutableList<Int> = mutableListOf<Int>()) {
    fun nextInput() = input.removeAt(0)

    fun clear() {
        input.clear()
        output.clear()
    }
}

class IoInstrumentation(val dataStore:InstrumentationDataStore) : IO() {

    override fun inputImpl(): String {
        return dataStore.nextInput().toString()
    }

    override fun outputImpl(output: Int): Int {
        dataStore.output.add(output)
        return super.outputImpl(output)
    }
}


class AmplifierOptimizer(private val source:Array<Int>, private val loop:Boolean = false) {

    fun phaseSettings(): IntRange {
       return if (loop) { (5..9) } else { (0..4) }
    }

    fun optimize():Int {
        val signalsPerPhaseSetting = phaseSettings().toSet().permutations().map {
            it to if (loop) { runOnAmplifiersLoop(it) } else { runOnChainedAmplifiers(it) }
        }.toMap()
        return signalsPerPhaseSetting.values.max()!!
    }

    private fun instrumentIo(): InstrumentationDataStore {
        val dataStore = InstrumentationDataStore()
        IO.set(IoInstrumentation(dataStore))
        return dataStore
    }

    private fun runOnAmplifiersLoop(phaseSettings: List<Int>): Int {
        val dataStore = instrumentIo()
        var output = 0
        val computers = phaseSettings
                .map { it to ExtendedIntCodeComputer(source.copyOf()) }.toMap()
        var currentIterationCounter = 0
        val total = (1+phaseSettings().last-phaseSettings().first)
        exitLabel@do {
            if(currentIterationCounter < 5 ) {
                dataStore.input.add(phaseSettings[currentIterationCounter])
            }
            dataStore.input.add(output)
            computers.getValue(phaseSettings[currentIterationCounter % total]).executeChunk()
            if (computers.getValue(phaseSettings[currentIterationCounter % total]).isDone()) {
                break@exitLabel
            }
            if (dataStore.output.size != 1) {
                println ("$currentIterationCounter ${dataStore.output}")
                throw IllegalStateException()
            }
            output = dataStore.output[0]
            dataStore.clear()
            currentIterationCounter++
        } while(true)
        return output
    }

    private fun runOnChainedAmplifiers(phaseSettings: List<Int>): Int {
        val io = instrumentIo()
        var output = 0
        phaseSettings.forEach { phaseSetting ->
            io.input.add(phaseSetting)
            io.input.add(output)
            ExtendedIntCodeComputer(source.copyOf()).executeAll()
            if (io.output.size != 1) {
                println (io.output)
                throw IllegalStateException()
            }
            output = io.output[0]
            io.clear()
        }
        return output
    }


}

fun main(args:Array<String>) {
    val input = AmplifierOptimizer::class.java.getResource("/com/adventofcode/_2019/day/_7/amplifiers/source.txt")
    val source = input.readText().split(Regex(",")).map { it.toInt() }.toTypedArray()
    IO.printOutput = false
    val chainedMax = AmplifierOptimizer(source.copyOf(),false).optimize()
    val loopedMax = AmplifierOptimizer(source.copyOf(),true).optimize()
    println("$chainedMax $loopedMax")
}

