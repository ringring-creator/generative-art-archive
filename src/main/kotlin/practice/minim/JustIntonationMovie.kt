package practice.minim

import common.RecorderablePAppletMinim
import ddf.minim.signals.SineWave

class JustIntonationMovie : RecorderablePAppletMinim() {
    private lateinit var sine: SineWave
    private var currentFreqIdx: Int = 0
    private var currentC = 200F
    private var currentIdx = 0
    private val JUST_INTONATION = arrayListOf<Float>(
        1F,
        17F / 16F,
        9F / 8F,
        19F / 16F,
        5F / 4F,
        4F / 3F,
        17F / 12F,
        3F / 2F,
        8F / 5F,
        5F / 3F,
        16F / 9F,
        19F / 10F,
        2F
    )
    private var finCount = 0

    override fun setupBody() {
        val sampleRate = out.sampleRate()
        sine = SineWave(calcFreq(), 0.5F, sampleRate)
        updateParam()
        sine.portamento(100)
        out.addSignal(sine)
        currentFreqIdx += 1
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
        if ((frameCount % getFps().toInt() == 0).and(calcFreq() <= 1200F)) {
            sine.setFreq(calcFreq())
            updateParam()
        }
        if (1200 < calcFreq()) {
            if (finCount == getFps().toInt() * 5) {
                exit()
            }
            finCount += 1
        }
    }

    override fun exitBody() {
        println("exitBody")
    }

    private fun calcFreq(): Float {
        var result = currentC * JUST_INTONATION.get(currentIdx)
        println("freq: ${result}")
        return result
    }

    private fun updateParam() {
        if (currentIdx == (JUST_INTONATION.size - 1)) {
            currentC = calcFreq()
            currentIdx = 0
        } else {
            currentIdx += 1
        }
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    override fun isRecord(): Boolean = true

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    JustIntonationMovie().run()
}