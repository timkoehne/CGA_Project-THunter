package cga.exercise.components.map

import cga.exercise.collision.AABB
import cga.exercise.components.entities.traits.GravityTrait
import cga.exercise.components.entities.traits.IGravityTrait
import cga.exercise.components.geometry.IRenderable
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader

open class Foliage(var models: List<Renderable>, collisionBoxPath: String? = null) : Transformable(), IRenderable {
    constructor(model: Renderable, collisionBoxPath: String? = null) : this(listOf(model), collisionBoxPath)

    val collisionBox: AABB? = collisionBoxPath?.let { AABB(this, ModelLoader.loadModel(it)) }

    companion object {

        val allFoliage = mutableListOf<Foliage>()
        val treeCollsionPath = "project/assets/trees/tree hitbox.obj"
        val stoneCollsionPath = "project/assets/deko/bigstonecube.obj"
    }


    init {
        models.forEach { it.parent = this }
        allFoliage.add(this)
    }

    override fun render(shaderProgram: ShaderProgram) {

        collisionBox?.render(shaderProgram)

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