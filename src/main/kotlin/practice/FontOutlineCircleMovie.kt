package practice

import common.RecorderablePApplet
import geomerative.RFont
import geomerative.RG
import geomerative.RPoint

class FontOutlineCircleMovie : RecorderablePApplet() {
    private var pnts: Array<RPoint> = arrayOf()
    private var bgColor: Int = 0

    private val text = (('a'..'z') + ('A'..'Z') + ('0'..'9') + ('あ'..'ん'))
    private var textIdx = -1
    private var pntsIdx = 0
    private val sizeText = 120
    private var finCount = 0

    override fun setupBody() {
        println("setupBody")
        //set black background
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        RG.init(this)
        println("text: ${text}")
    }

    override fun drawBody() {
        if (pnts.size == pntsIdx || pntsIdx == 0) {
            val color = color(random(360F), 50F, 100F, 100F)
            stroke(color)
            fill(color)
            textIdx += 1
            if (text.size <= textIdx) {
                if (finCount == getFps().toInt() * 20) {
                    exit()
                }
                finCount += 1
                return
            }
            println("textIdx: ${textIdx}")
            val text = RG.getText(text[textIdx].toString(), "FreeSans.ttf", 120, RFont.LEFT)
            pnts = text.getPoints().let { it }
            pntsIdx = 1
        }
        ellipse(
            pnts[pntsIdx - 1].x + (sizeText * (textIdx % 16)).toFloat(),
            pnts[pntsIdx - 1].y + sizeText + (sizeText * (textIdx / 16)).toFloat(),
            4F, 4F
        )
        pntsIdx += 1
    }

    override fun exitBody() {
        println("exitBody")
    }

    //override fun isRecordOnly(): Boolean = false
    override fun isRecordOnly(): Boolean = true

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("practice.${this::class.simpleName}", arrayOf())
    }
}

fun main() {
    FontOutlineCircleMovie().run()
}