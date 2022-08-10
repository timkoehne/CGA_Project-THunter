package cga.exercise.components.entities.traits

interface IGravityTrait : ITrait {

    val gravityTrait: GravityTrait

    fun jump(dt: Float, time: Float) {
        gravityTrait.jump(dt, time)
    }

}