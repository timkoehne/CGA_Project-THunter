package cga.exercise.components.map

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.entities.Entity
import cga.exercise.components.map.Chunk.Companion.chunkSize
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.concurrent.thread
import kotlin.math.floor

class ProceduralGroundInfinite(
    val myMap: MyMap,
    val numChunksSquare: Int,
    val abstand: Float
) {

    var chunks: HashMap<Vector3i, Chunk> = HashMap()
    //TODO aktuell werden chunks geladen bis das programm beendet wird und nie entladen


    init {
        val playerposXZ = Vector3f(myMap.getCamera().getWorldPosition().x, 0f, myMap.getCamera().getWorldPosition().z)



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

    fun getChunk(pos: Vector3f): Chunk? {
//        println("$pos ist an hat chunk index ${chunkPosToIndex(getChunkPos(pos, 0, 0))}")
        return chunks[chunkPosToIndex(getChunkPos(pos, 0, 0))]

    }

    fun render(shaderProgram: ShaderProgram) {
        shaderProgram.use()
        val playerposXZ = Vector3f(myMap.getCamera().getWorldPosition().x, 0f, myMap.getCamera().getWorldPosition().z)
        for (z in -numChunksSquare / 2..numChunksSquare / 2) {
            for (x in -numChunksSquare / 2..numChunksSquare / 2) {
                val chunkPos = getChunkPos(playerposXZ, x, z)

                val chunkIndex = chunkPosToIndex(chunkPos)
                if (chunks[chunkIndex] == null) {
                    chunks[chunkIndex] = Chunk(abstand, chunkPos)
                }
                chunks[chunkIndex]?.render(shaderProgram)
            }
        }
    }

    fun renderEntites(shaderProgram: ShaderProgram) {
        shaderProgram.use()
        val playerposXZ = Vector3f(myMap.getCamera().getWorldPosition().x, 0f, myMap.getCamera().getWorldPosition().z)
        for (z in -numChunksSquare / 2..numChunksSquare / 2) {
            for (x in -numChunksSquare / 2..numChunksSquare / 2) {
                val chunkPos = getChunkPos(playerposXZ, x, z)

                val chunkIndex = chunkPosToIndex(chunkPos)
                //camera
                chunks[chunkIndex]?.renderEntities(shaderProgram)
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


