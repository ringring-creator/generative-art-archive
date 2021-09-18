package practice.minim

import common.RecorderablePAppletMinim
import ddf.minim.ugens.FilePlayer
import java.io.File

class MidiSynthEffectsWaveformMovie : RecorderablePAppletMinim() {
    private lateinit var player: FilePlayer

    override fun setupBody() {
        val file = File("../out/wav/MidiSynthEffects.wav")
        if (file.exists().not()) {
            println("not found")
        }
        println("file.absolutePath: ${file.absolutePath}")
        player = FilePlayer(minim.loadFileStream(file.absolutePath, out.bufferSize(), true))
        player.patch(out)
        strokeWeight(4F)
    }

    override fun drawBody() {
        background(255f, 255f, 255f)
        stroke(0f, 0f, 0f)
        noFill()
        beginShape()
        stroke(0f, 0f, 0f)
        for (i in 0 until out.bufferSize()) {
            vertex(
                i.toFloat() / out.bufferSize() * getScreenWidth(),
                (getScreenHeight() / 2 + out.right[i] * getScreenHeight()).toFloat()
            )
        }
        endShape()
        if (!player.isPlaying) {
            exit()
        }
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    override fun isRecord(): Boolean = true

    //override fun isRecordOnly(): Boolean = true

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    MidiSynthEffectsWaveformMovie().run()
}