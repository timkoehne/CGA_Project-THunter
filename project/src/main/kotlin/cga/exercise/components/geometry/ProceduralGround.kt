package cga.exercise.components.geometry

import cga.exercise.components.TerrainGenerator
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import java.nio.FloatBuffer

class ProceduralGround(
    val anzX: Int,
    val anzZ: Int,
    val abstand: Float,
    val vertexData: FloatArray,
    val indexData: IntArray,
    attributes: Array<VertexAttribute>
) : Mesh(
    vertexData, indexData, attributes, material
) {

    val terrainGenerator = TerrainGenerator()

    companion object {
        val material: Material

        val diff = Texture2D.invoke("project/assets/textures/ground_diff.png", false)
        val emit = Texture2D.invoke("project/assets/textures/ground_emit.png", false)
        val spec = Texture2D.invoke("project/assets/textures/ground_spec.png", false)

        init {
            diff.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            emit.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            spec.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)

            material = Material(diff, emit, spec, 60.0f, Vector2f(64.0f, 64.0f))
        }

        fun createGround(anzX: Int, anzZ: Int, abstand: Float): ProceduralGround {
            var vertexdata = FloatArray(stride * (anzX * anzZ))
            var indexdata = IntArray((anzX * anzZ) * 6)
            return ProceduralGround(anzX, anzZ, abstand, vertexdata, indexdata, vertexAttributes)
        }

    }

    init {
        //Vertices erstellen
        for (z in 0 until anzZ) {
            for (x in 0 until anzX) {
                //TODO evtl vertices aussen auf den boden setzen um fliegen zu verhindern
                when (z % 2) {
                    0 -> when (x % 2) {
                        0 -> insertVertex(x, z, 0)
                        1 -> insertVertex(x, z, 1)
                    }
                    1 -> when (x % 2) {
                        0 -> insertVertex(x, z, 2)
                        1 -> insertVertex(x, z, 3)

                    }
                }
            }

            vertexData.forEachIndexed { index, it -> println(it); if (index % 8 == 7) println("Vertex ${index / 8}\n") }


            //Vertices zu Grid kombinieren
            var anz = 0
            for (z in 0 until anzZ) {
                for (x in 0 until anzX - 1) {
                    //CCW um die Vertices
                    //Dreieck1
                    indexData[anz] = (z * anzX) + x
                    indexData[anz + 1] = ((z + 1) * anzX) + x //einen nach unten
                    indexData[anz + 2] = (z * anzX) + x + 1 //diagonal rechts hoch
                    //Dreieck2
                    indexData[anz + 3] = ((z + 1) * anzX) + x //einen unterhalb anfangen
                    indexData[anz + 4] = ((z + 1) * anzX) + x + 1 //einen nach rechts
                    indexData[anz + 5] = (z * anzX) + x + 1 //einen nach oben
                    anz += 6
                }
            }

        }

    }


    fun insertVertex(x: Int, z: Int, texture: Int) {
        val pos = ((z * anzX) + x) * 8
        //pos
        vertexData[pos] = x * abstand
        vertexData[pos + 1] = terrainGenerator.getTerrainHeight(x, z)
        vertexData[pos + 2] = z * abstand
        //texture coords
        when (texture % 4) {
            0 -> {
                vertexData[pos + 3] = 0f
                vertexData[pos + 4] = 1f
            }
            1 -> {
                vertexData[pos + 3] = 1f
                vertexData[pos + 4] = 1f
            }
            2 -> {
                vertexData[pos + 3] = 0f
                vertexData[pos + 4] = 0f
            }
            3 -> {
                vertexData[pos + 3] = 1f
                vertexData[pos + 4] = 0f
            }
        }
        //normal
        vertexData[pos + 5] = 0f
        vertexData[pos + 6] = 1f
        vertexData[pos + 7] = 0f
    }

    fun getHeight(x: Int, z: Int): Float {
        return if (x <= anzX && z <= anzZ) vertexData[((z * anzX) + x) * 8] else 0f
    }
}
