package com.adventofcode._2019.day._8.imagedecoder


class ImageDecoder(val input:String,val width:Int, val height:Int) {

    val layerCount = width * height

    fun decode(): List<String> {
        check (input.length % layerCount == 0)
        val layers = mutableListOf<String>()
        for (offset in 0..(input.length / layerCount)) {
            layers.add(input.slice(offset..(offset + layerCount)) )
        }
        return layers.toList()
    }

    fun calculate(layers:List<String>):Int {
        val layerWithMinZeroes = layers.minBy { it.count { i-> i == '0' } }
        return layerWithMinZeroes!!.count { it == '2' } * layerWithMinZeroes!!.count { it == '1' }
    }
}

fun main(args:Array<String>) {
    val input = ImageDecoder::class.java.getResource("/com/adventofcode/_2019/day/_8/imagedecoder/image.txt")
    val decoder = ImageDecoder(input.readText(),25,6)
    println(decoder.calculate(decoder.decode()))
}