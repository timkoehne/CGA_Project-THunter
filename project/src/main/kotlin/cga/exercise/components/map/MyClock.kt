package cga.exercise.components.map

class MyClock(
    val sonnenaufgangUhrzeit: Float,
    val sonnenuntergangUhrzeit: Float,
    val fadeDauerIngameStunden: Float,
    val ingameStundenDauerInSekunden: Int
) {

    companion object{
        var stopTime = false
    }

    private var lastPrint = 0f
    var ingameTime = 0f
    var ingameTag: Int = 0

    fun update(time: Float) {
        if (!stopTime) {
            ingameTime = (time / ingameStundenDauerInSekunden) % 24
        }

            if ((time / ingameStundenDauerInSekunden) > ingameTag * 24 + ingameTime) {
                ingameTag++
                println("ingameTime: 0,00 Uhr")
                lastPrint = 0f
            }

            if (ingameTime > (lastPrint + 0.5f)) {
                println("ingameTime: %.2f Uhr".format(ingameTime))
                lastPrint += 0.5f
            }
    }


}