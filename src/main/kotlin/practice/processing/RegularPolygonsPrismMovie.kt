package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector

class RegularPolygonsPrismMovie : Recorderable3DPApplet() {
    private val verNum: Int = 9
    private val polygons = mutableListOf<PVector>()
    private var bgColor: Int = 0
    private var finCount = 0
    private var z: Float = -getScreenWidth().toFloat() / 4F * 3F
    private val radius = 300


    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F)
        bgColor = color(0F, 0F, 100F)
        background(bgColor)
        stroke(random(360F), 50F, 80F, 100F)
        strokeWeight(8F)
        fill(random(360F), 50F, 80F, 100F)
        hint(ENABLE_DEPTH_TEST)
        hint(ENABLE_DEPTH_SORT)
    }

    override fun drawBody() {
        blendMode(REPLACE)
        if (getScreenHeight() / 2 - 150 < z) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
            println("finCount: ${finCount}")
        } else {
            lights()
            pushMatrix()
            translate(
                (getScreenWidth() / 2).toFloat(),
                (getScreenHeight() / 2).toFloat(),
                0F
            )
            drawPolygon()
            popMatrix()
        }
    }

    override fun exitBody() {
        println("exitBody")
    }

    private fun drawPolygon() {
        polygons.clear()
        repeat(verNum) {
            val vertex = PVector(
                sin(TWO_PI / verNum.toFloat() * it.toFloat()) * radius.toFloat(),
                cos(TWO_PI / verNum.toFloat() * it.toFloat()) * radius.toFloat(),
                z
            )
            polygons.add(vertex)
        }
        rotateX(-PI / 6);
        rotateZ(PI / 12);
        beginShape()
        for (vertexIdx in 0..verNum) {
            val polygon = polygons[vertexIdx % verNum]
            vertex(polygon.x, polygon.y, polygon.z)
            vertex(polygon.x, polygon.y, polygon.z + 1F)
            vertex(polygon.x, polygon.y, polygon.z + 2F)
            println("vertexIdx: ${vertexIdx}, vertex: ${polygon}")
        }
        z += 1F
        endShape()
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    RegularPolygonsPrismMovie().run()
}