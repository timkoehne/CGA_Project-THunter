package cga.exercise.components.map

import cga.exercise.components.entities.traits.GravityTrait
import cga.exercise.components.entities.traits.IGravityTrait
import cga.exercise.components.geometry.IRenderable
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram

open class Foliage(var models: List<Renderable>) : Transformable(), IRenderable {
    constructor(model: Renderable) : this(listOf(model))


    init {
        models.forEach { it.parent = this }
    }

    override fun render(shaderProgram: ShaderProgram) {
        for (model in models) {
            model.render(shaderProgram)
        }
    }

    fun update(dt: Float, time: Float) {
        
    }

    fun cleanUp() {
        models.forEach { it.cleanUp() }
    }


}