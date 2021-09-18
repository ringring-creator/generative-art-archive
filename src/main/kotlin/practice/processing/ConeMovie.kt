package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector

class ConeMovie : Recorderable3DPApplet() {
    private var bgColor: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var u: Float = -180F
    private var v: Float = 0F
    private var radius = 150F
    private val diff = 6F

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        noStroke()
        fill(0F, 0F, 40F, 100F)
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
            directionalLight(0F, 0F, 100F, 0F, 0.5F, -1F);
            drawCone()
            if (360F <= v) {
                v = 0F
                u += diff
            } else {
                v += diff
            }
        }
    }

    private fun drawCone() {
        pushMatrix()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
            -(getScreenHeight() / 2).toFloat()
        )
        rotateX(PI / 3F)
        rotateZ(PI / 6F)
        beginShape()
        var uRad = radians(this.u)
        var vRad = radians(this.v)
        var cone = PVector(
            cos(vRad),
            sin(vRad),
            1F,
        ).mult(radius * uRad)
        println("cone: ${cone}")
        vertex(cone.x, cone.y, cone.z)
        uRad = radians(this.u + diff + 1F)
        vRad = radians(this.v)
        cone = PVector(
            cos(vRad),
            sin(vRad),
            1F,
        ).mult(radius * uRad)
        vertex(cone.x, cone.y, cone.z)
        uRad = radians(this.u + diff + 1F)
        vRad = radians(this.v + diff + 1F)
        cone = PVector(
            cos(vRad),
            sin(vRad),
            1F,
        ).mult(radius * uRad)
        vertex(cone.x, cone.y, cone.z)
        uRad = radians(this.u)
        vRad = radians(this.v + diff + 1F)
        cone = PVector(
            cos(vRad),
            sin(vRad),
            1F,
        ).mult(radius * uRad)
        vertex(cone.x, cone.y, cone.z)
        endShape()
        popMatrix()
        if ((180F <= this.u).and(360F <= this.v)) {
            fin = true
        }
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    ConeMovie().run()
}