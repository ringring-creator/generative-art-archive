package practice.processing

import common.RecorderablePApplet
import processing.core.PVector

class PolygonsAlignmentMovie : RecorderablePApplet() {
    private var bgColor: Int = 0
    private val radius = 120F
    private var type: Int = 1
    private var num: Int = 0

    override fun setupBody() {
        colorMode(HSB, 360F, 100F, 100F, 100F)
        bgColor = color(0F, 0F, 100F, 100F)
        background(bgColor)
    }

    override fun drawBody() {
        if (frameCount % 5 == 1) {
            when (type) {
                1 -> drawTriangle()
                2 -> drawSquare()
                3 -> drawHexagon()
                4 -> {
                    if (20 * getFps() < num * 5) {
                        exit()
                    }
                }
            }
            num += 1
        }
    }

    private fun drawTriangle() {
        pushMatrix()
        val translatePoint = genTriangleTranslate(num, radius)
        println("translatePoint: ${translatePoint}")
        translate(
            translatePoint.x,
            translatePoint.y
        )
        rotate(PI * (num % 2))
        val triangle = createShape()
        triangle.beginShape()
        for (idx in 0..2) {
            val v = PVector.fromAngle(TWO_PI * idx / 3 + PI / 2)
            v.mult(radius)
            triangle.vertex(v.x, v.y)
        }
        triangle.endShape(CLOSE)
        triangle.setFill(color(random(360F), 50F, 100F, 100F))
        println("triangle: ${triangle.getVertex(0)}, ${triangle.getVertex(1)}, ${triangle.getVertex(2)}")
        shape(triangle)
        popMatrix()
        if (getScreenHeight() + radius < translatePoint.y) {
            type = 2
            num = 0
            background(bgColor)
        }
    }

    private fun genTriangleTranslate(num: Int, radius: Float): PVector {
        println("num: ${num}")
        var sign = 1
        val widthNum = 21
        if ((num / widthNum) % 2 == 0) {
            sign = -1
        } else {
            sign = 1
        }
        val oddX = (num % widthNum) / 2
        val evenX = oddX + (num % widthNum) % 2
        val evenVec = PVector.mult(PVector.fromAngle(sign * -PI / 6), evenX * radius)
        val oddVec = PVector.mult(PVector.fromAngle(sign * PI / 6), oddX * radius)
        val oddY = (num / widthNum) / 2
        val evenY = oddY + (num / widthNum) % 2
        val yVec = PVector.mult(PVector(0F, 1F), (oddY * radius + evenY * 2 * radius))
        println("evenVec: ${evenVec}")
        println("oddVec: ${oddVec}")
        println("yVec: ${yVec}")
        val result = PVector(0F, 0F)
        result.add(evenVec)
            .add(oddVec)
            .add(yVec)
        println("result: ${result}")
        return result
    }

    private fun drawSquare() {
        pushMatrix()
        val translatePoint = genSquareTranslate(num - 1, radius)
        translate(
            translatePoint.x,
            translatePoint.y
        )
        val square = createShape()
        square.beginShape()
        for (idx in 0..3) {
            val v = PVector.fromAngle((TWO_PI * (idx + 0.5) / 4).toFloat())
            v.mult(radius / sqrt(2F))
            square.vertex(v.x, v.y)
        }
        square.endShape()
        square.setFill(color(random(360F), 50F, 100F, 100F))
        shape(square)
        popMatrix()
        if (getScreenHeight() + radius < translatePoint.y) {
            type = 3
            num = 1
            background(bgColor)
        }
    }

    private fun genSquareTranslate(num: Int, radius: Float): PVector {
        val widthNum = 17
        val result = PVector.mult(PVector(1F, 0F), (num % widthNum) * radius)
        result.add(PVector.mult(PVector(0F, 1F), (num / widthNum) * radius))
        return result
    }

    private fun drawHexagon() {
        pushMatrix()
        val hexagonPoint = genHexagonTranslate(num, radius)
        println("translatePoint: ${hexagonPoint}")
        translate(
            hexagonPoint.x,
            hexagonPoint.y
        )
        val hexagon = createShape()
        hexagon.beginShape()
        for (idx in 0..5) {
            val vec = PVector.fromAngle(TWO_PI * idx / 6)
            vec.mult(radius)
            hexagon.vertex(vec.x, vec.y)
        }
        hexagon.endShape()
        hexagon.setFill(color(random(360F), 50F, 100F, 100F))
        shape(hexagon)
        popMatrix()
        if (getScreenHeight() + radius * 2 < hexagonPoint.y) {
            type = 4
            num = 0
        }
    }

    private fun genHexagonTranslate(num: Int, radius: Float): PVector {
        println("num: ${num}")
        var sign = 1
        val widthNum = 21
        val oddX = (num % widthNum) / 2
        val evenX = oddX + (num % widthNum) % 2
        val evenVec = PVector.mult(PVector.fromAngle(sign * -PI / 6), evenX * radius * sqrt(3F))
        val oddVec = PVector.mult(PVector.fromAngle(sign * PI / 6), oddX * radius * sqrt(3F))
        val yVec = PVector.mult(PVector(0F, 1F), ((num / widthNum) - 1) * (sqrt(3F) * radius))
        println("evenVec: ${evenVec}")
        println("oddVec: ${oddVec}")
        println("yVec: ${yVec}")
        val result = PVector(0F, 0F)
        result.add(evenVec)
            .add(oddVec)
            .add(yVec)
        println("result: ${result}")
        return result
    }

    override fun isRecordOnly(): Boolean = true

    override fun exitBody() {
        println("exitBody")
    }

    override fun getRecordFilePath(): String = "../out/practice/${this::class.simpleName}.mp4"

    fun run() {
        main("${this::class.qualifiedName}", arrayOf())
    }

}

fun main() {
    PolygonsAlignmentMovie().run()
}