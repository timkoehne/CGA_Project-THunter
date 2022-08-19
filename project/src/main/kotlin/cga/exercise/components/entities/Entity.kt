package cga.exercise.components.entities

import cga.exercise.collision.AABB
import cga.exercise.components.Util
import cga.exercise.components.entities.movementcontroller.MovementController
import cga.exercise.components.entities.traits.GravityTrait
import cga.exercise.components.entities.traits.IGravityTrait
import cga.exercise.components.geometry.IRenderable
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.exercise.components.gui.WantedPoster
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.Scene
import cga.framework.ModelLoader
import org.joml.Math

open class Entity(var models: List<Renderable>, val myMap: MyMap, collisionBoxPath: String? = null) : Transformable(),
    IRenderable, IGravityTrait {
    constructor(model: Renderable, myMap: MyMap, collisionBoxPath: String? = null) : this(
        listOf(model), myMap, collisionBoxPath
    )

    companion object {
        var animalDyingSound = Scene.audioMaster.createAudioSource("project/assets/sounds/animal_dying.ogg")

        init {
            animalDyingSound.setVolume(0.05f)
        }


        val allEntities = mutableListOf<Entity>()
    }

    open val movementSpeed: Float = 5f
    open val jumpSpeed = 8f
    open val weight = 1f
    open val height = 0f
    var alive = true

    override val gravityTrait: GravityTrait? = GravityTrait(this, myMap)
    open val movementController: MovementController? = MovementController(this)
    val collisionBox: AABB? = collisionBoxPath?.let { AABB(this, ModelLoader.loadModel(it)) }

    init {
        models.forEach { it.parent = this }
        allEntities.add(this)

    }

    override fun render(shaderProgram: ShaderProgram) {
        if (alive) {
            collisionBox?.render(shaderProgram)

            for (model in models) {
                model.render(shaderProgram)
            }
        }
    }

    override fun update(dt: Float, time: Float) {
        if (alive) {
            movementController?.update(dt, time)
            collisionBox?.setPosition(getPosition())
            gravityTrait?.update(dt, time)

            checkForBulletCollision()

        }
    }

    open fun onMouseMove(xDiff: Double, yDiff: Double) {
        rotate(0f, Math.toRadians((xDiff * 0.1f).toFloat()), 0f)
    }

    fun cleanUp() {
        models.forEach { it.cleanUp() }
    }

    fun movementCollision(): Boolean {
        if (this.collisionBox != null) {
//            if (checkForEntityCollision()) return true
            //foliage koennte in chunks aufgeteilt werden, dann muesste hier viel weniger gecheckt werden
            if (checkForFoliageCollision()) return true
        }

        return false
    }

    fun checkForBulletCollision(): Boolean {
        if (this.collisionBox != null && this.alive) {
            EntityManager.bullets.forEach {
                if (it.collisionBox != null && this != it && it.alive) {
                    val distance1 = it.collisionBox.minExtend.sub(this.collisionBox!!.maxExtend)
                    val distance2 = this.collisionBox!!.minExtend.sub(it.collisionBox.maxExtend)
                    val maxDistance = distance1.max(distance2)
                    val maxValue = Util.largestValueInVector(maxDistance)
                    if (maxValue < 0) {
                        it.alive = false

                        this.hitByBullet()
                        return true
                    }
                }
            }
        }
        return false
    }

    fun checkForEntityCollision(): Boolean {
        allEntities.forEach {
            if (it.collisionBox != null && this != it) {
                val distance1 = it.collisionBox.minExtend.sub(this.collisionBox!!.maxExtend)
                val distance2 = this.collisionBox!!.minExtend.sub(it.collisionBox.maxExtend)
                val maxDistance = distance1.max(distance2)
                val maxValue = Util.largestValueInVector(maxDistance)
                if (maxValue < 0) {
                    println("${this.javaClass} entity collision with ${it.javaClass}")
                    println("distance 1 $distance1")
                    println("distance 2 $distance2")
                    println("maxdistance $maxDistance")
                    println("maxvalue $maxValue")
                    return true
                }
            }
        }
        return false
    }

    fun checkForFoliageCollision(): Boolean {
        if (alive) {
            //TODO check only foliage in same chunk
            //TODO seperate foliage so that this wont loop over every grass element

            if (this is cga.exercise.components.entities.Character) {
                myMap.getChunk(this.getWorldPosition())?.getCollidables()?.forEach {
                    if (it.collisionBox != null && this != it) {
                        val distance1 = it.collisionBox.minExtend.sub(this.collisionBox!!.maxExtend)
                        val distance2 = this.collisionBox!!.minExtend.sub(it.collisionBox.maxExtend)
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
        }
        return false
    }

    open fun hitByBullet() {
        alive = false
        animalDyingSound.play()

        val animalName = this.javaClass.simpleName
        println(animalName)

        if (WantedPoster.killList[animalName] == null) {
            WantedPoster.killList[animalName] = 0
        }
        WantedPoster.killList[animalName] = (WantedPoster.killList[animalName] as Int) + 1
        println("$animalName has been hit ${WantedPoster.killList[animalName]} times")
    }


    fun moveForward(dt: Float): Boolean {
        return movementController?.moveForward(dt) ?: false
    }

    fun moveLeft(dt: Float): Boolean {
        return movementController?.moveLeft(dt) ?: false
    }

    fun moveRight(dt: Float): Boolean {
        return movementController?.moveRight(dt) ?: false
    }

    fun moveBack(dt: Float): Boolean {
        return movementController?.moveBack(dt) ?: false
    }

    fun moveDown(dt: Float): Boolean {
        return movementController?.moveDown(dt) ?: false
    }

    fun moveUp(dt: Float): Boolean {
        return movementController?.moveUp(dt) ?: false
    }


}