package cga.exercise.components.entities

import cga.exercise.components.geometry.ComplexModel
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.texture.Texture2D
import cga.framework.ModelLoader
import org.joml.Vector2f
import org.lwjgl.opengl.GL11

class Sniper : Entity(listOf(ModelLoader.loadModel(filepath, 0f, 0f, 0f))) {

    companion object {
        val filepath = "project/assets/sniper/sniper.obj"
    }

    override fun update(dt: Float, time: Float) {

    }
}