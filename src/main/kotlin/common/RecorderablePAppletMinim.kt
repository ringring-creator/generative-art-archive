package common

import ddf.minim.AudioListener
import ddf.minim.AudioOutput
import ddf.minim.Minim
import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.FrameRecorder
import org.bytedeco.javacv.OpenCVFrameConverter
import processing.core.PApplet
import toBGRAMat
import java.nio.FloatBuffer
import java.util.*
import javax.sound.sampled.AudioFormat

abstract class RecorderablePAppletMinim() : PApplet() {
    val minim = Minim(this)
    lateinit var out: AudioOutput
    private lateinit var listener: AudioRecordListener
    private var startTime: Long
    private lateinit var ffmpegRecorder: FFmpegFrameRecorder
    private val toMat: OpenCVFrameConverter.ToMat = OpenCVFrameConverter.ToMat()
    private val sampleRate = 48000
    //private val audioBitrate = 192000
    private val audioBitrate = 384000
    private var audioCount = 1
    private val AUDIO_MAX_QUEUE_SIZE = 10
    private val audioQueue = LinkedList<FloatBuffer>()

    init {
        startTime = System.currentTimeMillis()
    }


    override fun settings() {
        //set screen size
        size(getScreenWidth(), getScreenHeight())
        if (isRecord()) {
            ffmpegRecorder =
                FrameRecorder.create(
                    "FFmpegFrameRecorder",
                    getRecordFilePath(),
                    getScreenWidth(),
                    getScreenHeight()
                ) as FFmpegFrameRecorder
            ffmpegRecorder.frameRate = getFps()
            ffmpegRecorder.videoCodec = avcodec.AV_CODEC_ID_H264
            ffmpegRecorder.setAudioBitrate(audioBitrate)
            ffmpegRecorder.setSampleRate(sampleRate)
            ffmpegRecorder.setAudioChannels(getChannels())
            ffmpegRecorder.audioQuality = 0.0
            ffmpegRecorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            val bufSize = (sampleRate / getFps()).toInt()
            println("bufSize: ${bufSize}")
            out = minim.getLineOut(getChannels(), bufSize, sampleRate.toFloat())
        } else {
            out = minim.getLineOut(getChannels(), 2048, sampleRate.toFloat())
        }
    }

    abstract fun setupBody()

    override fun setup() {
        setupBody()
        val recordFps: Float = if (isRecordOnly()) Float.MAX_VALUE else getFps().toFloat()
        println("isRecordOnly: ${isRecordOnly()}, recordFps: ${recordFps}")
        frameRate(recordFps)
        if (isRecord()) {
            ffmpegRecorder.start()
            listener = AudioRecordListener(this)
            out.addListener(listener)
        }
        if (isRecordOnly()) {
            surface.setVisible(false)
        }
    }

    abstract fun drawBody()

    override fun draw() {
        if (frameCount % 100 == 1) {
            println("frameCount: ${frameCount}")
        }
        drawBody()
        if (exitCalled().not().and(isRecord())) {
            record()
        }
    }

    abstract fun exitBody()

    override fun exit() {
        exitBody()
        if (isRecord()) {
            while (audioCount < frameCount) {
                Thread.sleep(0, 10)
            }
            ffmpegRecorder.stop()
            ffmpegRecorder.release()
        }
        val endTime = System.currentTimeMillis()
        println("Running time is ${endTime - startTime} ms")
        super.exit()
    }

    override fun stop() {
        if (isRecord()) {
            out.removeListener(listener)
        }
        out.close()
        minim.stop()
        super.stop()
    }

    fun getSampleRate() = sampleRate

    // I want to set in constructor, but PApplet run in static.
    // So I defined overridable function.
    open fun getScreenWidth(): Int = 1920

    open fun getScreenHeight(): Int = 1080

    open fun getFps(): Double = 60.0

    open fun getRecordFilePath(): String = "./out.mp4"

    open fun isRecordOnly(): Boolean = false

    open fun isRecord(): Boolean = true

    open fun getChannels(): Int = Minim.STEREO

    private fun pushAudio(ele: FloatBuffer): Boolean {
        if (audioQueue.size < AUDIO_MAX_QUEUE_SIZE) {
            audioQueue.push(ele)
            return true
        }
        return false
    }

    private fun popAudio(): FloatBuffer {
        return audioQueue.pop()
    }

    private fun record() {
        val mat = toBGRAMat(g.get())
        val result = toMat.convert(mat)
        ffmpegRecorder.record(result, avutil.AV_PIX_FMT_BGRA)
        var samples = popAudio()
        while (samples == null) {
            samples = popAudio()
        }
        ffmpegRecorder.recordSamples(samples)
        audioCount += 1
    }

    class AudioRecordListener(val pm: RecorderablePAppletMinim) : AudioListener {
        override fun samples(p0: FloatArray?) {
            if (p0 == null) {
                return
            }
            val ele = FloatBuffer.wrap(p0)
            while (pm.pushAudio(ele).not()) {
                Thread.sleep(0, 10)
            }
        }

        override fun samples(left: FloatArray?, right: FloatArray?) {
            if (left == null || right == null) {
                return
            }
            val samples = left.zip(right).flatMap {
                listOf(it.first, it.second)
            }.toFloatArray()
            val ele = FloatBuffer.wrap(samples)
            while (pm.pushAudio(ele).not()) {
                Thread.sleep(0, 10)
            }
        }
    }
}