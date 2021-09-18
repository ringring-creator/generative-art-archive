package practice.processing

import common.Recorderable3DPApplet
import processing.core.PVector

class SemiRegularPolyhedonMovie : Recorderable3DPApplet() {
    val uMaxLength = 360F
    val uMinLength = 0F
    val vMaxLength = 360F
    val vMinLength = 0F
    private var bgColor: Int = 0
    private var finCount = 0
    private var fin: Boolean = false
    private var u: Float = uMinLength
    private var v: Float = vMinLength
    private val vDiff = 3F
    private val uDiff = 3F
    private val radius = 300F

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
            drawPolyhedron()
            if (vMaxLength <= v) {
                u += uDiff
                v = vMinLength
            } else {
                v += vDiff
            }
        }
    }

    private fun drawPolyhedron() {
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
            1F
        )
        beginShape()
        var uVad = radians(this.u)
        var vVad = radians(this.v)
        var polyhedron = genPolyhedron(uVad, vVad)
        println("polyhedron: ${polyhedron}")
        vertex(polyhedron.x, polyhedron.y, polyhedron.z)
        uVad = radians(this.u + uDiff * 2)
        vVad = radians(this.v)
        polyhedron = genPolyhedron(uVad, vVad)
        vertex(polyhedron.x, polyhedron.y, polyhedron.z)
        uVad = radians(this.u + uDiff * 2)
        vVad = radians(this.v + vDiff * 2)
        polyhedron = genPolyhedron(uVad, vVad)
        vertex(polyhedron.x, polyhedron.y, polyhedron.z)
        uVad = radians(this.u)
        vVad = radians(this.v + vDiff * 2)
        polyhedron = genPolyhedron(uVad, vVad)
        vertex(polyhedron.x, polyhedron.y, polyhedron.z)
        endShape()
        popMatrix()
        if ((uMaxLength <= this.u).and(vMaxLength <= this.v)) {
            fin = true
        }
    }

    //Todo: 半正多面体
    private fun genPolyhedron(uRad: Float, vRad: Float): PVector = PVector(
        0F, 0F, 0F
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
    SemiRegularPolyhedonMovie().run()
}