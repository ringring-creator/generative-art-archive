package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector
import kotlin.math.log
import kotlin.math.tan

class AppleTorusMovie : Recorderable3DPApplet() {
    private var bgColor: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var u: Float = -180F
    private var v: Float = 0F
    private val diff = 3F
    private val radius = 50F
    private var type = 1

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        noStroke()
        fill(0F, 60F, 50F, 100F)
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
            when (type) {
                1 -> drawTorus(::genTorus1)
                2 -> drawTorus(::genTorus2)
                3 -> drawTorus(::genTorus3)
                4 -> drawTorus(::genTorus4)
            }

            if (180F <= u) {
                u = -180F
                v += diff
            } else {
                u += diff
            }
        }
    }

    private fun drawTorus(genTorus: (uRad: Float, vRad: Float) -> PVector) {
        println("u: ${u}, v: ${v}")
        pushMatrix()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
        )
        /*camera(
            //getScreenWidth().toFloat() / 2,
            0F,
            getScreenHeight().toFloat() / 2,
            getScreenHeight().toFloat() / 2,
            0.0F,
            0.0F,
            0.0F,
            0F,
            -1F,
            0F
        )*/
        camera(
            0F,
            getScreenHeight().toFloat(),
            ((height / 2.0) / tan(PI * 30.0 / 180.0)).toFloat(),
            0F,
            0F,
            0F,
            0F,
            1F,
            0F
        )
        beginShape()
        var uVad = radians(this.u)
        var vVad = radians(this.v)
        var torus = genTorus(uVad, vVad)
        println("torus: ${torus}")
        vertex(torus.x, torus.y, torus.z)
        uVad = radians(this.u + diff + diff * 2)
        vVad = radians(this.v)
        torus = genTorus(uVad, vVad)
        vertex(torus.x, torus.y, torus.z)
        uVad = radians(this.u + diff + diff * 2)
        vVad = radians(this.v + diff + diff * 2)
        torus = genTorus(uVad, vVad)
        vertex(torus.x, torus.y, torus.z)
        uVad = radians(this.u)
        vVad = radians(this.v + diff + diff * 2)
        torus = genTorus(uVad, vVad)
        vertex(torus.x, torus.y, torus.z)
        endShape()
        popMatrix()
        if ((180F <= this.u).and(360F <= this.v).and(type == 4)) {
            fin = true
        }
        if ((180F <= this.u).and(360F <= this.v).and(type < 4)) {
            type += 1
            this.u = -180F
            this.v = 0F
        }
    }

    private fun genTorus1(uRad: Float, vRad: Float): PVector = PVector(
        (4F + cos(uRad)) * cos(vRad),
        (4F + cos(uRad)) * sin(vRad),
        sin(uRad)
    ).mult(radius)

    private fun genTorus2(uRad: Float, vRad: Float): PVector = PVector(
        (4F + 3.8F * cos(uRad)) * cos(vRad),
        (4F + 3.8F * cos(uRad)) * sin(vRad),
        5 * sin(uRad)
    ).mult(radius)

    private fun genTorus3(uRad: Float, vRad: Float): PVector = PVector(
        (4F + 3.8F * cos(uRad)) * cos(vRad),
        (4F + 3.8F * cos(uRad)) * sin(vRad),
        5 * sin(uRad) + 2 * cos(uRad)
    ).mult(radius)

    private fun genTorus4(uRad: Float, vRad: Float): PVector = PVector(
        (4F + 3.8F * cos(uRad)) * cos(vRad),
        (4F + 3.8F * cos(uRad)) * sin(vRad),
        -5 * log(1F - 0.315F * uRad, 10F) + 5 * sin(uRad) + 2 * cos(uRad)
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
    AppleTorusMovie().run()
}