package practice.processing

import common.Recorderable3DPApplet
import processing.core.PApplet
import processing.core.PVector
import java.math.BigInteger


class StarRegularPrismMovie : Recorderable3DPApplet() {
    private val verNum: Int = 9
    private var verDis = 4
    private val starPolygons = mutableListOf<PVector>()
    private var bgColor: Int = 0
    private var vertexIdx = 0
    private var finCount = 0
    private var z: Float = -getScreenWidth().toFloat() / 4F * 3F
    private val radius = 300


    override fun setupBody() {
        //colorMode(HSB, 360F, 100F, 100F, 100F)
        colorMode(HSB, 360F, 100F, 100F)
        //bgColor = color(0F, 0F, 100F, 100F)
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
            drawStarPolygon()
            popMatrix()
        }
    }

    override fun exitBody() {
        println("exitBody")
    }

    private fun drawStarPolygon() {
        starPolygons.clear()
        repeat(verNum) {
            val vertex = PVector(
                sin(TWO_PI / verNum.toFloat() * it.toFloat()) * radius.toFloat(),
                cos(TWO_PI / verNum.toFloat() * it.toFloat()) * radius.toFloat(),
                z
            )
            starPolygons.add(vertex)
        }
        vertexIdx = 0
        beginShape()
        rotateX(-PI / 6);
        rotateZ(PI / 12);
        do {
            var starPolygon = starPolygons[vertexIdx]
            vertex(starPolygon.x, starPolygon.y, starPolygon.z)
            vertex(starPolygon.x, starPolygon.y, starPolygon.z + 1F)
            vertex(starPolygon.x, starPolygon.y, starPolygon.z + 2F)
            vertex(starPolygon.x, starPolygon.y, starPolygon.z + 3F)
            vertexIdx = (vertexIdx + verDis) % verNum
            println("vertexIdx: ${vertexIdx}, vertex: ${starPolygon}")
        } while (vertexIdx != 0)
        var starPolygon = starPolygons[vertexIdx]
        vertex(starPolygon.x, starPolygon.y, starPolygon.z + 2F)
        z += 1F
        endShape()
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    val calcVerNumTimes: Int = 2000
    var verDis = 2
    var verNum = 5
    repeat(calcVerNumTimes) {
        do {
            verDis += 1
            if (verNum <= 2 * verDis) {
                verNum += 1
                verDis = 2
            }
        } while (verNum.toBigInteger().gcd(verDis.toBigInteger()) != BigInteger.valueOf(1))
        PApplet.println("fin verNum: ${verNum}, verDis: ${verDis}")
    }
    StarRegularPrismMovie().run()
}
/*
fin verNum: 7, verDis: 2
fin verNum: 7, verDis: 3
fin verNum: 8, verDis: 3
fin verNum: 9, verDis: 2
fin verNum: 9, verDis: 4
fin verNum: 10, verDis: 3
fin verNum: 11, verDis: 2
fin verNum: 11, verDis: 3
fin verNum: 11, verDis: 4
fin verNum: 11, verDis: 5
fin verNum: 12, verDis: 5
fin verNum: 13, verDis: 2
fin verNum: 13, verDis: 3
fin verNum: 13, verDis: 4
fin verNum: 13, verDis: 5
fin verNum: 13, verDis: 6
fin verNum: 14, verDis: 3
fin verNum: 14, verDis: 5
fin verNum: 15, verDis: 2
fin verNum: 15, verDis: 4
fin verNum: 15, verDis: 7
fin verNum: 16, verDis: 3
fin verNum: 16, verDis: 5
fin verNum: 16, verDis: 7
fin verNum: 17, verDis: 2
fin verNum: 17, verDis: 3
fin verNum: 17, verDis: 4
fin verNum: 17, verDis: 5
fin verNum: 17, verDis: 6
fin verNum: 17, verDis: 7
fin verNum: 17, verDis: 8
fin verNum: 18, verDis: 5
fin verNum: 18, verDis: 7
fin verNum: 19, verDis: 2
fin verNum: 19, verDis: 3
fin verNum: 19, verDis: 4
fin verNum: 19, verDis: 5
fin verNum: 19, verDis: 6
fin verNum: 19, verDis: 7
fin verNum: 19, verDis: 8
fin verNum: 19, verDis: 9
fin verNum: 20, verDis: 3
fin verNum: 20, verDis: 7
fin verNum: 20, verDis: 9
fin verNum: 21, verDis: 2
fin verNum: 21, verDis: 4
fin verNum: 21, verDis: 5
fin verNum: 21, verDis: 8
fin verNum: 21, verDis: 10
fin verNum: 22, verDis: 3
fin verNum: 22, verDis: 5
fin verNum: 22, verDis: 7
fin verNum: 22, verDis: 9
fin verNum: 23, verDis: 2
fin verNum: 23, verDis: 3
fin verNum: 23, verDis: 4
fin verNum: 23, verDis: 5
fin verNum: 23, verDis: 6
fin verNum: 23, verDis: 7
fin verNum: 23, verDis: 8
fin verNum: 23, verDis: 9
fin verNum: 23, verDis: 10
fin verNum: 23, verDis: 11
fin verNum: 24, verDis: 5
fin verNum: 24, verDis: 7
fin verNum: 24, verDis: 11
fin verNum: 25, verDis: 2
fin verNum: 25, verDis: 3
fin verNum: 25, verDis: 4
fin verNum: 25, verDis: 6
fin verNum: 25, verDis: 7
fin verNum: 25, verDis: 8
fin verNum: 25, verDis: 9
fin verNum: 25, verDis: 11
fin verNum: 25, verDis: 12
 */