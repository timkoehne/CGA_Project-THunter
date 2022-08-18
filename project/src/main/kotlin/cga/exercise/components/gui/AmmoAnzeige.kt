package cga.exercise.components.gui

import cga.exercise.components.texture.Texture2D
import cga.exercise.game.Scene
import org.joml.Vector2f

class AmmoAnzeige : GuiElement(null, Vector2f(1f - 0.05f, -1f), Vector2f(0.2f, 0.2f)) {

    val maxAmmo = 5
    var currentAmmoIndex: Int = maxAmmo - 1
    var nachladeZeit: Float = 1f

    var reloadingStartTime: Float = -1f

    var reloadSound = Scene.audioMaster.createAudioSource("project/assets/sounds/reload.ogg")

    companion object {

        val reloadingDuration = 1f

        val background = Texture2D.invoke("project/assets/textures/wanted.png", false)
        val bullet = Texture2D.invoke("project/assets/textures/bullet.png", false)
        val bulletEmpty = Texture2D.invoke("project/assets/textures/bulletEmpty.png", false)
    }

    init {
        for (x in 0 until maxAmmo) {
            children.add(GuiElement(bullet, Vector2f(-1f, 1f), Vector2f(0.07f, 0.3f)))
            children[x].parent = this
            children[x].translate(Vector2f(x * 3f, 0f))
        }

        reloadSound.setVolume(0.2f)
    }

    fun ammoVorhanden(): Boolean{
        return currentAmmoIndex >= 0
    }

    fun shoot() {
        if (currentAmmoIndex >= 0) {
            children[currentAmmoIndex].texID = bulletEmpty.texID
            currentAmmoIndex--
        }
    }

    fun reloadOneBullet() {
        if(reloadAllowed()){
            currentAmmoIndex++
            reloadSound.play()
            children[currentAmmoIndex].texID = bullet.texID
        }
    }

    fun reloadAllowed(): Boolean {
        return currentAmmoIndex < maxAmmo - 1
    }

}