package cga.exercise.components.map

class MyClock(
    val sonnenaufgangUhrzeit: Float,
    val sonnenuntergangUhrzeit: Float,
    val fadeDauerIngameStunden: Float,
    val ingameStundenDauerInSekunden: Int
) {
    var ingameTime = 8f

    fun update(time: Float) {
        ingameTime = (time / ingameStundenDauerInSekunden) % 24
        println("ingameTime: $ingameTime Uhr")
    }


}