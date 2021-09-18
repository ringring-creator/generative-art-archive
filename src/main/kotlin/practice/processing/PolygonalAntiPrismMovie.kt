package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector

class PolygonalAntiPillarMovie : Recorderable3DPApplet() {
    private var bgColor: Int = 0
    private val verNum: Int = 9
    private val topSurfPolygons = mutableListOf<PVector>()
    private val bottomSurfPolygons = mutableListOf<PVector>()
    private val radius = 300
    private var type = 0
    private var currentIdx = 0
    private var top = true
    private var finCount = 0
    private val fillColor = random(360F)

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F)
        bgColor = color(0F, 0F, 100F)
        background(bgColor)
        stroke(fillColor, 100F, 80F, 100F)
        strokeWeight(4F)
        fill(fillColor, 80F, 100F, 100F)
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
                sin(TWO_PI / verNum.toFloat() * it.toFloat() + TWO_PI / verNum.toFloat() / 2F) * radius.toFloat(),
                cos(TWO_PI / verNum.toFloat() * it.toFloat() + TWO_PI / verNum.toFloat() / 2F) * radius.toFloat(),
                -getScreenHeight().toFloat() / 4F
            )
            bottomSurfPolygons.add(vertex)
        }
    }

    override fun drawBody() {
        if (frameCount % 30 == 1) {
            lights()
            pushMatrix()
            translate(
                (getScreenWidth() / 2).toFloat(),
                (getScreenHeight() / 2).toFloat(),
                0F
            )
            rotateX(PI / 2);
            rotateZ(PI / 12);
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
        beginShape()
        val offset = 4
        var polygon = if (top) {
            val idx = currentIdx % verNum
            print("topSurf Line idx: ${idx}, ")
            topSurfPolygons[idx]
        } else {
            var idx = offset - currentIdx
            idx = if (idx < 0) {
                verNum + idx
            } else {
                idx
            }
            print("bottomSurf Line idx: ${idx}, ")
            bottomSurfPolygons[idx]
        }
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        polygon = if (top) {
            var idx = offset - currentIdx
            idx = if (idx < 0) {
                verNum + idx
            } else {
                idx
            }
            println("bottomSurf Line idx: ${idx}")
            bottomSurfPolygons[idx]
        } else {
            val idx = (currentIdx + 1) % verNum
            println("topSurf Line idx: ${idx}")
            topSurfPolygons[idx]
        }
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        top = top.not()
        if (top) {
            currentIdx += 1
        }
        endShape()
        if (currentIdx == verNum) {
            currentIdx = 0
            type = 5
        }
    }

    private fun drawSideSurf() {
        fill(fillColor, 50F, 80F, 100F)
        beginShape()
        val offset = 4
        var polygon = if (top) {
            val idx = currentIdx % verNum
            print("topSurf idx: ${idx}, ")
            topSurfPolygons[idx]
        } else {
            var idx = offset - currentIdx
            idx = if (idx < 0) {
                verNum + idx
            } else {
                idx
            }
            print("bottomSurf idx: ${idx}, ")
            bottomSurfPolygons[idx]
        }
        vertex(polygon.x, polygon.y, polygon.z)
        polygon = if (top) {
            var idx = offset - currentIdx
            idx = if (idx < 0) {
                verNum + idx
            } else {
                idx
            }
            print("bottomSurf idx: ${idx}, ")
            bottomSurfPolygons[idx]
        } else {
            val idx = currentIdx % verNum
            print("topSurf idx: ${idx}, ")
            topSurfPolygons[idx]
        }
        vertex(polygon.x, polygon.y, polygon.z)
        polygon = if (top) {
            var idx = offset + 1 - currentIdx
            idx = if (idx < 0) {
                verNum + idx
            } else {
                idx
            }
            println("bottomSurf idx: ${idx}")
            bottomSurfPolygons[idx]
        } else {
            val idx = (currentIdx + 1) % verNum
            println("topSurf idx: ${idx}")
            topSurfPolygons[idx]
        }
        vertex(polygon.x, polygon.y, polygon.z)
        top = top.not()
        if (top) {
            currentIdx += 1
        }
        endShape()
        if (currentIdx == verNum) {
            currentIdx = 0
            type = 6
        }
    }

    private fun drawBottomSurf() {
        beginShape()
        for (vertexIdx in 0..verNum) {
            val polygon = bottomSurfPolygons[vertexIdx % verNum]
            vertex(polygon.x, polygon.y, polygon.z)
            println("bottomSurf vertexIdx: ${vertexIdx}, vertex: ${polygon}")
        }
        endShape()
        type = 4
    }

    private fun drawBottomSurfLine() {
        beginShape()
        var polygon = bottomSurfPolygons[currentIdx % verNum]
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        polygon = bottomSurfPolygons[(currentIdx + 1) % verNum]
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        endShape()
        currentIdx += 1
        if (currentIdx == verNum) {
            currentIdx = 0
            type = 3
        }
    }

    private fun drawTopSurf() {
        beginShape()
        for (vertexIdx in 0..verNum) {
            val polygon = topSurfPolygons[vertexIdx % verNum]
            vertex(polygon.x, polygon.y, polygon.z)
            println("bottomSurf vertexIdx: ${vertexIdx}, vertex: ${polygon}")
        }
        endShape()
        type = 2
    }

    private fun drawTopSurfLine() {
        beginShape()
        var polygon = topSurfPolygons[currentIdx % verNum]
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        polygon = topSurfPolygons[(currentIdx + 1) % verNum]
        vertex(polygon.x, polygon.y, polygon.z)
        vertex(polygon.x, polygon.y, polygon.z)
        endShape()
        currentIdx += 1
        if (currentIdx == verNum) {
            currentIdx = 0
            type = 1
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
    PolygonalAntiPillarMovie().run()
}