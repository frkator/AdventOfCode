package com.adventofcode._2019.day._6.weirdorbits

import java.lang.IllegalStateException

data class Node(val id:String, val parent:Node?, val children:MutableSet<Node> = mutableSetOf()) {
    override fun toString(): String {
        return "($id, ${parent?.id ?: "ROOT" }, ${children.map { it.id }})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}

class OrbitCounter {

    private var _direct:Int = 0
    val direct
        get() = _direct

    private var _indirect:Int = 0
    val indirect
        get() = _indirect


    fun countForNode(graph:Map<String,Node>, nextNodeId:String) {
        var current = graph[nextNodeId] ?: error("should not happen")
        if (current.parent != null) {
            _direct++
            while (current.parent!!.parent != null) {
                current = current.parent!!
                _indirect++
            }
        }
    }

    fun count(graph:Map<String,Node>):OrbitCounter {
        graph.forEach{countForNode(graph,it.key)}
        return this
    }

    override fun toString(): String {
        return "$_direct,$_indirect = ${_direct + _indirect}"
    }

}

class OrbitDecoder(private val encodedOrbits:List<String>) {

    fun decode():Map<String, Node> {
        val graph = mutableMapOf<String, Node>()
        encodedOrbits.forEach {  encodedEdge ->
            val split = encodedEdge.split(")")
            val parent = graph.computeIfAbsent(split[0]) { id -> Node(id, null) }
            val child = graph.computeIfAbsent(split[1]) { id -> Node(id, parent) }
            parent.children.add(child)
        }
        val rootCandidates = graph.filter { it.value.parent == null }
        if (rootCandidates.size > 1) {
            val rootCandidatesParentPerChild = graph
                    .filter { it.value.children.map { it.id }.intersect(rootCandidates.keys).isNotEmpty() }
                    .flatMap { e -> e.value.children.map {  it.id to e.value.id } }
                    .toMap()
            rootCandidates
                    .filter { it.key in rootCandidatesParentPerChild }
                    .forEach {
                        graph[it.key] = Node(it.key, graph[rootCandidatesParentPerChild[it.key]], it.value.children)
                    }
        }
        return graph.toMap()
    }

    fun verify(graph: Map<String, Node>) {

        check(
                graph.filter { it.value.children.isEmpty() }.map { it.key }.size
                        ==
                graph.filter { it.value.children.isEmpty() }.map { it.key }.toSet().size
        )

        val leaves = graph.filter { it.value.children.isEmpty() }.map { it.key }.toSet()
        val onlyParents = graph.filter { it.key !in leaves }
        for (encodedOrbit in encodedOrbits) {
            val current = encodedOrbit.split(")")[0]
            val child = encodedOrbit.split(")")[1]
            if(onlyParents.containsKey(current)) {
                onlyParents[current]!!.children.remove(graph[child])
            }
        }
        if (onlyParents.filter { it.value.children.isNotEmpty() }.isNotEmpty()) {
            print(onlyParents.filter { it.value.children.isNotEmpty()})
            throw IllegalStateException()
        }
    }

}

fun main(args:Array<String>) {
    val input = OrbitCounter::class.java.getResource("/com/adventofcode/_2019/day/_6/weirdorbits/orbitsMap.txt")
    val source = input.readText().lines()
    val orbitDecoder = OrbitDecoder(source)
    val graph = orbitDecoder.decode()
    orbitDecoder.verify(graph)
    println(OrbitCounter().count(graph))
    println(graph.size)
    println(WeirdOrbit2(source).countOrbits())
    println(WeirdOrbit2(source).countTransfers())
}