@file:Suppress("unused")

package de.miraculixx.kpaper.extensions.bukkit

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

/**
 * Displays a title with [Duration]
 */
fun Audience.title(main: Component, sub: Component, fadeIn: Duration = Duration.ZERO, stay: Duration = 5.seconds, fadeOut: Duration = Duration.ZERO) {
    showTitle(Title.title(main, sub, Title.Times.times(fadeIn.toJavaDuration(), stay.toJavaDuration(), fadeOut.toJavaDuration())))
}

/**
 * Add two audiences together
 */
fun Audience.plus(audience: Audience) = Audience.audience(this, audience)

/**
 * Add multiple audiences together
 */
fun Audience.plus(audiences: Collection<Audience>) = Audience.audience(audiences.plus(this))