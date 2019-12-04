package com.adventofcode._2019.day._3.wirecrosses

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.lang.IllegalStateException

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

    fun placeWiresOnPanel():Map<Pair<Int,Int>,MutableMap<String,Int>> {
        val panel = mutableMapOf<Pair<Int,Int>,MutableMap<String,Int>>()
        encodedLines.forEachIndexed { index, line ->
            var currentPoint = 0 to 0
            var lineStepCounter = 0
            for (encodedLineSegment in line) {
                val (direction, length) = LineSegmentDecoder(encodedLineSegment).decode()
                val lineSegment = direction.toCartesian(currentPoint, length + 1)
                currentPoint = lineSegment.last()
                lineSegment.withIndex().forEach {
                    val lineId = toLineId(index)
                    panel.putIfAbsent(it.value, mutableMapOf())
                    val linesThatCrossThisPoint = panel[it.value]!!
                    if (!linesThatCrossThisPoint.containsKey(lineId)) {
                        linesThatCrossThisPoint[lineId] = it.index + lineStepCounter
                    }
                    else {
                        lineStepCounter = linesThatCrossThisPoint[lineId]!!
                    }
                }
                lineStepCounter += lineSegment.size - 1
            }
        }
        return panel
    }

    fun findSpatiallyClosestCrossing(panel:Map<Pair<Int,Int>,MutableMap<String,Int>>):Pair<Int,Int> {
        return panel
                .filter { it.key.first != 0 && it.key.second != 0 }
                .filter { it.value.keys.size > 1 }
                .keys
                .minBy{ manhattanDistance(it) }!!
    }

    fun findTemporallyClosestCrossing(panel:Map<Pair<Int,Int>,MutableMap<String,Int>>):Pair<Int,Int> {
        return panel
                .filter { it.key.first != 0 && it.key.second != 0 }
                .filter { it.value.keys.size > 1 }
                .minBy { it.value.values.sum() }!!
                .key
    }

    fun findNumberOfStepsToClosestCrossing(): Int {
        val panel = placeWiresOnPanel()
        val closestCrossing = findTemporallyClosestCrossing(panel)
        return panel[closestCrossing]!!.values.sum()
    }

    fun findDistanceToClosestCrossing():Int {
        val panel = placeWiresOnPanel()
        val closestCrossing = findSpatiallyClosestCrossing(panel)
        return manhattanDistance(closestCrossing)
    }

    fun printPanel():String {
        val panel = placeWiresOnPanel()
        val x = panel.keys.map { it.first }.max()!!
        val y = panel.keys.map { it.second }.max()!!
        val data = ByteArrayOutputStream()
        val printStream = PrintStream(data)
        (0..x).forEach { i->
            (0..y).forEach { j ->
                val point = Pair(i,j)
                printStream.print(
                        if (point == Pair(0,0)) {
                             " o "
                        }
                        else if (panel.containsKey(point)) {
                            val lineIds = panel.get(point)!!
                            if (lineIds.keys.size > 1) {
                                " X "
                            }
                            else {
                                " ${toIndex(lineIds.keys.first())} "
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

fun main(args:Array<String>) {
    val input = WiresPanel::class.java.getResource("/com/adventofcode/_2019/day/_3/wirecrosses/wires.txt")
    val wires = input
                            .readText()
                            .split(Regex(System.getProperty("line.separator")))
                            .map {
                                it.split(",").toList()
                            }
    println(WiresPanel(wires).findDistanceToClosestCrossing())
    println(WiresPanel(wires).findNumberOfStepsToClosestCrossing())
}