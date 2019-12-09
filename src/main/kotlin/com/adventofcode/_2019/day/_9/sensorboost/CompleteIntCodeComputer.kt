package com.adventofcode._2019.day._9.sensorboost

import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.math.BigInteger
import java.util.function.Function

enum class LoadMode {
    POSITION,
    IMMEDIATE,
    RELATIVE
}

enum class Operation(val code:Int, val parameterCount:Int, val regular:Boolean) : Function<List<BigInteger>,BigInteger> {
    ADD(1, 3, true) {
        override fun apply(operand: List<BigInteger>): BigInteger = operand[0] + operand[1]
    },
    MULTIPLY(2, 3,true) {
        override fun apply(operand:List<BigInteger>): BigInteger  = operand[0] * operand[1]
    },
    INPUT (3,1,true) {
        override fun apply(operand: List<BigInteger>): BigInteger {
            println("input:")
            return readLine()!!.toBigInteger()
        }
    },
    OUTPUT (4,1,true) {
        override fun apply(operand: List<BigInteger>): BigInteger = operand[0]
    },
    JUMP_IF_TRUE(5,2,false) {
        override fun apply(operand: List<BigInteger>): BigInteger =  if (operand[0] > 0.toBigInteger()) { operand[1] } else { -1.toBigInteger() }

    },
    JUMP_IF_FALSE(6,2,false) {
        override fun apply(operand: List<BigInteger>): BigInteger =  if (operand[0] == 0.toBigInteger()) { operand[1] } else { -1.toBigInteger() }

    },
    LESS_THAN(7,3,true) {
        override fun apply(operand: List<BigInteger>): BigInteger =  if (operand[0] < operand[1]) { 1.toBigInteger() } else { 0.toBigInteger() }

    },
    EQUALS(8,3,true) {
        override fun apply(operand: List<BigInteger>): BigInteger = if (operand[0] == operand[1]) { 1.toBigInteger() } else { 0.toBigInteger() }
    },
    ADJUST_RELATIVE_BASE(9,1,false) {
        override fun apply(operand: List<BigInteger>): BigInteger = operand[0]
    },
    EXIT(99, -1,false) {
        override fun apply(operand:List<BigInteger>): BigInteger {
            throw IllegalStateException("should never ever happen")
        }
    },
    ILLEGAL(-1, -1,false) {
        override fun apply(operand:List<BigInteger>): BigInteger {
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
                                    1 -> LoadMode.IMMEDIATE
                                    2 -> LoadMode.RELATIVE
                                    else -> throw IllegalArgumentException()
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

    private fun load(code: MutableList<BigInteger>, loadModeIndex:Int, address:Int):BigInteger {
        if (address >= code.size) {
            println("adding ${address-code.size+1} memory cells, from address ${code.size} to ${address}")
        }
        while (address >= code.size) {
            code.add(0.toBigInteger())
        }
        return when(loadMode[loadModeIndex]) {
            LoadMode.IMMEDIATE  -> code[address]
            LoadMode.POSITION -> code[code[address].toInt()]
            LoadMode.RELATIVE -> code[code[address].toInt() + programCounter.offset.value]
        }
    }

    fun execute(code: MutableList<BigInteger>) {
        val firstParameterAddress = programCounter.read() + 1
        val lastParameterAddress = programCounter.read() + operation.parameterCount
        val resultAddress = code[lastParameterAddress]
        val parameterValues = (firstParameterAddress .. lastParameterAddress).map {
            load(code,it-firstParameterAddress,it)
        }
        print("${parameterValues.take(operation.parameterCount - 1)} ")
        if (operation.regular) {
            code[resultAddress.toInt()] = operation.apply(parameterValues)
            println("writes to memory @ address ${resultAddress} value ${code[resultAddress.toInt()]}")
            programCounter.increase(operation.parameterCount + 1)
        }
        else {
            val result = operation.apply(parameterValues)
            if (operation == Operation.JUMP_IF_FALSE || operation == Operation.JUMP_IF_TRUE) {
                println("jumping at $result ")
                programCounter.write(result.toInt())
            }
            else if (operation == Operation.ADJUST_RELATIVE_BASE) {
                print("adjusting relative base for $result from ${programCounter.offset.value} ")
                programCounter.offset.value = result.toInt()
                println("to ${programCounter.offset.value}")
            }
            else if (operation == Operation.EXIT){
                println("no operation")
                programCounter.increase(operation.parameterCount + 1)
            }
            else {
                throw IllegalStateException()
            }

        }
    }
}

class RelativeOffsetBase {
    private var _value = 0
    var value = _value
    get set(value:Int){
        _value += value
    }

    override fun toString(): String {
        return "$_value"
    }

}

data class ProgramCounter(private var value:Int,val offset:RelativeOffsetBase = RelativeOffsetBase()) {

    fun increase(value:Int) {
        this.value += value
    }

    fun write(value:Int) {
        this.value = value
    }

    fun read():Int = value
}

class CompleteIntCodeComputer(private val code:MutableList<BigInteger>) {

    private val programCounter = ProgramCounter(0)

    fun executeAll() {
        check(programCounter.read() == 0)
        do {
            print("PC=$programCounter: ${code[programCounter.read()]} ")
            val (operation,loadMode) = Operation.decode(code[programCounter.read()].toInt())
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

    private var isDone = false
    fun isDone():Boolean = isDone

    fun executeChunk() {
        loop@ do {
            print("PC=$programCounter: ${code[programCounter.read()]} ")
            val (operation,loadMode) = Operation.decode(code[programCounter.read()].toInt())
            println("code=${operation.code} ${operation.name} load mode=${loadMode.toList()}")
            println (code.withIndex().groupBy { it.index }.toMap().mapValues { it.value.first().value })
            when(operation) {
                Operation.ILLEGAL -> throw IllegalStateException()
                Operation.EXIT -> { isDone = true}
                else -> {
                    Instruction(programCounter, operation, loadMode).execute(code)
                    if (operation == Operation.OUTPUT) {
                        break@loop
                    }
                }
            }
        }
        while(Operation.EXIT != operation)
        println("--end chunk")
    }

}

fun main(args:Array<String>) {
    val input = CompleteIntCodeComputer::class.java.getResource("/com/adventofcode/_2019/day/_9/sensorboost/source.txt")
    val source = input.readText().split(Regex(",")).map { it.toBigInteger() }.toList()
    val memory = source.toMutableList()
    CompleteIntCodeComputer(memory).executeAll()
}