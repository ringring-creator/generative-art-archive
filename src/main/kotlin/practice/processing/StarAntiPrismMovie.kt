package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector

class StarAntiPillarMovie : Recorderable3DPApplet() {
    private var sideSurfTimes: Int = 1
    private var bgColor: Int = 0

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
    */
    private val verNum: Int = 13
    private var verDis = 6
    private val topSurfPolygons = mutableListOf<PVector>()
    private val bottomSurfPolygons = mutableListOf<PVector>()
    private val radius = 300
    private var type = 0
    private var currentIdx = 0
    private var top = true
    private var finCount = 0
    private val topFillColor = random(360F)
    private val bottomFillColor = topFillColor //+ 180F

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F)
        bgColor = color(0F, 0F, 100F)
        background(bgColor)
        stroke(topFillColor, 100F, 80F, 100F)
        strokeWeight(4F)
        fill(topFillColor, 80F, 100F, 100F)
        hint(ENABLE_DEPTH_TEST)
        hint(ENABLE_DEPTH_SORT)
        repeat(verNum) {
            val vertex = PVector(
                sin(TWO_PI / verNum.toFloat() * it.toFloat()) * radius.toFloat(),
                cos(TWO_PI / verNum.toFloat() * it.toFloat()) * radius.toFloat(),
                getScreenHeight().toFloat() / 4F
            )
            topSurfPolygons.add(vertex)
        }

        repeat(verNum) {
            val vertex = PVector(
                sin(TWO_PI / verNum.toFloat() * it.toFloat()) * radius.toFloat(),
                cos(TWO_PI / verNum.toFloat() * it.toFloat()) * radius.toFloat(),
                -getScreenHeight().toFloat() / 4F
            )
            bottomSurfPolygons.add(vertex)
        }
    }

    override fun drawBody() {
        if (frameCount % 30 == 1) {
            pushMatrix()
            lights()
            translate(
                (getScreenWidth() / 2).toFloat(),
                (getScreenHeight() / 2).toFloat(),
                0F
            )
            rotateX(PI / 2)
            rotateZ(PI / 12)
            /*textSize(150F)
            for (vertexIdx in 0..(verNum - 1)) {
                val polygon = topSurfPolygons[vertexIdx]
                text(vertexIdx, polygon.x, polygon.y, polygon.z)
            }
            for (vertexIdx in 0..(verNum - 1)) {
                val polygon = bottomSurfPolygons[vertexIdx]
                text(vertexIdx, polygon.x, polygon.y, polygon.z)
            }*/
            when (type) {
                0 -> drawTopSurfLine()
                1 -> drawTopSurf()
                2 -> drawBottomSurfLine()
                3 -> drawBottomSurf()
                4 -> drawSideSurfLine()
                5 -> drawSideSurf()
                6 -> fin()
            }
            popMatrix()
        }
    }

    private fun fin() {
        if (finCount == getFps().toInt() / 30 * 20) {
            exit()
        }
        finCount += 1
    }

    private fun drawSideSurfLine() {
        noFill()
        if (top) {
            stroke(topFillColor, 100F, 80F, 100F)
        } else {
            stroke(bottomFillColor, 100F, 80F, 100F)
        }
        beginShape()
        var polygon = if (top) {
            val idx = (currentIdx + verDis / 2) % verNum
            print("topSurf Line idx: ${idx}, ")
            topSurfPolygons[idx]
        } else {
            var idx = (currentIdx + verDis / 2) % verNum
            print("bottomSurf Line idx: ${idx}, ")
            bottomSurfPolygons[idx]
        }
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        polygon = if (top) {
            val idx = (currentIdx) % verNum
            bottomSurfPolygons[idx]
        } else {
            var idx = (currentIdx) % verNum
            topSurfPolygons[idx]
        }
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        endShape()
        beginShape()
        polygon = if (top) {
            val idx = (currentIdx + verDis / 2) % verNum
            print("topSurf Line idx: ${idx}, ")
            topSurfPolygons[idx]
        } else {
            val idx = (currentIdx + verDis / 2) % verNum
            print("bottomSurf Line idx: ${idx}, ")
            bottomSurfPolygons[idx]
        }
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        polygon = if (top) {
            val idx = (currentIdx + verDis) % verNum
            bottomSurfPolygons[idx]
        } else {
            var idx = (currentIdx + verDis) % verNum
            topSurfPolygons[idx]
        }
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        top = top.not()
        if (top) {
            currentIdx = (currentIdx + verDis) % verNum
        }
        endShape()
        println("currentIdx: ${currentIdx}")
        if (top.and(currentIdx == 0)) {
            type = 5
        }
    }

    private fun drawSideSurf() {
        background(bgColor)
        drawTopSurf()
        drawBottomSurf()
        do {
            println("top currentIdx: ${currentIdx}")
            drawSideSurfLine()
        } while (top.not() || currentIdx != 0)

        repeat(sideSurfTimes) {
            if (top) {
                fill(topFillColor, 50F, 80F, 100F)
            } else {
                fill(bottomFillColor, 50F, 80F, 100F)
            }
            beginShape()
            var polygon = if (top) {
                val idx = currentIdx % verNum
                print("topSurf Line idx: ${idx}, ")
                topSurfPolygons[idx]
            } else {
                var idx = currentIdx % verNum
                print("bottomSurf Line idx: ${idx}, ")
                bottomSurfPolygons[idx]
            }
            vertex(polygon.x, polygon.y, polygon.z)
            polygon = if (top) {
                val idx = (currentIdx + verDis) % verNum
                topSurfPolygons[idx]
            } else {
                var idx = (currentIdx + verDis) % verNum
                bottomSurfPolygons[idx]
            }
            vertex(polygon.x, polygon.y, polygon.z)
            polygon = if (top) {
                val idx = (currentIdx + verDis / 2) % verNum
                bottomSurfPolygons[idx]
            } else {
                var idx = (currentIdx + verDis / 2) % verNum
                topSurfPolygons[idx]
            }
            vertex(polygon.x, polygon.y, polygon.z)
            top = top.not()
            if (top) {
                currentIdx = (currentIdx + verDis) % verNum
            }
            endShape()
            if (top.and(currentIdx == 0)) {
                type = 6
            }
        }
        sideSurfTimes += 1
        currentIdx = 0
        top = true
    }

    private fun drawBottomSurf() {
        fill(bottomFillColor, 80F, 100F, 100F)
        beginShape()
        do {
            var starPolygon = bottomSurfPolygons[currentIdx]
            vertex(starPolygon.x, starPolygon.y, starPolygon.z)
            currentIdx = (currentIdx + verDis) % verNum
        } while (currentIdx != 0)
        endShape()
        if (type < 4) {
            type = 4
        }
    }

    private fun drawBottomSurfLine() {
        stroke(bottomFillColor, 100F, 80F, 100F)
        beginShape()
        var polygon = bottomSurfPolygons[currentIdx]
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        currentIdx = (currentIdx + verDis) % verNum
        polygon = bottomSurfPolygons[currentIdx]
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        endShape()
        if (type < 3) {
            if (currentIdx == 0) {
                type = 3
            }
        }
    }

    private fun drawTopSurf() {
        fill(topFillColor, 80F, 100F, 100F)
        beginShape()
        do {
            var starPolygon = topSurfPolygons[currentIdx]
            vertex(starPolygon.x, starPolygon.y, starPolygon.z)
            currentIdx = (currentIdx + verDis) % verNum
        } while (currentIdx != 0)
        endShape()
        if (type < 2) {
            type = 2
        }
    }

    private fun drawTopSurfLine() {
        stroke(topFillColor, 100F, 80F, 100F)
        beginShape()
        var polygon = topSurfPolygons[currentIdx]
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        currentIdx = (currentIdx + verDis) % verNum
        polygon = topSurfPolygons[currentIdx]
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        endShape()
        if (type < 1) {
            if (currentIdx == 0) {
                type = 1
            }
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
    StarAntiPillarMovie().run()
}