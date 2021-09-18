package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector
import kotlin.math.cosh
import kotlin.math.sinh

class OneLeafHyperboloidMovie : Recorderable3DPApplet() {
    private var bgColor: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var u: Float = -200F
    private var v: Float = 0F
    private var radius = 30F
    private val diff = 6F

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
        noStroke()
        fill(45F, 50F, 40F, 100F)
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
            drawOneLeafHyperboloid()
            if (360F <= v) {
                v = 0F
                u += diff
            } else {
                v += diff
            }
        }
    }

    private fun drawOneLeafHyperboloid() {
        pushMatrix()
        translate(
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat(),
            -(getScreenHeight() / 2).toFloat()
        )
        rotateX(PI / 3F)
        //rotateY(radians(-10F))
        beginShape()
        var uRad = radians(this.u)
        var vRad = radians(this.v)
        var oneLeafHyperboloid = genOneLeafHyperboloid(uRad, vRad)
        println("pot: ${oneLeafHyperboloid}")
        vertex(oneLeafHyperboloid.x, oneLeafHyperboloid.y, oneLeafHyperboloid.z)
        uRad = radians(this.u + diff + 1F)
        vRad = radians(this.v)
        oneLeafHyperboloid = genOneLeafHyperboloid(uRad, vRad)
        vertex(oneLeafHyperboloid.x, oneLeafHyperboloid.y, oneLeafHyperboloid.z)
        uRad = radians(this.u + diff + 1F)
        vRad = radians(this.v + diff + 1F)
        oneLeafHyperboloid = genOneLeafHyperboloid(uRad, vRad)
        vertex(oneLeafHyperboloid.x, oneLeafHyperboloid.y, oneLeafHyperboloid.z)
        uRad = radians(this.u)
        vRad = radians(this.v + diff + 1F)
        oneLeafHyperboloid = genOneLeafHyperboloid(uRad, vRad)
        vertex(oneLeafHyperboloid.x, oneLeafHyperboloid.y, oneLeafHyperboloid.z)
        endShape()
        popMatrix()
        if ((200F <= this.u).and(360F <= this.v)) {
            fin = true
        }
    }

    private fun genOneLeafHyperboloid(uRad: Float, vRad: Float) = PVector(
        cosh(uRad) * cos(vRad),
        cosh(uRad) * sin(vRad),
        sinh(uRad),
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
    OneLeafHyperboloidMovie().run()
}