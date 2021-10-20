package practice.csound

import common.CsoundSystem
import java.io.File

class Dynamics(outDac: Boolean, outputFilePath: String) {
    private val c: CsoundSystem

    init {
        // Create an instance of the Csound object
        c = CsoundSystem(outDac, outputFilePath)
    }

    fun run() {
        c.run()
    }

    fun setupDynamics() {
        val orcFile = File("./src/main/resources/practices/orc/dynamics.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 8.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"aToZ\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"noiseGate\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"compressedAtoZ\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"expandedAtoZ\" ${currentTime} ${duration}")
        currentTime += duration
        duration = 16.0
        scoBuilder.appendLine("i \"followEx\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")
        c.readSco(sco)
    }
}

fun main() {
    var dynamics = Dynamics(false, "../out/wav/dynamics.wav")
    dynamics.setupDynamics()
    dynamics.run()
}