@file:Suppress("unused")

package de.miraculixx.kpaper.main

import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventPriority

/**
 * Global configurations to change the way KPaper works
 */
object KPaperConfiguration {
    /**
     * Global configurations for event listener default values
     */
    object Events {
        /**
         * If no value is set on event listening, this will define if the listener will be automatically registered
         */
        var autoRegistration = true

        /**
         * If no value is set on event listening, this will define if manual cancellation should be ignored
         */
        var ignoreCancelled = false

        /**
         * If no value is set on event listening, this will define the priority when multiple listeners handle this event
         * @see EventPriority
         */
        var eventPriority = EventPriority.NORMAL

        fun update(autoRegistration: Boolean, ignoreCancelled: Boolean, eventPriority: EventPriority) {
            this.autoRegistration = autoRegistration
            this.ignoreCancelled = ignoreCancelled
            this.eventPriority = eventPriority
        }
    }

    /**
     * Global configurations for customizing KPaper text components
     */
    object Text {
        /**
         * Base color used in text components that does not specify an extra color
         */
        var baseColor: TextColor = KColors.GRAY

        /**
         * Error color used in text components if something fails
         */
        var errorColor: TextColor = KColors.RED

        /**
         * Highlighting color used in text components if something is important
         */
        var highlightColor: TextColor = KColors.BLUE

        /**
         * Text prefix that is displayed before some messages. Change it to your plugin prefix
         */
        var prefix = cmp("KPaper", highlightColor) + cmp(" >> ", NamedTextColor.DARK_GRAY)

        /**
         * MiniMessage parser used to parse translations. Define your own for custom tags
         */
        var miniMessageParser = MiniMessage.miniMessage()

        fun update(baseColor: TextColor, errorColor: TextColor, highlightColor: TextColor, prefix: Component) {
            this.baseColor = baseColor
            this.errorColor = errorColor
            this.highlightColor = highlightColor
            this.prefix = prefix
        }
    }
}