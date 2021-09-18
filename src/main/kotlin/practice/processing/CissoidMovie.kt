package practice.processing

import common.RecorderablePApplet
import processing.core.PVector

class CissoidMovie : RecorderablePApplet() {
    private var param: Float = 0F
    private var strokeColor: Float = random(360F)
    private var bgColor: Int = 0
    private var finCount = 0

    private val variation = 0.01F

    //private val variation = 0.1F
    private var leftFin = false
    private var denominator = 2
    private var radian = radians(90F)

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        stroke(strokeColor, 50F, 100F, 100F)
        strokeWeight(4F)
    }


    override fun drawBody() {
        if ((10 < denominator).and(radian == radians(270F))) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
        } else {
            drawCissoid()
        }
        param += variation
    }


    private fun drawCissoid() {
        pushMatrix()
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        rotate(radian)
        val a = getScreenHeight() / denominator
        var paramSquare = param * param
        beginShape()
        var cissoid = PVector(
            ((a * paramSquare) / (1 + paramSquare)),
            ((a * paramSquare * this.param) / (1 + paramSquare))
        )
        curveVertex(cissoid.x, cissoid.y)
        curveVertex(cissoid.x, cissoid.y)
        paramSquare = (param + variation) * (param + variation)
        cissoid = PVector(
            ((a * paramSquare) / (1 + paramSquare)),
            ((a * paramSquare * (this.param + variation)) / (1 + paramSquare))
        )
        curveVertex(cissoid.x, cissoid.y)
        curveVertex(cissoid.x, cissoid.y)
        endShape()
        popMatrix()
        if (getScreenWidth() / 2 < cissoid.y) {
            param *= -1
            leftFin = true
        }
        if ((0F < param).and(leftFin)) {
            denominator += 1
            strokeColor = random(360F)
            stroke(strokeColor, 50F, 100F, 100F)
            leftFin = false
        }
        if ((denominator == 10).and(radian == radians(90F))) {
            radian = radians(270F)
            denominator = 2
        }
        println("param: ${param}, denominator: ${denominator}, leftFin: ${leftFin}, radian: ${radian}")
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
    CissoidMovie().run()
}