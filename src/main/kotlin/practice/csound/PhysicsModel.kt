package practice.csound

import common.CsoundSystem
import java.io.File

class PhysicsModel(outDac: Boolean, outputFilePath: String) {
    private val c: CsoundSystem

    init {
        // Create an instance of the Csound object
        c = CsoundSystem(outDac, outputFilePath)
    }

    fun run() {
        c.run()
    }

    fun setupKsOps() {
        val orcFile = File("./src/main/resources/practices/orc/physicsModel.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 16.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"playWgpluck2Instr\" ${currentTime} ${duration} 0.25 0.1")
        currentTime += duration
        scoBuilder.appendLine("i \"playWgpluck2Instr\" ${currentTime} ${duration} 0.5 0.1")
        currentTime += duration
        scoBuilder.appendLine("i \"playWgpluck2Instr\" ${currentTime} ${duration} 0.75 0.1")
        currentTime += duration
        scoBuilder.appendLine("i \"playWgpluck2Instr\" ${currentTime} ${duration} 0.5 0.5")
        currentTime += duration
        scoBuilder.appendLine("i \"playWgpluck2Instr\" ${currentTime} ${duration} 0.5 0.9")
        currentTime += duration
        scoBuilder.appendLine("i \"playWgpluck2StresonInstr\" ${currentTime} ${duration} 0.5 0.9")
        currentTime += duration
        scoBuilder.appendLine("i \"playRepluckInstr\" ${currentTime} ${duration} 0.25 0.1")
        currentTime += duration
        scoBuilder.appendLine("i \"playRepluckInstr\" ${currentTime} ${duration} 0.5 0.1")
        currentTime += duration
        scoBuilder.appendLine("i \"playRepluckInstr\" ${currentTime} ${duration} 0.75 0.1")
        currentTime += duration
        scoBuilder.appendLine("i \"playRepluckInstr\" ${currentTime} ${duration} 0.5 0.5")
        currentTime += duration
        scoBuilder.appendLine("i \"playRepluckInstr\" ${currentTime} ${duration} 0.5 0.9")
        currentTime += duration

        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }

    fun setupWaveguide() {
        val orcFile = File("./src/main/resources/practices/orc/physicsModel.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 10.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"waveguideInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"waveguide2Instr\" ${currentTime} ${duration}")
        currentTime += duration
        duration = 16.0
        scoBuilder.appendLine("i \"playWgbowInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"playWgbrassInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"playWgclarInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"playWgfluteInstr\" ${currentTime} ${duration}")
        currentTime += duration



        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")
        c.readSco(sco)
    }
}

fun main() {
    var csoundIntro = PhysicsModel(false, "../out/wav/ksOps.wav")
    csoundIntro.setupKsOps()
    csoundIntro.run()
    csoundIntro = PhysicsModel(false, "../out/wav/waveguide.wav")
    csoundIntro.setupWaveguide()
    csoundIntro.run()
}