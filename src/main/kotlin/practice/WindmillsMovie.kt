package practice

class WindmillsMovie : RecorderablePApplet() {
    private val windmills: MutableList<Windmill> = mutableListOf()
    private var index = 0

    override fun setupBody() {
        println("setupBody")
        //set black background
        background(255F, 255F, 255F, 255F)
    }

    override fun drawBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        if (frameCount % (getFps().toInt() * 15) == 1) {
            val centerX: Float = RADIUS + 5F + 30F * (index / 6) + (RADIUS * 2 + 10F) * (index % 6)
            val centerY: Float = RADIUS + 10F + (RADIUS * 2 + 10F) * (index / 6)
            val windmill = Windmill(
                random(0F, 360F),
                centerX,
                centerY
            )
            windmills.add(windmill)
            index += 1
            println("centerX: ${centerX}, centerY: ${centerY}")
        }

        background(0F, 0F, 100F, 100F)
        windmills.forEach {
            drawWindmill(RADIUS, it.hue, frameCount.toFloat() / 100F, it.centerX, it.centerY)
        }
        if (265 * getFps() < frameCount) {
            exit()
        }
    }

    private fun drawHandle(radius: Float, centerX: Float, centerY: Float) {
        repeat(5) {
            strokeWeight(radius / 17F - it.toFloat())
            var hueColor = color(0F, 0F, 80F - it * 3, 100F)
            stroke(hueColor)
            line(centerX, centerY, centerX, getScreenWidth().toFloat())
        }
    }

    private fun drawWindmill(radius: Float, hue: Float, angle: Float, centerX: Float, centerY: Float) {
        drawHandle(radius, centerX, centerY)
        repeat(4) {
            drawWing(radius, (hue + 90F * it.toFloat()) % 360F, angle + PI / 2F * it.toFloat(), centerX, centerY)
        }
        drawCenterButton(radius, centerX, centerY)
    }

    private fun drawCenterButton(radius: Float, centerX: Float, centerY: Float) {
        pushMatrix()
        translate(centerX, centerY)
        repeat(10) {
            var hueColor = color(0F, 0F, 50F + it.toFloat() * 5F, 100F)
            fill(hueColor)
            ellipse(0F, 0F, radius / (16F + it.toFloat()) * 2F, radius / (16F + it.toFloat()) * 2F)
        }
        popMatrix()
    }

    private fun drawWing(radius: Float, hue: Float, angle: Float, centerX: Float, centerY: Float) {
        pushMatrix()
        noStroke()
        var hueColor = color(hue, 50F, 70F, 100F)
        fill(hueColor)
        translate(centerX, centerY)
        rotate(angle)
        triangle(
            radius / -16F,
            radius * -1F,
            radius / -16F,
            0F,
            -radius / 2F - radius / 16F,
            radius / -2F
        )
        hueColor = color(hue, 50F, 100F, 100F)
        fill(hueColor)
        triangle(
            -radius / 2F - radius / 16F,
            radius / -2F,
            -radius / 2F - radius / 16F,
            0F,
            0F,
            0F
        )
        popMatrix()
    }

    override fun getFps(): Double {
        return 60.0
    }

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/${this::class.simpleName}.mp4"

    fun run() {
        main("practice.${this::class.simpleName}", arrayOf())
    }

    data class Windmill(val hue: Float, val centerX: Float, val centerY: Float)

    companion object {
        private const val RADIUS = 150F
    }
}

fun main() {
    WindmillsMovie().run()
}