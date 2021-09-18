package practice.csound

import java.io.File
import csnd6.csnd6;
import csnd6.Csound;


class Midi2Wav(val midiFilepath: String) {
    private val c: Csound

    init {
        csnd6.csoundInitialize(
            csnd6.CSOUNDINIT_NO_ATEXIT or csnd6.CSOUNDINIT_NO_SIGNAL_HANDLER
        )
        // Create an instance of the Csound object
        c = Csound()
        c.SetOption("-F${midiFilepath}")
        //c.SetOption("-odac")
        c.SetOption("-o${midiFilepath}.wav")

        val orcFile = File("./src/main/resources/orc/midi2wav.orc")

        val orc = orcFile.readText()
        println("orc: ")
        println("${orc}")
        c.CompileOrc(orc)

        val scoBuilder = StringBuilder()
        scoBuilder.appendLine("i99 0 60")
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
    var midiFilepath = "../out/midi/Organs.mid"
    Midi2Wav(midiFilepath).run()
    midiFilepath = "../out/midi/Guitars.mid"
    Midi2Wav(midiFilepath).run()
}