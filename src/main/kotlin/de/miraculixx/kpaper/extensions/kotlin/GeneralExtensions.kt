package de.miraculixx.kpaper.extensions.kotlin

import java.util.*

internal fun <T> T.applyIfNotNull(block: (T.() -> Unit)?): T {
    if (block != null)
        apply(block)
    return this
}

fun String.toUUID(): UUID? {
    return try {
        UUID.fromString(this)
    } catch (_: IllegalArgumentException) {
        null
    }
}