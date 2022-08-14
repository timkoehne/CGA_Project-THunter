import cga.exercise.components.entities.Drone
import cga.exercise.components.entities.traits.IDroneAnimationTrait
import cga.exercise.components.entities.traits.Trait
import cga.exercise.components.geometry.Transformable
import org.joml.Vector3f

class DroneAnimationTrait(val drone: Drone) : Trait(drone) {

    var animationStartTime = 0f
    var state = State.Closed

    companion object {
        const val rotationsSpeed = 20f
        const val propellerClosedAngle = 152.74
        const val animationDuration = 1.0f
    }

    enum class State {
        Opening, Closing, Open, Closed, SpinningUp, SpinningDown, FindingClosingPosition
    }

    private fun animationPercentage(time: Float): Float {
        return (time - animationStartTime) / animationDuration
    }

    override fun update(dt: Float, time: Float) {
        val animationPercentage = animationPercentage(time)

        when (state) {
            State.Open -> {
                rotatePropeller(rotationsSpeed * dt)
            }
            State.Closed -> {
            }
            State.FindingClosingPosition -> {
                drone.frontRightPropeller.rotationInsgesamt.let {
                    if (it.y % Math.toRadians(360.0) !in Math.toRadians(propellerClosedAngle - 1)..Math.toRadians(
                            propellerClosedAngle + 1
                        )
                    ) {
                        rotatePropeller(rotationsSpeed * dt, Math.toRadians(propellerClosedAngle).toFloat())
                    } else {
                        spinDown(time)
                        drone.gravityTrait.enable()
                    }
                }
            }
            State.SpinningDown -> {
                if (animationPercentage >= 1f) {
                    state = State.Closing
                }
                rotatePropeller(rotationsSpeed * dt * (1 - animationPercentage))
            }

            State.SpinningUp -> {
                if (animationPercentage >= 1f) {
                    state = State.Open
                    drone.gravityTrait.disable()
                }
                rotatePropeller(rotationsSpeed * dt * animationPercentage)
            }
            State.Closing -> {
                if (drone.frontRightArm.rotationInsgesamt.y <= Math.toRadians(30.0)) {
                    state = State.Closed
                } else {
                    rotateArms(-dt)
                }
            }
            State.Opening -> {
                if (drone.frontRightArm.rotationInsgesamt.y >= Math.toRadians(135.0)) {
                    spinUp(time)
                } else {
                    rotateArms(dt)
                }
            }
        }
    }


    private fun rotateArms(speed: Float) {
        drone.frontRightArm.rotate(0f, speed, 0f)
        drone.frontLeftArm.rotate(0f, -speed, 0f)
        drone.backLeftArm.rotate(0f, speed * 135 / 105, 0f)
        drone.backRightArm.rotate(0f, -speed * 135 / 105, 0f)
    }

    private fun rotatePropeller(speed: Float) {
        drone.frontRightPropeller.rotate(0f, speed, 0f)
        drone.frontLeftPropeller.rotate(0f, -speed, 0f)
        drone.backLeftPropeller.rotate(0f, speed, 0f)
        drone.backRightPropeller.rotate(0f, -speed, 0f)
    }

    private fun rotatePropeller(speed: Float, goal: Float) {
        drone.frontRightPropeller.rotationInsgesamt.let {

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


    private fun spinDown(time: Float) {
        state = State.SpinningDown
        animationStartTime = time
    }

    private fun spinUp(time: Float) {
        state = State.SpinningUp
        animationStartTime = time
    }

    fun open(time: Float) {
        if (state == State.Closed) {
            state = State.Opening
        }
    }

    fun close(time: Float) {
        if (state == State.Open) {
            state = State.FindingClosingPosition
        }
    }


}