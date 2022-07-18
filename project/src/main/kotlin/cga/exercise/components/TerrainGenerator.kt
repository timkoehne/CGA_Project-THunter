package cga.exercise.components

import java.lang.Math.floor
import java.util.Random

class TerrainGenerator() {


    private val random = Random()
    var seed = random.nextInt(1_000_000)

    val terrainMaxHeight = 4

    private fun getRandomNoise(x: Int, z: Int): Float {
        random.setSeed((x * 12345 + z * 54321 + seed).toLong())
        return random.nextFloat()
    }

    private fun getSmoothNoise(x: Int, z: Int): Float {
        val sides = (getRandomNoise(x - 1, z) +
                getRandomNoise(x + 1, z) +
                getRandomNoise(x, z - 1) +
                getRandomNoise(x, z + 1)) / 4


        return (getRandomNoise(x, z) / 3) + (sides / 2)
    }

    private fun interpolate(a: Float, b: Float, blend: Float): Float {
        val theta = blend * Math.PI
        val f = (1f - Math.cos(theta)) / 2
        return (a * (1f - f) + b * f).toFloat()

    }

    private fun getInterpolatedNoise(x: Float, z: Float): Float {
        val intX = x.toInt()
        val intZ = z.toInt()
        val fracX = x - intX
        val fracZ = z - intZ

        val v1 = getSmoothNoise(intX, intZ)
        val v2 = getSmoothNoise(intX + 1, intZ)
        val v3 = getSmoothNoise(intX, intZ + 1)
        val v4 = getSmoothNoise(intX + 1, intZ + 1)

        val i1 = interpolate(v1, v2, fracX)
        val i2 = interpolate(v3, v4, fracX)
        return interpolate(i1, i2, fracZ)
    }

    fun getTerrainHeight(x: Int, z: Int): Float {
        println(getInterpolatedNoise(x / 8f, z / 8f) * terrainMaxHeight)
        return (getInterpolatedNoise(x / 8f, z / 8f) * terrainMaxHeight) - 0
    }

}