package cga.exercise.components.entities

import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader
import org.joml.Vector3f

class Drone : Entity(
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
    )
) {

    companion object {
        enum class Zustand {
            Opening, Closing, Open, Closed, SpinningUp, SpinningDown, FindingClosingPosition
        }

        private const val filepath = "project/assets/drone/parts/"
        private val propellerTransform = Vector3f(0.135f, 0f, -0.405f)
        private var rotationsSpeed = 20f
        private const val propellerClosedAngle = 152.74
    }

    private val body = models[0]
    private val frontLeftArm = models[1]
    private val frontRightArm = models[2]
    private val backLeftArm = models[3]
    private val backRightArm = models[4]
    private val frontLeftPropeller = models[5]
    private val frontRightPropeller = models[6]
    private val backLeftPropeller = models[7]
    private val backRightPropeller = models[8]

    private var aktion = Zustand.Closed

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

    private fun spinDown(time: Float) {
        aktion = Zustand.SpinningDown
        animationStartTime = time
    }

    private fun spinUp(time: Float) {
        aktion = Zustand.SpinningUp
        animationStartTime = time
    }

    fun open(time: Float) {
        if (aktion == Zustand.Closed) {
            aktion = Zustand.Opening
        }
    }

    fun close(time: Float) {
        if (aktion == Zustand.Open) {
            aktion = Zustand.FindingClosingPosition
        }
    }

    private fun rotatePropeller(speed: Float) {
        frontRightPropeller.rotate(0f, speed, 0f)
        frontLeftPropeller.rotate(0f, -speed, 0f)
        backLeftPropeller.rotate(0f, speed, 0f)
        backRightPropeller.rotate(0f, -speed, 0f)
    }

    private fun rotatePropeller(speed: Float, goal: Float) {
        frontRightPropeller.rotationInsgesamt.let {

//            println("current ${it.y}")
//            println("current rotations ${(it.y / Math.toRadians(360.0)).toInt()}")
//            println("next step ${it.y + speed}")
//            println("next step rotations ${(it.y + speed / Math.toRadians(360.0)).toInt()}")

            var rotationNum = (it.y / Math.toRadians(360.0)).toInt()
            if (goal < it.y % Math.toRadians(360.0)) rotationNum += 1
//            println("reaching goal in rotation $rotationNum")
//            println("goal $goal")

            if ((it.y) < rotationNum * Math.toRadians(360.0) + goal &&
                (it.y + speed) > rotationNum * Math.toRadians(360.0) + goal
            ) {
                var differenz: Float = (goal.toDouble() - it.y % Math.toRadians(360.0)).toFloat()
//                println("moving only ${Math.toDegrees(differenz.toDouble())} instead")
                rotatePropeller(differenz)
//                println("aktueller angle ${Math.toDegrees(it.y % Math.toRadians(360.0))}")
            } else {
                rotatePropeller(speed)
            }
        }

    }

    private fun rotateArms(speed: Float) {
        frontRightArm.rotate(0f, speed, 0f)
        frontLeftArm.rotate(0f, -speed, 0f)
        backLeftArm.rotate(0f, speed * 135 / 105, 0f)
        backRightArm.rotate(0f, -speed * 135 / 105, 0f)

    }

    override fun update(dt: Float, time: Float) {
        val animationPercentage = animationPercentage(time)

        when (aktion) {
            Zustand.Open -> {
                rotatePropeller(rotationsSpeed * dt)
            }
            Zustand.Closed -> {
            }
            Zustand.FindingClosingPosition -> {
                frontRightPropeller.rotationInsgesamt.let {
                    if (it.y % Math.toRadians(360.0) !in Math.toRadians(propellerClosedAngle - 1)..Math.toRadians(
                            propellerClosedAngle + 1
                        )
                    ) {
                        rotatePropeller(rotationsSpeed * dt, Math.toRadians(propellerClosedAngle).toFloat())
                    } else {
                        spinDown(time)
                    }
                }
            }
            Zustand.SpinningDown -> {
                if (animationPercentage >= 1f) {
                    aktion = Zustand.Closing
                }
                rotatePropeller(rotationsSpeed * dt * (1 - animationPercentage))
            }

            Zustand.SpinningUp -> {
                if (animationPercentage >= 1f) {
                    aktion = Zustand.Open
                }
                rotatePropeller(rotationsSpeed * dt * animationPercentage)
            }
            Zustand.Closing -> {
                if (frontRightArm.rotationInsgesamt.y <= Math.toRadians(30.0)) {
                    aktion = Zustand.Closed
                } else {
                    rotateArms(-dt)
                }
            }
            Zustand.Opening -> {
                if (frontRightArm.rotationInsgesamt.y >= Math.toRadians(135.0)) {
                    spinUp(time)
                } else {
                    rotateArms(dt)
                }
            }
        }
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