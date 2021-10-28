package practice.csound

import common.CsoundSystem
import java.io.File

class Distortion(outDac: Boolean, outputFilePath: String) {
    private val c: CsoundSystem

    init {
        // Create an instance of the Csound object
        c = CsoundSystem(outDac, outputFilePath)
    }

    fun run() {
        c.run()
    }

    fun setupDistortion() {
        val orcFile = File("./src/main/resources/practices/orc/nonLinearDistortion.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 5.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"simpleDistortion\" ${currentTime} ${duration}  0.5   440  2")
        currentTime += duration
        scoBuilder.appendLine("i \"simpleDistortion\" ${currentTime} ${duration}  0.5 440 3")
        currentTime += duration
        scoBuilder.appendLine("i \"simpleDistortion\" ${currentTime} ${duration}  0.5 440 4")
        currentTime += duration
        scoBuilder.appendLine("i \"simpleDistortion\" ${currentTime} ${duration}  0.5 440 5")
        currentTime += duration
        duration = 10.0
        scoBuilder.appendLine("i \"ridgeDistortion\" ${currentTime} ${duration} 440")
        currentTime += duration
        duration = 16.0
        scoBuilder.appendLine("i \"playSimpleDistortion\" ${currentTime} ${duration} 4")
        currentTime += duration
        scoBuilder.appendLine("i \"playSimpleDistortion\" ${currentTime} ${duration} 5")
        currentTime += duration
        scoBuilder.appendLine("i \"playRidgeDistortion\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")
        c.readSco(sco)
    }
}

fun main() {
    var granulation = Distortion(false, "../out/wav/distortion.wav")
    granulation.setupDistortion()
    granulation.run()
}