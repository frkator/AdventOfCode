package com.adventofcode._2019.day._3.wirecrosses

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.lang.IllegalStateException
import java.lang.StringBuilder

val manhattanDistance = { point:Pair<Int,Int> -> Math.abs(point.first) + Math.abs(point.second) }

interface CoordinateTranslator {
    fun toCartesian(point:Pair<Int,Int>,length:Int):List<Pair<Int,Int>>
}

class CoordinateTranslatorCalculator(val point:Pair<Int,Int>,val length:Int,val nextPoint:(Pair<Int,Int>)->Pair<Int,Int>) {
    fun calculate():List<Pair<Int, Int>> {
        val line = mutableListOf(point)
        for (counter in 1 until length) {
            line.add(nextPoint(line.last()))
        }
        return line
    }
}

data class DecodedDirection(val direction:Direction, val length:Int)

class LineSegmentDecoder(val encodedLineSegment:String) {
    fun decode() = DecodedDirection(
            Direction.decode(
                    encodedLineSegment
                            .replace(
                                    Regex("\\d+"),
                                    ""
                            )
            ),
            encodedLineSegment
                    .replace(
                            Regex("[^\\d]+"),
                            ""
                    )
                    .toInt()
    )
}

enum class Direction(val code:String) : CoordinateTranslator {

    UP("U") {
        override fun toCartesian(point: Pair<Int, Int>, length: Int): List<Pair<Int, Int>> {
            return CoordinateTranslatorCalculator(point,length) { point -> Pair(point.first, point.second + 1) }.calculate()
        }
    },
    DOWN("D") {
        override fun toCartesian(point: Pair<Int, Int>, length: Int): List<Pair<Int, Int>> {
            return CoordinateTranslatorCalculator(point,length) { point -> Pair(point.first, point.second - 1) }.calculate()
        }
    },
    LEFT("L") {
        override fun toCartesian(point: Pair<Int, Int>, length: Int): List<Pair<Int, Int>> {
            return CoordinateTranslatorCalculator(point,length) { point -> Pair(point.first - 1, point.second) }.calculate()
        }
    },
    RIGHT("R") {
        override fun toCartesian(point: Pair<Int, Int>, length: Int): List<Pair<Int, Int>> {
            return CoordinateTranslatorCalculator(point,length) { point -> Pair(point.first + 1, point.second) }.calculate()
        }
    },
    ILLEGAL("") {
        override fun toCartesian(point: Pair<Int, Int>, length: Int): List<Pair<Int, Int>> {
            throw IllegalStateException("should never happen")
        }
    };

    companion object {
        @JvmStatic
        fun decode(code: String): Direction = values().find { direction -> direction.code == code } ?: ILLEGAL
    }

}

class WiresPanel(private val encodedLines:List<List<String>>) {

    private fun toLineId(index:Int)  = "line-${index}"
    private fun toIndex(lineId:String)  = lineId.replaceFirst("line-","").toInt()

    fun placeWiresOnPanel():Map<Pair<Int,Int>,MutableList<String>> {
        val panel = mutableMapOf<Pair<Int,Int>,MutableList<String>>()
        encodedLines.forEachIndexed() { index, line ->
            var currentPoint = 0 to 0
            panel.putIfAbsent(currentPoint, mutableListOf())
            panel.get(currentPoint)!!.add(toLineId(index))
            for (encodedLineSegment in line) {
                val (direction, length) = LineSegmentDecoder(encodedLineSegment).decode()
                val lineSegment = direction.toCartesian(currentPoint, length)
                currentPoint = lineSegment.last().copy()
                //if (lineSegment.size > 1) {
                    lineSegment/*.takeLast(lineSegment.size - 1)*/.forEach {
                        panel.putIfAbsent(it, mutableListOf())
                        panel.get(it)!!.add(toLineId(index))
                  //  }
                }

            }
        }
        return panel
    }

    fun findClosestCrossing(panel:Map<Pair<Int,Int>,MutableList<String>>):Pair<Int,Int> {
        return panel.filter { it.key.first != 0 && it.key.second != 0 }.filter { it.value.toSet().size > 1 }.keys.minBy{ manhattanDistance(it) }!!
    }

    fun findDistanceToClosestCrossing():Int {
        val panel = placeWiresOnPanel()
        val closestCrossing = findClosestCrossing(panel)
        return manhattanDistance(closestCrossing)
    }

    fun printPanel():String {
        val panel = placeWiresOnPanel()
        val x = panel.keys.map { it.first }.max()!!
        val y = panel.keys.map { it.second }.max()!!
        val data = ByteArrayOutputStream()
        val printStream = PrintStream(data)
        (y+1 downTo 0).forEach { i->
            (0..x).forEach { j ->
                val point = Pair(i,j)
                printStream.print(
                        if (point == Pair(0,0)) {
                             " o "
                        }
                        else if (panel.containsKey(point)) {
                            val lineIds = panel.get(point)!!
                            if (lineIds.toSet().size > 1) {
                                " X "
                            }
                            else {
                                " ${toIndex(lineIds.first())} "
                            }
                        }
                        else {
                            " . "
                        }
                )
            }
            printStream.println()
        }
        return data.toString()
    }

}