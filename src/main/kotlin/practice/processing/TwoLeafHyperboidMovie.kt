package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector
import kotlin.math.cosh
import kotlin.math.sinh

class TwoLeafHyperboidMovie : Recorderable3DPApplet() {
    private var bgColor: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var u: Float = 0F
    private var v: Float = 0F
    private var zSign: Float = -1F
    private var radius = 25F
    private val diff = 6F

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        noStroke()
        fill(90F, 50F, 40F, 100F)
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
            drawTwoLeafHyperboid()
            if (360F <= v) {
                v = 0F
                u += diff
            } else {
                v += diff
            }
        }
    }

    private fun drawTwoLeafHyperboid() {
        println("u: ${u}, v: ${v}")
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
        var twoLeafHyperboid = genTwoLeafHyperboid(uRad, vRad)
        println("twoLeafHyperboid: ${twoLeafHyperboid}")
        vertex(twoLeafHyperboid.x, twoLeafHyperboid.y, twoLeafHyperboid.z)
        uRad = radians(this.u + diff + 1F)
        vRad = radians(this.v)
        twoLeafHyperboid = genTwoLeafHyperboid(uRad, vRad)
        vertex(twoLeafHyperboid.x, twoLeafHyperboid.y, twoLeafHyperboid.z)
        uRad = radians(this.u + diff + 1F)
        vRad = radians(this.v + diff + 1F)
        twoLeafHyperboid = genTwoLeafHyperboid(uRad, vRad)
        vertex(twoLeafHyperboid.x, twoLeafHyperboid.y, twoLeafHyperboid.z)
        uRad = radians(this.u)
        vRad = radians(this.v + diff + 1F)
        twoLeafHyperboid = genTwoLeafHyperboid(uRad, vRad)
        vertex(twoLeafHyperboid.x, twoLeafHyperboid.y, twoLeafHyperboid.z)
        endShape()
        popMatrix()
        if ((200F <= this.u).and(360F <= this.v).and(0 < zSign)) {
            fin = true
        }
        if ((200F <= this.u).and(360F <= this.v).and(zSign < 0)) {
            this.u = 0F
            zSign *= -1
        }
    }

    private fun genTwoLeafHyperboid(uRad: Float, vRad: Float) = PVector(
        sinh(uRad) * cos(vRad),
        sinh(uRad) * sin(vRad),
        cosh(uRad) * zSign,
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
    TwoLeafHyperboidMovie().run()
}