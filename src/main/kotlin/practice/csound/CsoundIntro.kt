package practice.csound

import java.io.File

class CsoundIntro(outDac: Boolean, outputFilePath: String) {
    private val c: CsoundSystem

    init {
        // Create an instance of the Csound object
        c = CsoundSystem(outDac, outputFilePath)
    }

    fun setupEmprise() {
        val orcFile = File("./src/main/resources/orc/csoundEmprise.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 16.0
        //Abbreviated notation
        /*scoBuilder.appendLine("i \"simpleOcsil\" 0 1 0.7 440")
        scoBuilder.appendLine("i \"simpleOcsil\" + . 0.5 >")
        scoBuilder.appendLine("i \"simpleOcsil\" + . 0.5 >")
        scoBuilder.appendLine("i \"simpleOcsil\" 2 . 440")
        currentTime += 3
        scoBuilder.appendLine("i \"noInterpoOscil\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"interpoOscil\" ${currentTime} ${duration}")
        currentTime += duration
        duration = 10.0
        scoBuilder.appendLine("i \"playOsciliSine\" ${currentTime} ${duration}")
        duration = 16.0
        scoBuilder.appendLine("i \"playMidiSimpleAddiSynth\" ${currentTime} ${duration} ")
        duration = 16.0
        scoBuilder.appendLine("i \"playMidiSynthByRandom\" ${currentTime} ${duration} ")
        duration = 30.0
        scoBuilder.appendLine("i \"infiniteGlissando\" ${currentTime} ${duration} 0.8 5000")
        duration = 16.0
        scoBuilder.appendLine("i \"playRmSynth\" ${currentTime} ${duration}")
        duration = 5.0
        scoBuilder.appendLine("i \"playRmSynth\" ${currentTime} ${duration} 0.6 500 0.01 100")*/
        duration = 16.0
        scoBuilder.appendLine("i \"playAmSynth\" ${currentTime} ${duration}  0.6 500 0.01 100")

        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }

    fun run() {
        c.run()
    }
}

fun main() {
    val csoundIntro = CsoundIntro(true, "../out/wav/csoundIntro.wav")
    csoundIntro.setupEmprise()
    csoundIntro.run()
}