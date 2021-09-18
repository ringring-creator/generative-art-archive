package practice.processing

import common.RecorderablePApplet
import processing.core.PVector

class PolygonsMovie : RecorderablePApplet() {
    private var fin: Boolean = false
    private var bgColor: Int = 0
    private val radius = getScreenWidth().toBigInteger().gcd(getScreenHeight().toBigInteger()).toInt()
    private var verNum = 3
    private var vertexIdx = 0
    private val polygons = mutableListOf<PVector>()
    private var finCount = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        println("radius: ${radius}")
        updateVertex()
    }

    override fun drawBody() {
        if (fin) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
        } else {
            if (frameCount % 15 == 0) {
                pushMatrix()
                val length = radius * 2
                val centerX = (length * ((verNum - 3) % (getScreenWidth() / length - 1) + 1)).toFloat()
                val centerY = (length * ((verNum - 3) / (getScreenWidth() / length - 1) + 1)).toFloat()
                println("centerX: ${centerX}, centerY: ${centerY}, vertexIdx: ${vertexIdx}")
                translate(
                    centerX,
                    centerY
                )
                drawPolygon()
                if (verNum + 1 == vertexIdx) {
                    verNum += 1
                    vertexIdx = 0
                    updateVertex()
                    finJudge(centerX, centerY)
                }
                popMatrix()
            }
        }
    }

    private fun finJudge(centerX: Float, centerY: Float) {
        if (getScreenWidth() < centerX + radius || getScreenHeight() < centerY + radius) {
            fin = true
        }
    }

    private fun drawPolygon() {
        if (verNum == (vertexIdx)) {
            noStroke()
            beginShape()
            repeat(verNum) {
                var polygon = polygons[it]
                vertex(polygon.x, polygon.y)
            }
            endShape()
        } else {
            beginShape()
            var polygon = polygons[vertexIdx]
            vertex(polygon.x, polygon.y)
            polygon = polygons[(vertexIdx + 1) % verNum]
            vertex(polygon.x, polygon.y)
            endShape()
        }
        vertexIdx += 1
    }

    private fun updateVertex() {
        val random = random(360F)
        stroke(color(random, 100F, 100F, 100F))
        fill(color(random, 50F, 100F, 100F))
        polygons.clear()
        repeat(verNum) {
            val vertex = PVector(sin(TWO_PI / verNum * it), cos(TWO_PI / verNum * it)).mult((radius.toFloat() - 30F))
            println("vertex: ${vertex}")
            polygons.add(vertex)
        }
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
    PolygonsMovie().run()
}