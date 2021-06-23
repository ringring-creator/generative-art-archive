package practice

import common.Recorderable3DPApplet
import common.RecorderablePApplet
import processing.core.PVector

class Lissajous3DFigureMovie : Recorderable3DPApplet() {
    private var cycleFin: Boolean = false
    private var ab: Float = 0F
    private var c: Float = 0F//radians(180F) / 4F
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
        strokeWeight(1F)
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
            drawLissajuous3D()
            num += 2F
        }
    }

    private fun drawLissajuous3D() {
        stroke(strokeColor, 50F, 100F, 100F)
        pushMatrix()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
            -(getScreenHeight() / 2).toFloat()
        )
        beginShape()
        val a = ab % 5 + 1F
        val b = ab / 5 + 1F
        var radians = radians(num)
        var lissajous = PVector(
            cos(a * radians),
            sin(b * radians + c),
            cos(a * radians)
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
        fill(strokeColor, 50F, 100F, 100F)
        vertex(0F, 0F, 0F)
        vertex(lissajous.x, lissajous.y, lissajous.z)
        //vertex(lissajous.x, lissajous.y, lissajous.z)
        radians = radians(num + 3F)
        lissajous = PVector(
            cos(a * radians),
            sin(b * radians + c),
            cos(a * radians)
        ).mult(length)
        //vertex(lissajous.x, lissajous.y, lissajous.z)
        vertex(lissajous.x, lissajous.y, lissajous.z)
        endShape()
        popMatrix()
        if (cycleFin) {
            if (20 < ab) {
                fin = true
            } else {
                initVec = PVector(Float.MAX_VALUE, Float.MAX_VALUE)
                num = 0F
                ab += 1F
                strokeColor = random(360F)
                stroke(strokeColor, 50F, 100F, 100F)
                background(bgColor)
            }
            cycleFin = false
            println("ab: ${ab}, a: ${a}, b: ${b}, c: ${c}")
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
    Lissajous3DFigureMovie().run()
}