package practice

import common.RecorderablePApplet
import processing.core.PVector

class EpitrochoidMovie : RecorderablePApplet() {
    private var showEllipse: Boolean = true
    private var fin: Boolean = false
    private var bgColor: Int = 0
    private var strokeColor: Float = random(360F)
    private var r: Float = (getScreenHeight() / 16).toFloat()
    private var k: Float = 2F
    private var num: Float = 0F
    private var finCount = 0
    private var initVec: PVector = PVector(Float.MAX_VALUE, Float.MAX_VALUE)
    private var cycleFin: Boolean = false

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
            drawEpitrochoid()
            num += 1
        }
        println("k: ${k}, showEllipse: ${showEllipse}")
    }

    private fun drawEpitrochoid() {
        var R: Float = k * r
        pushMatrix()
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        var radians = radians(num)
        if (showEllipse) {
            stroke(0F, 0F, 0F, 100F)
            ellipse(0F, 0F, R * 2, R * 2)
            ellipse((R + r) * cos(radians), (R + r) * sin(radians), r * 2, r * 2)
            stroke(strokeColor, 50F, 100F, 100F)
            line(
                (R + r) * cos(radians),
                (R + r) * sin(radians),
                (R + r) * cos(radians) - r * cos((R + r) / r * radians),
                (R + r) * sin(radians) - r * sin((R + r) / r * radians)
            )
        }
        stroke(strokeColor, 50F, 100F, 100F)
        beginShape()
        var epitrochoid = PVector(
            (R + r) * cos(radians) - r * cos((R + r) / r * radians),
            (R + r) * sin(radians) - r * sin((R + r) / r * radians)
        )
        if ((initVec.x == Float.MAX_VALUE).and(initVec.y == Float.MAX_VALUE)) {
            initVec = epitrochoid
        } else {
            println(
                "initVec: ${initVec}, epitrochoid: ${epitrochoid}, initVec.dist(epitrochoid): ${
                    initVec.dist(
                        epitrochoid
                    )
                }"
            )
            if (initVec.dist(epitrochoid) < 0.4F) {
                cycleFin = true
            }
        }
        curveVertex(epitrochoid.x, epitrochoid.y)
        curveVertex(epitrochoid.x, epitrochoid.y)
        radians = radians(num + 1)
        epitrochoid = PVector(
            (R + r) * cos(radians) - r * cos((R + r) / r * radians),
            (R + r) * sin(radians) - r * sin((R + r) / r * radians)
        )
        curveVertex(epitrochoid.x, epitrochoid.y)
        curveVertex(epitrochoid.x, epitrochoid.y)
        endShape()
        popMatrix()
        if ((360 < num).and(showEllipse)) {
            k += 1
            num = 0F
            strokeColor = random(360F)
            background(bgColor)
            if (5 < k) {
                k = 2F
                showEllipse = false
            }
        }
        if (cycleFin.and(showEllipse.not())) {
            if (5 < k) {
                fin = true
            } else {
                k += 0.2F
                num = 0F
                strokeColor = random(360F)
                background(bgColor)
                cycleFin = false
                initVec = PVector(Float.MAX_VALUE, Float.MAX_VALUE)
            }
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
    EpitrochoidMovie().run()
}