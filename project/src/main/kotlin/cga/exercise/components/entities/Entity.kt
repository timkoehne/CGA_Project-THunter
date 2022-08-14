package cga.exercise.components.entities

import cga.exercise.collision.AABB
import cga.exercise.components.Util
import cga.exercise.components.entities.traits.GravityTrait
import cga.exercise.components.entities.traits.IGravityTrait
import cga.exercise.components.geometry.IRenderable
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.exercise.components.map.Foliage
import cga.exercise.components.map.Foliage.Companion.allFoliage
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import kotlin.math.max

open class Entity(var models: List<Renderable>, val myMap: MyMap, collisionBoxPath: String? = null) : Transformable(),
    IRenderable, IGravityTrait {
    constructor(model: Renderable, myMap: MyMap, collisionBoxPath: String? = null) : this(
        listOf(model), myMap, collisionBoxPath
    )

    companion object {
        val allEntities = mutableListOf<Entity>()
    }

    open val movementSpeed: Float = 5f
    open val jumpSpeed = 8f
    open val weight = 1f
    open val height = 0f

    override val gravityTrait = GravityTrait(this, myMap)
    val collisionBox: AABB? = collisionBoxPath?.let { AABB(this, ModelLoader.loadModel(it)) }

    init {
        models.forEach { it.parent = this }

        allEntities.add(this)

    }

    override fun render(shaderProgram: ShaderProgram) {
        collisionBox?.render(shaderProgram)

        for (model in models) {
            model.render(shaderProgram)
        }
    }

    override fun update(dt: Float, time: Float) {
        collisionBox?.setPosition(getPosition())
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

    fun collision(direction: Vector3f): Boolean {
        if (this.collisionBox != null) {
//            allEntities.forEach {
//                if (it.collisionBox != null && this != it) {
//                    val distance1 = it.collisionBox.minExtend.sub(this.collisionBox.maxExtend)
//                    val distance2 = this.collisionBox.minExtend.sub(it.collisionBox.maxExtend)
//                    val maxDistance = distance1.max(distance2)
//                    val maxValue = Util.largestValueInVector(maxDistance)
//                    if (maxValue < 0) {
//                        println("${this.javaClass} entity collision with ${it.javaClass}")
//                        println("distance 1 $distance1")
//                        println("distance 2 $distance2")
//                        println("maxdistance $maxDistance")
//                        println("maxvalue $maxValue")
//                        return true
//                    }
//                }
//            }
            allFoliage.forEach {
                if (it.collisionBox != null && this != it) {
                    val distance1 = it.collisionBox.minExtend.sub(this.collisionBox.maxExtend)
                    val distance2 = this.collisionBox.minExtend.sub(it.collisionBox.maxExtend)
                    val maxDistance = distance1.max(distance2)
                    val maxValue = Util.largestValueInVector(maxDistance)
                    if (maxValue < 0) {
                        println("${this.javaClass} entity collision with ${it.javaClass}")
//                        println("distance 1 $distance1")
//                        println("distance 2 $distance2")
//                        println("maxdistance $maxDistance")
//                        println("maxvalue $maxValue")
                        return true
                    }
                }
            }
        }

        return false
    }

    private fun move(direction: Vector3f) {
        translate(direction)
        if (collision(direction)) {
            translate(direction.mul(-1f))
        }
    }

    fun moveForward(dt: Float) {
        val direction = Vector3f(0f, 0f, -movementSpeed * dt)
        move(direction)
    }

    fun moveLeft(dt: Float) {
        val direction = Vector3f(-movementSpeed * dt, 0f, 0f)
        move(direction)
    }

    fun moveRight(dt: Float) {
        val direction = Vector3f(movementSpeed * dt, 0f, 0f)
        move(direction)
    }

    fun moveBack(dt: Float) {
        val direction = Vector3f(0f, 0f, movementSpeed * dt)
        move(direction)
    }

    fun moveDown(dt: Float) {
        val direction = Vector3f(0f, movementSpeed * dt, 0f)
        move(direction)
    }

    fun moveUp(dt: Float) {
        val direction = Vector3f(0f, -movementSpeed * dt, 0f)
        move(direction)
    }


    open fun movementControl(dt: Float, time: Float, window: GameWindow) {

        if (window.getKeyState(GLFW.GLFW_KEY_W)) moveForward(dt)
        if (window.getKeyState(GLFW.GLFW_KEY_S)) moveBack(dt)
        if (window.getKeyState(GLFW.GLFW_KEY_A)) moveLeft(dt)
        if (window.getKeyState(GLFW.GLFW_KEY_D)) moveRight(dt)

        if (window.getKeyState(GLFW.GLFW_KEY_SPACE)) jump()
    }


}