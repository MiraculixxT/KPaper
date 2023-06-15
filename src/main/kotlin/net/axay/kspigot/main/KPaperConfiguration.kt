package net.axay.kspigot.main

import net.axay.kspigot.chat.KColors
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
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
    }
}