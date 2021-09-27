package practice.csound

import common.CsoundSystem
import java.io.File

class SpeechSynth(outDac: Boolean, outputFilePath: String) {
    private val c: CsoundSystem

    init {
        // Create an instance of the Csound object
        c = CsoundSystem(outDac, outputFilePath)
    }

    fun run() {
        c.run()
    }

    fun setupFormant() {
        val orcFile = File("./src/main/resources/practices/orc/formant.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 16.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"playFormantSopranoA\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"playFormantSopranoI\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"playFormantSopranoU\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"playFormantSopranoE\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"playFormantSopranoO\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }

    fun setupVocoder() {
        val orcFile = File("./src/main/resources/practices/orc/vocoder.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 8.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"aToZ\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"nightingale\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"vocoder\" ${currentTime} ${duration} 150")
        currentTime += duration
        scoBuilder.appendLine("i \"vocoder\" ${currentTime} ${duration} 200")
        currentTime += duration
        scoBuilder.appendLine("i \"vocoder\" ${currentTime} ${duration} 250")
        currentTime += duration
        scoBuilder.appendLine("i \"vocoder\" ${currentTime} ${duration} 400")
        currentTime += duration
        scoBuilder.appendLine("i \"vocoder\" ${currentTime} ${duration} 800")
        currentTime += duration
        scoBuilder.appendLine("i \"vocoder\" ${currentTime} ${duration} 1200")
        currentTime += duration
        scoBuilder.appendLine("i \"vocoder\" ${currentTime} ${duration} 2400")
        currentTime += duration
        scoBuilder.appendLine("i \"vocoder\" ${currentTime} ${duration} 4800")
        currentTime += duration
        scoBuilder.appendLine("i \"vocoder\" ${currentTime} ${duration} 9600")
        currentTime += duration
        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }

    fun setupPhaseVocoder() {
        val orcFile = File("./src/main/resources/practices/orc/vocoder.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 8.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"aToZ\" ${currentTime} ${duration}")
        //scoBuilder.appendLine("i \"pvsanalInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }
}

fun main() {
    /*var csoundIntro = SpeechSynth(false, "../out/wav/formant.wav")
    csoundIntro.setupFormant()
    csoundIntro.run()*/
    /*var csoundIntro = SpeechSynth(false, "../out/wav/vocoder.wav")
    csoundIntro.setupVocoder()
    csoundIntro.run()*/
    var csoundIntro = SpeechSynth(true, "../out/wav/phaseVocoder.wav")
    csoundIntro.setupPhaseVocoder()
    csoundIntro.run()
}