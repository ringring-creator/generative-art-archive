package practice.processing

import java.lang.Math.floor
import kotlin.math.absoluteValue


/**
 * wythoffSymbol is string with space-separated format (e.x. 3 | 4 2)
 */
class Polyhedron(val wythoffSymbol: String) {
    private var type = 2

    init {
        unpacksym(wythoffSymbol)
        moebius(wythoffSymbol)
    }

    private fun moebius(wythoffSymbol: String) {
        var twos = 0
        type = 2
        val wythoffNums = arrayListOf<Double>()
        wythoffSymbol.replace("|", "0").split(" ").forEach {
            if (it.contains("/")) {
                val division = it.split("/")
                val result = division[0].toDouble() / division[1].toDouble()
                wythoffNums.add(result)
            } else {
                wythoffNums.add(it.toDouble())
            }
        }
        for (value in wythoffNums) {
            if (value != 2.0) {
                val tmpType = frac(value).numer
                if (tmpType > type) {
                    if (type == 4) {
                        break;
                    } else {
                        type = tmpType.toInt()
                    }
                }
            } else {
                twos++
            }
        }
    }

    private fun frac(value: Double): Fraction {
        var n = 1L
        var d = 0L
        var beforeN = 0L
        var beforeD = 1L
        var s = value

        while (true) {
            if (value.absoluteValue > Long.MAX_VALUE) {
                return Fraction(n, d)
            }
            val f = floor(value).toLong()
            beforeN = n
            beforeD = d
            n = n * f + beforeN
            d = d * f + beforeD
            if (value == n.toDouble() / d.toDouble()) {
                return Fraction(n, d)
            }
            s = 1L / (s - f)
        }
    }

    private fun unpacksym(wythoffSymbol: String) {

        TODO("Not yet implemented")
    }

    data class Fraction(val numer: Long, val deno: Long)
}