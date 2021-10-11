package practice.csound

import common.CsoundSystem
import java.io.File

class Lpc(outDac: Boolean, outputFilePath: String) {
    private val c: CsoundSystem

    init {
        // Create an instance of the Csound object
        c = CsoundSystem(outDac, outputFilePath)
    }

    fun run() {
        c.run()
    }

    fun setupLpc() {
        val orcFile = File("./src/main/resources/practices/orc/lpc.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 8.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"aToZ\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"lpc\" ${currentTime} ${duration} 1 1")
        currentTime += duration
        scoBuilder.appendLine("i \"lpc\" ${currentTime} ${duration} 1 2")
        currentTime += duration
        scoBuilder.appendLine("i \"lpc\" ${currentTime} ${duration} 1 0.5")
        currentTime += duration
        duration = 16.0
        scoBuilder.appendLine("i \"lpc\" ${currentTime} ${duration} 0.5 1")
        currentTime += duration
        duration = 4.0
        scoBuilder.appendLine("i \"lpc\" ${currentTime} ${duration} 2 1")
        currentTime += duration

        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }
}

fun main() {
    var csoundIntro = Lpc(false, "../out/wav/lpc.wav")
    csoundIntro.setupLpc()
    csoundIntro.run()
}