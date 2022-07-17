package cga.exercise.components.light

import cga.exercise.components.shader.ShaderProgram
import org.joml.Math.cos
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3f

class SpotLight(
    pos: Vector3f,
    color: Vector3f,
    private val innerAngle: Float,
    private val outerAngle: Float
) :
    PointLight(pos, color), ISpotLight {
    override fun bind(shaderProgram: ShaderProgram, viewMatrix: Matrix4f) {

        shaderProgram.setUniform("spotlightPos", getWorldPosition())
        shaderProgram.setUniform("lightColorSpot", Vector3f(1f, 1f, 1f))

        shaderProgram.setUniform("cosInnen", cos(innerAngle))
        shaderProgram.setUniform("cosAussen", cos(outerAngle))
        shaderProgram.setUniform("spotlightDir", getWorldZAxis().negate().mul(Matrix3f(viewMatrix)))

    }
}