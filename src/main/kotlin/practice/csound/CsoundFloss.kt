package practice.csound

import common.CsoundSystem
import java.io.File

class CsoundFloss(outDac: Boolean, outputFilePath: String) {
    private val c: CsoundSystem

    init {
        // Create an instance of the Csound object
        c = CsoundSystem(outDac, outputFilePath)
    }

    fun setupDbVersusAmp() {
        val orcFile = File("./src/main/resources/practices/orc/csoundFloss.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 10.0
        scoBuilder.appendLine("i \"linearAmp\" ${currentTime} ${duration} 3000")
        currentTime += duration
        scoBuilder.appendLine("i \"dbAmp\" ${currentTime} ${duration} 3000")
        currentTime += duration
        duration = 5.0
        for (amp in arrayListOf(0.1, 0.5, 1.0)) {
            for (freq in arrayListOf(100, 1000, 3000, 10000)) {
                scoBuilder.appendLine("i \"sineWave\" ${currentTime} ${duration} ${amp} ${freq}")
                currentTime += duration
            }
        }
        scoBuilder.appendLine("e")
        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }

    fun setupOpeCheck() {
        val orcFile = File("./src/main/resources/practices/orc/csoundFloss.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 1.0
        scoBuilder.appendLine("i \"variableCheck\" ${currentTime} ${duration}")
        currentTime += duration
        duration = 16.0
        scoBuilder.appendLine("i \"arrayCheck\" ${currentTime} ${duration}")
        currentTime += duration
        duration = 5.0
        scoBuilder.appendLine("i \"gen01Sample\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"sine\" ${currentTime} ${duration}")
        currentTime += duration
        duration = 5.0
        scoBuilder.appendLine("i \"gen08Sample\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"gen16Sample\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"gen19Sample\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"gen01Sample\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"gen30Sample\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"arraySort\" ${currentTime} 0")
        scoBuilder.appendLine("i \"useCustomOp\" ${currentTime} 0")
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
    /*val dbAmp = CsoundFloss(false, "../out/wav/dbVsAmp.wav")
    dbAmp.setupDbVersusAmp()
    dbAmp.run()*/
    val csoundIntro = CsoundFloss(false, "../out/wav/opeCheck.wav")
    csoundIntro.setupOpeCheck()
    csoundIntro.run()
}