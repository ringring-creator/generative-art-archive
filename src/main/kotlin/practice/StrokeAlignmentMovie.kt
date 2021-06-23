package practice

import common.RecorderablePApplet

class StrokeAlignmentMovie : RecorderablePApplet() {
    private var bgColor: Int = 0
    private val tileCount = 20
    private var tileIdx = 0
    private var tileType = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 255F, 255F)
        background(bgColor)
    }

    override fun drawBody() {
        if (frameCount % 100 == 0) {
            println("frameCount: ${frameCount}")
        }
        if ((frameCount % 30 == 1).and(tileType < 3)) {
            val x = getScreenWidth() / tileCount * (tileIdx % tileCount)
            val y = getScreenHeight() / tileCount * (tileIdx / tileCount)
            when (tileType) {
                0 -> drawStroke(x, y, getScreenWidth() / tileCount, getScreenHeight() / tileCount, ROUND)
                1 -> drawStroke(x, y, getScreenWidth() / tileCount, getScreenHeight() / tileCount, SQUARE)
                2 -> drawStroke(x, y, getScreenWidth() / tileCount, getScreenHeight() / tileCount, PROJECT)
            }
            tileIdx += 1
        }
        if ((frameCount % (tileCount * tileCount * 30) == 0).and(tileType < 3)) {
            println("tileType: ${tileType}, tileIdx: ${tileIdx}")
            tileType += 1
            tileIdx = 0
            background(bgColor)
        }
        if (frameCount == tileCount * tileCount * 30 * 3 + 20 * getFps().toInt()) {
            exit()
        }
    }

    private fun drawStroke(x: Int, y: Int, width: Int, height: Int, type: Int) {
        val color = color(
            random(360F), 100F - y.toFloat() / getScreenHeight().toFloat() * 100F,
            100F, 100F
        )
        strokeCap(type)
        strokeWeight(20F)
        stroke(color)
        if (random(1F) <= 0.5) {
            line(x.toFloat(), y.toFloat(), x.toFloat() + width.toFloat(), y.toFloat() + height.toFloat())
        } else {
            line(x.toFloat(), y.toFloat() + height.toFloat(), x.toFloat() + width.toFloat(), y.toFloat())
        }
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun isRecordOnly(): Boolean {
        return true
        //return false
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("practice.${this::class.simpleName}", arrayOf())
    }
}

fun main() {
    StrokeAlignmentMovie().run()
}