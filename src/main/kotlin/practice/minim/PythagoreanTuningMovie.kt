package practice.minim

import common.RecorderablePAppletMinim
import ddf.minim.signals.SineWave


class PythagoreanTuningMovie : RecorderablePAppletMinim() {
    private lateinit var sine: SineWave
    private var currentFreqIdx: Int = 0
    private var mag: Int = 1
    private val C = 261.6F
    private val CDEFGAHC = arrayListOf<Float>(
        C,
        C * pow(3F, 2F) / pow(2F, 3F),
        C * pow(3F, 4F) / pow(2F, 6F),
        C * pow(2F, 2F) / pow(3F, 1F),
        C * pow(3F, 1F) / pow(2F, 1F),
        C * pow(3F, 3F) / pow(2F, 4F),
        C * pow(3F, 3F) / pow(2F, 4F),
        C * pow(3F, 5F) / pow(2F, 7F),
        C * 2
    )
    private var finCount = 0

    override fun setupBody() {
        val sampleRate = out.sampleRate()
        sine = SineWave(CDEFGAHC[currentFreqIdx], 0.5F, sampleRate)
        sine.portamento(100)
        out.addSignal(sine)
        currentFreqIdx += 1
        for (c2c in CDEFGAHC) {
            println("${c2c}")
        }
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
        if ((frameCount % getFps().toInt() == 0).and(currentFreqIdx != CDEFGAHC.size)) {
            sine.setFreq(CDEFGAHC[currentFreqIdx] * mag)
            println("CDEFGAHC[currentFreqIdx]: ${CDEFGAHC[currentFreqIdx]}")
            currentFreqIdx += 1
        }
        if ((currentFreqIdx == CDEFGAHC.size).and(mag < 3)) {
            mag += 1
            currentFreqIdx = 0
        }
        if (3 <= mag) {
            if (finCount == getFps().toInt() * 3) {
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
    PythagoreanTuningMovie().run()
}