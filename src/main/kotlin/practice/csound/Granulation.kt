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

    fun setupGranulationFile() {
        val orcFile = File("./src/main/resources/practices/orc/granulation.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 10.0
        //Abbreviated notation
        scoBuilder.appendLine("i \"nightingale\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"granulationFileSynth\" ${currentTime} ${duration}")
        currentTime += duration
        scoBuilder.appendLine("i \"granuDynamicSpeed\" ${currentTime} ${duration} 2")
        currentTime += duration
        duration = duration / 2
        scoBuilder.appendLine("i \"granuDynamicSpeed\" ${currentTime} ${duration} 4")
        currentTime += duration
        duration = duration / 2
        scoBuilder.appendLine("i \"granuDynamicSpeed\" ${currentTime} ${duration} 8")
        currentTime += duration
        scoBuilder.appendLine("e")

        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")
        c.readSco(sco)
    }

    fun setupCsoundGranul() {
        val orcFile = File("./src/main/resources/practices/orc/granulation.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        var currentTime = 0.0
        var duration = 10.0
        scoBuilder.appendLine(
            //                                                    amplitude  ivoice  iratio
            "i \"csoundGranul\" ${currentTime} ${duration}  0.5        12      0.25 " +
                    //imode  ithd  ipshift  igskip  igskip_os  ilength  kgap  igap_os  kgsize
                    " 0      0     4        0       0          1.3     0.01  50       0.005" +
                    //igsize_os  iatt  idec  iseed  ipitch1  ipitch2  ipitch3  ipitch4
                    " 30         20    20    0.39   0.5      1        1.25     1.5"
        )
        currentTime += 5
        scoBuilder.appendLine(
            //                                                    amplitude  ivoice  iratio
            "i \"csoundGranul\" ${currentTime} ${duration}  0.5        12      0.5 " +
                    //imode  ithd  ipshift  igskip  igskip_os  ilength  kgap  igap_os  kgsize
                    " 0      0     4        0       0          1.3     0.01  50       0.005" +
                    //igsize_os  iatt  idec  iseed  ipitch1  ipitch2  ipitch3  ipitch4
                    " 30         20    20    0.39   0.5      1        1.25     1.5"
        )
        currentTime += 5
        scoBuilder.appendLine(
            //                                                    amplitude  ivoice  iratio
            "i \"csoundGranul\" ${currentTime} ${duration}  0.5        12      1.0 " +
                    //imode  ithd  ipshift  igskip  igskip_os  ilength  kgap  igap_os  kgsize
                    " 0      0     4        0       0          1.3     0.01  50       0.005" +
                    //igsize_os  iatt  idec  iseed  ipitch1  ipitch2  ipitch3  ipitch4
                    " 30         20    20    0.39   0.5      1        1.25     1.5"
        )
        currentTime += duration
        scoBuilder.appendLine(
            "i \"fogInstr\" ${currentTime} ${duration} 0.7 0.8"
        )
        currentTime += 5.0
        scoBuilder.appendLine(
            "i \"fogInstr\" ${currentTime} ${duration} 0.5 0.8"
        )
        currentTime += 5.0
        scoBuilder.appendLine(
            "i \"fogInstr\" ${currentTime} ${duration} 0.2 0.8"
        )
        currentTime += 5.0
        scoBuilder.appendLine(
            "i \"fogInstr\" ${currentTime} ${duration} 0.5 0.5"
        )
        currentTime += 5.0
        scoBuilder.appendLine(
            "i \"fogInstr\" ${currentTime} ${duration} 0.5 0.2"
        )


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
    granulation = Granulation(false, "../out/wav/granulationFile.wav")
    granulation.setupGranulationFile()
    granulation.run()
    granulation = Granulation(false, "../out/wav/granulationOpcodes.wav")
    granulation.setupCsoundGranul()
    granulation.run()
}