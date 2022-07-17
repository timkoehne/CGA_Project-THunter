package cga.exercise.components.geometry

import cga.exercise.components.TerrainGenerator
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import java.nio.FloatBuffer

class ProceduralGround(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>) : Mesh(
    vertexdata, indexdata, attributes, this.material
) {

    companion object {
        val material: Material
        var vertexdata: FloatArray
        var indexdata: IntArray
        val anzX = 1000
        val anzZ = 1000
        val abstand = 0.1f

        val stride: Int = 8 * 4
        val attrPos = VertexAttribute(3, GL11.GL_FLOAT, stride, 0) //position
        val attrTC = VertexAttribute(2, GL11.GL_FLOAT, stride, 3 * 4) //textureCoordinate
        val attrNorm = VertexAttribute(3, GL11.GL_FLOAT, stride, 5 * 4) //normalval
        val vertexAttributes = arrayOf<VertexAttribute>(attrPos, attrTC, attrNorm)


        val diff = Texture2D.invoke("project/assets/textures/ground_diff.png", false)
        val emit = Texture2D.invoke("project/assets/textures/ground_emit.png", false)
        val spec = Texture2D.invoke("project/assets/textures/ground_spec.png", false)

        init {
            diff.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            emit.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            spec.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)

            material = Material(diff, emit, spec, 60.0f, Vector2f(64.0f, 64.0f))


            vertexdata = FloatArray(stride * (anzX * anzZ))
            indexdata = IntArray((anzX * anzZ) * 6)

            //TODO texturen
            //Vertices erstellen
            var texture = 0
            for (z in 0 until anzZ) {
                for (x in 0 until anzX) {
                    //TODO evtl vertices aussen auf den boden setzen um fliegen zu verhindern
                    insertVertex(x, z, 1)
                }
            }

            //Vertices zu Grid kombinieren
            var anz = 0
            for (z in 0 until anzZ) {
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

        }

        fun insertVertex(x: Int, z: Int, texture: Int) {
            val pos = ((z * anzX) + x) * 8
            //pos
            vertexdata[pos] = x * abstand
            vertexdata[pos + 1] = TerrainGenerator.getTerrainHeight(x, z)
            vertexdata[pos + 2] = z * abstand
            //texture coords
            when (texture % 4) {
                0 -> {
                    vertexdata[pos + 3] = 0f
                    vertexdata[pos + 4] = 0f
                }
                1 -> {
                    vertexdata[pos + 3] = 0f
                    vertexdata[pos + 4] = 1f
                }
                2 -> {
                    vertexdata[pos + 3] = 1f
                    vertexdata[pos + 4] = 0f
                }
                3 -> {
                    vertexdata[pos + 3] = 1f
                    vertexdata[pos + 4] = 1f
                }
            }
            //normal
            vertexdata[pos + 5] = 0f
            vertexdata[pos + 6] = 1f
            vertexdata[pos + 7] = 0f
        }


    }

    constructor() : this(vertexdata, indexdata, vertexAttributes)

}

