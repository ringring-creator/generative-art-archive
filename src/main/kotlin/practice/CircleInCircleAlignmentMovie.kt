package practice

import common.RecorderablePApplet

class CircleInCircleAlignmentMovie : RecorderablePApplet() {
    private var bgColor: Int = 0
    private val tileCount = 20
    private var tileIdx = 0
    private var tileType = 0
    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 255F, 255F)
        background(bgColor)
        noStroke()
    }

    override fun drawBody() {
        if (frameCount % 100 == 0) {
            println("frameCount: ${frameCount}")
        }
        if ((frameCount % 30 == 1).and(tileType < 2)) {
            val x = getScreenWidth() / tileCount * (tileIdx % tileCount)
            val y = getScreenHeight() / tileCount * (tileIdx / tileCount)
            when (tileType) {
                0 -> drawCircleInCircle(x, y, getScreenWidth() / tileCount, getScreenHeight() / tileCount, 0)
                1 -> drawCircleInCircle(x, y, getScreenWidth() / tileCount, getScreenHeight() / tileCount, 1)
            }
            tileIdx += 1
        }
        if ((frameCount % (tileCount * tileCount * 30) == 0).and(tileType < 2)) {
            println("tileType: ${tileType}, tileIdx: ${tileIdx}")
            tileType += 1
            tileIdx = 0
            background(bgColor)
        }
        if (frameCount == tileCount * tileCount * 30 * 2 + 20 * getFps().toInt()) {
            exit()
        }
    }

    private fun drawCircleInCircle(x: Int, y: Int, width: Int, height: Int, type: Int) {
        pushMatrix()
        translate((x + width / 2).toFloat(), (y + height / 2).toFloat())
        val maxSize = min(width, height).toFloat()
        var shiftX = 0F
        var shiftY = 0F
        if (type == 1) {
            shiftX = random(-width.toFloat() / 2, width.toFloat() / 2)
            shiftY = random(-height.toFloat() / 2, height.toFloat() / 2)
        }
        val circleColor = color(
            random(360F), 100F - y.toFloat() / getScreenHeight().toFloat() * 100F,
            100F, 100F
        )
        fill(circleColor)
        ellipse(shiftX, shiftY, maxSize, maxSize)
        val circleInCircleColor = color(0F, 0F, 255F, 255F)
        fill(circleInCircleColor)
        val center = random(-maxSize / 4, maxSize / 4)
        ellipse(center + shiftX, center + shiftY, maxSize / 2 - center * 2, maxSize / 2 - center * 2)
        popMatrix()
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    override fun isRecordOnly(): Boolean {
        return true
        //return false
    }

    fun run() {
        main("practice.${this::class.simpleName}", arrayOf())
    }
}

fun main() {
    CircleInCircleAlignmentMovie().run()
}