package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f

open class PointLight(pos: Vector3f, var color: Vector3f) : Transformable(), IPointLight {

    init {
        translate(pos)
    }

    override fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("lightInput[0].lightPos", getWorldPosition())
        shaderProgram.setUniform("lightInput[0].lightColor", color)
    }

    fun bind(shaderProgram: ShaderProgram, arrpos: Int) {
        shaderProgram.setUniform("lightInput[$arrpos].lightPos", getWorldPosition())
        shaderProgram.setUniform("lightInput[$arrpos].lightColor", color)
    }
}