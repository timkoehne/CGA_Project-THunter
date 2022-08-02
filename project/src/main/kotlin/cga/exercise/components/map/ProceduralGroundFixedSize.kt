package cga.exercise.components.map

import cga.exercise.components.TerrainGenerator
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.texture.Texture2D
import org.lwjgl.opengl.GL11

class ProceduralGroundFixedSize(
    val anzX: Int,
    val anzZ: Int,
    val abstand: Float,
    vertexData: FloatArray,
    indexData: IntArray,
    attributes: Array<VertexAttribute>
) : Mesh(
    vertexData, indexData, attributes, material
) {
    val grid: Array<Array<Float>>

    init {
        grid = Array(anzZ) { z -> Array(anzX) { x -> vertexData[(z * anzX + x) * 8 + 1] } }
    }

    companion object {
        val material: Material
        val heightGenerator = TerrainGenerator()
        val materialGenerator = TerrainGenerator()

        val diff = Texture2D.invoke("project/assets/textures/grass.png", true)
        val emit = Texture2D.invoke("project/assets/textures/black.png", true)
        val spec = Texture2D.invoke("project/assets/textures/white.png", true)

        init {
            diff.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            emit.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            spec.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)

            material = Material(diff, emit, spec)
        }

        fun createGround(anzX: Int, anzZ: Int, abstand: Float): ProceduralGroundFixedSize {
            var vertexdata = FloatArray(stride * (anzX * anzZ))
            var indexdata = IntArray(((anzX - 1) * (anzZ - 1)) * 6)

            //Vertices erstellen
            for (z in 0 until anzZ) {
                for (x in 0 until anzX) {
                    when (z % 2) {
                        0 -> when (x % 2) {
                            0 -> insertVertex(x, z, 0, anzX, vertexdata, abstand)
                            1 -> insertVertex(x, z, 1, anzX, vertexdata, abstand)
                        }
                        1 -> when (x % 2) {
                            0 -> insertVertex(x, z, 2, anzX, vertexdata, abstand)
                            1 -> insertVertex(x, z, 3, anzX, vertexdata, abstand)

                        }
                    }
                }
            }
            //vertexdata.forEachIndexed { index, it -> if (index % 8 == 7) println("Vertex ${index / 8}\n $it") }

            //Vertices zu Grid kombinieren
            var anz = 0
            for (z in 0 until anzZ - 1) {
                for (x in 0 until anzX - 1) {
                    //CCW um die Vertices
                    //Dreieck1
                    indexdata[anz] = (z * anzX) + x
                    indexdata[anz + 1] = ((z + 1) * anzX) + x //einen nach unten
                    indexdata[anz + 2] = (z * anzX) + x + 1 //diagonal rechts hoch
                    //Dreieck2
                    indexdata[anz + 3] = ((z + 1) * anzX) + x //einen unterhalb anfangen
                    indexdata[anz + 4] = ((z + 1) * anzX) + x + 1 //einen nach rechts
                    indexdata[anz + 5] = (z * anzX) + x + 1 //einen nach oben
                    anz += 6
                }
            }

            return ProceduralGroundFixedSize(anzX, anzZ, abstand, vertexdata, indexdata, vertexAttributes)
        }

        private fun insertVertex(x: Int, z: Int, texture: Int, anzX: Int, vertexData: FloatArray, abstand: Float) {
            val pos = ((z * anzX) + x) * 8
            //pos
            vertexData[pos] = x * abstand
            vertexData[pos + 1] = heightGenerator.getTerrainHeight(x.toFloat(), z.toFloat())
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
            //TODO normalen vom ground sind einfach grade nach oben
            vertexData[pos + 5] = 0f
            vertexData[pos + 6] = 1f
            vertexData[pos + 7] = 0f


        }

    }

    fun getHeight(x: Float, z: Float): Float {
        return heightGenerator.getTerrainHeight(x / abstand, z / abstand)
    }
}
