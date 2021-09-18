package practice.processing

import common.RecorderablePApplet
import processing.core.PVector

class EightCharaCurveMovie : RecorderablePApplet() {
    private var bgColor: Int = 0
    private var strokeColor: Float = random(360F)
    private var num: Float = 0F
    private var length: Float = (getScreenHeight() / 2).toFloat()
    private var finCount = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        stroke(strokeColor, 50F, 100F, 100F)
        strokeWeight(4F)
    }

    override fun drawBody() {
        if (length < 0) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
            println("finCount: ${finCount}")
        } else {
            showEightCharaCurve()
            num += 1F
        }
    }

    private fun showEightCharaCurve() {
        pushMatrix()
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        var radians = radians(num)
        beginShape()
        var eightCharaCurve = PVector(
            cos(radians),
            sin(2 * radians)
        ).mult(length)
        curveVertex(eightCharaCurve.x, eightCharaCurve.y)
        curveVertex(eightCharaCurve.x, eightCharaCurve.y)
        radians = radians(num + 1)
        eightCharaCurve = PVector(
            cos(radians),
            sin(2 * radians)
        ).mult(length)
        curveVertex(eightCharaCurve.x, eightCharaCurve.y)
        curveVertex(eightCharaCurve.x, eightCharaCurve.y)
        endShape()
        popMatrix()
        if (num == 360F) {
            num = 0F
            length -= 50F
            strokeColor = random(360F)
            stroke(strokeColor, 50F, 100F, 100F)
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
    EightCharaCurveMovie().run()
}