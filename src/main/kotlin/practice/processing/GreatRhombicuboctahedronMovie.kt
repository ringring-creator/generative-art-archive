package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector

class GreatRhombicuboctahedronMovie : Recorderable3DPApplet() {
    private var bgColor: Int = 0
    private val frameNum: Int = 6

    //private val jumpNum: Int = 1
    //Circumscribed sphere radius
    private val radius = 100F
    private val lengthOfSide = 1F
    private val partRadius = lengthOfSide / sin(frameNum * PI)
    private val height = Math.sqrt((radius * radius - partRadius * partRadius).toDouble())
    private val rg = mutableListOf<SurfPolygons>()

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        stroke(random(360F), 50F, 80F, 100F)
        strokeWeight(8F)
        fill(random(360F), 50F, 80F, 100F)
        hint(ENABLE_DEPTH_TEST)
        hint(ENABLE_DEPTH_SORT)
        println("partRadius: ${partRadius}, height: ${height}")

        for (z in listOf(1, -1)) {
            val polygons = mutableListOf<PVector>()
            repeat(frameNum) {
                val vertex = PVector(
                    sin(TWO_PI / frameNum.toFloat() * it.toFloat()) * lengthOfSide,
                    cos(TWO_PI / frameNum.toFloat() * it.toFloat()) * lengthOfSide,
                    height.toFloat()
                )
                polygons.add(vertex)
            }
            val surfPolygons = SurfPolygons(polygons)
            rg.add(surfPolygons)
        }
        /*for (y in listOf(1, -1)) {
            val polygons = mutableListOf<PVector>()
            repeat(frameNum) {
                val vertex = PVector(
                    sin(TWO_PI / frameNum.toFloat() * it.toFloat()) * radius.toFloat(),
                    getScreenHeight().toFloat() / 4F * y,
                    cos(TWO_PI / frameNum.toFloat() * it.toFloat()) * radius.toFloat()
                )
                polygons.add(vertex)
            }
            val surfPolygons = SurfPolygons(polygons)
            rg.add(surfPolygons)
        }
        for (x in listOf(1, -1)) {
            val polygons = mutableListOf<PVector>()
            repeat(frameNum) {
                val vertex = PVector(
                    getScreenHeight().toFloat() / 4F * x,
                    cos(TWO_PI / frameNum.toFloat() * it.toFloat()) * radius.toFloat(),
                    sin(TWO_PI / frameNum.toFloat() * it.toFloat()) * radius.toFloat()
                )
                polygons.add(vertex)
            }
            val surfPolygons = SurfPolygons(polygons)
            rg.add(surfPolygons)
        }*/
    }

    override fun drawBody() {
        pushMatrix()
        lights()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
            0F
        )
        rotateX(-PI / 6);
        rotateZ(PI / 12);
        for (idx in 0..1) {
            val surfPolygons = rg.get(idx)
            beginShape()
            for (vertexIdx in 0..(frameNum - 1)) {
                val polygon = surfPolygons.polygons[vertexIdx % frameNum]
                vertex(polygon.x, polygon.y, polygon.z)
                println("vertexIdx: ${vertexIdx}, vertex: ${polygon}")
            }
            endShape()
        }
        popMatrix()
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }

    data class SurfPolygons(val polygons: MutableList<PVector>)
}

fun main() {
    GreatRhombicuboctahedronMovie().run()
}