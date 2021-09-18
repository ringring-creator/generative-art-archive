package practice.processing

import common.RecorderablePApplet
import processing.core.PVector

class CochleoidMovie : RecorderablePApplet() {
    private var bgColor: Int = 0
    private var finCount = 0
    private var strokeColor: Float = 0F
    private var num: Float = -360.0F * 2.0F
    private var radian = radians(0F)
    private val variation = 0.5F
    private var type = 1

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        //colorMode(HSB, 360F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        bgColor = color(0F, 0F, 100F)
        background(bgColor)
        stroke(strokeColor, 50F, 100F, 100F)
        //stroke(strokeColor, 50F, 100F)
        strokeWeight(4F)
        blendMode(REPLACE)
    }


    override fun drawBody() {
        if (type < 7) {
            drawCochleoid()
            num += variation
        } else {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
        }
    }

    private fun drawCochleoid() {
        pushMatrix()
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        var radians = radians(num)
        rotate(radian)
        val a = getScreenHeight() / 2
        beginShape()
        var cissoid = PVector(
            a * sin(radians) * cos(radians) / radians,
            a * sin(radians) * sin(radians) / radians
        )
        curveVertex(cissoid.x, cissoid.y)
        curveVertex(cissoid.x, cissoid.y)
        val drawRange = variation * 2
        radians = radians(num + drawRange / 5)
        cissoid = PVector(
            a * sin(radians) * cos(radians) / radians,
            a * sin(radians) * sin(radians) / radians
        )
        curveVertex(cissoid.x, cissoid.y)
        radians = radians(num + drawRange / 5 * 2)
        cissoid = PVector(
            a * sin(radians) * cos(radians) / radians,
            a * sin(radians) * sin(radians) / radians
        )
        curveVertex(cissoid.x, cissoid.y)
        radians = radians(num + drawRange / 5 * 3)
        cissoid = PVector(
            a * sin(radians) * cos(radians) / radians,
            a * sin(radians) * sin(radians) / radians
        )
        curveVertex(cissoid.x, cissoid.y)
        radians = radians(num + drawRange / 5 * 4)
        cissoid = PVector(
            a * sin(radians) * cos(radians) / radians,
            a * sin(radians) * sin(radians) / radians
        )
        curveVertex(cissoid.x, cissoid.y)
        radians = radians(num + drawRange)
        cissoid = PVector(
            a * sin(radians) * cos(radians) / radians,
            a * sin(radians) * sin(radians) / radians
        )
        curveVertex(cissoid.x, cissoid.y)
        curveVertex(cissoid.x, cissoid.y)
        endShape()
        popMatrix()
        if (360.0F * 2.0F <= num) {
            num = -360.0F * 2.0F
            radian += radians(180F)
            strokeColor += 45F
            stroke(strokeColor, 50F, 100F, 100F)
            //stroke(strokeColor, 50F, 100F)
            type += 1
        }
    }

    override fun isRecordOnly(): Boolean = true

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    CochleoidMovie().run()
}