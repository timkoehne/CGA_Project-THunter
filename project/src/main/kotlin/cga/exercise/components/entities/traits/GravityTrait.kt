package cga.exercise.components.entities.traits

import cga.exercise.components.entities.Entity
import cga.exercise.components.map.MyMap
import org.joml.Vector3f


open class GravityTrait(entity: Entity, val myMap: MyMap) : Trait(entity) {

    private enum class State {
        OnTheGround, JumpingUp, Falling
    }

    private val gravitySpeed = 8f

    private var state = State.OnTheGround
    private var jumpStartTime: Float = -1f
    private var jumpUpDuration: Float = 0.20f

    override fun update(dt: Float, time: Float) {
        val pos = entity.getPosition()

        when (state) {
            State.OnTheGround -> {
                entity.translate(
                    Vector3f(
                        0f, myMap.getHeight(
                            pos.x, pos.z
                        ) - pos.y, 0f
                    )
                )
            }
            State.JumpingUp -> {

                //during the jump
                if (time < jumpStartTime + jumpUpDuration) {
                    entity.translate(Vector3f(0f, (entity.jumpSpeed) * dt, 0f))
                } else {
                    //after the jump
                    state = State.Falling
                }
            }
            State.Falling -> {
                entity.translate(Vector3f(0f, -(gravitySpeed * entity.weight * dt), 0f))
                if (pos.y <= myMap.getHeight(pos.x, pos.z)) {
                    state = State.OnTheGround
                }
            }
        }
    }

    fun jump(dt: Float, time: Float) {
        if (state == State.OnTheGround) {
            state = State.JumpingUp
            jumpStartTime = time
        }
    }

}