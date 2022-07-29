package cga.exercise.components.entities

import cga.exercise.components.geometry.ComplexModel
import cga.exercise.components.geometry.Material
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f
import org.lwjgl.opengl.GL11

class Sniper : ComplexModel(filepath) {

    companion object {
        val filepath = "project/assets/models/sniper.obj"
        val diff0 = Texture2D.invoke("project/assets/textures/sniper0.png", false)
        val diff1 = Texture2D.invoke("project/assets/textures/sniper1.png", false)
        val diff2 = Texture2D.invoke("project/assets/textures/sniper2.png", false)
        val diff3 = Texture2D.invoke("project/assets/textures/sniper3.png", false)
        val emit = Texture2D.invoke("project/assets/textures/black.png", false)
        val spec = Texture2D.invoke("project/assets/textures/black.png", false)

        init {
            diff0.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            diff0.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            diff2.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            diff3.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            emit.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            spec.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
        }
    }

    init {
        meshes[0].material = Material(diff0, emit, spec, 60.0f, Vector2f(64.0f, 64.0f))
        meshes[1].material = Material(diff1, emit, spec, 60.0f, Vector2f(64.0f, 64.0f))
        meshes[2].material = Material(diff2, emit, spec, 60.0f, Vector2f(64.0f, 64.0f))
        meshes[3].material = Material(diff3, emit, spec, 60.0f, Vector2f(64.0f, 64.0f))
    }
}