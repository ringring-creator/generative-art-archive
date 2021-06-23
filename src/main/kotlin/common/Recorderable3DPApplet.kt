package common

import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.FrameRecorder
import org.bytedeco.javacv.OpenCVFrameConverter
import processing.core.PApplet
import processing.core.PGraphics
import toBGRAMat

abstract class Recorderable3DPApplet() : PApplet() {
    private var startTime: Long
    private lateinit var ffmpegRecorder: FFmpegFrameRecorder
    private val toMat: OpenCVFrameConverter.ToMat = OpenCVFrameConverter.ToMat()

    init {
        startTime = System.currentTimeMillis()
    }

    override fun settings() {
        //set screen size
        size(getScreenWidth(), getScreenHeight(), P3D)
        ffmpegRecorder =
            FrameRecorder.create(
                "FFmpegFrameRecorder",
                getRecordFilePath(),
                getScreenWidth(),
                getScreenHeight()
            ) as FFmpegFrameRecorder
        ffmpegRecorder.frameRate = getFps()
        ffmpegRecorder.videoCodec = avcodec.AV_CODEC_ID_H264
    }

    abstract fun setupBody()

    override fun setup() {
        setupBody()
        val recordFps: Float = if (isRecordOnly()) Float.MAX_VALUE else getFps().toFloat()
        println("isRecordOnly: ${isRecordOnly()}, recordFps: ${recordFps}")
        frameRate(recordFps)
        ffmpegRecorder.start()
        if (isRecordOnly()) {
            surface.setVisible(false)
            frame.setVisible(false)
        }
    }

    abstract fun drawBody()

    override fun draw() {
        if (frameCount % 100 == 1) {
            println("frameCount: ${frameCount}")
        }
        drawBody()
        if (exitCalled().not()) {
            recordPImage()
        }
    }

    abstract fun exitBody()

    override fun exit() {
        exitBody()
        ffmpegRecorder.stop()
        ffmpegRecorder.release()
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

    open fun isRecordOnly(): Boolean = false

    private fun recordPImage() {
        val mat = toBGRAMat(g.get())
        val result = toMat.convert(mat)
        ffmpegRecorder.record(result, avutil.AV_PIX_FMT_BGRA)
    }
}