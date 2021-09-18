package practice.processing

import common.RecorderablePApplet
import geomerative.RFont
import geomerative.RG
import geomerative.RPoint

class OutlineFontMyLogo : RecorderablePApplet() {
    private val radius: Float = (getScreenWidth() / 4).toFloat()
    private var pnts: Array<RPoint> = arrayOf()
    private var bgColor: Int = 0
    private val text = "Chihiro".toCharArray()
    private var textIdx = -1
    private var pntsIdx = 0
    private val sizeText = 40
    private var finCount = 0

    override fun setupBody() {
        println("setupBody")
        //set black background
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        RG.init(this)
        println("text: ${text[0]}, ${text[1]}")
        val color = color(0F, 0F, 0F, 100F)
        stroke(color)
        fill(color)
    }

    override fun drawBody() {
        if (pnts.size == pntsIdx || pntsIdx == 0) {
            textIdx += 1
            if (text.size - 1 < textIdx) {
                if (finCount == getFps().toInt() * 20) {
                    exit()
                }
                finCount += 1
                return
            }
            println("text.size: ${text.size}, textIdx: ${textIdx}")
            val text = RG.getText(text[textIdx].toString(), "RechtmanPlain.ttf", sizeText, RFont.LEFT)
            //val text = RG.getText(text[textIdx].toString(), "FreeSans.ttf", sizeText, RFont.LEFT)
            pnts = text.getPoints().let { it }
            pntsIdx = 1
        }
        pushMatrix()
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat());
        val degree = 360F / text.size.toFloat() * textIdx
        println("degree: ${degree}")
        val theta = radians(degree)
        println("theta: ${theta}, TWO_PI: ${TWO_PI}")
        translate(radius * cos(theta - PI / 2), radius * sin(theta - PI / 2))
        rotate(theta)
        ellipse(
            pnts[pntsIdx - 1].x,
            pnts[pntsIdx - 1].y,
            1F, 1F
        )
        pntsIdx += 1
        popMatrix()
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getScreenWidth(): Int = 98
    //override fun getScreenWidth(): Int = 500

    override fun getScreenHeight(): Int = 98
    //override fun getScreenHeight(): Int = 500

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    OutlineFontMyLogo().run()
}