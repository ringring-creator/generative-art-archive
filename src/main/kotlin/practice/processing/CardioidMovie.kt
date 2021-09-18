package practice.processing

import common.RecorderablePApplet
import processing.core.PVector

class CardioidMovie : RecorderablePApplet() {
    private var type: Int = 1
    private var num: Int = 0
    private var strokeColor: Float = random(360F)
    private var bgColor: Int = 0
    private var finCount = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
    }

    override fun drawBody() {
        if (type != 7) {
            drawCardioid()
            num += 1
        }
        if (type == 7) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
        }
    }

    private fun drawCardioid() {
        val length = getScreenHeight() / 4
        if (type < 3) {
            pushMatrix()
            translate((getScreenWidth() / 2 + length / 2).toFloat(), (getScreenHeight() / 2).toFloat())
            strokeWeight(4F)
            stroke(0F, 0F, 0F, 100F)
            ellipse(0F, 0F, length.toFloat(), length.toFloat())
            ellipse(
                length * cos(radians(num.toFloat())),
                length * sin(radians(num.toFloat())),
                length.toFloat(), length.toFloat()
            )
            strokeWeight(8F)
            stroke(strokeColor, 50F, 100F, 100F)
            line(
                length * cos(radians(num.toFloat())),
                length * sin(radians(num.toFloat())),
                length * cos(radians(num.toFloat())) + length / 2 * cos(radians(2 * num.toFloat())),
                length * sin(radians(num.toFloat())) + length / 2 * sin(radians(2 * num.toFloat()))
            )
            popMatrix()
        }
        pushMatrix()
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        strokeWeight(8F)
        stroke(strokeColor, 50F, 100F, 100F)
        beginShape()
        val currentRadians = radians(num.toFloat())
        var cardioid = PVector(
            (1 + cos(currentRadians)) * cos(currentRadians),
            (1 + cos(currentRadians)) * sin(currentRadians),
        ).mult(length.toFloat())
        println("asteroids: $cardioid")
        curveVertex(cardioid.x, cardioid.y)
        curveVertex(cardioid.x, cardioid.y)
        val nextRadians = radians(1 + num.toFloat())
        cardioid = PVector(
            (1 + cos(nextRadians)) * cos(nextRadians),
            (1 + cos(nextRadians)) * sin(nextRadians),
        ).mult(length.toFloat())
        println("asteroids: $cardioid")
        curveVertex(cardioid.x, cardioid.y)
        curveVertex(cardioid.x, cardioid.y)
        endShape()
        popMatrix()
        if (num == 360) {
            num = 0
            strokeColor = random(360F)
            type += 1
            if (type == 3) {
                background(bgColor)
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
    CardioidMovie().run()
}