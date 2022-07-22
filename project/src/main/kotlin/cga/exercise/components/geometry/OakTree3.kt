package cga.exercise.components.geometry

import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f
import org.lwjgl.opengl.GL11

class OakTree3: ComplexModel(filepath) {

    companion object {
        val filepath = "project/assets/models/oaktree3.obj"
    }

    init {
        val diff1 = Texture2D.invoke("project/assets/textures/OakTreeLeaf.png", false)
        val diff2 = Texture2D.invoke("project/assets/textures/OakTreeTrunk.png", false)
        val emit = Texture2D.invoke("project/assets/textures/black.png", false)
        val spec = Texture2D.invoke("project/assets/textures/black.png", false)

        diff1.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
        diff2.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
        emit.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
        spec.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)

        meshes[0].material = Material(diff1, emit, spec, 60.0f, Vector2f(64.0f, 64.0f))
        meshes[1].material = Material(diff2, emit, spec, 60.0f, Vector2f(64.0f, 64.0f))
    }
}