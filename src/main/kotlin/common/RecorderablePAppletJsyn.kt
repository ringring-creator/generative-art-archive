package common

import com.jsyn.JSyn
import com.jsyn.devices.AudioDeviceManager
import com.jsyn.unitgen.LineOut
import com.jsyn.unitgen.PassThrough
import com.jsyn.util.AudioStreamReader
import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.FrameRecorder
import org.bytedeco.javacv.OpenCVFrameConverter
import processing.core.PApplet
import toBGRAMat
import java.nio.FloatBuffer

abstract class RecorderablePAppletJsyn() : PApplet() {
    private var currentTime = 0.0
    private var startTime: Long

    private lateinit var ffmpegRecorder: FFmpegFrameRecorder

    private val toMat: OpenCVFrameConverter.ToMat = OpenCVFrameConverter.ToMat()

    val synth = JSyn.createSynthesizer()
    val lineOut = LineOut()
    val finalLeft = PassThrough()
    val finalRight = PassThrough()

    lateinit var audioStreamReader: AudioStreamReader
    private val sampleRate = 48000 * 2
    //private val audioBitrate = 256000 * 2

    val playDuration = 6.0
    val bufSize = (this.sampleRate / getFps() * playDuration.toInt() * 2.5).toInt()
    val audioDuration = 1.0 * playDuration / getFps()

    val floatArray = FloatArray(bufSize)

    init {
        startTime = System.currentTimeMillis()
    }

    override fun settings() {
        //set screen size
        size(getScreenWidth(), getScreenHeight())
        synth.isRealTime = isRecord().not()
        if (isRecord()) {
            ffmpegRecorder =
                FrameRecorder.create(
                    "FFmpegFrameRecorder",
                    getRecordFilePath(),
                    getScreenWidth(),
                    getScreenHeight()
                ) as FFmpegFrameRecorder
            //ffmpegRecorder = FFmpegFrameRecorder(getRecordFilePath(), 2)
            ffmpegRecorder.frameRate = getFps()
            ffmpegRecorder.videoCodec = avcodec.AV_CODEC_ID_H264
            //ffmpegRecorder.format = "wav"
            //ffmpegRecorder.setAudioBitrate(audioBitrate)
            ffmpegRecorder.setSampleRate(this.sampleRate)
            ffmpegRecorder.setAudioChannels(getChannels())
            ffmpegRecorder.setAudioCodec(avcodec.AV_CODEC_ID_PCM_S16LE)
        } else {
            synth.add(lineOut)
            finalLeft.getOutput().connect(0, lineOut.input, 0)
            finalRight.getOutput().connect(0, lineOut.input, 1)
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
            this.sampleRate,
            AudioDeviceManager.USE_DEFAULT_DEVICE,
            getChannels(),
            AudioDeviceManager.USE_DEFAULT_DEVICE,
            getChannels()
        )
        finalLeft.start()
        finalRight.start()
        if (isRecord()) {
            audioStreamReader = AudioStreamReader(synth, getChannels())
            finalLeft.getOutput().connect(0, audioStreamReader.input, 0)
            finalRight.getOutput().connect(0, audioStreamReader.input, 1)
            println("audioStreamReader.available(): ${audioStreamReader.available()}")
            ffmpegRecorder.start()
            surface.setVisible(false)
        } else {
            lineOut.start()
        }
    }

    abstract fun drawBody()

    override fun draw() {
        if (frameCount % 100 == 1) {
            println("frameCount: ${frameCount}")
        }
        drawBody()
        if (exitCalled().not().and(isRecord())) {
            try {
                if (frameCount % playDuration.toInt() == 0) {
                    currentTime = currentTime + audioDuration
                    synth.sleepUntil(currentTime)
                    println("audioStreamReader.available() after sleep: ${audioStreamReader.available()}")
                }
                record()
            } catch (e: InterruptedException) {
                println("e: ${e.toString()}")
            }

        }
    }

    abstract fun exitBody()

    override fun exit() {
        exitBody()
        finalLeft.stop()
        synth.stop()
        if (isRecord()) {
            audioStreamReader.close()
            ffmpegRecorder.stop()
            ffmpegRecorder.release()
        } else {
            lineOut.stop()
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

    open fun getRecordFilePath(): String = "./out.mp4"

    open fun getChannels(): Int = 2

    open fun isRecord(): Boolean = true

    private fun record() {
        val mat = toBGRAMat(g.get())
        val result = toMat.convert(mat)
        ffmpegRecorder.record(result, avutil.AV_PIX_FMT_BGRA)

        var frameCount = 0
        while (0 < audioStreamReader.available()) {
            val value = audioStreamReader.read()
            if (frameCount < floatArray.size) {
                floatArray.set(frameCount, value.toFloat())
                frameCount += 1
            }
        }
        if (0 < frameCount) {
            ffmpegRecorder.recordSamples(
                this.sampleRate,
                getChannels(),
                FloatBuffer.wrap(floatArray, 0, frameCount - 1)
            )
        }
    }
}