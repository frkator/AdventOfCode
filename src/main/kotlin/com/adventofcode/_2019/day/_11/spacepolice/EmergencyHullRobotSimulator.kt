package com.adventofcode._2019.day._11.spacepolice

import com.adventofcode._2019.day._10.monitoringstation.Point
import com.adventofcode._2019.day._9.sensorboost.CompleteIntCodeComputer
import com.adventofcode._2019.day._9.sensorboost.IO
import java.lang.IllegalStateException
import java.math.BigInteger

class InstrumentationDataStore(val input:MutableList<Int> = mutableListOf(), val output: MutableList<BigInteger> = mutableListOf()) {

    fun nextInput() = input.removeAt(0)

    fun clear() {
        input.clear()
        output.clear()
    }
}

class IoInstrumentation(val dataStore:InstrumentationDataStore) : IO() {

    override fun inputImpl(): String {
        return dataStore.nextInput().toString()
    }

    override fun outputImpl(output: BigInteger): BigInteger {
        dataStore.output.add(output)
        return super.outputImpl(output)
    }
}

enum class RobotDirection(val symbol:Int) {

    LEFT(0),RIGHT(1);

    companion object {
        fun from(symbol:Int):RobotDirection {
            return if (symbol == RIGHT.symbol) {RIGHT} else {LEFT}
        }
    }
}

enum class Tile(val symbol:Char,val code:Int) {
    BLACK('.',0),WHITE('#',1);

    fun code(): Int {
        return if (this == BLACK) {0} else {1}
    }

    companion object {
        fun from(symbol:Int):Tile {
            return if (symbol == BLACK.code) {BLACK} else {WHITE}
        }
    }
}

interface Directionable {
    fun next(currentPosition:Point, newRobotDirection: RobotDirection):Pair<Point,MapDirection>
}

enum class MapDirection(val symbol:Char) : Directionable {
    UP('^') {
        override fun next(currentPosition: Point, newRobotDirection: RobotDirection): Pair<Point, MapDirection> =
                when(newRobotDirection){
                    RobotDirection.LEFT -> Pair(Point(currentPosition.x - 1, currentPosition.y), LEFT)
                    RobotDirection.RIGHT -> Pair(Point(currentPosition.x + 1, currentPosition.y), RIGHT)
                }
    },
    DOWN('V') {
        override fun next(currentPosition: Point, newRobotDirection: RobotDirection): Pair<Point, MapDirection> =
                when(newRobotDirection){
                    RobotDirection.LEFT -> Pair(Point(currentPosition.x + 1, currentPosition.y), RIGHT)
                    RobotDirection.RIGHT -> Pair(Point(currentPosition.x - 1, currentPosition.y), LEFT)
                }
    },
    LEFT ('<'){
        override fun next(currentPosition: Point, newRobotDirection: RobotDirection): Pair<Point, MapDirection> =
                when(newRobotDirection){
                    RobotDirection.LEFT -> Pair(Point(currentPosition.x, currentPosition.y - 1), DOWN)
                    RobotDirection.RIGHT -> Pair(Point(currentPosition.x, currentPosition.y + 1), UP)
                }
    },
    RIGHT('>') {
        override fun next(currentPosition: Point, newRobotDirection: RobotDirection): Pair<Point, MapDirection> =
                when(newRobotDirection){
                    RobotDirection.LEFT -> Pair(Point(currentPosition.x, currentPosition.y + 1), UP)
                    RobotDirection.RIGHT -> Pair(Point(currentPosition.x, currentPosition.y - 1), DOWN)
                }
    }
}

class DirectionCalculator(initialDirection:MapDirection = MapDirection.UP,initialPosition:Point = Point(0,0)) {

    private var currentDirection = initialDirection
    private var currentPosition = initialPosition

    fun calculate(newRobotDirection: RobotDirection):Point {
        val (newPosition, newMapDirection) = currentDirection.next(currentPosition, newRobotDirection)
        currentDirection = newMapDirection
        currentPosition = newPosition
        return currentPosition
    }

}

class EmergencyHullRobotSimulator {

    private val hull:MutableMap<Point,Pair<Tile,Int>> = mutableMapOf()

    fun paint(source:String, dump:Boolean = true) {
        val memory = source.split(Regex(",")).map { it.toBigInteger() }.toList()
        val computer = CompleteIntCodeComputer(memory.toMutableList())
        IO.printOutput = dump
        var currentPosition = Point(0,0)
        val direction = DirectionCalculator()
        val dataStore = InstrumentationDataStore()
        IO.set(IoInstrumentation(dataStore))
        do {
            val (tile,tileRepaintCount) = hull.getOrDefault(currentPosition, Pair(Tile.BLACK,0))
            dataStore.input.add(tile.code())
            computer.executeChunk()
            check(dataStore.output.size != 2)
            println(dataStore.output.size)//above fails
            if (dataStore.output.size > 2) {
                val newDirectionCode = dataStore.output[1].toInt()
                currentPosition = direction.calculate(RobotDirection.from(newDirectionCode))
            }
            val newTileColorCode = dataStore.output[0].toInt()
            hull[currentPosition] = Tile.from(newTileColorCode) to tileRepaintCount + 1
            dataStore.clear()
        }
        while (!computer.isDone())
        val maxPainted = hull.maxBy { it.value.second }!!
        println("maxPainted")
    }
}

fun main(args:Array<String>){
    val input = EmergencyHullRobotSimulator::class.java.getResource("/com/adventofcode/_2019/day/_11.spacepolice/source.txt")
    EmergencyHullRobotSimulator().paint(input.readText())
}
