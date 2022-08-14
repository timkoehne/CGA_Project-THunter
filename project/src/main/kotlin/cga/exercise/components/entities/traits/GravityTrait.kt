package cga.exercise.components.entities.traits

import cga.exercise.components.entities.Entity
import cga.exercise.components.map.MyMap
import org.joml.Vector3f


open class GravityTrait(entity: Entity, val myMap: MyMap) : Trait(entity) {

    private enum class State {
        OnTheGround, JumpingUp, Falling
    }

    private val gravitySpeed = 8f

    private var state = State.Falling
    private var jumpStartTime: Float = -1f
    private var jumpUpDuration: Float = 0.20f
    private var gravityEnabled = true

    fun enable() {
        gravityEnabled = true
        println(gravityEnabled)
    }

    fun disable() {
        gravityEnabled = false
        println(gravityEnabled)
        jump()
    }

    override fun update(dt: Float, time: Float) {
        val pos = entity.getWorldPosition()

        when (state) {
            State.OnTheGround -> {
                entity.preTranslate(
                    Vector3f(
                        0f, (myMap.getHeight(
                            pos.x, pos.z
                        ) + entity.height) - pos.y, 0f
                    )
                )
            }
            State.JumpingUp -> {

                if (jumpStartTime == -1f) {
                    jumpStartTime = time
                }

                //during the jump
                if (time < jumpStartTime + jumpUpDuration) {
                    entity.preTranslate(Vector3f(0f, (entity.jumpSpeed) * dt, 0f))
                } else {
                    //after the jump
                    state = State.Falling
                }
            }
            State.Falling -> {
                if (gravityEnabled) {
                    entity.preTranslate(Vector3f(0f, -(gravitySpeed * entity.weight * dt), 0f))
                }
                if (pos.y <= myMap.getHeight(pos.x, pos.z) + entity.height) {
                    state = State.OnTheGround
                }
            }
        }
    }

    fun jump() {
        if (state == State.OnTheGround) {
            state = State.JumpingUp
            jumpStartTime = -1f
        }
    }

}