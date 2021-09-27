package practice.csound

import csnd6.csnd6;
import csnd6.Csound;
import java.io.File

class AddOvertones() {
    private val c: Csound

    init {
        csnd6.csoundInitialize(
            csnd6.CSOUNDINIT_NO_ATEXIT or csnd6.CSOUNDINIT_NO_SIGNAL_HANDLER
        )
        // Create an instance of the Csound object
        c = Csound()
        //c.SetOption("-odac")
        c.SetOption("-o../out/wav/AddOvertones.wav")

        val orcFile = File("./src/main/resources/practices/orc/Chord.orc")

        val orc = orcFile.readText()
        println("orc: ")
        println("${orc}")
        c.CompileOrc(orc)

        val scoBuilder = StringBuilder()
        scoBuilder.appendLine("f1 0 16384 10 1")
        var currentTime = 0.0
        val duration = 2//sec
        for (idx in 0..7) {
            val freq = noteToFreq(60.0 + idx)
            scoBuilder.appendLine("i1 ${duration * idx.toDouble()} ${duration} ${freq} ${freq * 2} ${freq * 4}")
        }
        currentTime += duration * 8
        for (idx in 0..7) {
            val freq = noteToFreq(60.0 + idx)
            scoBuilder.appendLine("i1 ${currentTime + duration * idx} ${duration} ${freq} ${freq * 3} ${freq * 5}")
        }
        currentTime += duration * 8
        for (idx in 0..7) {
            val freq = noteToFreq(60.0 + idx)
            scoBuilder.appendLine("i1 ${currentTime + duration * idx} ${duration} ${freq} ${freq * 2} ${freq * 3}")
        }

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

        c.Stop()
        c.Cleanup()
    }
}

fun main() {
    AddOvertones().run()
}