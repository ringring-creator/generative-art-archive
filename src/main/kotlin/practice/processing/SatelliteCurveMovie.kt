package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector

class SatelliteCurveMovie : Recorderable3DPApplet() {
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
            drawSatelliteCurve()
            u += 0.1F
        }
    }

    private fun drawSatelliteCurve() {
        println("u: ${this.u}")
        pushMatrix()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
            -(getScreenHeight() / 2).toFloat()
        )
        val k = 10F
        val alpha = radians(90F)
        rotateX(PI / 3F)
        beginShape()
        var radians = radians(this.u)
        var sphericalSineCurve = PVector(
            cos(alpha) * cos(radians) * cos(radians * k) - sin(radians) * sin(k * radians),
            cos(alpha) * sin(radians) * cos(radians * k) - cos(radians) * sin(k * radians),
            sin(alpha) * cos(radians * k)
        ).mult(radius)
        println("SphericalSineCurve: ${sphericalSineCurve}")
        vertex(sphericalSineCurve.x, sphericalSineCurve.y, sphericalSineCurve.z)
        vertex(sphericalSineCurve.x, sphericalSineCurve.y, sphericalSineCurve.z)
        radians = radians(this.u + 0.1F)
        sphericalSineCurve = PVector(
            cos(alpha) * cos(radians) * cos(radians * k) - sin(radians) * sin(k * radians),
            cos(alpha) * sin(radians) * cos(radians * k) - cos(radians) * sin(k * radians),
            sin(alpha) * cos(radians * k)
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
    SatelliteCurveMovie().run()
}