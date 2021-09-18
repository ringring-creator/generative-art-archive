package practice.processing

import common.RecorderablePApplet
import processing.core.PVector

class RoseCurveMovie : RecorderablePApplet() {
    private var cycleFin: Boolean = false
    private val radius: Float = (getScreenHeight() / 2).toFloat() * 0.9F
    private var num: Float = 0F
    private var nd: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var bgColor: Int = 0
    private var strokeColor: Float = random(360F)

    //http://nalab.mind.meiji.ac.jp/~mk/lecture/tahensuu1-2008/plane-curve/node38.html
    //https://ja.wikipedia.org/wiki/%E3%83%90%E3%83%A9%E6%9B%B2%E7%B7%9A
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
            drawRoseCurve()
            num += 0.5F
        }
    }

    private fun drawRoseCurve() {
        pushMatrix()
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        beginShape()
        val n = (nd % 8 + 1).toFloat()
        val d = (nd / 8 + 1).toFloat()
        var periodsRadians = radians(num * n / d)
        var radians = radians(num)
        var roseCurve = PVector(
            sin(periodsRadians) * cos(radians),
            sin(periodsRadians) * sin(radians)
        ).mult(radius)
        if ((n.toInt() * d.toInt()) % 2 == 1) {
            cycleFin = num == 180 * d
        } else {
            cycleFin = num == 180 * d * 2
        }
        curveVertex(roseCurve.x, roseCurve.y)
        curveVertex(roseCurve.x, roseCurve.y)
        periodsRadians = radians((num + 1F) * n / d)
        radians = radians(num + 1F)
        roseCurve = PVector(
            sin(periodsRadians) * cos(radians),
            sin(periodsRadians) * sin(radians)
        ).mult(radius)
        curveVertex(roseCurve.x, roseCurve.y)
        curveVertex(roseCurve.x, roseCurve.y)
        endShape()
        popMatrix()
        if (cycleFin) {
            num = 0F
            nd += 1
            if ((2 <= nd % 8 + 1).and((nd % 8 + 1).toFloat() == (nd / 8 + 1).toFloat())) {
                nd += 1
            }
            background(bgColor)
            strokeColor = random(360F)
            stroke(strokeColor, 50F, 100F, 100F)
            cycleFin = false
        }
        if (8F * 8F < nd) {
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
    RoseCurveMovie().run()
}