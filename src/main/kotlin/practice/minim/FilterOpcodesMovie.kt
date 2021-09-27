package practice.minim

import common.RecorderablePAppletMinim
import ddf.minim.analysis.FFT
import ddf.minim.ugens.FilePlayer
import java.io.File

class FilterOpcodesMovie : RecorderablePAppletMinim() {
    private var bgColor: Int = 0
    private lateinit var fft: FFT
    private lateinit var filePlayer: FilePlayer
    private var type = 0

    private val wavFilePath = "../out/wav/FilterOpcodes.wav"

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        val filename = wavFilePath
        val file = File(filename)
        if (file.exists().not()) {
            println("not found")
        }
        filePlayer = FilePlayer(minim.loadFileStream(filename, out.bufferSize(), false))
        filePlayer.patch(out)
        if (isRecord()) {
            fft = FFT(512, getSampleRate().toFloat())
        } else {
            fft = FFT(out.bufferSize(), getSampleRate().toFloat())
        }

        println("fft.specSize(): ${fft.specSize()}")
    }

    override fun drawBody() {
        if (!filePlayer.isPlaying) {
            if (type == 0) {
                val filename = wavFilePath
                filePlayer = FilePlayer(minim.loadFileStream(filename, out.bufferSize(), false))
                filePlayer.patch(out)
            }
            if (type == 1) {
                exit()
            }
            type += 1
        }
        if (type == 0) {
            showSpectrum()
        } else {
            showWave()
        }
    }

    private fun showSpectrum() {
        background(bgColor)
        strokeWeight(8F)
        noFill()
        beginShape()
        if (isRecord()) {
            fft.forward(out.mix, out.mix.size() - 512)
        }
        for (idx in 0 until fft.specSize()) {
            val x = idx.toFloat() / fft.specSize().toFloat() * getScreenWidth().toFloat()
            stroke(fft.getBand(idx) * 8, 50F, 100F, 100F)
            line(
                x,
                getScreenHeight().toFloat(),
                x,
                getScreenHeight().toFloat() - fft.getBand(idx) * 16
            )
        }
    }

    private fun showWave() {
        background(bgColor)
        stroke(0f, 0f, 0f)
        strokeWeight(1F)
        noFill()
        beginShape()
        stroke(0f, 0f, 0f)
        for (i in 0 until out.bufferSize()) {
            vertex(
                i.toFloat() / out.bufferSize() * getScreenWidth(),
                //out.right[i] * getScreenHeight()
                (getScreenHeight() / 2 + out.right[i] * getScreenHeight())
            )
        }
        endShape()
    }

    override fun exitBody() {
        println("exitBody")
        filePlayer.close()
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    override fun isRecord(): Boolean = true

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    FilterOpcodesMovie().run()
}