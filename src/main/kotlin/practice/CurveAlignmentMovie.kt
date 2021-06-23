package practice

import common.RecorderablePApplet

class CurveAlignmentMovie : RecorderablePApplet() {
    private var bgColor: Int = 0
    private val RADIUS = 60F
    private val PI_TWELVE = TWO_PI / 12
    private var waveIdx = 0
    private var finCount = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        stroke(0F, 0F, 0F, 50F)
        noFill()
        for (x in 0..16) {
            for (y in 0..9) {
                ellipse(60F + x.toFloat() * 120F, 60F + y.toFloat() * 120F, 120F, 120F)
                ellipse(x.toFloat() * 120F, y.toFloat() * 120F, 120F, 120F)
            }
        }
    }

    override fun drawBody() {
        strokeWeight(8F)
        /*if (frameCount % 30 == 1) {
            drawWave()
        }*/
        stroke(frameCount.toFloat() % 361F, 50F, 100F, 255F)
        val x = (frameCount % getScreenWidth()).toFloat()
        val y = sin(TWO_PI / 240 * frameCount) * RADIUS + 120F * (frameCount / getScreenWidth()) + 60F
        println("x: ${x}, y: ${y}")
        point(x, y)
        if (getScreenHeight() < y) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
        }
    }

    private fun drawWave() {
        stroke(waveIdx.toFloat() % 361F, 50F, 100F, 255F)
        pushMatrix()
        val translateX = 60F + ((waveIdx % 16) * 120).toFloat()
        val translateY = 60F + ((waveIdx / 16) * 120).toFloat()
        println("translateX: ${translateX}, translateY: ${translateY}")
        translate(translateX, translateY)
        beginShape()
        curveVertex(cos(PI_TWELVE + PI_TWELVE * 5) * RADIUS, sin(PI_TWELVE + PI_TWELVE * 5) * RADIUS)
        curveVertex(cos(PI_TWELVE + PI_TWELVE * 5) * RADIUS, sin(PI_TWELVE + PI_TWELVE * 5) * RADIUS)
        if (waveIdx % 2 == 0) {
            curveVertex(cos(PI_TWELVE * 2 + PI_TWELVE * 5) * RADIUS, sin(PI_TWELVE * 2 + PI_TWELVE * 5) * RADIUS)
            curveVertex(cos(PI_TWELVE * 3 + PI_TWELVE * 5) * RADIUS, sin(PI_TWELVE * 3 + PI_TWELVE * 5) * RADIUS)
            curveVertex(cos(PI_TWELVE * 4 + PI_TWELVE * 5) * RADIUS, sin(PI_TWELVE * 4 + PI_TWELVE * 5) * RADIUS)
            curveVertex(cos(PI_TWELVE * 5 + PI_TWELVE * 5) * RADIUS, sin(PI_TWELVE * 5 + PI_TWELVE * 5) * RADIUS)
            curveVertex(cos(PI_TWELVE * 6 + PI_TWELVE * 5) * RADIUS, sin(PI_TWELVE * 6 + PI_TWELVE * 5) * RADIUS)
        } else {
            curveVertex(-cos(PI_TWELVE * 6 + PI_TWELVE * 5) * RADIUS, -sin(PI_TWELVE * 6 + PI_TWELVE * 5) * RADIUS)
            curveVertex(-cos(PI_TWELVE * 5 + PI_TWELVE * 5) * RADIUS, -sin(PI_TWELVE * 5 + PI_TWELVE * 5) * RADIUS)
            curveVertex(-cos(PI_TWELVE * 4 + PI_TWELVE * 5) * RADIUS, -sin(PI_TWELVE * 4 + PI_TWELVE * 5) * RADIUS)
            curveVertex(-cos(PI_TWELVE * 3 + PI_TWELVE * 5) * RADIUS, -sin(PI_TWELVE * 3 + PI_TWELVE * 5) * RADIUS)
            curveVertex(-cos(PI_TWELVE * 2 + PI_TWELVE * 5) * RADIUS, -sin(PI_TWELVE * 2 + PI_TWELVE * 5) * RADIUS)
        }
        curveVertex(cos(PI_TWELVE * 7 + PI_TWELVE * 5) * RADIUS, sin(PI_TWELVE * 7 + PI_TWELVE * 5) * RADIUS)
        curveVertex(cos(PI_TWELVE * 7 + PI_TWELVE * 5) * RADIUS, sin(PI_TWELVE * 7 + PI_TWELVE * 5) * RADIUS)
        endShape()
        popMatrix()
        waveIdx += 1
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("practice.${this::class.simpleName}", arrayOf())
    }
}

fun main() {
    CurveAlignmentMovie().run()
}