package cga.exercise.components.map

class MyClock(
    val sonnenaufgangUhrzeit: Float,
    val sonnenuntergangUhrzeit: Float,
    val fadeDauerIngameStunden: Float,
    val ingameStundenDauerInSekunden: Int
) {
    private var lastMessage = 0f
    var ingameTime = 8f

    fun update(time: Float) {
        ingameTime = (time / ingameStundenDauerInSekunden) % 24
        if (ingameTime > lastMessage + 0.5f) {
            println("ingameTime: %.2f Uhr".format(ingameTime))
            lastMessage += 0.5f
            if (lastMessage > 23.5)
                lastMessage = 0f
        }
    }


}