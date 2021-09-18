package practice.minim

import common.RecorderablePAppletMinim
import ddf.minim.signals.SineWave

class VariableAmplitudeMovie : RecorderablePAppletMinim() {
    private lateinit var sine: SineWave
    private var currentFreqIdx: Int = 0
    private val currentC = 261.6F
    private var amplitude = 0.1F
    private var finCount = 0

    override fun setupBody() {
        val sampleRate = out.sampleRate()
        sine = SineWave(currentC, amplitude, sampleRate)
        sine.portamento(100)
        out.addSignal(sine)
        currentFreqIdx += 1
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
                (getScreenHeight() / 2 + out.right[i] * getScreenHeight() / 2.5).toFloat()
            )
        }
        endShape()
        if ((frameCount % getFps().toInt() == 0).and(amplitude < 1F)) {
            amplitude += 0.025F
            sine.setAmp(amplitude)
            println("amplitude: ${amplitude}")
        }
        if (1F <= amplitude) {
            if (finCount == getFps().toInt() * 5) {
                exit()
            }
            finCount += 1
        }
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    override fun isRecord(): Boolean = true

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    VariableAmplitudeMovie().run()
}