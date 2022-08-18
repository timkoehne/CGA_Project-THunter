package cga.exercise.components.entities

import DroneAnimationTrait
import cga.exercise.components.entities.movementcontroller.DroneController
import cga.exercise.components.entities.traits.IDroneAnimationTrait
import cga.exercise.components.map.MyMap
import cga.exercise.game.Scene
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
    ), myMap, hitbox
), IDroneAnimationTrait {

    var hitByBulletSound = Scene.audioMaster.createAudioSource("project/assets/sounds/drone-bullet-hit.ogg")

    companion object {
        val propellerTransform = Vector3f(0.135f, 0f, -0.405f)
        private const val filepath = "project/assets/drone/parts/"

        private val hitbox = "project/assets/drone/drone hitbox.obj"
    }

    override val animationTrait = DroneAnimationTrait(this)
    override val movementController = DroneController(this)


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

        body.rotate(0f, Math.toRadians(180.0).toFloat(), 0f)

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

    override fun toggle(){
        if(animationTrait.state == DroneAnimationTrait.State.Closed){
            open()
        }else{
            close()
        }
    }

    override fun open() {
        animationTrait.open()
    }

    override fun close() {
        animationTrait.close()
    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        animationTrait.update(dt, time)
    }

    override fun hitByBullet() {
        hitByBulletSound.play()
        super.hitByBullet()
    }


}