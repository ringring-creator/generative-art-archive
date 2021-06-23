package practice

import common.Recorderable3DPApplet
import processing.core.PVector
import kotlin.math.absoluteValue

class EggMovie : Recorderable3DPApplet() {
    private var bgColor: Int = 0
    private var u: Float = 0F
    private var v: Float = 0F
    private var finCount = 0
    private var fin: Boolean = false
    private var strokeColor: Float = random(360F)

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        noStroke()
        fill(strokeColor, 50F, 100F, 100F)
    }

    override fun drawBody() {
        if (fin) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
            println("finCount: ${finCount}")
        } else {
            lights()
            drawEgg()
            if (v.absoluteValue == 360F) {
                v = 0F
                u += 5
            } else {
                v += 5
            }
        }
    }

    private fun drawEgg() {
        pushMatrix()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
            -(getScreenWidth()).toFloat()
        )
        rotateY(PI / 5F)
        rotateZ(PI / 6F)
        beginShape()
        val vari = 10F
        var u = radians((this.u + vari).toFloat())
        var v = radians((this.v + vari).toFloat())
        var egg = PVector(
            2 * cos(u) * cos(v),
            3 * cos(u) * sin(v),
            2 * sin(u)
        ).mult((getScreenHeight() / 3).toFloat())
        vertex(egg.x, egg.y, egg.z)
        u = radians((this.u + vari).toFloat())
        v = radians((this.v).toFloat())
        egg = PVector(
            2 * cos(u) * cos(v),
            3 * cos(u) * sin(v),
            2 * sin(u)
        ).mult((getScreenHeight() / 3).toFloat())
        vertex(egg.x, egg.y, egg.z)
        u = radians((this.u).toFloat())
        v = radians((this.v + vari).toFloat())
        egg = PVector(
            2 * cos(u) * cos(v),
            3 * cos(u) * sin(v),
            2 * sin(u)
        ).mult((getScreenHeight() / 3).toFloat())
        vertex(egg.x, egg.y, egg.z)
        u = radians((this.u).toFloat())
        v = radians((this.v).toFloat())
        egg = PVector(
            2 * cos(u) * cos(v),
            3 * cos(u) * sin(v),
            2 * sin(u)
        ).mult((getScreenHeight() / 3).toFloat())
        vertex(egg.x, egg.y, egg.z)
        endShape()
        popMatrix()
        if ((this.u.absoluteValue == 360F).and(this.v.absoluteValue == 360F)) {
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
    EggMovie().run()
}