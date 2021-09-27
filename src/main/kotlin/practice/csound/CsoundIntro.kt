package practice.csound

import common.CsoundSystem
import java.io.File

class CsoundIntro(outDac: Boolean, outputFilePath: String) {
    private val c: CsoundSystem

    init {
        // Create an instance of the Csound object
        c = CsoundSystem(outDac, outputFilePath)
    }

    fun setupEmpriseOther() {
        val orcFile = File("./src/main/resources/practices/orc/csoundEmprise.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 16.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"simpleOcsil\" 0 1 0.7 440")
        scoBuilder.appendLine("i \"simpleOcsil\" + . 0.5 >")
        scoBuilder.appendLine("i \"simpleOcsil\" + . 0.5 >")
        scoBuilder.appendLine("i \"simpleOcsil\" 2 . 440")
        currentTime += 3
        scoBuilder.appendLine("i \"playMidiSimpleAddiSynth\" ${currentTime} ${duration} ")
        currentTime += duration
        duration = 11.0
        scoBuilder.appendLine("i \"playOsciliSine\" ${currentTime} ${duration} ")
        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }

    fun setupModulations() {
        val orcFile = File("./src/main/resources/practices/orc/csoundEmprise.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 16.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"playRmSynth\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"playAmSynth\" ${currentTime} ${duration} 0.8 0.4 300")
        currentTime += duration
        var amp = 1.0
        for (cm in arrayListOf(8, 2, 0.5, 0.1)) {
            scoBuilder.appendLine("i \"playFmSynth\" ${currentTime} ${duration} ${amp} ${cm}")
            amp -= 0.15
            currentTime += duration
        }
        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }

    fun run() {
        c.run()
    }

    fun setupInfiniteGlissando() {
        val orcFile = File("./src/main/resources/practices/orc/csoundEmprise.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        //Abbreviated notation
        var duration = 60.0
        scoBuilder.appendLine("i \"infiniteGlissando\" ${currentTime} ${duration} 0.8 5000")
        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }

    fun setupInteropAndRandom() {
        val orcFile = File("./src/main/resources/practices/orc/csoundEmprise.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 16.0
        scoBuilder.appendLine("i \"playMidiSynthByRandom\" ${currentTime} ${duration} ")
        currentTime += duration
        scoBuilder.appendLine("i \"noInterpoOscil\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"interpoOscil\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }

    fun setupFmModulApp() {
        val orcFile = File("./src/main/resources/practices/orc/csoundEmprise.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 16.0
        //Abbreviated notation
        //                                                                              amp    C  M     MaxI MinI
        scoBuilder.appendLine("i \"playBellByFmSynth\" ${currentTime} ${duration} 1     1   1.215 6    0")
        currentTime += duration
        scoBuilder.appendLine("i \"playBellByFmSynth\" ${currentTime} ${duration} 0.7   1   2     6    3")
        currentTime += duration
        scoBuilder.appendLine("i \"playComplexFmSynth\" ${currentTime} ${duration} 5 12")
        currentTime += duration
        scoBuilder.appendLine("i \"playComplexFmSynth\" ${currentTime} ${duration} 10 12")
        currentTime += duration

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }

    fun setupBasicFilter() {
        val orcFile = File("./src/main/resources/practices/orc/csoundEmprise.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 16.0
        //Abbreviated notation
        //                                                                          kamp rise  dec
        scoBuilder.appendLine("i \"playImpulseTrain\" ${currentTime} ${duration} 0.8   0.5   0.7")
        currentTime += duration
        //                                                                                        kamp rise  dec
        scoBuilder.appendLine("i \"playSimpleFirLowPassFilter\" ${currentTime} ${duration} 0.8   0.5   0.7")
        currentTime += duration
        scoBuilder.appendLine("i \"playSimpleTwoTapFirLowPassFilter\" ${currentTime} ${duration} 0.8   0.5   0.7")
        currentTime += duration
        scoBuilder.appendLine("i \"playSimpleIirLowPassFilter\" ${currentTime} ${duration} 0.8   0.5   0.7")
        currentTime += duration
        scoBuilder.appendLine("i \"playSimpleIirHighPassFilter\" ${currentTime} ${duration} 0.8   0.5   0.7")
        currentTime += duration
        scoBuilder.appendLine("i \"playCombFilter\" ${currentTime} ${duration} 0.8   0.5   0.7")
        currentTime += duration


        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }

    fun setupFilterOpcodes() {
        val orcFile = File("./src/main/resources/practices/orc/csoundEmprise.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 10.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"whiteNoise\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"butterhpInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"butterlpInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"butterbpInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"resonInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"aresonInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"toneInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"atoneInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"noBalanceInstr\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"balanceInstr\" ${currentTime} ${duration}")
        currentTime += duration
        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }
}

fun main() {
    /*var csoundIntro = CsoundIntro(true, "../out/wav/empriseOther.wav")
    csoundIntro.setupEmpriseOther()
    csoundIntro.run()*/

    /*var csoundIntro = CsoundIntro(false, "../out/wav/interopAndRandom.wav")
    csoundIntro.setupInteropAndRandom()
    csoundIntro.run()*/

    /*var csoundIntro = CsoundIntro(false, "../out/wav/infiniteGlissando.wav")
    csoundIntro.setupInfiniteGlissando()
    csoundIntro.run()*/

    /*var csoundIntro = CsoundIntro(false, "../out/wav/basicModulations.wav")
    csoundIntro.setupModulations()
    csoundIntro.run()*/
    /*var csoundIntro = CsoundIntro(false, "../out/wav/FmModulationsApp.wav")
    csoundIntro.setupFmModulApp()
    csoundIntro.run()*/
    /*var csoundIntro = CsoundIntro(false, "../out/wav/BasicFilter.wav")
    csoundIntro.setupBasicFilter()
    csoundIntro.run()*/
    var csoundIntro = CsoundIntro(false, "../out/wav/FilterOpcodes.wav")
    csoundIntro.setupFilterOpcodes()
    csoundIntro.run()
}