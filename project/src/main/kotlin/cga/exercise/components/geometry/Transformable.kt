package cga.exercise.components.geometry

import cga.exercise.components.camera.TronCamera
import org.joml.*
import org.lwjgl.assimp.AICamera
import java.util.*
import kotlin.math.acos

open class Transformable(private var modelMatrix: Matrix4f = Matrix4f(), var parent: Transformable? = null) {

    var rotationInsgesamt = Vector3f()

    /**
     * Returns copy of object model matrix
     * @return modelMatrix
     */
    fun getModelMatrix(): Matrix4f {
        return Matrix4f(modelMatrix)
    }

    fun lookAlong(viewDir: Vector3f) {
        modelMatrix.setLookAlong(viewDir, getWorldYAxis())
    }

    fun setPosition(pos: Vector3f) {
        modelMatrix.set(3, 0, pos.x)
        modelMatrix.set(3, 1, pos.y)
        modelMatrix.set(3, 2, pos.z)
    }

    /**
     * Returns multiplication of world and object model matrices.
     * Multiplication has to be recursive for all parents.
     * Hint: scene graph
     * @return world modelMatrix
     */
    fun getWorldModelMatrix(): Matrix4f {
        return parent?.getWorldModelMatrix()?.mul(modelMatrix) ?: getModelMatrix()
    }

    /**
     * Rotates object around its own origin.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     */
    fun rotate(pitch: Float, yaw: Float, roll: Float) {
        rotationInsgesamt.add(Vector3f(pitch, yaw, roll))

//        println(
//            "aktuelle gesamt rotation ${Math.toDegrees(rotationInsgesamt.x.toDouble())}, " +
//                    "${Math.toDegrees(rotationInsgesamt.y.toDouble())}, " +
//                    "${Math.toDegrees(rotationInsgesamt.z.toDouble())}"
//        )

        modelMatrix.rotateX(pitch)
        modelMatrix.rotateY(yaw)
        modelMatrix.rotateZ(roll)

    }


    fun worldRotate(pitch: Float, yaw: Float, roll: Float) {
        modelMatrix.rotateLocalX(pitch)
        modelMatrix.rotateLocalY(yaw)
        modelMatrix.rotateLocalZ(roll)
    }

    /**
     * Rotates object around given rotation center.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     * @param altMidpoint rotation center
     */
    fun rotateAroundPoint(pitch: Float, yaw: Float, roll: Float, altMidpoint: Vector3f) {

        val tmp = Matrix4f()
        tmp.translate(altMidpoint)
        tmp.rotateXYZ(pitch, yaw, roll)
        tmp.translate(Vector3f(altMidpoint).negate())
        modelMatrix = tmp.mul(modelMatrix)

//        val to = altMidpoint.sub(getWorldPosition())
//        translate(to)
//        rotate(pitch, yaw, roll)
//        translate(to.mul(-1f))

//        translate(to)
//        to.rotateX(pitch)
//        to.rotateY(yaw)
//        to.rotateZ(roll)
//        translate(to.mul(-1f))

    }

    fun setWorldPosition(location: Vector3f) {
        preTranslate(getPosition().sub(location))
    }

    /**
     * Translates object based on its own coordinate system.
     * @param deltaPos delta positions
     */
    fun translate(deltaPos: Vector3f) {
        modelMatrix.translate(deltaPos)
    }

    /**
     * Translates object based on its parent coordinate system.
     * Hint: this operation has to be left-multiplied
     * @param deltaPos delta positions (x, y, z)
     */
    fun preTranslate(deltaPos: Vector3f) {
        modelMatrix.translateLocal(deltaPos)
    }

    /**
     * Scales object related to its own origin
     * @param scale scale factor (x, y, z)
     */
    fun scale(scale: Vector3f) {
        modelMatrix.scale(scale)
    }

    /**
     * Returns position based on aggregated translations.
     * Hint: last column of model matrix
     * @return position
     */
    fun getPosition(): Vector3f {
        return Vector3f(
            modelMatrix.get(3, 0),
            modelMatrix.get(3, 1),
            modelMatrix.get(3, 2)
        )
    }

    /**
     * Returns position based on aggregated translations incl. parents.
     * Hint: last column of world model matrix
     * @return position
     */
    fun getWorldPosition(): Vector3f {
        val world = getWorldModelMatrix()
        return Vector3f(
            world.get(3, 0),
            world.get(3, 1),
            world.get(3, 2)
        )
    }


    /**
     * Returns x-axis of object coordinate system
     * Hint: first normalized column of model matrix
     * @return x-axis
     */
    fun getXAxis(): Vector3f {
        return Vector3f(
            modelMatrix.get(0, 0),
            modelMatrix.get(0, 1),
            modelMatrix.get(0, 2)
        ).normalize()
    }

    /**
     * Returns y-axis of object coordinate system
     * Hint: second normalized column of model matrix
     * @return y-axis
     */
    fun getYAxis(): Vector3f {
        return Vector3f(
            modelMatrix.get(1, 0),
            modelMatrix.get(1, 1),
            modelMatrix.get(1, 2)
        ).normalize()
    }

    /**
     * Returns z-axis of object coordinate system
     * Hint: third normalized column of model matrix
     * @return z-axis
     */
    fun getZAxis(): Vector3f {
        return Vector3f(
            modelMatrix.get(2, 0),
            modelMatrix.get(2, 1),
            modelMatrix.get(2, 2)
        ).normalize()
    }

    /**
     * Returns x-axis of world coordinate system
     * Hint: first normalized column of world model matrix
     * @return x-axis
     */
    fun getWorldXAxis(): Vector3f {
        val world = getWorldModelMatrix()
        return Vector3f(
            world.get(0, 0),
            world.get(0, 1),
            world.get(0, 2)
        ).normalize()
    }

    /**
     * Returns y-axis of world coordinate system
     * Hint: second normalized column of world model matrix
     * @return y-axis
     */
    fun getWorldYAxis(): Vector3f {
        val world = getWorldModelMatrix()
        return Vector3f(
            world.get(1, 0),
            world.get(1, 1),
            world.get(1, 2)
        ).normalize()
    }

    /**
     * Returns z-axis of world coordinate system
     * Hint: third normalized column of world model matrix
     * @return z-axis
     */
    fun getWorldZAxis(): Vector3f {
        val world = getWorldModelMatrix()
        return Vector3f(
            world.get(2, 0),
            world.get(2, 1),
            world.get(2, 2)
        ).normalize()
    }
}