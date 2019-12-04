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
        return line.takeLast(line.size-1)
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

enum class Direction(val code:String,val operation:(Pair<Int,Int>)->Pair<Int,Int> ) : CoordinateTranslator {

    UP("U",{ point -> Pair(point.first, point.second + 1) }) {
        override fun toCartesian(point: Pair<Int, Int>, length: Int): List<Pair<Int, Int>> {
            return CoordinateTranslatorCalculator(point,length, operation) .calculate()
        }
    },
    DOWN("D",{ point -> Pair(point.first, point.second - 1) }) {
        override fun toCartesian(point: Pair<Int, Int>, length: Int): List<Pair<Int, Int>> {
            return CoordinateTranslatorCalculator(point,length, operation) .calculate()
        }
    },
    LEFT("L", { point -> Pair(point.first - 1, point.second) }) {
        override fun toCartesian(point: Pair<Int, Int>, length: Int): List<Pair<Int, Int>> {
            return CoordinateTranslatorCalculator(point,length,operation) .calculate()
        }
    },
    RIGHT("R", { point -> Pair(point.first + 1, point.second) }) {
        override fun toCartesian(point: Pair<Int, Int>, length: Int): List<Pair<Int, Int>> {
            return CoordinateTranslatorCalculator(point,length,operation) .calculate()
        }
    },
    ILLEGAL("",{throw IllegalStateException()}) {
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
/*
    fun placeWiresOnPanel():Map<Pair<Int,Int>,MutableMap<String,Int>> {
        val panel = mutableMapOf<Pair<Int,Int>,MutableMap<String,Int>>()
        encodedLines.forEachIndexed { index, line ->
            var currentPoint = 0 to 0
            var lineStepCounter = 1
            for (encodedLineSegment in line) {
                //println ("$encodedLineSegment")
                val (direction, length) = LineSegmentDecoder(encodedLineSegment).decode()
                val lineSegment = direction.toCartesian(currentPoint, length +1 )
                currentPoint = lineSegment.last()
                lineSegment.forEach {
                    val lineId = toLineId(index)
                    panel.putIfAbsent(it, mutableMapOf())
                    val linesThatCrossThisPoint = panel[it]!!
                    if (!linesThatCrossThisPoint.containsKey(lineId)) {
                        linesThatCrossThisPoint[lineId] = lineStepCounter
                    }
                    else {
                       // println ("$lineId : prev ${linesThatCrossThisPoint[lineId]} current $lineStepCounter")
                        lineStepCounter = linesThatCrossThisPoint[lineId]!!
                    }
                    lineStepCounter++
                }
            }
        }
        return panel
    }
*/

    fun placeWiresOnPanel():Map<Pair<Int,Int>,MutableMap<String,Int>> {
        val pointsA = mutableMapOf<Pair<Int, Int>,Int>()
        val pointsB = mutableMapOf<Pair<Int, Int>,Int>()

        encodedLines.forEachIndexed { index, line ->
            var x=0
            var y =0
            var steps = 0
            for (encodedLineSegment in line) {
                val (direction, length) = LineSegmentDecoder(encodedLineSegment).decode()
                for (i in 0..length) {
                    steps++
                    val point = direction.operation(Pair(x, y))
                    x = point.first
                    y = point.second
                    (if(index==0){pointsA}else {pointsB}).putIfAbsent(point,steps)
                }
            }

        }
        println (pointsA.keys.intersect(pointsB.keys).map {(pointsA[it]!!+pointsB[it]!!)}.min())
        println (manhattanDistance(pointsA.keys.intersect(pointsB.keys).minBy (manhattanDistance )!!) )
        throw IllegalStateException()
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
        val xMax = panel.keys.map { it.first }.max()!!
        val yMax = panel.keys.map { it.second }.max()!!
        val xMin = panel.keys.map { it.first }.min()!!
        val yMin = panel.keys.map { it.second }.min()!!
        val data = ByteArrayOutputStream()
        val printStream = PrintStream(data)
        (xMin..xMax).forEach { i->
            (yMin..yMax).forEach { j ->
                val point = Pair(i,j)
                printStream.print(
                        if (point == Pair(0,0)) {
                             "o"
                        }
                        else if (panel.containsKey(point)) {
                            val lineIds = panel.get(point)!!
                            if (lineIds.keys.size > 1) {
                                "X"
                            }
                            else {
                                "${toIndex(lineIds.keys.first())}"
                            }
                        }
                        else {
                            "."
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
  //  println(WiresPanel(wires).findNumberOfStepsToClosestCrossing())
}
/*
651
7534
 */