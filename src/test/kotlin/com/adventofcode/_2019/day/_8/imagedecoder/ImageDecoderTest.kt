package com.adventofcode._2019.day._8.imagedecoder

import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.ArgumentAccessException
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class ImageDecoderTest {

    companion object {
        @JvmStatic
        fun imagesDecode(): List<Arguments> {
            return listOf(
                    Arguments.of(
                            3,
                            2,
                            "123456789012",
                            0,
                            1
                    )
            )
        }

        @JvmStatic
        fun imagesCompose(): List<Arguments> {
            return listOf(
                    Arguments.of(
                            2,
                            2,
                            "0222112222120000",
                            "0110"
                    )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("imagesDecode")
    fun testDecode(width:Int,height:Int,image:String,expectedLayerIndex:Int, expectedResult:Int) {
        val imageDecoder = ImageDecoder(image, width, height)
        val layers = imageDecoder.decode()
        assertEquals(expectedLayerIndex, imageDecoder.findLayerWithMinZeroes(layers),"wrong layer index")
        assertEquals(expectedResult,imageDecoder.calculate(layers), "wrong result")
    }


    @ParameterizedTest
    @MethodSource("imagesCompose")
    fun testCompose(width:Int,height:Int,image:String,expectedResult:String) {
        val imageDecoder = ImageDecoder(image, width, height)
        val layers = imageDecoder.decode()
        val actualResult = imageDecoder.compose(layers)
        assertEquals(expectedResult,actualResult, "wrong result")
    }

}