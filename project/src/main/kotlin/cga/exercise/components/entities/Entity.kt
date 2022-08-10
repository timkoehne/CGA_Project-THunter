package cga.exercise.components.entities

import cga.exercise.components.entities.traits.GravityTrait
import cga.exercise.components.entities.traits.IGravityTrait
import cga.exercise.components.geometry.IRenderable
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram

open class Entity(var models: List<Renderable>, val myMap: MyMap) : Transformable(), IRenderable, IGravityTrait {
    constructor(model: Renderable, myMap: MyMap) : this(listOf(model), myMap)

    open val movementSpeed: Float = 5f
    open val jumpSpeed = 8f
    open val weight = 1f

    override val gravityTrait = GravityTrait(this, myMap)

    init {
        models.forEach { it.parent = this }
    }

    override fun render(shaderProgram: ShaderProgram) {
        for (model in models) {
            model.render(shaderProgram)
        }
    }

    override fun update(dt: Float, time: Float) {
        gravityTrait.update(dt, time)
    }

    open fun onMouseMove(xDiff: Double, yDiff: Double) {
        rotate(0f, (xDiff * 0.002f).toFloat(), 0f)
        rotate((yDiff * 0.002f).toFloat(), 0f, 0f)
    }

    fun cleanUp() {
        models.forEach { it.cleanUp() }
    }


}