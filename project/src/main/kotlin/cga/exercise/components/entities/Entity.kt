package cga.exercise.components.entities

import cga.exercise.components.entities.traits.GravityTrait
import cga.exercise.components.entities.traits.IGravityTrait
import cga.exercise.components.geometry.IRenderable
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GameWindow
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import java.awt.SystemColor.window
import kotlin.math.cos

open class Entity(var models: List<Renderable>, val myMap: MyMap) : Transformable(), IRenderable, IGravityTrait {
    constructor(model: Renderable, myMap: MyMap) : this(listOf(model), myMap)

    open val movementSpeed: Float = 5f
    open val jumpSpeed = 8f
    open val weight = 1f
    open val height = 0f

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
//        println(Math.toDegrees(rotationInsgesamt.x.toDouble()))
        rotate((yDiff * 0.002f).toFloat(), 0f, 0f)
        rotate(0f, (xDiff * 0.002f).toFloat(), 0f)
    }

    fun cleanUp() {
        models.forEach { it.cleanUp() }
    }


    open fun movementControl(dt: Float, time: Float, window: GameWindow) {

        if (window.getKeyState(GLFW.GLFW_KEY_W)) translate(Vector3f(0f, 0f, -5 * dt))
        if (window.getKeyState(GLFW.GLFW_KEY_S)) translate(Vector3f(0f, 0f, 5 * dt))
        if (window.getKeyState(GLFW.GLFW_KEY_A)) translate(Vector3f(-5 * dt, 0f, 0f))
        if (window.getKeyState(GLFW.GLFW_KEY_D)) translate(Vector3f(5 * dt, 0f, 0f))

        if (window.getKeyState(GLFW.GLFW_KEY_SPACE)) jump(dt, time)
    }


}