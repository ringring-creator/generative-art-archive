package practice.csound

fun noteToFreq(note: Double): Double {
    val pitch = 440.0
    return pitch * Math.pow(2.0, (note - 69) / 12)
}