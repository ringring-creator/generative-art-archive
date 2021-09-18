package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector

class CircularSpiralMovie : Recorderable3DPApplet() {
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
        rotateX(PI / 3F)
        //rotateY(PI / 3F)
        beginShape()
        var radians = radians(num)
        var circularSpiral = PVector(
            getScreenHeight() / 4 * cos(radians), getScreenHeight() / 4 * sin(radians), radians * 10F
        )
        vertex(circularSpiral.x, circularSpiral.y, circularSpiral.z)
        vertex(circularSpiral.x, circularSpiral.y, circularSpiral.z)
        radians = radians(num + 1)
        circularSpiral = PVector(
            getScreenHeight() / 4 * cos(radians), getScreenHeight() / 4 * sin(radians), radians * 10F
        )
        vertex(circularSpiral.x, circularSpiral.y, circularSpiral.z)
        vertex(circularSpiral.x, circularSpiral.y, circularSpiral.z)
        endShape()
        popMatrix()
        if (getScreenHeight() < circularSpiral.z) {
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
    CircularSpiralMovie().run()
}