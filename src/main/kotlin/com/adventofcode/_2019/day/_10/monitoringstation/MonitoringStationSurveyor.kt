package com.adventofcode._2019.day._10.monitoringstation

import java.lang.IllegalStateException
import java.lang.Math.pow
import java.lang.Math.sqrt
import kotlin.math.abs
import kotlin.math.pow
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

data class Slope(val dX:Int, val dY:Int) : Comparable<Slope> {
    fun distance():Double {
        return kotlin.math.sqrt(dX.toDouble().times(gcd).pow(2) + dY.toDouble().times(gcd).pow(2))
    }

    override fun compareTo(other: Slope): Int {
        //val distance = {s:Slope -> kotlin.math.sqrt(s.dX.toDouble().pow(2) + s.dY.toDouble().pow(2)) }
        return distance().compareTo(other.distance())
    }

    val gcd = greatestCommonDenominator(Math.abs(dY), Math.abs(dX))

    override fun equals(other: Any?): Boolean {
        return if (other is Slope) {
            this.hashCode() == other.hashCode()
        }
        else {
            false
        }
    }

    override fun hashCode(): Int {
        return if (gcd==0){
            0 // only 0,0
        }
        else {
            (dX/gcd).hashCode()*7 + (dY/gcd).hashCode()*13
        }
    }

    companion object {

        fun of(center:Point, x:Int, y:Int):Slope {
            return of(center, Point(x,y))
        }

        fun of(center:Point, other:Point):Slope {
            val point2 = if (center.x < other.x) { other } else { center }
            val point1 = if (point2 == center) { other } else { center }
            return Slope(point2.x - point1.x, point2.y - point1.y)
        }

    }

    override fun toString(): String {
        return "Slope($dX,$dY) = Slope(${dX/gcd},${dY/gcd})"
    }
}

data class Point(val x: Int, val y: Int) {

    fun quadrant(center: Point):Int {
        return when {
                    this.x >= center.x && this.y >= center.y -> 1
                    this.x >= center.x && this.y < center.y -> 0
                    this.x < center.x && this.y < center.y -> 3
                    this.x < center.x && this.y >= center.y -> 2
                    else -> {
                        println("center $center")
                        throw IllegalStateException("$x $y")
                    }
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

    fun createPair(current:Pair<Point,Point?>?, slope:Slope, point:Point, center:Point):Pair<Point,Point?> {
        return if (current != null) {
                    if (current.first.opposite(slope,point,center)) {
                        if (current.second != null ){
                            if (slope >= Slope.of(center, current.second!!)) {
                                Pair(current.first, point)
                            }
                            else {
                                current
                            }
                        }
                        else {
                            Pair(current.first, point)
                        }
                    }
                    else {
                        if (slope >= Slope.of(center, current.first)) {
                            Pair(point, current.second)
                        }
                        else {
                            current
                        }
                    }
                }
                else {
                    Pair(point,null)
                }
    }

    fun generateMapEdgePoints(center: Point): MutableMap<Slope, Pair<Point,Point?>> {
        val edgePoints = mutableMapOf<Slope, Pair<Point,Point?>>()
        val left = Point(0, center.y)
        val right = Point(xMax, center.y)
        val up = Point(center.x,0)
        val down = Point(center.x,yMax)
        val centerEdgePoints = setOf(left,right,up,down)
        for (x in 0..xMax) {
            for (y in 0..yMax) {
                val point = Point(x,y)
                if (center.x != x && center.y != y || (point in centerEdgePoints) ) {
                    val slope = Slope.of(center,x,y)
                    edgePoints[slope] = createPair(edgePoints[slope], slope, point, center)
                }
            }
        }
        return edgePoints
    }

    fun sortToEdgeRectangleBasedOnCenterPoint(center: Point):List<Point> {
        val edgePoints = generateMapEdgePoints(center)
        return edgePoints.values.flatMap { listOf(it.first,it.second) }.filterNotNull().sortedWith(
            compareBy(
                { it.quadrant(center) },
                { when(it.quadrant(center)) {
                    0 -> it.x-center.x 
                    1 -> center.x - it.x
                    2 -> center.x - it.x
                    3 -> it.x -center.x 
                    else -> throw IllegalStateException()
                }
                },
                { when(it.quadrant(center)) {
                    0 -> it.y - center.y
                    1 -> it.y - center.y
                    2 -> center.y - it.y
                    3 -> center.y - it.y
                    else -> throw IllegalStateException()
                } }
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

    fun vaporizeAsteroids(stationLocation:Point, dump: Boolean = false): List<Point> {
        val map = indexedMap.toMutableMap()
        map[stationLocation] = 'x'
        val vaporizedAsteroids = mutableListOf<Point>()
        val visibleAsteroids = mutableSetOf<Point>()
        val sortedMapEdge = sortToEdgeRectangleBasedOnCenterPoint(stationLocation)
        do {
            //println("${visibleAsteroids.size} $visibleAsteroids")/*
            dump(map,xMax,yMax)
            visibleAsteroids.clear()
            for (currentPoint in sortedMapEdge) {
                val line = LineSegment(stationLocation, currentPoint)
                val asteroid = line.value.find { it != stationLocation && map[it] == '#' }
                if (asteroid != null) {
                    visibleAsteroids.add(asteroid)
                }
            }
            //visibleAsteroids.addAll(findVisibleAsteroids(stationLocation, map,dump))
            vaporizedAsteroids.addAll(visibleAsteroids)
            visibleAsteroids.forEach { map[it] = '.' }
            //break
        } while (map.values.count{ it == '#' } > 0)
        return vaporizedAsteroids
    }


}

fun main(args:Array<String>) {
    val input = MonitoringStationSurveyor::class.java.getResource("/com/adventofcode/_2019/day/_10/monitoringstation/asteroid-map.txt")
    val monitoringStationSurveyor = MonitoringStationSurveyor(input.readText())
    val bestLocation = Point(11,11)//monitoringStationSurveyor.findBestLocation()
    //println ("$bestLocation ${monitoringStationSurveyor.findVisibleAsteroids(bestLocation).size}")
    val asteroidVaporizationOrder = monitoringStationSurveyor.vaporizeAsteroids(bestLocation)
    println("${asteroidVaporizationOrder[199]} ${asteroidVaporizationOrder[200]}")
}