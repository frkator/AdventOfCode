package com.adventofcode._2019.day._10.monitoringstation

import java.lang.IllegalStateException
import kotlin.math.abs
import kotlin.math.roundToInt

fun dump(map:Map<Point, Char>, xMax:Int, yMax:Int) {
    print("   ")
    (0..xMax).forEach {
        print(if (it / 10 > 0) {
            "${it / 10}"
        } else {
            " "
        })
    }
    println()
    print("   ")
    (0..xMax).forEach { print(it % 10) }
    println()
    println(map
            .map { it }
            .groupBy { it.key.y }
            .map { "%2d ${it.value.map { it.value }.joinToString(separator = "")}".format(it.key) }
            .joinToString(separator = "\n")

    )
    println()
}

fun greatestCommonDenominator(a:Int, b:Int):Int {
    return if (b==0) { a } else { greatestCommonDenominator(b,a%b) }
}

data class Slope(val dX:Int, val dY:Int) {

    val gcd = greatestCommonDenominator(dY, dX)

    override fun equals(other: Any?): Boolean {
        return if (other is Slope) {
            this.hashCode() == other.hashCode()
        }
        else {
            false
        }
    }

    override fun hashCode(): Int {
        return (dX/gcd).hashCode() + (dY/gcd).hashCode()
    }

    companion object {

        fun of(center:Point, x:Int, y:Int):Slope {
            return Slope(Math.abs(center.x-x),y-center.y)
        }

        fun of(center:Point, other:Point):Slope {
            return of(center, other.x, other.y)
        }

    }

    override fun toString(): String {
        return "Slope($dX,$dY) = Slope(${dX/gcd},${dY/gcd})"
    }
}

data class Point(val x: Int, val y: Int) {
    fun quadrant(center: Point):Int {
        return when {
                    this.x >= center.x && this.y >= center.y -> 0
                    this.x > center.x && this.y < center.y -> 1
                    this.x <= center.x && this.y <= center.y -> 2
                    this.x < center.x && this.y > center.y -> 3
                    else -> throw IllegalStateException()
                }
    }

    fun opposite(slope:Slope, other:Point, center:Point):Boolean {
        val thisSlope = Slope.of(this,other)
        if (thisSlope != slope) {
            return false
        }
        return this.quadrant(center) != other.quadrant(center)
    }
}

class LineSegment(val start: Point, val end: Point) {

    val value: LinkedHashSet<Point> = calculate()

    private fun calculate(): LinkedHashSet<Point> {
        val lineSegment = LinkedHashSet<Point>().toMutableSet()
        val f = { x: Int -> ((end.y - start.y).toDouble() / (end.x - start.x).toDouble()) * (x - end.x) + end.y }
        if (start.x == end.x) {
            val codomain = if (start.y > end.y) {
                end.y..start.y
            } else {
                start.y..end.y
            }
            lineSegment.addAll(codomain.map { Point(start.x, it) })
        } else {
            val domain = if (start.x > end.x) {
                end.x..start.x
            } else {
                start.x..end.x
            }
            for (x in domain) {
                val ordinate = f(x)
                if (ordinate - ordinate.roundToInt().toDouble() == 0.0) {
                    lineSegment.add(Point(x, ordinate.toInt()))
                }
            }
        }
        return LinkedHashSet(if(lineSegment.last() == start) {lineSegment.reversed()} else {lineSegment})
    }

}

class MonitoringStationSurveyor(map: String) {
    val indexedMap: Map<Point, Char> = map
            .lines()
            .mapIndexed { idx, line -> idx to line.mapIndexed { idx, value -> idx to value } }
            .flatMap { it.second.map { x -> Point(x.first, it.first) to x.second } }
            .toMap()
    val xMax = indexedMap.keys.maxBy { it.x }!!.x
    val yMax = indexedMap.keys.maxBy { it.y }!!.y

    fun generateMapEdgePoints(center: Point): MutableMap<Slope, Point> {
        val edgePoints = mutableMapOf<Slope, Point>()
        val left = Point(0, center.y)
        val right = Point(xMax, center.y)
        val up = Point(center.x,0)
        val down = Point(center.x,yMax)
        val centerEdgePoints = setOf(left,right,up,down)
        for (x in 0..xMax) {
            for (y in 0..yMax) {
                val point = Point(x,y)
                if (center.x != x && center.y != y) {
                    edgePoints[Slope.of(center,x,y)] = point
                }
                else if (point in centerEdgePoints) {
                    edgePoints[
                        when (point) {
                            left -> Slope(Int.MAX_VALUE,Int.MAX_VALUE-1)
                            right -> Slope(Int.MAX_VALUE,Int.MAX_VALUE-2)
                            up -> Slope(Int.MAX_VALUE,Int.MAX_VALUE-3)
                            down -> Slope(Int.MAX_VALUE,Int.MAX_VALUE-4)
                            else -> throw IllegalStateException()
                        }
                    ] = point
                }
            }
        }
        return edgePoints
    }

    fun sortToEdgeRectangleBasedOnCenterPoint(center: Point):List<Point> {
        val edgePoints = generateMapEdgePoints(center)
        return edgePoints.values.toList().sortedWith(
            compareBy(
                { it.quadrant(center) },
                { abs(center.x - it.x) },
                { it.y - center.y }
            )
        )
    }

    fun findVisibleAsteroids(point: Point, indexedMap:Map<Point, Char> = this.indexedMap,dump:Boolean = false): Set<Point> {
        val allAsteroids = indexedMap.keys.filter { it != point }.toMutableList()
        val visibleAsteroids = mutableSetOf<Point>()
        if(dump)println ("for point $point")
        for (x in 0..xMax) {
            for (y in 0..yMax) {
                val currentPoint = Point(x, y)
                if (allAsteroids.contains(currentPoint)) {
                    val currentLine = LineSegment(point, currentPoint)
                    val firstAsteroid = currentLine.value.find { it != point && indexedMap[it] == '#' }
                    if (firstAsteroid != null) {
                        if (dump) println("$firstAsteroid -> |${currentLine.start},${currentLine.end}| ${currentLine.value.size} ${currentLine.value}")
                        visibleAsteroids.add(firstAsteroid)
                    }
                    allAsteroids.removeAll(currentLine.value)
                }
            }
        }
        if (dump) println("visible asteroids ${visibleAsteroids.size} $visibleAsteroids")
        return visibleAsteroids.toSet()
    }

    fun findBestLocation(dump:Boolean = false): Point {
        return indexedMap.filter { it.value == '#' }.keys.maxBy { findVisibleAsteroids(it,indexedMap,dump).size }!!
    }

    fun vaporizeAsteroids(stationLocation:Point): List<Point> {
        val map = indexedMap.filter { it.key != stationLocation }.toMutableMap()
        map[stationLocation] = 'x'
        val vaporizedAsteroids = mutableListOf<Point>()
        val visibleAsteroids = mutableSetOf<Point>()
        do {
            //println("${visibleAsteroids.size} $visibleAsteroids")/*
            dump(map,xMax,yMax)
            visibleAsteroids.clear()
            visibleAsteroids.addAll(findVisibleAsteroids(stationLocation, map,true))
            vaporizedAsteroids.addAll(visibleAsteroids)
            visibleAsteroids.forEach { map[it] = '.' }
            //break
        } while (visibleAsteroids.isNotEmpty())
        return vaporizedAsteroids
    }


}

fun main(args:Array<String>) {
    val input = MonitoringStationSurveyor::class.java.getResource("/com/adventofcode/_2019/day/_10/monitoringstation/asteroid-map.txt")
    val monitoringStationSurveyor = MonitoringStationSurveyor(input.readText())
    val bestLocation = monitoringStationSurveyor.findBestLocation()
    println ("$bestLocation ${monitoringStationSurveyor.findVisibleAsteroids(bestLocation).size}")
    val asteroidVaporizationOrder = monitoringStationSurveyor.vaporizeAsteroids(bestLocation)
    println("${asteroidVaporizationOrder[199]}")
}