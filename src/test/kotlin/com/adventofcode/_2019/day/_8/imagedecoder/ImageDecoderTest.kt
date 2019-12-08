package com.adventofcode._2019.day._8.imagedecoder

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class ImageDecoderTest {

    @Test
    fun test() {
        val imageDecoder = ImageDecoder("1233456678990112", 4, 2)
        println (imageDecoder.calculate(imageDecoder.decode()))
    }
}