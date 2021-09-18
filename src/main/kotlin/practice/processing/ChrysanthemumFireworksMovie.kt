package practice

import common.RecorderablePApplet
import processing.core.PApplet
import processing.core.PVector
import java.util.*

class ChrysanthemumFireworksMovie : RecorderablePApplet() {
    private var bgColor: Int = 0
    private val fireworks: Queue<List<Chrysanthemum>> = LinkedList<List<Chrysanthemum>>()
    private var currentFireworks: List<Chrysanthemum> = listOf()

    override fun setupBody() {
        println("setupBody")
        //set black background
        colorMode(HSB, 360F, 100F, 100F, 100F)
        //bgColor = color(44F, 0.09F, 0.25F, 100F)
        bgColor = color(0F, 0F, 0F, 255F)
        background(bgColor)
        repeat(20) {
            val brocadeCrowns = mutableListOf<Chrysanthemum>()
            //val range = random(1F, 6F).toInt()
            for (idx in 1..it) {
                var h = random(360F)
                val yVelocity = random(-4F, -8F)
                brocadeCrowns.add(
                    Chrysanthemum(
                        this,
                        PVector(getScreenWidth() / (it.toFloat() + 1F) * idx.toFloat(), getScreenHeight().toFloat()),
                        PVector(0F, yVelocity),
                        //color(h, 100F, 100F, 100F),
                        color(h, 100F, 100F),
                        //color(h, 70F, 70F, 100F),
                        color(h, 70F, 70F),
                        120
                    )
                )
            }
            fireworks.add(brocadeCrowns)
        }
        currentFireworks = fireworks.poll()
    }

    override fun drawBody() {
        var allFinish: Boolean = true
        for (firework in currentFireworks) {
            if (firework.isFinish().not()) {
                firework.draw()
                allFinish = false
            }
        }
        if (fireworks.size == 0) {
            println("exit")
            exit()
        }
        if (allFinish) {
            currentFireworks = fireworks.poll()
        }
    }

    override fun isRecordOnly(): Boolean = true
    //override fun isRecordOnly(): Boolean = false

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }
}

class Chrysanthemum(
    val pApplet: RecorderablePApplet,
    var position: PVector,
    var launchVelocity: PVector,
    val centerStarColor: Int,
    val outsideStarColor: Int,
    val lifeFrameNum: Int
) {
    private val centerStars: List<Star>
    private val outsideStars: List<Star>
    private val gravity: PVector = PVector(0F, 0.01F)
    private var frameCount: Int = 1

    init {
        centerStars = mutableListOf()
        outsideStars = mutableListOf()
        var angle = 0F
        val radius = 1F
        for (idx in 0..360) {
            angle += 0.5F
            val centerStar = Star(
                PVector(
                    pApplet.random(-1F, 1F), pApplet.random(-1F, 1F)
                ),
                PVector(pApplet.random(-1.5F, 1.5F), pApplet.random(-1.5F, 1.5F)),
                centerStarColor
            )
            centerStars.add(centerStar)
        }
        for (idx in 0..720) {
            angle += 1F
            val theta = PApplet.radians(angle)
            val outsideStar = Star(
                PVector(radius * PApplet.cos(theta), radius * PApplet.sin(theta)),
                PVector(pApplet.random(2F, 3F) * PApplet.cos(theta), pApplet.random(2F, 3F) * PApplet.sin(theta)),
                outsideStarColor
            )
            outsideStars.add(outsideStar)
        }
    }

    fun draw() {
        /*pApplet.noStroke()
        pApplet.fill(0F, 0F, 0F, 10F)
        pApplet.rect(0F, 0F, pApplet.getScreenWidth().toFloat(), pApplet.getScreenHeight().toFloat())*/
        pApplet.loadPixels()
        for (idx in 0 until pApplet.pixels.size) {
            pApplet.pixels[idx] = pApplet.color(
                pApplet.hue(pApplet.pixels[idx]),
                pApplet.saturation(pApplet.pixels[idx]),
                pApplet.brightness(pApplet.pixels[idx]) / 10F * 9F,
                255F
            )
        }
        pApplet.updatePixels()
        if (launchVelocity.y < 0) {
            val beforeFireColor = pApplet.color(51F, 100F, 100F, 100F)
            pApplet.stroke(beforeFireColor)
            pApplet.strokeWeight(3F)
            pApplet.line(position.x, position.y, position.x, position.y + 5)
            launchVelocity.add(gravity)
            position.add(launchVelocity)
        } else {
            pApplet.pushMatrix()
            pApplet.translate(position.x, position.y)
            pApplet.noStroke()
            for (star in centerStars) {
                pApplet.fill(star.color)
                pApplet.ellipse(star.position.x, star.position.y, 2F, 2F)
                star.updatePosition(gravity)
            }
            for (star in outsideStars) {
                pApplet.fill(star.color)
                pApplet.ellipse(star.position.x, star.position.y, 2F, 2F)
                star.updatePosition(gravity)
            }
            pApplet.popMatrix()
            frameCount += 1
        }
    }

    fun isFinish(): Boolean {
        return lifeFrameNum < frameCount
    }
}

data class Star(var position: PVector, var velocity: PVector, val color: Int) {
    fun updatePosition(gravity: PVector) {
        velocity.add(gravity)
        position.add(velocity)
    }
}

fun main() {
    ChrysanthemumFireworksMovie().run()
}