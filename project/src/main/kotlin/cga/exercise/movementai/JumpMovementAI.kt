package cga.exercise.movementai

import cga.exercise.components.entities.Entity

class JumpMovementAI(entity: Entity): MovementAI(entity) {

    override fun onMove(dt: Float, time: Float) {
        super.onMove(dt, time)
        entity.jump(dt, time)
    }


}