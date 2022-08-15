package cga.exercise.components.entities.traits

interface IGravityTrait : ITrait {

    val gravityTrait: GravityTrait?

    fun jump() {
        gravityTrait?.jump()
    }

}