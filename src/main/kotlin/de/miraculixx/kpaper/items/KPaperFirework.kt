@file:Suppress("MemberVisibilityCanBePrivate")

package de.miraculixx.kpaper.items

import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.inventory.meta.FireworkMeta

object KPaperFirework {
    inline fun buildFireworkMeta(fireworkMeta: FireworkMeta, builder: KPaperFireworkBuilder.() -> Unit): FireworkMeta {
        return KPaperFireworkBuilder().apply(builder).applyTo(fireworkMeta)
    }

    fun FireworkMeta.build(builder: KPaperFireworkBuilder.() -> Unit) = buildFireworkMeta(this, builder)
}

class KPaperFireworkBuilder {
    val effects = ArrayList<FireworkEffect>()
    var power: Int? = null

    inline fun effect(builder: FireworkEffectBuilder.() -> Unit) {
        effects += FireworkEffectBuilder().apply(builder).fireworkEffect
    }

    fun applyTo(fireworkMeta: FireworkMeta): FireworkMeta {
        fireworkMeta.addEffects(effects)

        power?.let { fireworkMeta.power = it }

        return fireworkMeta
    }
}

class FireworkEffectBuilder {
    private val fireworkBuilder = FireworkEffect.builder()

    var type: FireworkEffect.Type? = null
    var trail: Boolean? = null
    var flicker: Boolean? = null

    fun fade(vararg colors: Color) {
        fireworkBuilder.withFade(*colors)
    }

    fun color(vararg colors: Color) {
        fireworkBuilder.withColor(*colors)
    }

    val fireworkEffect: FireworkEffect
        get() {
            type?.let { fireworkBuilder.with(it) }
            trail?.let { fireworkBuilder.trail(it) }
            flicker?.let { fireworkBuilder.flicker(it) }

            return fireworkBuilder.build()
        }
}
