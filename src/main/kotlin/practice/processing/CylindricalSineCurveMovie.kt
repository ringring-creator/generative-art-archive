package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector

class CylindricalSineCurveMovie : Recorderable3DPApplet() {
    private var bgColor: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var num: Float = 0F
    private var strokeColor: Float = random(360F)
    private var radius = 100F

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        stroke(strokeColor, 50F, 100F, 100F)
        strokeWeight(8F)
    }

    override fun drawBody() {
        if (fin) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
            println("finCount: ${finCount}")
        } else {
            drawCylindricalSineCurve()
            num += 1F
        }
    }

    private fun drawCylindricalSineCurve() {
        if (radius == 500F) {
            fin = true
            return
        }
        pushMatrix()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
            -(getScreenHeight() / 2).toFloat()
        )
        rotateX(PI / 3F)
        beginShape()
        var radians = radians(num)
        var cylindricalSineCurve = PVector(
            cos(radians), sin(radians), sin(radians * 9F)
        ).mult(radius)
        println("cylindricalSineCurve: ${cylindricalSineCurve}")
        vertex(cylindricalSineCurve.x, cylindricalSineCurve.y, cylindricalSineCurve.z)
        vertex(cylindricalSineCurve.x, cylindricalSineCurve.y, cylindricalSineCurve.z)
        radians = radians(num + 1)
        cylindricalSineCurve = PVector(
            cos(radians), sin(radians), sin(radians * 9F)
        ).mult(radius)
        vertex(cylindricalSineCurve.x, cylindricalSineCurve.y, cylindricalSineCurve.z)
        vertex(cylindricalSineCurve.x, cylindricalSineCurve.y, cylindricalSineCurve.z)
        endShape()
        popMatrix()
        if (num == 360F) {
            strokeColor = random(360F)
            stroke(strokeColor, 50F, 100F, 100F)
            num = 0F
            radius += 100F
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
    CylindricalSineCurveMovie().run()
}