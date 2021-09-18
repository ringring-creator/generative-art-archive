package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector

class SphericalSineCurveMovie : Recorderable3DPApplet() {
    private var bgColor: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var u: Float = 0F
    private var strokeColor: Float = random(360F)
    private var radius = 450F

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
            drawSphericalSineCurve()
            u += 0.2F
        }
    }

    private fun drawSphericalSineCurve() {
        println("u: ${this.u}")
        pushMatrix()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
            -(getScreenHeight() / 2).toFloat()
        )
        val n = 9F
        val k = 10F
        rotateX(PI / 3F)
        beginShape()
        var radians = radians(this.u)
        var sphericalSineCurve = PVector(
            cos(radians) / pow((1 + k * k * cos(n * radians) * cos(n * radians)), 1 / 2F),
            sin(radians) / pow((1 + k * k * cos(n * radians) * cos(n * radians)), 1 / 2F),
            k * cos(n * radians) / pow((1 + k * k * cos(n * radians) * cos(n * radians)), 1 / 2F),
        ).mult(radius)
        println("SphericalSineCurve: ${sphericalSineCurve}")
        vertex(sphericalSineCurve.x, sphericalSineCurve.y, sphericalSineCurve.z)
        vertex(sphericalSineCurve.x, sphericalSineCurve.y, sphericalSineCurve.z)
        radians = radians(this.u + 0.2F)
        sphericalSineCurve = PVector(
            cos(radians) / pow((1 + k * k * cos(n * radians) * cos(n * radians)), 1 / 2F),
            sin(radians) / pow((1 + k * k * cos(n * radians) * cos(n * radians)), 1 / 2F),
            k * cos(n * radians) / pow((1 + k * k * cos(n * radians) * cos(n * radians)), 1 / 2F),
        ).mult(radius)
        vertex(sphericalSineCurve.x, sphericalSineCurve.y, sphericalSineCurve.z)
        vertex(sphericalSineCurve.x, sphericalSineCurve.y, sphericalSineCurve.z)
        endShape()
        popMatrix()
        if (360F < this.u) {
            fin = true
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
    SphericalSineCurveMovie().run()
}