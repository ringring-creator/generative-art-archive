package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector

class EnneperSurfaceMovie : Recorderable3DPApplet() {
    val uMaxLength = 2F
    val uMinLength = -2F
    val vMaxLength = 2F
    val vMinLength = -2F
    private var bgColor: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var u: Float = uMinLength
    private var v: Float = vMinLength
    private val vDiff = 0.05F
    private val uDiff = 0.05F
    private val radius = 100F

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        noStroke()
        fill(270F, 50F, 40F, 100F)
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
            directionalLight(0F, 0F, 100F, 0.1F, 0.5F, -1F);
            drawEnneperSurface()
            if (vMaxLength <= v) {
                u += uDiff
                v = vMinLength
            } else {
                v += vDiff
            }
        }
    }

    private fun drawEnneperSurface() {
        println("u: ${u}, v: ${v}")
        pushMatrix()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
        )
        camera(
            getScreenWidth().toFloat() / 2,
            -getScreenHeight().toFloat() / 2,
            getScreenHeight().toFloat(),
            0.0F,
            0.0F,
            0.0F,
            0.3F,
            -1F,
            //1F
            -1F
        )
        beginShape()
        var uVad = this.u
        var vVad = this.v
        var enneperSurface = genEnneperSurface(uVad, vVad)
        println("polyhedron: ${enneperSurface}")
        vertex(enneperSurface.x, enneperSurface.y, enneperSurface.z)
        uVad = this.u + uDiff * 2
        vVad = this.v
        enneperSurface = genEnneperSurface(uVad, vVad)
        vertex(enneperSurface.x, enneperSurface.y, enneperSurface.z)
        uVad = this.u + uDiff * 2
        vVad = this.v + vDiff * 2
        enneperSurface = genEnneperSurface(uVad, vVad)
        vertex(enneperSurface.x, enneperSurface.y, enneperSurface.z)
        uVad = this.u
        vVad = this.v + vDiff * 2
        enneperSurface = genEnneperSurface(uVad, vVad)
        vertex(enneperSurface.x, enneperSurface.y, enneperSurface.z)
        endShape()
        popMatrix()
        if ((uMaxLength <= this.u).and(vMaxLength <= this.v)) {
            fin = true
        }
    }

    private fun genEnneperSurface(uRad: Float, vRad: Float): PVector = PVector(
        uRad - pow(uRad, 3F) / 3 + u * pow(vRad, 2F),
        vRad - pow(vRad, 3F) / 3 + pow(uRad, 2F) * vRad,
        pow(uRad, 2F) - pow(vRad, 2F)
    ).mult(radius)

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    EnneperSurfaceMovie().run()
}