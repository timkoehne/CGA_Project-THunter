package cga.exercise.components.entities

import cga.exercise.components.geometry.IRenderable
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader

open class Entity(var models: List<Renderable>) : Transformable(), IRenderable {

    constructor(model: Renderable): this(listOf(model))

    var animationStartTime = 0f
    private val animationDuration = 1.0f

    init {
        models.forEach { it.parent = this }
    }

    fun animationPercentage(time: Float): Float {
        return (time - animationStartTime) / animationDuration
    }

     override fun render(shaderProgram: ShaderProgram) {
        for (model in models) {
            model.render(shaderProgram)
        }
    }

    open fun update(dt: Float, time: Float){

    }

    open fun onMouseMove(xDiff: Double, yDiff: Double) {
        rotate(0f, (xDiff * 0.002f).toFloat(), 0f)
        rotate((yDiff * 0.002f).toFloat(), 0f, 0f)
    }


}