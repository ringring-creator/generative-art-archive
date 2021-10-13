package practice.csound

import common.CsoundSystem
import java.io.File

class Delay(outDac: Boolean, outputFilePath: String) {
    private val c: CsoundSystem

    init {
        // Create an instance of the Csound object
        c = CsoundSystem(outDac, outputFilePath)
    }

    fun run() {
        c.run()
    }

    fun setupConstantDelay() {
        val orcFile = File("./src/main/resources/practices/orc/delay.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 8.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"variableDelay\" ${currentTime} ${duration} 0.0001")
        currentTime += duration
        //lowpass filter
        scoBuilder.appendLine("i \"variableDelay\" ${currentTime} ${duration} 0.001")
        currentTime += duration
        scoBuilder.appendLine("i \"variableDelay\" ${currentTime} ${duration} 0.01")
        currentTime += duration
        //com filter
        scoBuilder.appendLine("i \"variableDelay\" ${currentTime} ${duration} 0.035")
        currentTime += duration
        scoBuilder.appendLine("i \"variableDelay\" ${currentTime} ${duration} 0.05")
        currentTime += duration
        //echo
        scoBuilder.appendLine("i \"variableDelay\" ${currentTime} ${duration} 0.1")
        currentTime += duration
        scoBuilder.appendLine("i \"variableDelay\" ${currentTime} ${duration} 0.5")
        currentTime += duration

        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")
        c.readSco(sco)
    }

    fun setupVdelayTransposition() {
        val orcFile = File("./src/main/resources/practices/orc/delay.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 8.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"vdelayTransposition\" ${currentTime} ${duration} 1")
        currentTime += duration
        duration = 8.0 / 0.8
        scoBuilder.appendLine("i \"vdelayTransposition\" ${currentTime} ${duration} 0.8")
        currentTime += duration
        duration = 8.0 / 0.5
        scoBuilder.appendLine("i \"vdelayTransposition\" ${currentTime} ${duration} 0.5")
        currentTime += duration
        duration = 8.0 / 0.2
        scoBuilder.appendLine("i \"vdelayTransposition\" ${currentTime} ${duration} 0.2")
        currentTime += duration

        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")
        c.readSco(sco)
    }

    fun setupVariableDelay() {
        val orcFile = File("./src/main/resources/practices/orc/delay.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 10.0
        //Abbreviated notationp
        scoBuilder.appendLine("i \"flanger\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"DopplerEffect\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"modulationInstr\" ${currentTime} ${duration} 1")
        currentTime += duration
        scoBuilder.appendLine("i \"modulationInstr\" ${currentTime} ${duration} 0.8")
        currentTime += duration
        scoBuilder.appendLine("i \"modulationInstr\" ${currentTime} ${duration} 0.5")
        currentTime += duration
        scoBuilder.appendLine("i \"modulationInstr\" ${currentTime} ${duration} 0.2")
        currentTime += duration
        scoBuilder.appendLine("i \"modulationInstr\" ${currentTime} ${duration} 0.01")
        currentTime += duration
        //lowpass filter

        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")
        c.readSco(sco)
    }
}

fun main() {
    var csoundIntro = Delay(false, "../out/wav/constantDelay.wav")
    csoundIntro.setupConstantDelay()
    csoundIntro.run()
    csoundIntro = Delay(false, "../out/wav/vdelayTransposition.wav")
    csoundIntro.setupVdelayTransposition()
    csoundIntro.run()
    csoundIntro = Delay(false, "../out/wav/variableDelay.wav")
    csoundIntro.setupVariableDelay()
    csoundIntro.run()
}