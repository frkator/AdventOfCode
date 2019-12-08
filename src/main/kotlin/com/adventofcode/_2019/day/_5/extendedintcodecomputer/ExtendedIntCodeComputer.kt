package com.adventofcode._2019.day._5.extendedintcodecomputer

import java.lang.Exception
import java.lang.IllegalStateException
import java.util.function.Function

enum class LoadMode {
    POSITION,
    IMMEDIATE
}

enum class Operation(val code:Int, val parameterCount:Int, val regular:Boolean) : Function<List<Int>,Int> {
    ADD(1, 3, true) {
        override fun apply(operand:List<Int>): Int = operand[0] + operand[1]
    },
    MULTIPLY(2, 3,true) {
        override fun apply(operand:List<Int>): Int  = operand[0] * operand[1]
    },
    INPUT (3,1,true) {
        override fun apply(operand: List<Int>): Int {
            println("input:")
            return readLine()!!.toInt()
        }
    },
    OUTPUT (4,1,true) {
        override fun apply(operand: List<Int>): Int = operand[0]
    },
    JUMP_IF_TRUE(5,2,false) {
        override fun apply(operand: List<Int>): Int =  if (operand[0] > 0) { operand[1] } else { -1 }

    },
    JUMP_IF_FALSE(6,2,false) {
        override fun apply(operand: List<Int>): Int =  if (operand[0] == 0) { operand[1] } else { -1 }

    },
    LESS_THAN(7,3,true) {
        override fun apply(operand: List<Int>): Int =  if (operand[0] < operand[1]) { 1 } else { 0 }

    },
    EQUALS(8,3,true) {
        override fun apply(operand: List<Int>): Int = if (operand[0] == operand[1]) { 1 } else { 0 }
    },
    EXIT(99, -1,false) {
        override fun apply(operand:List<Int>): Int {
            throw IllegalStateException("should never ever happen")
        }
    },
    ILLEGAL(-1, -1,false) {
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
                val loadMode = if (operation.parameterCount > 0) {
                            code
                            .toString()
                            .padStart(operation.parameterCount + 2, '0')
                            .substring(0, operation.parameterCount)
                            .map {
                                when (it.toString().toInt()) {
                                    0 -> LoadMode.POSITION
                                    else -> LoadMode.IMMEDIATE
                                }
                            }
                            .reversed()
                            .toTypedArray()
                } else { arrayOf() }
                return operation to loadMode
            }
            catch (e:Exception) {
                println("illegal opcode:$code")
                throw e
            }
        }
    }
}


data class Instruction(val programCounter:ProgramCounter, val operation:Operation, val loadMode: Array<LoadMode>) {

    private fun load(code: Array<Int>, loadModeIndex:Int, address:Int):Int {
        return when(loadMode[loadModeIndex]) {
            LoadMode.IMMEDIATE  -> code[address]
            LoadMode.POSITION -> code[code[address]]
        }
    }

    fun execute(code: Array<Int>) {
        val firstParameterAddress = programCounter.read() + 1
        val lastParameterAddress = programCounter.read() + operation.parameterCount
        val resultAddress = code[lastParameterAddress]
        val parameterValues = (firstParameterAddress .. lastParameterAddress).map {
                                                load(code,it-firstParameterAddress,it)
                                        }
        print("${parameterValues.take(operation.parameterCount - 1)} ")
        if (operation.regular) {
            code[resultAddress] = operation.apply(parameterValues)
            println("writes to memory @ address ${resultAddress} value ${code[resultAddress]}")
            programCounter.increase(operation.parameterCount + 1)
        }
        else {
            val result = operation.apply(parameterValues)
            if (result > -1) {
                println("jumping at $result ")
                programCounter.write(result)
            }
            else {
                println("no operation")
                programCounter.increase(operation.parameterCount + 1)
            }

        }
    }
}

data class ProgramCounter(private var value:Int) {

    fun increase(value:Int) {
        this.value += value
    }

    fun write(value:Int) {
        this.value = value
    }

    fun read():Int = value
}

class ExtendedIntCodeComputer(private val code:Array<Int>) {

    fun execute() {
        val programCounter = ProgramCounter(0)
        do {
            print("PC=$programCounter: ${code[programCounter.read()]} ")
            val (operation,loadMode) = Operation.decode(code[programCounter.read()])
            println("code=${operation.code} ${operation.name} load mode=${loadMode.toList()}")
            println (code.withIndex().groupBy { it.index }.toMap().mapValues { it.value.first().value })
            when(operation) {
                Operation.ILLEGAL -> throw IllegalStateException()
                Operation.EXIT -> {}
                else -> {
                    Instruction(programCounter, operation, loadMode).execute(code)
                }
            }
            println("--end")
        }
        while(Operation.EXIT != operation)
    }

}

fun main(args:Array<String>) {
    val input = ExtendedIntCodeComputer::class.java.getResource("/com/adventofcode/_2019/day/_5/extendedintcodecomputer/source.txt")
    val source = input.readText().split(Regex(",")).map { it.toInt() }.toTypedArray()
    //val source = arrayOf(3,0,4,0,99)
    val fileInput = source.copyOf()
    ExtendedIntCodeComputer(fileInput).execute()
    println(fileInput.toList())
}