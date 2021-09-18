package practice.minim

import common.RecorderablePAppletMinim
import ddf.minim.signals.*

class VariableWaveformMovie : RecorderablePAppletMinim() {
    private lateinit var wave: Oscillator
    private val currentC = 150F
    private var currentE: Float = 0F
    private var type = 0
    private var finCount = 0
    private var sampleRate = 0F

    private val amp = 0.1F

    override fun setupBody() {
        sampleRate = out.sampleRate()
        wave = PulseWave(currentC, amp, sampleRate)
        wave.portamento(100)
        out.addSignal(wave)
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
        if (frameCount % getFps().toInt() == 0) {
            wave.setFreq(calcFreq())
            updateParam()
        }
        if (type == 5) {
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
            currentE = 0F
            type += 1
            updateWave(type)
        } else {
            currentE += 1F
        }
    }

    private fun updateWave(type: Int) {
        when (type) {
            1 -> sawWave()
            2 -> sineWave()
            3 -> squareWave()
            4 -> triangleWave()
        }
    }

    private fun triangleWave() {
        out.removeSignal(wave)
        wave = TriangleWave(currentC, amp, sampleRate)
        out.addSignal(wave)
    }

    private fun sineWave() {
        out.removeSignal(wave)
        wave = SineWave(currentC, amp, sampleRate)
        out.addSignal(wave)
    }

    private fun sawWave() {
        out.removeSignal(wave)
        wave = SawWave(currentC, amp, sampleRate)
        out.addSignal(wave)
    }

    private fun squareWave() {
        out.removeSignal(wave)
        wave = SquareWave(currentC, amp, sampleRate)
        out.addSignal(wave)
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    override fun isRecord(): Boolean = false

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    VariableWaveformMovie().run()
}