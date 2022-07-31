package cga.exercise.components.map

class MyClock(
    val sonnenaufgangUhrzeit: Float,
    val sonnenuntergangUhrzeit: Float,
    val fadeDauerIngameStunden: Float,
    val ingameStundenDauerInSekunden: Int
) {
    private var lastPrint = 0f
    var ingameTime = 0f
    var ingameTag: Int = 0

    fun update(time: Float) {

//        println("ingametime $ingameTime")
//        println("lastprint $lastPrint")

        ingameTime = (time / ingameStundenDauerInSekunden) % 24

        if (lastPrint >= 23.5) {
            println("test")
            lastPrint = 0f
        }

        if (ingameTime > (lastPrint + 0.5f)) {
            println("ingameTime: %.2f Uhr".format(ingameTime))
            lastPrint += 0.5f
        }
    }


}