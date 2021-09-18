package practice.minim

import common.RecorderablePAppletMinim
import ddf.minim.signals.SineWave

class EqualTemperamentMovie : RecorderablePAppletMinim() {
    private var finCount: Int = 0
    private lateinit var sine: SineWave
    private var currentC = 200F
    private var currentE: Float = 0F

    override fun setupBody() {
        val sampleRate = out.sampleRate()
        sine = SineWave(calcFreq(), 0.5F, sampleRate)
        updateParam()
        sine.portamento(100)
        out.addSignal(sine)
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

    private fun calcFreq(): Float {
        val result = currentC * pow(2F, currentE / 12F)
        println("freq: ${result}")
        return result
    }

    private fun updateParam() {
        if (currentE == 12F) {
            currentC = calcFreq()
            currentE = 0F
        } else {
            currentE += 1F
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
    EqualTemperamentMovie().run()
}