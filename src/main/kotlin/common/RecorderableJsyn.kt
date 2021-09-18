package common

import com.jsyn.JSyn
import com.jsyn.devices.AudioDeviceManager
import com.jsyn.engine.SynthesisEngine
import com.jsyn.unitgen.LineOut
import com.jsyn.unitgen.PassThrough
import com.jsyn.util.AudioStreamReader
import com.jsyn.util.WaveRecorder
import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.FrameRecorder
import org.bytedeco.javacv.OpenCVFrameConverter
import processing.core.PApplet
import toBGRAMat
import java.io.File
import java.nio.FloatBuffer

abstract class RecorderableJsyn() : PApplet() {
    private lateinit var audioVisReader: AudioStreamReader
    private lateinit var waveRecorder: WaveRecorder
    private var startTime: Long
    private var bgColor: Int = 0

    val synth: SynthesisEngine = JSyn.createSynthesizer() as SynthesisEngine
    val lineOut = LineOut()
    val finalLeft = PassThrough()
    val finalRight = PassThrough()
    private val sampleRate = 48000 * 2
    var visBuffer = DoubleArray((sampleRate / getFps()).toInt())

    val audioDuration = 1.0 / getFps()

    init {
        startTime = System.currentTimeMillis()
    }


    override fun settings() {
        //set screen size
        size(getScreenWidth(), getScreenHeight())
        synth.isRealTime = isRecord().not()
        if (isRecord()) {
        } else {
            synth.add(lineOut)
            finalLeft.getOutput().connect(0, lineOut.input, 0)
            finalRight.getOutput().connect(0, lineOut.input, 1)
            audioVisReader = AudioStreamReader(synth, getChannels())
            finalLeft.getOutput().connect(0, audioVisReader.input, 0)
            finalRight.getOutput().connect(0, audioVisReader.input, 1)
        }
        synth.add(finalLeft)
        synth.add(finalRight)
    }

    abstract fun setupBody()

    override fun setup() {
        setupBody()
        val recordFps: Float = if (!isRecord()) Float.MAX_VALUE else getFps().toFloat()
        println("isRecordOnly: ${isRecord()}, recordFps: ${recordFps}")
        frameRate(recordFps)
        synth.start(
            sampleRate,
            AudioDeviceManager.USE_DEFAULT_DEVICE,
            getChannels(),
            AudioDeviceManager.USE_DEFAULT_DEVICE,
            getChannels()
        )
        finalLeft.start()
        finalRight.start()
        if (isRecord()) {
            val waveFile = File(getRecordFilePath())
            waveRecorder = WaveRecorder(synth, waveFile, getChannels(), 16)
            finalLeft.getOutput().connect(0, waveRecorder.input, 0)
            finalRight.getOutput().connect(0, waveRecorder.input, 1)
            waveRecorder.start()
            surface.setVisible(false)
        } else {
            lineOut.start()
        }

        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
    }

    abstract fun drawBody()

    override fun draw() {
        if (frameCount % 100 == 1) {
            println("frameCount: ${frameCount}")
        }
        drawBody()
        if (!isRecord()) {
            background(bgColor)
            stroke(0f, 0f, 0f)
            noFill()
            beginShape()
            audioVisReader.read(visBuffer)
            visBuffer.forEachIndexed { idx, value ->
                if (idx % 2 == 0) {
                    val x = idx.toFloat() / visBuffer.size * getScreenWidth()
                    val y = (getScreenHeight() / 2 + value * getScreenHeight() / 2.5).toFloat()
                    println("x: ${x}, y: ${y}")
                    println("idx: ${idx / 2}, value: ${value}")
                    vertex(x, y)
                }
            }
            endShape()
        }
        if (exitCalled().not().and(isRecord())) {
            val nextTime = synth.currentTime + audioDuration
            println("duration: ${audioDuration}, nextTime: ${nextTime}")
            try {
                synth.sleepFor(audioDuration)
                println("done synth")
            } catch (e: InterruptedException) {
                println("synth.sleepFor's Exception: ${e}")
            }
        }
    }

    abstract fun exitBody()

    override fun exit() {
        exitBody()
        finalLeft.stop()
        synth.stop()
        if (isRecord()) {
            waveRecorder.stop()
            waveRecorder.close()
        } else {
            lineOut.stop()
            audioVisReader.close()
        }
        val endTime = System.currentTimeMillis()
        println("Running time is ${endTime - startTime} ms")
        super.exit()
    }

    // I want to set in constructor, but PApplet run in static.
    // So I defined overridable function.
    open fun getScreenWidth(): Int = 1920

    open fun getScreenHeight(): Int = 1080

    open fun getFps(): Double = 60.0

    open fun getRecordFilePath(): String = "./out.wav"

    open fun getChannels(): Int = 2

    open fun isRecord(): Boolean = true
}