package practice.csound

import csnd6.csnd6;
import csnd6.Csound;
import java.io.File

class Chord() {
    private val c: Csound
    private val midiDiff = arrayListOf(2, 2, 1, 2, 2, 2, 1)

    init {
        csnd6.csoundInitialize(
            csnd6.CSOUNDINIT_NO_ATEXIT or csnd6.CSOUNDINIT_NO_SIGNAL_HANDLER
        )
        // Create an instance of the Csound object
        c = Csound()
        //c.SetOption("-odac")
        c.SetOption("-o../out/wav/Chord.wav")

        val orcFile = File("./src/main/resources/practices/orc/Chord.orc")

        val orc = orcFile.readText()
        println("orc: ")
        println("${orc}")
        c.CompileOrc(orc)

        val scoBuilder = StringBuilder()
        scoBuilder.appendLine("f1 0 16384 10 1")
        var currentTime = 0.0
        val duration = 2.5//sec
        val startNote = 60.0
        var currentNote = startNote
        //Triad
        for (idx in 0..6) {
            val secondNote = currentNote + midiDiff.get(idx % 7) + midiDiff.get((idx + 1) % 7)
            val thirdNote = secondNote + midiDiff.get((idx + 2) % 7) + midiDiff.get((idx + 3) % 7)
            println("firstNote: ${currentNote}, secondNote: ${secondNote}, thirdNote: ${thirdNote}")
            val freq = noteToFreq(currentNote)
            val freq2 = noteToFreq(secondNote)
            val freq3 = noteToFreq(thirdNote)
            scoBuilder.appendLine("i1 ${duration * idx.toDouble()} ${duration} ${freq} ${freq2} ${freq3}")
            currentNote += midiDiff.get(idx)
        }
        //Four notes chord
        currentTime += duration * 7
        currentNote = startNote
        for (idx in 0..6) {
            val freq = noteToFreq(currentNote)
            val secondNote = currentNote + midiDiff.get(idx % 7) + midiDiff.get((idx + 1) % 7)
            val thirdNote = secondNote + midiDiff.get((idx + 2) % 7) + midiDiff.get((idx + 3) % 7)
            val fourthNote = thirdNote + midiDiff.get((idx + 4) % 7) + midiDiff.get((idx + 5) % 7)
            println("firstNote: ${currentNote}, secondNote: ${secondNote}, thirdNote: ${thirdNote}, fourthNote: ${fourthNote}")
            val freq2 = noteToFreq(secondNote)
            val freq3 = noteToFreq(thirdNote)
            val freq4 = noteToFreq(fourthNote)
            scoBuilder.appendLine("i2 ${currentTime + duration * idx} ${duration} ${freq} ${freq2} ${freq3} ${freq4}")
            currentNote += midiDiff.get(idx)
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
        //Post-processing
        c.Stop()
        c.Cleanup()
    }
}

fun main() {
    Chord().run()
}