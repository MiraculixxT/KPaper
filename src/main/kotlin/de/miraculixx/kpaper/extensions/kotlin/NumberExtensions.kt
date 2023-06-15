package de.miraculixx.kpaper.extensions.kotlin

import java.math.RoundingMode

fun Float.round(digits: Int): Float {
    return toBigDecimal().setScale(digits, RoundingMode.HALF_UP).toFloat()
}

fun Double.round(digits: Int): Double {
    return toBigDecimal().setScale(digits, RoundingMode.HALF_UP).toDouble()
}