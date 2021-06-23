package practice

import common.RecorderablePApplet
import processing.core.PApplet
import processing.core.PVector


class PinballMovie : RecorderablePApplet() {
    private lateinit var pinball: Pinball
    private var first: Boolean = true

    override fun setupBody() {
        println("setupBody")
        //set black background
        //background(255F, 255F, 255F, 255F)
        background(0F)
        colorMode(HSB, 360F, 100F, 100F, 100F)
    }

    override fun drawBody() {
        if (first) {
            val color = color(random(360F), 100F, 100F, 100F)
            var location = PVector(random(0F, getScreenWidth().toFloat()), random(0F, getScreenHeight().toFloat()))
            var velocity = PVector(5F, 5F)
            pinball =
                Pinball(this, 50F, color, getScreenWidth(), getScreenHeight(), getFps().toFloat(), location, velocity)
            noStroke()
            first = false
        }
        background(0F, 0F, 0F, 0F)
        /*strokeWeight(16F)
        var hueColor = color(0F, 0F, 0F, 100F)
        stroke(hueColor)
        line(0F, 0F, 0F, getScreenHeight().toFloat())
        line(getScreenWidth().toFloat(), getScreenHeight().toFloat(), 0F, getScreenHeight().toFloat())
        line(getScreenWidth().toFloat(), getScreenHeight().toFloat(), getScreenWidth().toFloat(), 0F)
        line(0F, getScreenHeight().toFloat(), 0F, 0F)*/
        noStroke()
        pinball.draw()

        if (1 * 60 * getFps() < frameCount) {
            exit()
        }
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getFps(): Double {
        return 10.0
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    //override fun isRecordOnly(): Boolean = true

    fun run() {
        main("practice.${this::class.simpleName}", arrayOf())
    }
}

class Pinball(
    val applet: PApplet,
    val radius: Float,
    val color: Int,
    val width: Int,
    val height: Int,
    val fps: Float,
    var location: PVector,
    var velocity: PVector
) {
    fun draw() {
        location.add(velocity)
        applet.fill(color)
        applet.ellipse(location.x, location.y, radius, radius)
        if ((location.x - radius < 0) or (width < location.x + radius)) {
            velocity.x = velocity.x * -1
        }
        if ((location.y - radius < 0) or (height < location.y + radius)) {
            velocity.y = velocity.y * -1
        }
    }
}

fun main() {
    PinballMovie().run()
}