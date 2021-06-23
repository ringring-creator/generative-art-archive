package practice

import common.RecorderablePApplet
import processing.core.PVector
import kotlin.math.cosh
import kotlin.math.sinh

class HyperbolaMovie : RecorderablePApplet() {
    private var xsign: Float = 1F
    private var bgColor: Int = 0
    private var strokeColor: Float = random(360F)
    private var fin: Boolean = false
    private var finCount = 0
    private val maxRadians = 180F

    private var num: Float = -maxRadians
    private var rotateRadians = 0F

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
            drawHyperbola()
            num += 1
        }

    }

    private fun drawHyperbola() {
        pushMatrix()
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        rotate(radians(rotateRadians))
        beginShape()
        var radians = radians(num)
        var hyperbola = PVector(xsign * cosh(radians), sinh(radians)).mult(50F)
        println("hyperbola: ${hyperbola}, num: ${num}")
        curveVertex(hyperbola.x, hyperbola.y)
        curveVertex(hyperbola.x, hyperbola.y)
        radians = radians(num + 1)
        hyperbola = PVector(xsign * cosh(radians), sinh(radians)).mult(50F)
        curveVertex(hyperbola.x, hyperbola.y)
        curveVertex(hyperbola.x, hyperbola.y)
        endShape()
        popMatrix()
        if (num == maxRadians) {
            num = -maxRadians
            xsign *= -1F
            if (0 < xsign) {
                rotateRadians += 10F
                strokeColor = random(360F)
                stroke(strokeColor, 50F, 100F, 100F)
            }
        }
        if (rotateRadians == 180F) {
            fin = true
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
    HyperbolaMovie().run()
}