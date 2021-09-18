package practice.processing

import common.RecorderablePApplet

class GoldenRatioSpiralMovie : RecorderablePApplet() {
    private var bgColor: Int = 0
    private var goldRatio: Float = (1 + sqrt(5F)) / 2

    private var rectX = 0F
    private var rectY = 0F
    private var rectWidth = 0F
    private var rectHeight = 0F
    private var finCount = 0
    private val spiralInfos: MutableList<SpiralInfo> = mutableListOf()
    private var spiralInfoIdx: Int = 0
    private var quadrant: Int = 2
    private var start = 180F
    private var spiralCurveIdx: Int = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 0F, 255F)
        background(bgColor)
        rectWidth = getScreenHeight().toFloat() * goldRatio
        rectHeight = getScreenHeight().toFloat()
        rect(rectX, rectY, rectWidth, rectHeight)
    }

    override fun drawBody() {
        /*test1()
        return*/
        if ((frameCount % 30 == 1).and(1 < rectWidth)) {
            drawRatioRect()
        }
        if ((frameCount % 30 == 1).and(rectWidth < 1).and(spiralInfoIdx != spiralInfos.size - 1)) {
            darwGoldenRatioSpiral()
        }
        if ((frameCount % 2 == 1).and(rectWidth < 1).and(spiralInfoIdx == spiralInfos.size - 1)
                .and(spiralCurveIdx != spiralInfos.size - 1)
        ) {
            darwGoldenRatioSpiralCurve()
        }
        if ((rectWidth < 1).and(spiralCurveIdx == spiralInfos.size - 1)) {
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
        }
    }

    private fun darwGoldenRatioSpiralCurve() {
        strokeWeight(4F)
        stroke(color(33F, 77F, 83F, 100F))
        val spiralInfo = spiralInfos.get(spiralCurveIdx)
        pushMatrix()
        translate(spiralInfo.centerX, spiralInfo.centerY)
        beginShape()
        val startX = round(cos(radians(start)) * spiralInfo.radius).toFloat()
        val startY = round(sin(radians(start)) * spiralInfo.radius).toFloat()
        curveVertex(startX, startY)
        curveVertex(startX, startY)
        val endX = round(cos(radians(start - 1)) * spiralInfo.radius).toFloat()
        val endY = round(sin(radians(start - 1)) * spiralInfo.radius).toFloat()
        curveVertex(endX, endY)
        curveVertex(endX, endY)
        println("$startX, $startY, " + "$endX, $endY")
        endShape()
        start -= 1
        if (start < 0) {
            start = 360F
        }
        println("start: ${start}")
        if (start == 90F * (spiralInfo.quadrant - 1)) {
            spiralCurveIdx += 1
        }
        popMatrix()
    }

    private fun darwGoldenRatioSpiral() {
        stroke(bgColor)
        fill(45F * spiralInfoIdx, 50F, 100F, 100F)
        val spiralInfo = spiralInfos.get(spiralInfoIdx)
        val start = 90F * (spiralInfo.quadrant - 1)
        arc(
            spiralInfo.centerX,
            spiralInfo.centerY,
            (spiralInfo.radius * 2F),
            (spiralInfo.radius * 2F),
            radians(start),
            radians(start + 90)
        )
        println(
            "spiralInfo.centerX: ${spiralInfo.centerX}, spiralInfo.centerY: ${spiralInfo.centerY}, " +
                    "spiralInfo.radius * 2F: ${spiralInfo.radius * 2F}, start: ${start}, quadrant: ${spiralInfo.quadrant}"
        )
        spiralInfoIdx += 1
    }

    private fun drawRatioRect() {
        val color = color(0F, 0F, 100F, 100F)
        fill(color)
        //3|4
        //-|-
        //2|1
        when (quadrant) {
            1 -> {//Division1
                rectHeight = rectHeight - rectWidth
                spiralInfos.add(SpiralInfo(rectX, rectY + rectHeight, rectWidth, quadrant))
            }
            2 -> {//Division2
                rectWidth = rectWidth - rectHeight
                rectX = rectX + rectHeight
                spiralInfos.add(SpiralInfo(rectX, rectY, rectHeight, quadrant))
            }
            3 -> {//Division3
                rectHeight = rectHeight - rectWidth
                rectY = rectY + rectWidth
                spiralInfos.add(SpiralInfo(rectX + rectWidth, rectY, rectWidth, quadrant))
            }
            4 -> {//Division4
                rectWidth = rectWidth - rectHeight
                spiralInfos.add(SpiralInfo(rectX + rectWidth, rectY + rectHeight, rectHeight, quadrant))
            }
        }
        rect(rectX, rectY, rectWidth, rectHeight)
        quadrant -= 1
        if (quadrant == 0) {
            quadrant = 4
        }
        println("rectX: ${rectX}, rectY: ${rectY}, rectWidth: ${rectWidth}, rectHeight: ${rectHeight}")
    }

    private fun test1() {
        stroke(0F, 0F, 100F, 100F)
        rect(
            (getScreenWidth() / 2).toFloat() - 150F,
            (getScreenHeight() / 2).toFloat() - 150F,
            300F,
            300F
        )
        fill(0F, 0F, 100F, 100F)
        fill(0F, 20F, 100F, 100F)
        ellipse(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
            300F,
            300F
        )
        fill(180F, 70F, 100F, 100F)
        arc(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
            300F,
            300F,
            0F,
            HALF_PI
        )
        delay(10000)
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }

    class SpiralInfo(val centerX: Float, val centerY: Float, val radius: Float, val quadrant: Int)
}

fun main() {
    GoldenRatioSpiralMovie().run()
}