package practice.csound

import java.io.File

class SoundEffects(outDac: Boolean, outputFilePath: String) {
    private val c: CsoundSystem

    init {
        // Create an instance of the Csound object
        c = CsoundSystem(outDac, outputFilePath)
    }

    fun setupBassDrum() {
        val orcFile = File("./src/main/resources/orc/midiSingleChnl.orc")
        val orc = orcFile.readText()
        c.compileOrc(orc)
        val scoBuilder = StringBuilder()
        scoBuilder.appendLine("i1 0 0.2 10 1 35 127")
        scoBuilder.appendLine("e")
        val sco = scoBuilder.toString()
        println("sco: ")
        println("${sco}")

        c.readSco(sco)
    }

    fun setupSoundEffects() {
        val orcFile = File("./src/main/resources/orc/soundEffects.orc")

        val orc = orcFile.readText()
        println("orc: ")
        println("${orc}")
        c.compileOrc(orc)
        //Decision sound
        var currentTime = 0.0
        val scoBuilder = StringBuilder()
        /*
        currentTime += 1

        scoBuilder.appendLine("f1 0 32768 10 1") // Sine curve
        val duration = 0.1
        val decBaseFreq = 523.3
        val decNextFreq = 698.5
        val amp = 0.1
        scoBuilder.appendLine("i1 ${currentTime} ${duration} ${amp} ${decBaseFreq} 1")
        scoBuilder.appendLine("i1 ${currentTime} ${duration} ${amp} ${decBaseFreq / 2} 1")
        scoBuilder.appendLine("i1 ${currentTime} ${duration} ${amp} ${decBaseFreq * 2} 1")
        currentTime += duration
        scoBuilder.appendLine("i1 ${currentTime} ${duration} ${amp} ${decNextFreq} 1")
        scoBuilder.appendLine("i1 ${currentTime} ${duration} ${amp} ${decNextFreq / 2} 1")
        scoBuilder.appendLine("i1 ${currentTime} ${duration} ${amp} ${decNextFreq * 2} 1")
        currentTime += duration
        scoBuilder.appendLine("i3 0 ${currentTime} 0.25 0.01")

        currentTime += 1
        var startTime = currentTime
        scoBuilder.appendLine("i2 ${currentTime} ${duration} ${amp} ${decBaseFreq} 3 1")
        scoBuilder.appendLine("i2 ${currentTime} ${duration} ${amp} ${decBaseFreq / 2} 3 1")
        scoBuilder.appendLine("i2 ${currentTime} ${duration} ${amp} ${decBaseFreq * 2} 3 1")
        currentTime += duration
        scoBuilder.appendLine("i2 ${currentTime} ${duration} ${amp} ${decNextFreq} 3 1")
        scoBuilder.appendLine("i2 ${currentTime} ${duration} ${amp} ${decNextFreq / 2} 3 1")
        scoBuilder.appendLine("i2 ${currentTime} ${duration} ${amp} ${decNextFreq * 2} 3 1")
        currentTime += duration
        scoBuilder.appendLine("i3 ${startTime} ${currentTime - startTime} 0.25 0.01")
        currentTime += 1
        //error sound
        val errBaseFreq = 164.81
        val errNextFreq = 174.61
        startTime = currentTime
        scoBuilder.appendLine("i2 ${currentTime} 0.2 ${amp} ${errBaseFreq} 1 1")
        currentTime += 0.2
        scoBuilder.appendLine("i2 ${currentTime} 1.0 ${amp} ${errNextFreq} 1 1")
        currentTime += 1.0
        scoBuilder.appendLine("i3 ${startTime} 0.5 2.0 0.1")
        currentTime += 1.0
        //rain
        scoBuilder.appendLine("i4 ${currentTime} 5 0.5 0.5")
        currentTime += 5.0
        scoBuilder.appendLine("i5 ${currentTime} 5 0.3 0.5")
        currentTime += 6.0
        //Snowstorm
        scoBuilder.appendLine("i6 ${currentTime} 10 0.05 2000")
        scoBuilder.appendLine("i6 ${currentTime} 10 0.75 1500")
        scoBuilder.appendLine("i6 ${currentTime} 10 0.1 500")
        currentTime += 11
        //Heartbeat
        for (idx in 0..10) {
            scoBuilder.appendLine("i7 ${currentTime} 1.1")
            currentTime += 1.1
        }*/
        /* too hard
        scoBuilder.appendLine("f80 0 0 1 \"../out/wav/BassDrum.wav\" 0 0 0")
        val warnDuration = 0.2
        for (idx in 0..3) {
            //scoBuilder.appendLine("i8 ${currentTime} ${warnDuration}")
            //currentTime += warnDuration
            scoBuilder.appendLine("i9 ${currentTime} ${warnDuration}")
            currentTime += warnDuration
        }*/
        scoBuilder.appendLine("f110 0 32768 10 1") // Sine curve
        repeat(10) {
            scoBuilder.appendLine("i10 ${currentTime} 0.1 1 0.3")
            currentTime += 0.2
        }
        currentTime += 2
        repeat(10) {
            scoBuilder.appendLine("i10 ${currentTime} 0.1 1 0.5")
            currentTime += 0.2
        }

        currentTime += 2
        repeat(10) {
            scoBuilder.appendLine("i10 ${currentTime} 0.1 1 0.7")
            currentTime += 0.2
        }

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
    val soundEffects = SoundEffects(true, "../out/wav/SoundEffects.wav")
    soundEffects.setupSoundEffects()
    //val soundEffects = SoundEffects(false, "../out/midi/BassDrum.mid")
    //soundEffects.setupBassDrum()
    soundEffects.run()
}