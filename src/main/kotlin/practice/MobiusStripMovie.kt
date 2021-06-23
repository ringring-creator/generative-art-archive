package practice

import common.Recorderable3DPApplet
import processing.core.PVector

class MobiusStripMovie : Recorderable3DPApplet() {
    val uMaxLength = 0.3F
    val uMinLength = -0.3F
    val vMaxLength = 360F
    val vMinLength = 0F
    private var bgColor: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var u: Float = uMinLength
    private var v: Float = vMinLength
    private val vDiff = 3F
    private val uDiff = 0.02F
    private val radius = 800F

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
            directionalLight(0F, 0F, 100F, 0.1F, 0.5F, -1F);
            drawMobiusStrip()
            if (vMaxLength <= v) {
                u += uDiff
                v = vMinLength
            } else {
                v += vDiff
            }
        }
    }

    private fun drawMobiusStrip() {
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
            0F//1F
        )
        beginShape()
        var uVad = this.u
        var vVad = radians(this.v)
        var torus = genMobiusStrip(uVad, vVad)
        println("torus: ${torus}")
        vertex(torus.x, torus.y, torus.z)
        uVad = this.u + uDiff * 2
        vVad = radians(this.v)
        torus = genMobiusStrip(uVad, vVad)
        vertex(torus.x, torus.y, torus.z)
        uVad = this.u + uDiff * 2
        vVad = radians(this.v + vDiff * 2)
        torus = genMobiusStrip(uVad, vVad)
        vertex(torus.x, torus.y, torus.z)
        uVad = this.u
        vVad = radians(this.v + vDiff * 2)
        torus = genMobiusStrip(uVad, vVad)
        vertex(torus.x, torus.y, torus.z)
        endShape()
        popMatrix()
        if ((uMaxLength <= this.u).and(vMaxLength <= this.v)) {
            fin = true
        }
    }

    private fun genMobiusStrip(uRad: Float, vRad: Float): PVector = PVector(
        (1F + uRad * cos(vRad / 2F) * cos(vRad) - 1F),
        (1F + uRad * cos(vRad / 2F) * sin(vRad) - 1F),
        u * sin(vRad / 2F)
    ).mult(radius)

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("practice.${this::class.simpleName}", arrayOf())
    }
}

fun main() {
    MobiusStripMovie().run()
}