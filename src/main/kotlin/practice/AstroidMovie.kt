package practice

import common.RecorderablePApplet
import processing.core.PVector

class AstroidMovie : RecorderablePApplet() {
    private var strokeColor: Float = random(360F)
    private var bgColor: Int = 0
    private var num: Int = 0
    private var type: Int = 0
    private var finCount = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
    }

    override fun drawBody() {
        if (type != 7) {
            drawAsteroid()
            num += 1
        }
        if (type == 7) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
        }
    }

    private fun drawAsteroid() {
        val length = getScreenHeight() / 3
        pushMatrix()
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        if (type < 3) {
            strokeWeight(4F)
            stroke(0F, 0F, 0F, 100F)
            ellipse(
                length * 3 / 4 * cos(radians(num.toFloat())),
                length * 3 / 4 * sin(radians(num.toFloat())),
                (length / 2).toFloat(), (length / 2).toFloat()
            )
            strokeWeight(8F)
            stroke(strokeColor, 50F, 100F, 100F)
            line(
                length * 3 / 4 * cos(radians(num.toFloat())),
                length * 3 / 4 * sin(radians(num.toFloat())),
                pow(cos(radians(num.toFloat())), 3F) * length,
                pow(sin(radians(num.toFloat())), 3F) * length
            )
        }
        strokeWeight(8F)
        stroke(strokeColor, 50F, 100F, 100F)
        beginShape()
        var astroid = PVector(
            pow(cos(radians(num.toFloat())), 3F),
            pow(sin(radians(num.toFloat())), 3F)
        ).mult(length.toFloat())
        println("asteroids: $astroid")
        curveVertex(astroid.x, astroid.y)
        curveVertex(astroid.x, astroid.y)
        astroid = PVector(
            pow(cos(radians((num + 1).toFloat())), 3F),
            pow(sin(radians((num + 1).toFloat())), 3F)
        ).mult(length.toFloat())
        println("asteroids: $astroid")
        curveVertex(astroid.x, astroid.y)
        curveVertex(astroid.x, astroid.y)
        endShape()
        popMatrix()
        if (num == 360) {
            num = 0
            strokeColor = random(360F)
            type += 1
            if (type == 3) {
                background(bgColor)
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
    AstroidMovie().run()
}