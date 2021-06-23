package practice

import common.RecorderablePApplet

//ベジェ曲線で多角形を作って並べる
class BezierCurvePolygonMovie : RecorderablePApplet() {
    private var bgColor: Int = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 0F, 255F)
        background(bgColor)
        stroke(color(0F, 0F, 100F, 255F))
    }

    override fun drawBody() {
        noFill()
        beginShape()
        vertex(0F, 0F)
        quadraticVertex(getScreenWidth().toFloat(), 0F, getScreenWidth().toFloat(), getScreenHeight().toFloat())
        endShape()
        if (20 < frameCount) {
            exit()
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
    BezierCurvePolygonMovie().run()
}