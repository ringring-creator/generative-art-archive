package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector

class TorusKnotMovie : Recorderable3DPApplet() {
    val maxLength = 360F
    val minLength = 0F
    private var bgColor: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var u: Float = minLength
    private var v: Float = minLength
    private var p: Float = 1F
    private var q: Float = 1F
    private val diff = 1F
    private val radius = 40F
    private val radiusL = 400F

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
            drawTorus()
            if (maxLength <= u) {
                u = minLength
                v += diff
            } else {
                u += diff
            }
        }
    }

    private fun drawTorus() {
        println("u: ${u}, v: ${v}")
        pushMatrix()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
        )
        camera(
            getScreenWidth().toFloat() / 3,
            -getScreenHeight().toFloat() / 3,
            getScreenHeight().toFloat(),
            0.0F,
            0.0F,
            0.0F,
            0.3F,
            0.3F,
            1F
        )
        beginShape()
        var uVad = radians(this.u)
        //var vVad = radians(this.v)
        var torus = genTorus(uVad, uVad)
        println("torus: ${torus}")
        vertex(torus.x, torus.y, torus.z)
        uVad = radians(this.u + diff)
        //vVad = radians(this.v)
        torus = genTorus(uVad, uVad)
        vertex(torus.x, torus.y, torus.z)
        uVad = radians(this.u + diff + diff)
        //vVad = radians(this.v)
        torus = genTorus(uVad, uVad)
        vertex(torus.x, torus.y, torus.z)
        uVad = radians(this.u + diff + diff + diff)
        //vVad = radians(this.v)
        torus = genTorus(uVad, uVad)
        vertex(torus.x, torus.y, torus.z)
        //uVad = radians(this.u + diff + diff * 2)
        //vVad = radians(this.v + diff + diff * 2)
        //torus = genTorus(uVad, vVad)
        //vertex(torus.x, torus.y, torus.z)
        //uVad = radians(this.u)
        //vVad = radians(this.v + diff + diff * 2)
        //torus = genTorus(uVad, vVad)
        //vertex(torus.x, torus.y, torus.z)
        endShape()
        popMatrix()
        //if ((maxLength <= this.u).and(maxLength <= this.v)) {
        if ((maxLength <= this.u)) {
            if (p < 10F) {
                background(bgColor)
                p += 1F
            } else if (q < 10F) {
                background(bgColor)
                q += 1F
                p = 1F
            } else {
                fin = true
            }
        }
    }

    private fun genTorus(uRad: Float, vRad: Float): PVector = PVector(
        (radiusL + radius * cos(p * uRad)) * cos(q * vRad),
        (radiusL + radius * cos(p * uRad)) * sin(q * vRad),
        radius * sin(p * uRad)
    )

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

fun main() {
    TorusKnotMovie().run()
}