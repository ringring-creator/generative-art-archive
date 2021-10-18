package practice.csound

import common.CsoundSystem
import java.io.File

class Spatialization(outDac: Boolean, outputFilePath: String) {
    private val c: CsoundSystem

    init {
        // Create an instance of the Csound object
        c = CsoundSystem(outDac, outputFilePath)
    }

    fun run() {
        c.run()
    }

    fun setupSpatialization() {
        val orcFile = File("./src/main/resources/practices/orc/spatialization.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 10.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"panoramization\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"panoramizationLinear\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"panoramizationSqrt\" ${currentTime} ${duration}")
        currentTime += duration
        duration = 30.0
        scoBuilder.appendLine("i \"threeDim\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")
        c.readSco(sco)
    }
}

fun main() {
    var specialization = Spatialization(false, "../out/wav/spatialization.wav")
    specialization.setupSpatialization()
    specialization.run()
}