package cga.exercise.components.camera

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f

class TronCamera(
    var fov: Float = Math.toRadians(90f),
    var aspect: Float = 16 / 9f,
    var nPlane: Float = 0.01f,
    var fPlane: Float = 100.0f
) : Transformable(), ICamera {

    override fun getCalculateViewMatrix(): Matrix4f {
        return Matrix4f().lookAt(getWorldPosition(), getWorldPosition().sub(getWorldZAxis()), getWorldYAxis())
    }

    override fun getCalculateProjectionMatrix(): Matrix4f {
        return Matrix4f().perspective(fov, aspect, nPlane, fPlane)
    }

    override fun bind(shader: ShaderProgram) {
        shader.setUniform("view", getCalculateViewMatrix(), false)
        shader.setUniform("projection", getCalculateProjectionMatrix(), false)
    }

}