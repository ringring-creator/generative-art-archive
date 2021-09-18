package practice.csound

import csnd6.csnd6;
import csnd6.Csound;
import java.io.File

class Pluck() {
    private val c: Csound

    init {
        csnd6.csoundInitialize(
            csnd6.CSOUNDINIT_NO_ATEXIT or csnd6.CSOUNDINIT_NO_SIGNAL_HANDLER
        )
        // Create an instance of the Csound object
        c = Csound()
        //c.SetOption("-odac")
        c.SetOption("-o../out/wav/Pluck.wav")

        val orcFile = File("./src/main/resources/orc/pluck.orc")

        val orc = orcFile.readText()
        println("orc: ")
        println("${orc}")
        c.CompileOrc(orc)

        val scoBuilder = StringBuilder()
        val duration = 2//sec
        var currentTime = 0.0
        for (imeth in 1..6) {
            for (idx in 0..7) {
                val freq = noteToFreq(60.0 + idx)
                scoBuilder.appendLine("i${imeth} ${currentTime} ${duration} ${freq}")
                currentTime += duration
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

        c.Stop()
        c.Cleanup()
    }
}

fun main() {
    Pluck().run()
}