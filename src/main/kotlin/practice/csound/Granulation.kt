package practice.csound

import common.CsoundSystem
import java.io.File

class Granulation(outDac: Boolean, outputFilePath: String) {
    private val c: CsoundSystem

    init {
        // Create an instance of the Csound object
        c = CsoundSystem(outDac, outputFilePath)
    }

    fun run() {
        c.run()
    }

    fun setupGranulation() {
        val orcFile = File("./src/main/resources/practices/orc/granulation.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 30.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"granulationSynth\" ${currentTime} ${duration} 250    0.8     .080    .6543")
        currentTime += 5
        scoBuilder.appendLine("i \"granulationSynth\" ${currentTime} ${duration} 500    0.6     .060    .3763")
        currentTime += 5
        scoBuilder.appendLine("i \"granulationSynth\" ${currentTime} ${duration} 750    0.4     .040    .1298")
        currentTime += 5
        scoBuilder.appendLine("i \"granulationSynth\" ${currentTime} ${duration} 1000   0.2     .020    .5678")
        currentTime += 5
        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")
        c.readSco(sco)
    }
}

fun main() {
    var granulation = Granulation(false, "../out/wav/granulation.wav")
    granulation.setupGranulation()
    granulation.run()
}