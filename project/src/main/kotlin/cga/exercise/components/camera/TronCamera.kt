package cga.exercise.components.camera

import cga.exercise.components.entities.movementcontroller.MovementController
import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Math
import org.joml.Math.sin
import org.joml.Matrix4f
import org.joml.Vector3f
import kotlin.math.cos

open class TronCamera(
    var fov: Float = default_fov, var aspect: Float = 16 / 9f, var nPlane: Float = 0.01f, var fPlane: Float = 100.0f
) : Transformable(), ICamera {
    var theta = 0f
    var phi = Math.toRadians(90f)
    var viewDir = Vector3f()

    companion object {
        val default_fov = Math.toRadians(90f)
        val zoom_fov = Math.toRadians(25f)
        val cameraOffset = Vector3f(0f, 1.6f, 0f)
    }

    override fun getCalculateViewMatrix(): Matrix4f {
        return Matrix4f().lookAt(getWorldPosition(), getWorldPosition().sub(viewDir), getWorldYAxis())
//        return Matrix4f().lookAt(getWorldPosition(), getWorldPosition().sub(getWorldZAxis()), getWorldYAxis())
    }

    override fun getCalculateProjectionMatrix(): Matrix4f {
        return Matrix4f().perspective(fov, aspect, nPlane, fPlane)
    }

    override fun bind(shader: ShaderProgram) {
        viewDir.x = cos(theta) * cos(phi)
        viewDir.y = sin(theta)
        viewDir.z = cos(theta) * sin(phi)

        setForward(viewDir)

        shader.setUniform("view", getCalculateViewMatrix(), false)
        shader.setUniform("projection", getCalculateProjectionMatrix(), false)
    }


    open fun updateTheta(offset: Double) {
        //pitch
        if (theta - Math.toRadians(offset) in Math.toRadians(-89f)..Math.toRadians(89f)) {
            theta -= Math.toRadians(offset).toFloat()
        }
    }

    open fun updatePhi(offset: Double) {
        //yaw
        phi -= Math.toRadians(offset).toFloat()
    }


}