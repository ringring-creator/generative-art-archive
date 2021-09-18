package practice.processing

import common.RecorderablePApplet
import processing.core.PVector

class SpiralPolygonMovie : RecorderablePApplet() {
    private val GAP: Float = 0.05F
    private var bgColor: Int = 0
    private var strokeColor: Int = 0
    private var vertexNum: Int = 3
    private var vec: MutableList<PVector> = mutableListOf()
    private var finCount = 0
    private var type: Int = 1

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        strokeColor = color(0F, 0F, 0F, 100F)
        stroke(strokeColor)
        initVec()
    }

    override fun drawBody() {
        translate((getScreenWidth() / 2).toFloat(), (getScreenHeight() / 2).toFloat())
        if ((vertexNum < 11).and(frameCount % 5 == 1)) {
            println("vertexNum: ${vertexNum}, type: ${type}, ")
            drawPolygon()
            updateVec()
            var isFin = true
            for (ele in vec) {
                if (isFin) {
                    isFin = (round(ele.x) == 0).and(round(ele.y) == 0)
                }
            }
            if (isFin) {
                if (type == 3) {
                    strokeColor = color(random(360F), 50F, 100F, 255F)
                }
                vertexNum += 1
                if (!(type == 3).and(10 < vertexNum)) {
                    stroke(strokeColor)
                    background(bgColor)
                    initVec()
                }
            }
        }
        if ((type < 3).and(10 < vertexNum)) {
            println("vertexNum: ${vertexNum}, type: ${type}, ")
            type += 1
            when (type) {
                2 -> {
                    bgColor = color(0F, 0F, 0F, 255F)
                    strokeColor = color(0F, 0F, 100F, 255F)
                }
                3 -> {
                    bgColor = color(0F, 0F, 0F, 255F)
                    strokeColor = color(random(360F), 50F, 100F, 255F)
                }
            }
            stroke(strokeColor)
            background(bgColor)
            vertexNum = 3
            initVec()
        }
        if ((3 <= type).and(10 < vertexNum)) {
            println("vertexNum: ${vertexNum}, type: ${type}, finCount: ${finCount}")
            if (finCount == getFps().toInt() * 20) {
                exit()
            }
            finCount += 1
        }
    }


    override fun exitBody() {
        println("exitBody")
    }

    override fun isRecordOnly(): Boolean {
        return true
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    private fun initVec() {
        vec.clear()
        for (idx in 0..(vertexNum - 1)) {
            vec.add(PVector.fromAngle(2 * idx * PI / vertexNum))
            vec.get(idx).mult((getScreenHeight() / 2).toFloat())
        }
    }

    private fun drawPolygon() {
        for (idx in 0..(vertexNum - 1)) {
            line(vec.get(idx).x, vec.get(idx).y, vec.get((idx + 1) % vertexNum).x, vec.get((idx + 1) % vertexNum).y)
        }
    }

    private fun updateVec() {
        var result = mutableListOf<PVector>()
        for (idx in 0..(vertexNum - 1)) {
            val subVec = PVector.sub(vec.get((idx + 1) % vertexNum), vec.get(idx))
            subVec.mult(GAP)
            result.add(PVector.add(vec.get(idx), subVec))
        }
        vec = result
    }

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    SpiralPolygonMovie().run()
}