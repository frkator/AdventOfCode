package com.adventofcode._2019.day._8.imagedecoder

import java.lang.IllegalStateException


class ImageDecoder(val input:String,val width:Int, val height:Int) {

    val layerElementsCount = width * height

    fun decode(): List<String> {
        check (input.length % layerElementsCount == 0)
        val layers = mutableListOf<String>()
        for (offset in 0 until input.length step layerElementsCount) {
            layers.add(input.slice(offset until (offset + layerElementsCount)) )
        }
        return layers.toList()
    }

    fun findLayerWithMinZeroes(layers:List<String>): Int {
        return layers.withIndex().minBy { it.value.count { i-> i == '0' } }!!.index
    }

    fun calculate(layers:List<String>):Int {
        val layerWithMinZeroes = layers[findLayerWithMinZeroes(layers)]
        return layerWithMinZeroes!!.count { it == '2' } * layerWithMinZeroes!!.count { it == '1' }
    }

    fun compose(layers:List<String>): String {
        val result = StringBuilder()
        for (index in 0 until layerElementsCount) {
            val nonZeroes = (0 until layers.size).map { layers[it].slice(index..index) }.filter { it != "2" }
            result.append( if (nonZeroes.isEmpty()) { "2" } else { nonZeroes.first()})
        }
        return result.toString()
    }

    fun printComposedImageWithInvertedColors(image:String) {
        println("image data")
        image.windowed(width, width) {
            println(it)
        }
        println("image")
        image.windowed(width, width) { layer ->
            layer.toString().forEach { pixel ->
                print(
                    when(pixel.toString()) {
                        "0" -> " "
                        "1" -> "█"
                        "2" -> "x"
                        else -> throw IllegalStateException()
                    }
                )
            }
            println()
        }
    }
}

fun main(args:Array<String>) {
    val input = ImageDecoder::class.java.getResource("/com/adventofcode/_2019/day/_8/imagedecoder/image.txt")
    val decoder = ImageDecoder(input.readText(),25,6)
    val imageData = decoder.decode()
    println(decoder.calculate(imageData))
    decoder.printComposedImageWithInvertedColors(decoder.compose(imageData))
}