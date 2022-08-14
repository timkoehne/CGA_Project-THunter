package cga.exercise.components.camera

import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import org.joml.Math
import org.joml.Math.sin
import org.joml.Matrix4f
import org.joml.Vector3f
import kotlin.math.cos

class OrbitCamera : TronCamera() {

    var radius = 5f
    var myMap: MyMap? = null

    init {
        theta = Math.toRadians(45f)
        phi = Math.toRadians(180f)
    }


    override fun bind(shader: ShaderProgram) {
        val pos = Vector3f()
        pos.x = (radius * sin(theta) * cos(phi))
        pos.y = radius * cos(theta)
        pos.z = (radius * sin(theta) * sin(phi))

        setPosition(pos)

        super.bind(shader)
    }

    override fun getCalculateViewMatrix(): Matrix4f {
        return Matrix4f().lookAt(getWorldPosition(), parent?.getWorldPosition() ?: Vector3f(), getWorldYAxis())
    }

    fun updateRadius(offset: Double) {
        if (radius - offset in 2.0..8.0) {
            radius -= offset.toFloat()
        }
    }

    fun updateTheta(offset: Double) {
        //pitch

        if (theta + Math.toRadians(offset) in Math.toRadians(1f)..Math.toRadians(179f)) {
            theta += Math.toRadians(offset).toFloat()
        }

    }

    fun updatePhi(offset: Double) {
        //yaw

        phi -= Math.toRadians(offset).toFloat()
    }
}