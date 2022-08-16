package cga.exercise.components.entities.traits

import DroneAnimationTrait

interface IDroneAnimationTrait: ITrait {

    val animationTrait: DroneAnimationTrait

    fun open()
    fun close()

    fun toggle()

}