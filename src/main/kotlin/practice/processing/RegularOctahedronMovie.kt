package practice.processing

import com.github.shiguruikai.combinatoricskt.permutations
import common.Recorderable3DPApplet
import org.bytedeco.javacpp.indexer.FloatIndexer
import org.bytedeco.opencv.global.opencv_calib3d
import org.bytedeco.opencv.global.opencv_core
import org.bytedeco.opencv.opencv_core.Mat
import processing.core.PVector
import kotlin.math.absoluteValue

class RegularOctahedronMovie : Recorderable3DPApplet() {
    private lateinit var regularOctahedron: RegularOctahedron
    private val radius: Float = 300F
    private var bgColor: Int = 0
    private val fillColor = random(360F)
    private var finCount = 0
    private var surfaceIdx = 0
    private var vertexIdx = 0
    private var surfaceDrawReady: Boolean = false

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        regularOctahedron = RegularOctahedron(radius)
        stroke(fillColor, 100F, 80F, 100F)
        strokeWeight(4F)
        fill(fillColor, 80F, 100F, 100F)
    }

    override fun drawBody() {
        if (surfaceIdx == regularOctahedron.surfaces.size) {
            println("finCount: ${finCount}")
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
            return
        }
        if (frameCount % 30 == 1) {
            lights()
            pushMatrix()
            translate(
                (getScreenWidth() / 2).toFloat(),
                (getScreenHeight() / 2).toFloat(),
                0F
            )
            rotateX(PI / 4 * 3);
            rotateY(PI / 5);
            rotateZ(PI / 5*6);

            if (surfaceIdx != 0) {
                for (doneIdx in 0..(surfaceIdx - 1)) {
                    val surface = regularOctahedron.surfaces.get(doneIdx)
                    for (vertexIdx in 0..(surface.size - 1)) {
                        beginShape()
                        drawLine(vertexIdx, surface)
                        endShape()
                    }
                    beginShape()
                    for (vertex in surface) {
                        vertex(vertex.x, vertex.y, vertex.z)
                    }
                    endShape()
                }
            }
            beginShape()
            val surface = regularOctahedron.surfaces.get(surfaceIdx)
            if (surfaceDrawReady) {
                for (vertex in surface) {
                    vertex(vertex.x, vertex.y, vertex.z)
                }
                surfaceIdx += 1
                surfaceDrawReady = false
                println("surface: ${surface}, surface size: ${surface.size}")
                println("vertexIdx: ${vertexIdx}, surfaceIdx: ${surfaceIdx}")
            } else {
                println("vertexIdx: ${vertexIdx}")
                drawLine(vertexIdx, surface)
            }
            if (vertexIdx == (surface.size - 1)) {
                vertexIdx = 0
                surfaceDrawReady = true
            } else {
                vertexIdx += 1
            }
            endShape()
            popMatrix()
        }
    }

    private fun drawLine(vertexIdx: Int, surface: List<PVector>) {
        var vertex = surface.get(vertexIdx)
        vertex(vertex.x, vertex.y, vertex.z)
        vertex(vertex.x, vertex.y, vertex.z)
        val nextIdx = (vertexIdx + 1) % surface.size
        println("nextIdx: ${nextIdx}")
        vertex = surface.get(nextIdx)
        vertex(vertex.x, vertex.y, vertex.z)
        vertex(vertex.x, vertex.y, vertex.z)
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }

    class RegularOctahedron(radius: Float) {
        val vertexes: MutableList<PVector> = mutableListOf()
        val surfaces: MutableList<List<PVector>> = mutableListOf()

        init {
            val startVec = Mat(radius, 0F, 0F)
            var rot = Mat(3, 3, opencv_core.CV_32FC1)
            var resultMat = Mat()
            for (idx in 0..3) {
                opencv_calib3d.Rodrigues(Mat(0F, 0F, opencv_core.CV_PI.toFloat() / 2F * idx.toFloat()), rot)
                opencv_core.gemm(startVec.getPointer(), rot.getPointer(), 1.0, Mat().getPointer(), 0.0, resultMat, 0)
                val indexer = resultMat.createIndexer<FloatIndexer>()
                vertexes.add(PVector(indexer.get(0, 0), indexer.get(0, 1), indexer.get(0, 2)))
            }
            for (idx in 0..1) {
                opencv_calib3d.Rodrigues(
                    Mat(0F, opencv_core.CV_PI.toFloat() / 2F + opencv_core.CV_PI.toFloat() * idx.toFloat(), 0F),
                    rot
                )
                opencv_core.gemm(startVec.getPointer(), rot.getPointer(), 1.0, Mat().getPointer(), 0.0, resultMat, 0)
                val indexer = resultMat.createIndexer<FloatIndexer>()
                vertexes.add(PVector(indexer.get(0, 0), indexer.get(0, 1), indexer.get(0, 2)))
            }

            for (vertex in vertexes) {
                println("vertex: ${vertex}")
            }

            val dist = PVector.dist(vertexes.get(0), vertexes.get(5))
            println("dist: ${dist}")
            vertexes.permutations(3).forEach {
                var isRegular = true
                for (idx in 0..(it.size - 1)) {
                    val currentDist = PVector.dist(it.get(idx), it.get((idx + 1) % it.size))
                    if (1 < (dist - currentDist).absoluteValue) {
                        isRegular = false
                    }
                }
                if (isRegular) {
                    var noDupli = true
                    for (surface in surfaces) {
                        if (surface.toSet().equals(it.toSet())) {
                            noDupli = false
                        }
                    }
                    if (noDupli) {
                        surfaces.add(it)
                    }
                }
            }
            println("surfaces size: ${this.surfaces.size}")
            for (surface in surfaces) {
                println("surface: ${surface}")
            }
        }
    }
}

fun main() {
    RegularOctahedronMovie().run()
}
