package practice

import common.RecorderablePApplet
import processing.core.PVector

class HeartCurveMovie : RecorderablePApplet() {
    private var finCount = 0
    private var fin: Boolean = false
    private var bgColor: Int = 0
    private var num: Float = 0F
    private var length: Float = (getScreenHeight() / 40).toFloat()
    private var strokeColor: Float = random(360F)

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        stroke(strokeColor, 50F, 100F, 100F)
        strokeWeight(4F)
    }

    override fun drawBody() {
        if (fin) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
            println("finCount: ${finCount}")
        } else {
            drawHeartCurve()
            num += 0.5F
        }
    }

    private fun drawHeartCurve() {
        pushMatrix()
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        rotate(radians(180F))
        beginShape()
        var radians = radians(num)
        var heartCurve = PVector(
            16 * pow(sin(radians), 3F),
            13 * cos(radians) - 5 * cos(2 * radians) - 2 * cos(3 * radians) - cos(4 * radians)
        ).mult(length)
        println("heartCurve: ${heartCurve}, num: ${num}")
        curveVertex(heartCurve.x, heartCurve.y)
        curveVertex(heartCurve.x, heartCurve.y)
        radians = radians(num + 1)
        heartCurve = PVector(
            16 * pow(sin(radians), 3F),
            13 * cos(radians) - 5 * cos(2 * radians) - 2 * cos(3 * radians) - cos(4 * radians)
        ).mult(length)
        curveVertex(heartCurve.x, heartCurve.y)
        curveVertex(heartCurve.x, heartCurve.y)
        endShape()
        popMatrix()
        if (num == 360F) {
            num = 0F
            length *= 0.7F
            strokeColor = random(360F)
            stroke(strokeColor, 50F, 100F, 100F)
            if (length < 1F) {
                fin = true
            }
        }
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
    HeartCurveMovie().run()
}