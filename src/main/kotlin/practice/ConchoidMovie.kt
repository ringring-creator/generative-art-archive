package practice

import common.RecorderablePApplet
import processing.core.PVector

class ConchoidMovie : RecorderablePApplet() {
    private var strokeColor: Float = random(360F)
    private var bgColor: Int = 0
    private var num: Int = 0
    var l = getScreenHeight() / 2
    private var finCount = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        strokeWeight(4F)
        stroke(strokeColor, 50F, 100F, 100F)
    }

    override fun drawBody() {
        //if ((10 < aDenominator).and(10 < lDenominator)) {
        if (l < 100) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
        } else {
            drawConchoid()
            num += 1
        }
    }

    private fun drawConchoid() {
        pushMatrix()
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        rotate(radians(90F))
        val a = getScreenHeight() / 20
        beginShape()
        var radians = radians(num.toFloat())
        var conchoid = PVector(
            a + l * cos(radians),
            a * tan(radians) + l * sin(radians)
        )
        curveVertex(conchoid.x, conchoid.y)
        curveVertex(conchoid.x, conchoid.y)
        radians = radians(num.toFloat() + 1F)
        conchoid = PVector(
            a + l * cos(radians),
            a * tan(radians) + l * sin(radians)
        )
        curveVertex(conchoid.x, conchoid.y)
        curveVertex(conchoid.x, conchoid.y)
        endShape()
        popMatrix()
        if (num == 360) {
            strokeColor = random(360F)
            stroke(strokeColor, 50F, 100F, 100F)
            num = 0
            //if (aDenominator < 10) {
            //aDenominator += 1
            //} else {
            //aDenominator = 3
            l -= 50
            //}
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
    ConchoidMovie().run()
}