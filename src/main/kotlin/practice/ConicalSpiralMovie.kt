package practice

import common.Recorderable3DPApplet
import processing.core.PVector

class ConicalSpiralMovie : Recorderable3DPApplet() {
    private var bgColor: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var num: Float = 0F

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
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
            lights()
            drawCircularSpiral()
            num += 1F
        }
    }

    private fun drawCircularSpiral() {
        stroke(num % 360F, 50F, 100F, 100F)
        pushMatrix()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight()).toFloat(),
            -(getScreenHeight() / 2).toFloat()
        )
        rotateX(PI / 4F * 3F)
        beginShape()
        var radians = radians(num)
        var conicalSpiral = PVector(
            radians * cos(radians), radians * sin(radians), radians
        ).mult(10F)
        vertex(conicalSpiral.x, conicalSpiral.y, conicalSpiral.z)
        vertex(conicalSpiral.x, conicalSpiral.y, conicalSpiral.z)
        radians = radians(num + 1)
        conicalSpiral = PVector(
            radians * cos(radians), radians * sin(radians), radians
        ).mult(10F)
        vertex(conicalSpiral.x, conicalSpiral.y, conicalSpiral.z)
        vertex(conicalSpiral.x, conicalSpiral.y, conicalSpiral.z)
        endShape()
        popMatrix()
        if (getScreenHeight() < conicalSpiral.z) {
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
    ConicalSpiralMovie().run()
}