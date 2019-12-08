package com.adventofcode._2019.day._6.weirdorbits

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class OrbitDecoderTest {

    companion object {

        @JvmStatic
        fun unsortedOrbitsMap() = listOf(
                Arguments.of(
                        "J)K\n" +
                                "C)D\n" +
                                "D)E\n" +
                                "B)G\n" +
                                "COM)B\n" +
                                "G)H\n" +
                                "E)F\n" +
                                "D)I\n" +
                                "E)J\n" +
                                "B)C\n" +
                                "K)L",
                        createExampleGraph()
                )
        )

        @JvmStatic
        fun orbitsMap() = listOf(
                Arguments.of(
                        "COM)B\n" +
                                "B)C\n" +
                                "C)D\n" +
                                "D)E\n" +
                                "E)F\n" +
                                "B)G\n" +
                                "G)H\n" +
                                "D)I\n" +
                                "E)J\n" +
                                "J)K\n" +
                                "K)L",
                        createExampleGraph()
                )
        )

        @JvmStatic
        fun orbitsCount() = listOf(
                Arguments.of("COM",0,0),
                Arguments.of("B",1,0),
                Arguments.of("C",1,1),
                Arguments.of("D",1,2),
                Arguments.of("E",1,3),
                Arguments.of("F",1,4),
                Arguments.of("G",1,1),
                Arguments.of("H",1,2),
                Arguments.of("I",1,3),
                Arguments.of("J",1,4),
                Arguments.of("K",1,5),
                Arguments.of("L",1,6)
        )

        fun createExampleGraph(): Map<String, Node> {
            val graph = mutableMapOf("COM" to Node("COM",null))            
            val addEdge = { parent:String, child:String ->
                graph[child] = Node(child, graph[parent])
                graph[parent]!!.children.add(graph[child]!!)
            }
            addEdge("COM","B")
            addEdge("B","C")
            addEdge("C","D")
            addEdge("D","E")
            addEdge("E","F")
            addEdge("B","G")
            addEdge("G","H")
            addEdge("D","I")
            addEdge("E","J")
            addEdge("J","K")
            addEdge("K","L")
            return graph
        }
    }

    @ParameterizedTest
    @MethodSource("orbitsMap")
    fun testDecode(encodedOrbits:String,expectedResult:Map<String,Node>) {
        val orbitDecoder = OrbitDecoder(encodedOrbits.split("\n"))
        val actualResult = orbitDecoder.decode()
        orbitDecoder.verify(actualResult)
        assert (expectedResult == actualResult)
    }
/*
    @ParameterizedTest
    @MethodSource("unsortedOrbitsMap")
    fun testDecodeUnsorted(encodedOrbits:String,expectedResult:Map<String,Node>) {
        val actualResult = OrbitDecoder(encodedOrbits.split("\n")).decode()
        assertEquals(expectedResult,actualResult)
    }
*/
    @ParameterizedTest
    @MethodSource("orbitsCount")
    fun testCount(nodeId:String, expectedDirectCount:Int, expectedIndirectCount:Int) {
        val orbitCounter = OrbitCounter()
        orbitCounter.countForNode(createExampleGraph(),nodeId)
        assertEquals(expectedDirectCount, orbitCounter.direct, "direct")
        assertEquals(expectedIndirectCount, orbitCounter.indirect, "indirect")
    }

    @Test
    fun testSum() {
        val orbitCounter = OrbitCounter()
        orbitCounter.count(createExampleGraph())
        assertEquals(11, orbitCounter.direct)
        assertEquals(31, orbitCounter.indirect)
    }


}