package com.adventofcode._2019.day._5.extendedintcodecomputer

import java.lang.Exception
import java.lang.IllegalStateException
import java.util.function.Function

enum class LoadMode {
    POSITION,
    IMMEDIATE
}

enum class Operation(val code:Int, val parameterCount:Int) : Function<List<Int>,Int> {
    ADD(1, 3) {
        override fun apply(operand:List<Int>): Int = operand[0] + operand[1]
    },
    MULTIPLY(2, 3) {
        override fun apply(operand:List<Int>): Int  = operand[0] * operand[1]
    },
    INPUT (3,1) {
        override fun apply(operand: List<Int>): Int {
            println("input:")
            return readLine()!!.toInt()
        }
    },
    OUTPUT (4,1) {
        override fun apply(operand: List<Int>): Int = operand[0]
    },
    EXIT(99, -1) {
        override fun apply(operand:List<Int>): Int {
            throw IllegalStateException("should never ever happen")
        }
    },
    ILLEGAL(-1, -1) {
        override fun apply(operand:List<Int>): Int {
            throw IllegalStateException("should never happen")
        }
    };

    companion object {
        @JvmStatic
        fun decode(code: Int): Pair<Operation,Array<LoadMode>> {
            try {
                val operation = values()
                        .find { operation ->
                            operation.code == code.toString().takeLast(2).toInt()
                        } ?: ILLEGAL
                val loadMode = code
                        .toString()
                        .padStart(operation.parameterCount + 2, '0')
                        .substring(0, operation.parameterCount)
                        .map {
                            when (it.toString().toInt()) {
                                0 -> LoadMode.POSITION
                                else -> LoadMode.IMMEDIATE
                            }
                        }
                        .toTypedArray()
                return operation to loadMode
            }
            catch (e:Exception) {
                println("illegal opcode:$code")
                throw e
            }
        }
    }
}


data class Instruction(val programCounter:Int, val operation:Operation, val loadMode: Array<LoadMode>) {

    private fun load(code: Array<Int>, loadModeIndex:Int, address:Int):Int {
        return when(loadMode[loadModeIndex]) {
            LoadMode.IMMEDIATE  -> code[address]
            LoadMode.POSITION -> code[code[address]]
        }
    }

    fun execute(code: Array<Int>) {
        val firstParameterAddress = programCounter + 1
        val lastParameterAddress = programCounter + operation.parameterCount
        val resultAddress = code[lastParameterAddress]
        val parameterValues = (firstParameterAddress .. lastParameterAddress).map {
                                                load(code,it-firstParameterAddress,it)
                                        }
        print("${parameterValues.take(operation.parameterCount - 1)} ")
        code[resultAddress] = operation.apply(parameterValues)
        println("writes ${resultAddress}=${code[resultAddress]}")
    }
}

class ExtendedIntCodeComputer(private val code:Array<Int>) {

    fun execute() {
        var programCounter = 0
        do {
            print("PC=$programCounter: ")
            val (operation,loadMode) = Operation.decode(code[programCounter])
            println("code=${operation.code} ${operation.name} load mode=${loadMode.toList()}")
            println (code.withIndex().groupBy { it.index }.toMap().mapValues { it.value.first().value })
            when(operation) {
                Operation.ILLEGAL -> throw IllegalStateException()
                Operation.EXIT -> {}
                else -> {
                   Instruction(programCounter, operation, loadMode).execute(code)
                }
            }
            programCounter += (operation.parameterCount + 1)
            println("--end")
        }
        while(Operation.EXIT != operation)
    }

}

fun main(args:Array<String>) {
    val input = ExtendedIntCodeComputer::class.java.getResource("/com/adventofcode/_2019/day/_5/extendedintcodecomputer/source.txt")
    val source = input.readText().split(Regex(",")).map { it.toInt() }.toTypedArray()
    val fileInput = source.copyOf()
    ExtendedIntCodeComputer(fileInput).execute()
    println(fileInput.toList())
}