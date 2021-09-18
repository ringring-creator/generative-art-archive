package practice.minim

import common.RecorderablePAppletMinim
import ddf.minim.AudioPlayer
import ddf.minim.AudioSample
import ddf.minim.ugens.FilePlayer
import java.io.File

class VisualizeCsoundWavMovie : RecorderablePAppletMinim() {
    private lateinit var filePlayer2: FilePlayer
    private lateinit var filePlayer3: FilePlayer
    private var finCount = 0

    override fun setupBody() {
        val file = File("data/wav/wtude2.wav")
        if (file.exists().not()) {
            println("not found")
        }
        println("file.absolutePath: ${file.absolutePath}")
        filePlayer2 = FilePlayer(minim.loadFileStream("./data/wav/wtude2.wav", out.bufferSize(), true))
        filePlayer2.patch(out)
        filePlayer3 = FilePlayer(minim.loadFileStream("./data/wav/wtudeTest.wav", out.bufferSize(), true))
        filePlayer3.patch(out)
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
        if (frameCount == 1000) {
            exit()
        }
        //if (!player.isPlaying) {
        /*if (finCount == getFps().toInt() * 5) {
            exit()
        }
        finCount += 1*/

    }

    override fun exitBody() {
        println("exitBody")
        filePlayer2.close()
        filePlayer3.close()
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    override fun isRecord(): Boolean = false

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    VisualizeCsoundWavMovie().run()
}