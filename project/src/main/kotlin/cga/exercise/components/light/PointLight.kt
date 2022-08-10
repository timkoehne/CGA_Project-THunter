package cga.exercise.components.light

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3f

open class PointLight(pos: Vector3f, var color: Vector3f) : Transformable(), IPointLight {

    init {
        translate(pos)
    }

    override fun bind(shaderProgram: ShaderProgram) {

        shaderProgram.setUniform("lights[0].lightPos", getWorldPosition())
        shaderProgram.setUniform("lights[0].lightColor", color)
    }

    fun bind(shaderProgram: ShaderProgram, arrpos: Int) {
        shaderProgram.setUniform("lights[$arrpos].lightPos", getWorldPosition())
        shaderProgram.setUniform("lights[$arrpos].lightColor", color)
    }
}