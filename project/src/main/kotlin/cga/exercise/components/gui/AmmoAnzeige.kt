package cga.exercise.components.gui

import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f

class AmmoAnzeige : GuiElement(null, Vector2f(1f - 0.05f, -1f), Vector2f(0.2f, 0.2f)) {

    val maxAmmo = 5
    var aktuelleAmmo: Int = maxAmmo
    var nachladeZeit: Float = 1f

    var state = State.Usable
    var reloadingStartTime: Float = -1f

    companion object {

        enum class State {
            Reloading, Usable
        }

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
    }

    fun shoot() {
        if (aktuelleAmmo > 0) {
            aktuelleAmmo--
            children[aktuelleAmmo].texID = bulletEmpty.texID
        }
    }

    fun reload() {
        state = State.Reloading
        reloadingStartTime = -1f

    }

    override fun update(dt: Float, time: Float) {

        //letzte kugel
        if (aktuelleAmmo == maxAmmo) {
            state = State.Usable
        }

        if (state == State.Reloading) {
            //anfang des nachladens
            if (reloadingStartTime == -1f) {
                reloadingStartTime = time
                children[aktuelleAmmo].texID = bullet.texID
                aktuelleAmmo++

                //weitere kugeln
            } else if (time > reloadingStartTime + reloadingDuration) {
                children[aktuelleAmmo].texID = bullet.texID
                reloadingStartTime = time
                aktuelleAmmo++
            }
        }
    }

}