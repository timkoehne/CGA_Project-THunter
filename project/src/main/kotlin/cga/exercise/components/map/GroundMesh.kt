package cga.exercise.components.map

import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.texture.Texture2D
import cga.framework.OBJLoader
import org.joml.Vector2f
import org.lwjgl.opengl.GL11


class GroundMesh(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>) : Mesh(
    vertexdata, indexdata, attributes, material
) {

    companion object {
        val res = OBJLoader.loadOBJ("project/assets/models/ground.obj")
        val material: Material

        val objMesh: OBJLoader.OBJMesh = res.objects[0].meshes[0]

        val stride: Int = 8 * 4
        val attrPos = VertexAttribute(3, GL11.GL_FLOAT, stride, 0) //position
        val attrTC = VertexAttribute(2, GL11.GL_FLOAT, stride, 3 * 4) //textureCoordinate
        val attrNorm = VertexAttribute(3, GL11.GL_FLOAT, stride, 5 * 4) //normalval
        val vertexAttributes = arrayOf<VertexAttribute>(attrPos, attrTC, attrNorm)

        init {

            objMesh.vertexData.forEachIndexed { index, it -> println(it); if (index%8==7) println("Vertex ${index / 8}\n")}

            val diff = Texture2D.invoke("project/assets/textures/ground_diff.png", false)
            val emit = Texture2D.invoke("project/assets/textures/ground_emit.png", false)
            val spec = Texture2D.invoke("project/assets/textures/ground_spec.png", false)

            diff.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            emit.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            spec.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)

            material = Material(diff, emit, spec, 60.0f, Vector2f(64.0f, 64.0f))

        }

    }

    constructor() : this(objMesh.vertexData, objMesh.indexData, vertexAttributes)


}