package practice

import common.RecorderablePApplet
import kotlin.math.absoluteValue

class VariousSpiralsMovie : RecorderablePApplet() {
    private var bgColor: Int = 0
    private var theta = 0F
    val STEP: Float = PI * 0.01F
    private var type: Int = 1
    private var finCount = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 0F, 255F)
        background(bgColor)
        stroke(color(0F, 0F, 100F, 255F))
    }

    override fun drawBody() {
        if (type < 4) {
            drawSpirals()
        }
        if (type == 4) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
        }
    }

    private fun drawSpirals() {
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        val radius: Float = when (type) {
            1 -> 3 * theta//Archimedes spiral
            2 -> 20 * sqrt(theta)//Fermat spiral
            3 -> pow(1.1F, theta)//Logarithmic spiral
            else -> 0F
        }
        val startX = round(cos(theta) * radius).toFloat()
        val startY = round(sin(theta) * radius).toFloat()
        val endX = round(cos(theta + STEP) * radius).toFloat()
        val endY = round(sin(theta + STEP) * radius).toFloat()
        line(startX, startY, endX, endY)
        theta += STEP
        //println("startX: ${startX}, startY: ${startY}, endX: ${endX}, endY: ${endY}, radisu: ${radius}")
        if ((endX.absoluteValue !in (0F..((getScreenWidth() / 2).toFloat())))
                .or(endY.absoluteValue !in (0F..((getScreenHeight() / 2).toFloat())))
        ) {
            theta = 0F
            type += 1
            if (type < 4) {
                background(bgColor)
            }
        }
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun isRecordOnly(): Boolean {
        return true
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("practice.${this::class.simpleName}", arrayOf())
    }
}

fun main() {
    VariousSpiralsMovie().run()
}