package practice

import common.RecorderablePApplet
import processing.core.PVector
import kotlin.math.absoluteValue

class CycloidMovie : RecorderablePApplet() {
    private var bgColor: Int = 0
    private var finCount = 0
    private var strokeColor: Float = random(360F)
    private var num: Float = 0F
    private var radius: Float = (getScreenHeight() / 4).toFloat()
    private var rotateRadians = radians(0F)
    private var variation = 3F
    private var showEllipse = true

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        stroke(strokeColor, 50F, 100F, 100F)
        strokeWeight(4F)
    }


    override fun drawBody() {
        if ((radius < 0).and(showEllipse.not())) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
            println("finCount: ${finCount}")
        } else {
            drawCycloid()
            num += variation
        }
    }


    private fun drawCycloid() {
        pushMatrix()
        translate(0F, (getScreenHeight() / 2).toFloat())
        rotate(rotateRadians)
        var radians = radians(num)
        if (showEllipse) {
            strokeWeight(4F)
            stroke(0F, 0F, 0F, 100F)
            ellipse(radius * radians, radius, radius * 2, radius * 2)
            strokeWeight(8F)
            stroke(strokeColor, 50F, 100F, 100F)
            line(radius * radians, radius, radius * (radians - sin(radians)), radius * (1 - cos(radians)))
        }
        beginShape()
        var cycloid = PVector(
            radius * (radians - sin(radians)),
            radius * (1 - cos(radians))
        )
        println("cycloid: ${cycloid}")
        curveVertex(cycloid.x, cycloid.y)
        curveVertex(cycloid.x, cycloid.y)
        radians = radians(num + 1)
        cycloid = PVector(
            radius * (radians - sin(radians)),
            radius * (1 - cos(radians))
        )
        curveVertex(cycloid.x, cycloid.y)
        radians = radians(num + 2)
        cycloid = PVector(
            radius * (radians - sin(radians)),
            radius * (1 - cos(radians))
        )
        curveVertex(cycloid.x, cycloid.y)
        radians = radians(num + 3)
        cycloid = PVector(
            radius * (radians - sin(radians)),
            radius * (1 - cos(radians))
        )
        curveVertex(cycloid.x, cycloid.y)
        curveVertex(cycloid.x, cycloid.y)
        endShape()
        popMatrix()
        if (getScreenWidth() < cycloid.x.absoluteValue) {
            strokeColor = random(360F)
            stroke(strokeColor, 50F, 100F, 100F)
            if ((variation < 0)) {
                radius -= 60
            }
            if ((radius < 0).and(showEllipse)) {
                background(bgColor)
                showEllipse = false
                radius = (getScreenHeight() / 4).toFloat()
            }
            num = 0F
            rotateRadians += radians(180F)
            variation = -variation
        }
        println("num: ${num}, variation: ${variation}, radius: ${radius}")
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
    CycloidMovie().run()
}