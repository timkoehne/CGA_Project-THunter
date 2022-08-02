package cga.exercise.components.map

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.map.Chunk.Companion.chunkSize
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.floor

class ProceduralGroundInfinite(
    val camera: TronCamera,
    val numChunksSquare: Int,
    val abstand: Float
) {
    var chunks: HashMap<Vector3i, Chunk> = HashMap<Vector3i, Chunk>()
    //TODO aktuell werden chunks geladen bis das programm beendet wird und nie entladen


    init {
        val playerposXZ = Vector3f(camera.getWorldPosition().x, 0f, camera.getWorldPosition().z)

        for (z in -numChunksSquare / 2..numChunksSquare / 2) {
            for (x in -numChunksSquare / 2..numChunksSquare / 2) {
                val chunkPos = getChunkPos(playerposXZ, x, z)
                val chunkIndex = chunkPosToIndex(chunkPos)
                val chunk = Chunk(abstand, chunkPos)
                chunks[chunkIndex] = chunk
            }
        }
    }

    private fun getChunkPos(position: Vector3f, x: Int, z: Int): Vector3f {
        return Vector3f(
            floor((position.x) / chunkSize) * chunkSize.toFloat() + (x * chunkSize),
            0f,
            floor((position.z) / chunkSize) * chunkSize.toFloat() + (z * chunkSize)
        )

    }

    private fun chunkPosToIndex(position: Vector3f): Vector3i {
        return Vector3i(
            (position.x / (chunkSize)).toInt(),
            0,
            (position.z / (chunkSize)).toInt()
        )
    }

    fun render(shaderProgram: ShaderProgram) {
        val playerposXZ = Vector3f(camera.getWorldPosition().x, 0f, camera.getWorldPosition().z)
        for (z in -numChunksSquare / 2..numChunksSquare / 2) {
            for (x in -numChunksSquare / 2..numChunksSquare / 2) {
                val chunkPos = getChunkPos(playerposXZ, x, z)
//                println(" chunk$x|$z ist an position $chunkPos")

                val chunkIndex = chunkPosToIndex(chunkPos)
//                println(" chunk$x|$z hat index $chunkIndex")

                if (chunks[chunkIndex] != null) {
                    chunks[chunkIndex]?.render(camera, shaderProgram)
                } else {
                    chunks[chunkIndex] = Chunk(abstand, chunkPos)
                }
            }
        }
    }

    fun update(dt: Float, time: Float) {

    }

    fun getHeight(x: Float, z: Float): Float {
        return Chunk.heightGenerator.getTerrainHeight(x / abstand, z / abstand)
    }

    fun cleanUp() {
        chunks.forEach { (_, chunk) -> chunk.cleanUp() }
        chunks.clear()
    }


}


