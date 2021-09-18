package practice.processing

import common.RecorderablePApplet

class InscribedPolyhedronMovie : RecorderablePApplet() {
    private var radius: Float = 0.0f
    private var initAngle: Float = 0F
    private var bgColor: Int = 0
    private var vertexNum: Int = 3
    private var finishSceneCount: Int = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 255F, 255F)
        background(bgColor)
        radius = getScreenHeight() / 10F * 4F
    }

    override fun drawBody() {
        if (frameCount % 100 == 0) {
            println("frameCount: ${frameCount}")
        }
        if (finishSceneCount == 20 * getFps().toInt()) {
            exit()
        }
        if (vertexNum == 16) {
            println("finishSceneCount: ${finishSceneCount}")
            finishSceneCount += 1
            return
        }
        if (radius < 1F) {
            println("radius < 1F")
            background(bgColor)
            initAngle = 0F
            radius = getScreenHeight() / 10F * 4F
            vertexNum += 1
            println("vertexNum: ${vertexNum}")
        }
        if (frameCount % 30 == 1) {
            drawInscribedPolygon(vertexNum)
        }
    }

    private fun drawInscribedPolygon(vertexNum: Int) {
        fill(color(random(360F), 50F, 100F, 255F))
        val angle = TWO_PI / vertexNum.toFloat()
        pushMatrix()
        translate(getScreenWidth() / 2F, getScreenHeight() / 2F)
        stroke(0F, 0F, 0F, 255F)
        beginShape()
        repeat(vertexNum + 1) {
            val x = cos(initAngle + angle * it) * radius
            val y = sin(initAngle + angle * it) * radius
            vertex(x, y)
        }
        endShape()
        popMatrix()
        initAngle += angle / 2F
        radius = radius * cos(angle / 2F)
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    override fun isRecordOnly(): Boolean {
        return true
        //return false
    }

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    InscribedPolyhedronMovie().run()
}