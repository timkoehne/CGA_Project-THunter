package cga.exercise.components.entities

import cga.exercise.components.geometry.IRenderable
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable

abstract class Entity : Transformable(), IRenderable {

    abstract var body: Renderable?

    var animationStartTime = 0f
    private val animationDuration = 1.0f

    fun animationPercentage(time: Float): Float {
        return (time - animationStartTime) / animationDuration
    }


}