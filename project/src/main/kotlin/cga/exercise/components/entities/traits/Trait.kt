package cga.exercise.components.entities.traits

import cga.exercise.components.entities.Entity
import cga.exercise.components.geometry.Transformable

abstract class Trait(val entity: Entity) {

    abstract fun update(dt: Float, time: Float)

}