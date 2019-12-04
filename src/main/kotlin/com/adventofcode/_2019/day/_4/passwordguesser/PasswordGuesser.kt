package com.adventofcode._2019.day._4.passwordguesser

class PasswordGuesser {

    fun guessPassword(range:IntRange ) {
        val passwords = mutableSetOf<Int>()
        var valid:Boolean = false
        for (password in range) {
            valid = false
            password.toString().windowed(2, 1) {
                if (it[0] == it[1]) {
                    valid = true
                }
            }
            if (valid) {
                valid = true
                password.toString().windowed(2, 1) {
                    if (it[0].toInt() > it[1].toInt()) {
                        valid = false
                    }
                }
                if (valid) {
                    passwords.add(password)
                }
            }
            else {
                continue
            }
        }
        println(passwords)
        println(passwords.size)
    }

    fun guessPassword2(range:IntRange ) {
        val passwords = mutableSetOf<Int>()
        var valid:Boolean = false
        for (password in range) {
            valid = false
            password.toString().windowed(2, 1) {
                if (it[0] == it[1]) {
                    val list = countCharFrequencies(password, it[0].toInt())
                    if (list.isEmpty()) {
                        valid = true
                    }
                }
            }
            if (valid) {
                valid = true
                password.toString().windowed(2, 1) {
                    if (it[0].toInt() > it[1].toInt()) {
                        valid = false
                    }
                }
                if (valid) {
                    passwords.add(password)
                }
            }
            else {
                continue
            }
        }
        println(passwords)
        println(passwords.size)
    }

    fun countCharFrequencies(password: Int, digit:Int): List<Int> {
        //val eachCount = password.toString().toCharArray().filter { it=="$digit".toCharArray()[0] }.groupingBy { it }.eachCount()
        val eachCount = password.toString().toCharArray().filter { it==digit.toChar() }.groupingBy { it }.eachCount()
        return eachCount.filter { it.value > 2  }.map { it.value }
    }
}

fun main(args:Array<String>) {
    /*
    val input = WiresPanel::class.java.getResource("/com/adventofcode/_2019/day/_3/wirecrosses/wires.txt")
    val wires = input
            .readText()
            .split(Regex(System.getProperty("line.separator")))
            .map {
                it.split(",").toList()
            }
    println(WiresPanel(wires).findDistanceToClosestCrossing())
 */
    println (PasswordGuesser().guessPassword(402328..864247))
    println (PasswordGuesser().guessPassword2(402328..864247))
}