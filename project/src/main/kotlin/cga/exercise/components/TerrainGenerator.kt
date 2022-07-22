package cga.exercise.components

import java.lang.Math.floor
import java.util.Random

class TerrainGenerator() {


    private val random = Random()
    var seed = random.nextInt(1_000_000)

    val terrainMaxHeight = 8

    private fun getSeededNoise(x: Int, z: Int): Float {
        random.setSeed((x * 12345 + z * 54321 + seed).toLong())
        return random.nextFloat()
    }

    private fun getSmoothedNoise(x: Int, z: Int): Float {
        val sides = (getSeededNoise(x - 1, z) +
                getSeededNoise(x + 1, z) +
                getSeededNoise(x, z - 1) +
                getSeededNoise(x, z + 1)) / 4
        return (getSeededNoise(x, z) / 3) + (sides * 2 / 3)
    }

    private fun interpolate(a: Float, b: Float, value: Float): Float {
        val theta = value * Math.PI
        val f = (1f - Math.cos(theta)) / 2
        return (a * (1f - f) + b * f).toFloat()

    }

    private fun getPerlinNoise(x: Float, z: Float): Float {
        val intX = x.toInt()
        val intZ = z.toInt()
        val fracX = x - intX
        val fracZ = z - intZ

        val obenlinks = getSmoothedNoise(intX, intZ)
        val obenrechts = getSmoothedNoise(intX + 1, intZ)
        val untenlinks = getSmoothedNoise(intX, intZ + 1)
        val untenrechts = getSmoothedNoise(intX + 1, intZ + 1)

        val oben = interpolate(obenlinks, obenrechts, fracX)
        val unten = interpolate(untenlinks, untenrechts, fracX)
        return interpolate(oben, unten, fracZ)
    }

    fun getTerrainHeight(x: Float, z: Float): Float {
        return (getPerlinNoise(x / 8, z / 8) * terrainMaxHeight)
    }

}