package practice.csound

import java.io.File
import csnd6.csnd6;
import csnd6.Csound;


class MidiEnsembles {
    private val c: Csound

    init {
        csnd6.csoundInitialize(
            csnd6.CSOUNDINIT_NO_ATEXIT or csnd6.CSOUNDINIT_NO_SIGNAL_HANDLER
        )
        // Create an instance of the Csound object
        c = Csound()
        c.SetOption("-n")
        c.SetOption("--midioutfile=../out/midi/MidiEnsembles.mid")


        val orcFile = File("./src/main/resources/practices/orc/midiSingleChnl.orc")

        val orc = orcFile.readText()
        println("orc: ")
        println("${orc}")
        c.CompileOrc(orc)

        val duration = 1//sec
        val scoBuilder = StringBuilder()
        val noteNumbers = arrayListOf(60, 62, 64, 65, 67, 69, 71, 72)
        var programs = (48..55).toList()
        for ((pIdx, program) in programs.withIndex()) {
            for ((idx, value) in noteNumbers.withIndex()) {
                scoBuilder.appendLine("i1 ${duration * noteNumbers.size * pIdx + duration * idx.toDouble()} ${duration} 1 ${program} ${value} 127")
            }
        }
        scoBuilder.appendLine("e")
        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.ReadScore(sco)
    }

    fun run() {
        // When compiling from strings, this call is necessary before doing
        // any performing
        c.Start()
        // This call runs Csound to completion
        c.Perform()
        //Post-processing
        c.Stop()
        c.Cleanup()
    }
}

fun main() {
    MidiEnsembles().run()
}