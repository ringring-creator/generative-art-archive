package practice.csound

import java.io.File
import csnd6.csnd6;
import csnd6.Csound;


class MidiPercussionNotes {
    private val c: Csound

    init {
        csnd6.csoundInitialize(
            csnd6.CSOUNDINIT_NO_ATEXIT or csnd6.CSOUNDINIT_NO_SIGNAL_HANDLER
        )
        // Create an instance of the Csound object
        c = Csound()
        c.SetOption("-n")
        c.SetOption("--midioutfile=../out/midi/MidiPercussionNotes.mid")


        val orcFile = File("./src/main/resources/practices/orc/midiSingleChnl.orc")

        val orc = orcFile.readText()
        println("orc: ")
        println("${orc}")
        c.CompileOrc(orc)

        val duration = 1//sec
        val scoBuilder = StringBuilder()
        val noteNumbers = (35..81).toList()
        for ((idx, value) in noteNumbers.withIndex()) {
            scoBuilder.appendLine("i1 ${duration * idx.toDouble()} ${duration} 10 1 ${value} 127")
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
    MidiPercussionNotes().run()
}