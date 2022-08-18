package cga.exercise.components.entities.traits

import cga.exercise.components.entities.Entity
import cga.exercise.components.gui.AmmoAnzeige
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.shadows.ShadowRenderer
import cga.exercise.game.Scene
import cga.framework.ModelLoader
import org.joml.Vector3f
import kotlin.math.max

class ReloadingAnimationTrait(entity: Entity) : Trait(entity) {

    enum class State {
        Idle, MovingToReload, Filling, MovingBack
    }

    val bullet = ModelLoader.loadModel("project/assets/character/bullet.obj")
    var bulletMoveStartTime = -1f


    var ammoAnzeige: AmmoAnzeige? = null

    var bulletMoveDuration = 0.5f
    val bulletMoveSpeed = 3.5f


    init {
        bullet.parent = entity
        bullet.rotate(0f, Math.toRadians(35.0).toFloat(), 0f)
    }


    var state = State.Idle
    var reloadStartTime = -1f

    private var reloadTurnDuration = 0.2f
    var reloadTurnSpeed = 4.5f


    fun render(shaderProgram: ShaderProgram) {
        if (state == State.Filling) {
            bullet.render(shaderProgram)
        }
    }

    override fun update(dt: Float, time: Float) {

        ammoAnzeige?.let {
            when (state) {
                State.MovingToReload -> {
                    if (reloadStartTime == -1f) {
                        reloadStartTime = time
                    }
                    if (time < reloadStartTime + reloadTurnDuration) {
                        entity.models[0].rotate(0f, reloadTurnSpeed * dt, 0f)
                    } else {
                        reloadStartTime = -1f
                        state = State.Filling
                    }
                }
                State.MovingBack -> {
                    if (reloadStartTime == -1f) {
                        reloadStartTime = time
                    }
                    if (time < reloadStartTime + reloadTurnDuration) {
                        entity.models[0].rotate(0f, -reloadTurnSpeed * dt, 0f)
                    } else {
                        reloadStartTime = -1f
                        state = State.Idle
                    }
                }
                State.Filling -> {
                    if (bulletMoveStartTime == -1f) {
                        bullet.setPosition(Vector3f(2f, 1.2f, -0.75f))
                        bulletMoveStartTime = time
                    }

                    if (time < bulletMoveStartTime + bulletMoveDuration) {
                        bullet.preTranslate(Vector3f(-bulletMoveSpeed * dt, 0f, 0f))
                    } else {
                        bulletMoveStartTime = -1f
                        ammoAnzeige!!.reloadOneBullet()
                        if (!ammoAnzeige!!.reloadAllowed()) {
                            state = State.MovingBack
                        }

                    }
                }
            }

        }
    }

    fun reload() {
        if (state == State.Idle) {
            if (ammoAnzeige!!.reloadAllowed()) {
                reloadStartTime = -1f
                state = State.MovingToReload
            }
        }
    }

}