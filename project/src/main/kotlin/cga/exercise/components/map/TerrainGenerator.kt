package cga.exercise.components.map

import java.util.Random
import kotlin.math.cos
import kotlin.math.pow

class TerrainGenerator() {

    companion object {
        val terrainMaxHeight = 6
        val octaves = 4
        val roughness = 8f
    }


    private val random = Random()
    var seed = random.nextInt(1_000_000)

    private fun getSeededNoise(x: Int, z: Int): Float {
        random.setSeed((x * 12345 + z * 54321 + seed).toLong())
        return random.nextFloat() * 2f - 1
    }

    private fun getSmoothedNoise(x: Int, z: Int): Float {
        val sides = (getSeededNoise(x - 1, z) + getSeededNoise(x + 1, z) +
                getSeededNoise(x, z - 1) + getSeededNoise(x, z + 1)) / 4
        return (getSeededNoise(x, z) / 3) + (sides * 2 / 3)
    }

    private fun cosInterpolation(left: Float, right: Float, value: Float): Float {
        val theta = value * Math.PI
        val weightedValue = (1f - cos(theta)) / 2
        return (left * (1f - weightedValue) + right * weightedValue).toFloat()

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

        val oben = cosInterpolation(obenlinks, obenrechts, fracX)
        val unten = cosInterpolation(untenlinks, untenrechts, fracX)
        return cosInterpolation(oben, unten, fracZ)
    }

    fun getTerrainHeight(x: Float, z: Float): Float {
        val xOffset = x + 50000
        val zOffset = z + 50000
//
        var total: Float = 0f
        for (num in 1..octaves) {
            total += getPerlinNoise(
                xOffset / 2.0.pow(octaves - num).toFloat(), zOffset / 2.0.pow(octaves - num).toFloat()
            ) * (terrainMaxHeight / roughness.pow(num - 1))
        }

        total += 2

        return total
    }

}