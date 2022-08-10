package cga.exercise.components.entities

import DroneAnimationTrait
import cga.exercise.components.entities.traits.IDroneAnimationTrait
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader
import org.joml.Vector3f

class Drone(myMap: MyMap) : Entity(
    ModelLoader.loadModelSameTextures(
        listOf(
            filepath + "droneBody.obj",
            filepath + "droneArm.obj",
            filepath + "droneArm.obj",
            filepath + "droneArm.obj",
            filepath + "droneArm.obj",
            filepath + "propeller.obj",
            filepath + "propeller.obj",
            filepath + "propeller.obj",
            filepath + "propeller.obj"
        )
    ), myMap
), IDroneAnimationTrait {

    companion object {
        val propellerTransform = Vector3f(0.135f, 0f, -0.405f)
        private const val filepath = "project/assets/drone/parts/"
    }

    override val animationTrait = DroneAnimationTrait(this)

    val body = models[0]
    val frontLeftArm = models[1]
    val frontRightArm = models[2]
    val backLeftArm = models[3]
    val backRightArm = models[4]
    val frontLeftPropeller = models[5]
    val frontRightPropeller = models[6]
    val backLeftPropeller = models[7]
    val backRightPropeller = models[8]

    init {
        frontRightArm.parent = body
        frontLeftArm.parent = body
        backLeftArm.parent = body
        backRightArm.parent = body
        frontRightPropeller.parent = frontRightArm
        frontLeftPropeller.parent = frontLeftArm
        backLeftPropeller.parent = backLeftArm
        backRightPropeller.parent = backRightArm

        frontRightArm.translate(Vector3f(-0.45f, 0f, 0.5f))
        frontRightArm.rotate(0f, Math.toRadians(30.0).toFloat(), 0f)
        frontLeftArm.translate(Vector3f(0.45f, 0f, 0.5f))
        frontLeftArm.rotate(0f, Math.toRadians(0.0).toFloat(), 0f)
        backLeftArm.translate(Vector3f(0.45f, 0f, -0.5f))
        backLeftArm.rotate(0f, Math.toRadians(185.0).toFloat(), 0f)
        backRightArm.translate(Vector3f(-0.45f, 0f, -0.5f))
        backRightArm.rotate(0f, Math.toRadians(215.0).toFloat(), 0f)

        frontRightPropeller.translate(propellerTransform)
        frontLeftPropeller.translate(propellerTransform)
        backLeftPropeller.translate(propellerTransform)
        backRightPropeller.translate(propellerTransform)

    }

    override fun open(time: Float) {
        animationTrait.open(time)
    }

    override fun close(time: Float) {
        animationTrait.close(time)
    }

    override fun update(dt: Float, time: Float) {
        animationTrait.update(dt, time)
    }

    override fun render(shaderProgram: ShaderProgram) {
        body.render(shaderProgram)
        frontRightArm.render(shaderProgram)
        frontLeftArm.render(shaderProgram)
        backLeftArm.render(shaderProgram)
        backRightArm.render(shaderProgram)

        frontRightPropeller.render(shaderProgram)
        frontLeftPropeller.render(shaderProgram)
        backLeftPropeller.render(shaderProgram)
        backRightPropeller.render(shaderProgram)
    }


}