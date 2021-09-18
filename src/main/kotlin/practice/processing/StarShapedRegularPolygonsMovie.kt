package practice.processing

import common.RecorderablePApplet
import processing.core.PVector
import java.math.BigInteger

class StarShapedRegularPolygonsMovie : RecorderablePApplet() {
    private var num: Int = 0
    private var fin: Boolean = false
    private var bgColor: Int = 0
    private val radius = getScreenWidth().toBigInteger().gcd(getScreenHeight().toBigInteger()).toInt()
    private var verNum = 5
    private var verDis = 2
    private var vertexIdx = 0
    private val starPolygons = mutableListOf<PVector>()
    private var finCount = 0


    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        noFill()
        updateStarPolyVal()
        println("radius: ${radius}")
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
                val centerX = length * (num % (getScreenWidth() / length - 1) + 1).toFloat()
                val centerY = length * (num / (getScreenWidth() / length - 1) + 1).toFloat()
                println("centerX: ${centerX}, centerY: ${centerY}, vertexIdx: ${vertexIdx}")
                translate(
                    centerX,
                    centerY
                )
                drawStarPolygon()
                if (vertexIdx == 0) {
                    updateVerNumAndDis()
                    updateStarPolyVal()
                    num += 1
                    finJudge(centerX, centerY)
                }
                popMatrix()
            }
        }
    }

    private fun updateStarPolyVal() {
        val random = random(360F)
        stroke(color(random, 100F, 100F, 100F))
        starPolygons.clear()
        repeat(verNum) {
            val vertex = PVector(sin(TWO_PI / verNum * it), cos(TWO_PI / verNum * it)).mult((radius.toFloat() - 30F))
            println("vertex: ${vertex}")
            starPolygons.add(vertex)
        }
    }

    private fun finJudge(centerX: Float, centerY: Float) {
        if (getScreenWidth() < centerX + radius || getScreenHeight() < centerY + radius) {
            fin = true
        }
    }

    private fun updateVerNumAndDis() {
        vertexIdx = 0
        do {
            verDis += 1
            if (verNum <= 2 * verDis) {
                verNum += 1
                verDis = 2
            }
            println("verNum: ${verNum}, verDis: ${verDis}")
        } while (verNum.toBigInteger().gcd(verDis.toBigInteger()) != BigInteger.valueOf(1))
        println("fin verNum: ${verNum}, verDis: ${verDis}")
    }

    private fun drawStarPolygon() {
        beginShape()
        var starPolygon = starPolygons[vertexIdx]
        vertex(starPolygon.x, starPolygon.y)
        vertexIdx = (vertexIdx + verDis) % verNum
        starPolygon = starPolygons[vertexIdx]
        vertex(starPolygon.x, starPolygon.y)
        endShape()
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
    StarShapedRegularPolygonsMovie().run()
}