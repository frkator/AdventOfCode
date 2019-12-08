package com.adventofcode._2019.day._6.weirdorbits

import java.lang.IllegalStateException

class WeirdOrbit2(private val encodedOrbits:List<String>){

    fun decode(): Map<String, List<String>> {
        val parentPerChild = encodedOrbits.map {
            val split = it.split(")")
            split[1] to split[0]
        }.toMap()

        val chains = mutableMapOf<String,List<String>>()
        parentPerChild.keys.forEach {
            if (it !in chains) {
                chains[it] = chain(it,parentPerChild)
            }
        }
        return chains.toMap()
    }

    private fun chain(it: String, parentPerChild: Map<String, String>): List<String> {
        if ("COM" == it) {
            return listOf()
        }

        var current = parentPerChild[it] ?: error("")
        val chain = mutableListOf<String>()
        while (current != "COM") {
            chain.add(current)
            current = parentPerChild[current] ?: error("")
        }
        chain.add(current)
        return  chain
    }

    fun countOrbits(): Int {
        return decode().values.map { it.size }.sum()
    }

    fun countTransfers(): Int {
        val chains = decode()
        val myChain = chains["YOU"]!!
        val santaChain = chains["SAN"]!!
        return myChain.size + santaChain.size - 2* myChain.intersect(santaChain).size

    }

}

