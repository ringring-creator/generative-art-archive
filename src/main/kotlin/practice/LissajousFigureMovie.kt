package practice

import common.RecorderablePApplet
import processing.core.PVector

class LissajousFigureMovie : RecorderablePApplet() {
    private var cycleFin: Boolean = false
    private var ab: Float = 0F
    private var c: Float = 0F
    private var finCount = 0
    private var fin: Boolean = false
    private var bgColor: Int = 0
    private var num: Float = 0F
    private var strokeColor: Float = random(360F)
    private val length: Float = (getScreenHeight() / 2).toFloat() * 0.9F
    private var initVec: PVector = PVector(Float.MAX_VALUE, Float.MAX_VALUE)

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
            drawLissajousFigure()
            num += 1F
        }
    }

    private fun drawLissajousFigure() {
        pushMatrix()
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        beginShape()
        val a = ab % 5 + 1F
        val b = ab / 5 + 1F
        var radians = radians(num)
        var lissajous = PVector(
            cos(a * radians),
            sin(b * radians + c)
        ).mult(length)
        println("lissajous: ${lissajous}, num: ${num}, ab: ${ab}")
        if ((initVec.x == Float.MAX_VALUE).and(initVec.y == Float.MAX_VALUE)) {
            initVec = lissajous
        } else {
            println("initVec: ${initVec}, lissajous: ${lissajous}, initVec.dist(lissajous): ${initVec.dist(lissajous)}")
            if (initVec.dist(lissajous) < 0.1F) {
                cycleFin = true
            }
        }

        curveVertex(lissajous.x, lissajous.y)
        curveVertex(lissajous.x, lissajous.y)
        radians = radians(num + 1F)
        lissajous = PVector(
            cos(a * radians),
            sin(b * radians + c)
        ).mult(length)
        curveVertex(lissajous.x, lissajous.y)
        curveVertex(lissajous.x, lissajous.y)
        endShape()
        popMatrix()
        if (cycleFin) {
            initVec = PVector(Float.MAX_VALUE, Float.MAX_VALUE)
            if (radians(180F) < c) {
                fin = true
            }
            num = 0F
            ab += 1F
            strokeColor = random(360F)
            stroke(strokeColor, 50F, 100F, 100F)
            background(bgColor)
            if (20 < ab) {
                ab = 0F
                c += radians(180F) / 4F
            }
            cycleFin = false
            println("ab: ${ab}, a: ${a}, b: ${b}, c: ${c}")
        }
    }

    override fun isRecordOnly(): Boolean = true

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("practice.${this::class.simpleName}", arrayOf())
    }
}

fun main() {
    LissajousFigureMovie().run()
}