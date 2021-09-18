package practice.processing

import com.github.shiguruikai.combinatoricskt.combinations
import common.Recorderable3DPApplet
import processing.core.PApplet
import processing.core.PVector

class RegularTetrahedronMovie : Recorderable3DPApplet() {
    private var surfaceDrawReady: Boolean = false
    private val radius: Float = 400F
    private lateinit var regularTetrahedron: RegularTetrahedronMovie.RegularTetrahedron
    private var bgColor: Int = 0
    private var surfaceIdx = 0
    private var vertexIdx = 0
    private val fillColor = random(360F)
    private var finCount = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        regularTetrahedron = RegularTetrahedron(radius)
        stroke(fillColor, 100F, 80F, 100F)
        strokeWeight(4F)
        fill(fillColor, 80F, 100F, 100F)
    }

    override fun drawBody() {
        if (surfaceIdx == 4) {
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
            rotateX(PI);
            rotateY(PI / 5);
            rotateZ(PI / 4);

            if (surfaceIdx != 0) {
                for (doneIdx in 0..(surfaceIdx - 1)) {
                    val surface = regularTetrahedron.surfaces.get(doneIdx)
                    for (vertexIdx in 0..2) {
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
            val surface = regularTetrahedron.surfaces.get(surfaceIdx)
            if (surfaceDrawReady) {
                for (vertex in surface) {
                    vertex(vertex.x, vertex.y, vertex.z)
                }
                surfaceIdx += 1
                surfaceDrawReady = false
                println("surface: ${surface}")
                println("vertexIdx: ${vertexIdx}, surfaceIdx: ${surfaceIdx}")
            }
            println("vertexIdx: ${vertexIdx}")
            drawLine(vertexIdx, surface)
            vertexIdx += 1
            if (vertexIdx == 3) {
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
        val nextIdx = (vertexIdx + 1) % 3
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

    class RegularTetrahedron(radius: Float) {
        val vertex: ArrayList<PVector> = ArrayList(4)
        val surfaces: List<List<PVector>>

        init {
            vertex.add(genSphericalCoordinate(radius, 0F, 0F))
            vertex.add(genSphericalCoordinate(radius, 109F, 0F))
            vertex.add(genSphericalCoordinate(radius, 109F, 120F))
            vertex.add(genSphericalCoordinate(radius, 109F, 240F))
            println("vertex: ${vertex}")
            surfaces = vertex.combinations(3).toList()
            val tmp = vertex.combinations(2).toList()
            for (a in tmp) {
                println(PVector.dist(a.get(0), a.get(1)))
            }
        }

        private fun genSphericalCoordinate(radius: Float, theta: Float, phi: Float): PVector {
            return PVector(
                sin(radians(theta)) * cos(radians(phi)),
                sin(radians(theta)) * sin(radians(phi)),
                cos(radians(theta))
            ).mult(radius)
        }
    }
}

fun main() {
    RegularTetrahedronMovie().run()
}
