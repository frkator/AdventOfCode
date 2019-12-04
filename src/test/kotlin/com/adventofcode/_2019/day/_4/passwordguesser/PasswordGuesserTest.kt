package com.adventofcode._2019.day._4.passwordguesser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PasswordGuesserTest  {

    @Test
    fun countCharFrequenciesTest() {
        val passwordGuesser = PasswordGuesser()
        var actualResult = passwordGuesser.countCharFrequencies(111111,0)
        assert(actualResult == listOf<Int>())
        actualResult = passwordGuesser.countCharFrequencies(111111,1)
        assert(actualResult == listOf<Int>(6))
        actualResult = passwordGuesser.countCharFrequencies(112222,2)
        assert(actualResult == listOf(4))
        actualResult = passwordGuesser.countCharFrequencies(1112222,1)
        assert(actualResult == listOf(3))

    }


}