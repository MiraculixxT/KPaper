@file:Suppress("unused")

package de.miraculixx.kpaper.main

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
         * Highlighting color used in text components if something is important
         */
        var highlightColor: TextColor = NamedTextColor.BLUE

        /**
         * Text prefix that is displayed before some messages. Change it to your plugin prefix
         */
        var prefix: Component = Component.text("KPaper", highlightColor).append(Component.text(" >> ", NamedTextColor.DARK_GRAY))

        /**
         * MiniMessage parser used to parse translations. Define your own for custom tags
         */
        var miniMessageParser = MiniMessage.miniMessage()

        fun update(highlightColor: TextColor, prefix: Component) {
            this.highlightColor = highlightColor
            this.prefix = prefix
        }
    }
}