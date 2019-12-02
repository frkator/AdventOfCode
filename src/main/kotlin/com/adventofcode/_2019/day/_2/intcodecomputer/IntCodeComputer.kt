package com.adventofcode._2019.day._2.intcodecomputer

import java.lang.IllegalStateException
import java.util.function.BinaryOperator


enum class Operation(val code:Int) : BinaryOperator<Int> {
    ADD(1) {
        override fun apply(firstOperand: Int, secondOperand: Int): Int = firstOperand + secondOperand
    },
    MULTIPLY(2) {
        override fun apply(firstOperand: Int, secondOperand: Int): Int  = firstOperand * secondOperand
    },
    EXIT(99) {
        override fun apply(firstOperand: Int, secondOperand: Int): Int {
            throw IllegalStateException("should never ever happen")
        }
    },
    ILLEGAL(-1) {
        override fun apply(t: Int, u: Int): Int {
            throw IllegalStateException("should never happen")
        }
    };

    companion object {
        @JvmStatic
        fun decode(code: Int): Operation = values().find { operation -> operation.code == code } ?: ILLEGAL
    }
}

data class Instruction(val firstOperandAddress: Int, val secondOperandAddress:Int, val resultAddress:Int) {
    fun load(code:Array<Int>) = Instruction(
                                    code[firstOperandAddress],
                                    code[secondOperandAddress],
                                    code[resultAddress]
            )
}

class Address(private val operationCodeAddress:Int) {
    fun calculate():Instruction = Instruction(
            operationCodeAddress + 1,
            operationCodeAddress + 2,
            operationCodeAddress + 3
    )
}
class IntCodeComputer(private val code:Array<Int>){

    fun execute() {
        var programCounter = 0
        do {
            val operation = Operation.decode(code[programCounter])
            when(operation) {
                Operation.ILLEGAL -> throw IllegalStateException()
                Operation.EXIT -> {}
                else -> {
                    val (
                            firstOperandAddress,
                            secondOperandAddress,
                            resultAddress
                    ) = Address(programCounter).calculate().load(code)
                    code[resultAddress] = operation.apply(code[firstOperandAddress],code[secondOperandAddress])
                }
            }
            programCounter += 4
        }
        while(Operation.EXIT != operation)
    }

}

fun main(args:Array<String>) {
    val input = IntCodeComputer::class.java.getResource("/com/adventofcode/_2019/day/_2/intcodecomputer/source.txt")
    val source = input.readText().split(Regex(",")).map { it.toInt() }.toTypedArray()
    val fileInput = source.copyOf()
    IntCodeComputer(fileInput).execute()
    println(fileInput.toList())
    (0..99).forEach { i ->
        (0..99).forEach { j->
            val input = source.copyOf()
            input[1] = i
            input[2] = j
            IntCodeComputer(input).execute()
            if (input[0] == 19690720) {
                println("noun = ${input[1]} verb = ${input[2]}\n100 * noun + verb = ${100*input[1]+input[2]}")
            }
        }
    }
}