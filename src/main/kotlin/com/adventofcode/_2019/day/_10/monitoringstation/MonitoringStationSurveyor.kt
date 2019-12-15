package com.adventofcode._2019.day._10.monitoringstation

import kotlin.math.roundToInt

data class Point(val x: Int, val y: Int)

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
        return LinkedHashSet(lineSegment)
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

    fun countAsteroids(point: Point): Int {
        val allAsteroids = indexedMap.keys.filter { it != point }.toMutableList()
        val visibleAsteroids = mutableSetOf<Point>()
        for (x in 0..xMax) {
            for (y in 0..yMax) {
                val currentPoint = Point(x, y)
                if (allAsteroids.contains(currentPoint)) {
                    val currentLine = LineSegment(point, currentPoint)
                    val firstAsteroid = currentLine.value.find { it != point && indexedMap[it] == '#' }
                    if (firstAsteroid != null) {
                        visibleAsteroids.add(firstAsteroid)
                    }
                    allAsteroids.removeAll(currentLine.value)
                }
            }
        }
        //println("$point ${visibleAsteroids.size} $visibleAsteroids")
        return visibleAsteroids.size
    }

    fun findBestLocation(): Point {
        return indexedMap.filter { it.value == '#' }.keys.maxBy { countAsteroids(it) }!!
    }

}

fun main(args:Array<String>) {
    val input = MonitoringStationSurveyor::class.java.getResource("/com/adventofcode/_2019/day/_10/monitoringstation/asteroid-map.txt")
    val monitoringStationSurveyor = MonitoringStationSurveyor(input.readText())
    val bestLocation = monitoringStationSurveyor.findBestLocation()
    println ("$bestLocation ${monitoringStationSurveyor.countAsteroids(bestLocation)}")
}