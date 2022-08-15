package cga.exercise.components.entities.movementcontroller

import cga.exercise.components.entities.Entity

class JumpMovementAI(entity: Entity): BasicMovementAI(entity) {

    override fun onMove(dt: Float, time: Float) {
        super.onMove(dt, time)
        entity.jump()
    }


}