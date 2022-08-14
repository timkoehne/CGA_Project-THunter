package cga.exercise.components.entities

import cga.exercise.components.entities.traits.IReloadAnimationTrait
import cga.exercise.components.entities.traits.ReloadingAnimationTrait
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader

class Character(myMap: MyMap) : Entity(mutableListOf(firstPersonView, thirdPersonView), myMap, hitbox),
    IReloadAnimationTrait {

    companion object {
        val firstPersonView =
            ModelLoader.loadModel("project/assets/character/sniper.obj")
        val thirdPersonView =
            ModelLoader.loadModel("project/assets/character/person.obj", 0f, Math.toRadians(180.0).toFloat(), 0f)

        private val hitbox = "project/assets/character/person hitbox.obj"


    }


    val reloadingAnimationTrait = ReloadingAnimationTrait(this)
    var isFirstPersonView = true

    override fun render(shaderProgram: ShaderProgram) {

//        if(showHitbox){
//            collisionBox?.render(shaderProgram)
//        }

        if (isFirstPersonView) {
            models[0].render(shaderProgram)
            reloadingAnimationTrait.render(shaderProgram)
        } else {
            models[1].render(shaderProgram)
        }
    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        reloadingAnimationTrait.update(dt, time)
    }

    override fun reload() {
        reloadingAnimationTrait.reload()
    }


}