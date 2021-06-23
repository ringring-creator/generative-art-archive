package practice

import common.Recorderable3DPApplet
import processing.core.PVector


class HyperbolicParaboloidMovie : Recorderable3DPApplet() {
    val initLength = getScreenWidth() / 4F
    private var bgColor: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var u: Float = -initLength
    private var v: Float = -initLength
    private val diff = 12F

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
            drawHyperbolicParaboloid()
            if (initLength <= v) {
                v = -initLength
                u += diff
            } else {
                v += diff
            }
        }
    }

    private fun drawHyperbolicParaboloid() {
        println("u: ${u}, v: ${v}")
        pushMatrix()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
        )
        camera(
            //getScreenWidth().toFloat(),
            0F,
            getScreenHeight().toFloat(),
            getScreenHeight().toFloat(),
            0.0F,
            0.0F,
            0.0F,
            1F,
            -1.0F,
            1F
        )
        beginShape()
        var uVad = this.u
        var vVad = this.v
        var hyperbolicParaboloid = genHyperbolicParaboloid(uVad, vVad)
        println("hyperbolicParaboloid: ${hyperbolicParaboloid}")
        vertex(hyperbolicParaboloid.x, hyperbolicParaboloid.y, hyperbolicParaboloid.z)
        uVad = this.u + diff + 1F
        vVad = this.v
        hyperbolicParaboloid = genHyperbolicParaboloid(uVad, vVad)
        vertex(hyperbolicParaboloid.x, hyperbolicParaboloid.y, hyperbolicParaboloid.z)
        uVad = this.u + diff + 1F
        vVad = this.v + diff + 1F
        hyperbolicParaboloid = genHyperbolicParaboloid(uVad, vVad)
        vertex(hyperbolicParaboloid.x, hyperbolicParaboloid.y, hyperbolicParaboloid.z)
        uVad = this.u
        vVad = this.v + diff + 1F
        hyperbolicParaboloid = genHyperbolicParaboloid(uVad, vVad)
        vertex(hyperbolicParaboloid.x, hyperbolicParaboloid.y, hyperbolicParaboloid.z)
        endShape()
        popMatrix()
        if ((initLength <= this.u).and(initLength <= this.v)) {
            fin = true
        }
    }

    private fun genHyperbolicParaboloid(u: Float, v: Float) = PVector(
        u,
        (u * u - v * v) * 0.01F - 1100F,
        v
    )


    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("practice.${this::class.simpleName}", arrayOf())
    }
}

fun main() {
    HyperbolicParaboloidMovie().run()
}