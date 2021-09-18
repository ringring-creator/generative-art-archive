package practice.processing

import common.Recorderable3DPApplet
import org.bytedeco.javacpp.indexer.FloatIndexer
import org.bytedeco.opencv.global.opencv_calib3d
import org.bytedeco.opencv.global.opencv_core
import org.bytedeco.opencv.opencv_core.Mat
import processing.core.PVector

class RegularDodecahedronMovie : Recorderable3DPApplet() {
    private var surfaceDrawReady: Boolean = false
    private val radius: Float = 200F
    private lateinit var regularDodecahedron: RegularPolyhedron
    private var bgColor: Int = 0
    private var surfaceIdx = 0
    private var vertexIdx = 0
    private val fillColor = random(360F)
    private var finCount = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        regularDodecahedron = RegularPolyhedron(radius)
        stroke(fillColor, 100F, 80F, 100F)
        strokeWeight(4F)
        fill(fillColor, 80F, 100F, 100F)
    }

    class RegularPolyhedron(radius: Float) {
        val vertexes: MutableList<PVector> = mutableListOf()
        val surfaces: MutableList<List<PVector>> = mutableListOf()

        init {
            //Todo: On the way
            val startVec = Mat(radius, radius, radius)
            var xRot = Mat(3, 3, opencv_core.CV_32FC1)
            var yRot = Mat(3, 3, opencv_core.CV_32FC1)
            var yMat = Mat()
            opencv_calib3d.Rodrigues(Mat(0F, opencv_core.CV_PI.toFloat(), 0F), yRot)
            var xMat = Mat()
            for (idx in 0..3) {
                opencv_calib3d.Rodrigues(Mat(opencv_core.CV_PI.toFloat() / 2F * idx.toFloat(), 0F, 0F), xRot)
                opencv_core.gemm(startVec.getPointer(), xRot.getPointer(), 1.0, Mat().getPointer(), 0.0, xMat, 0)
                var indexer = xMat.createIndexer<FloatIndexer>()
                vertexes.add(PVector(indexer.get(0, 0), indexer.get(0, 1), indexer.get(0, 2)))
                opencv_core.gemm(xMat.getPointer(), yRot.getPointer(), 1.0, Mat().getPointer(), 0.0, yMat, 0)
                indexer = yMat.createIndexer<FloatIndexer>()
                vertexes.add(PVector(indexer.get(0, 0), indexer.get(0, 1), indexer.get(0, 2)))
            }
            for (vertex in vertexes) {
                println("vertex: ${vertex}")
            }

        }
    }

    override fun drawBody() {
        TODO("Not yet implemented")
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    RegularDodecahedronMovie().run()
}