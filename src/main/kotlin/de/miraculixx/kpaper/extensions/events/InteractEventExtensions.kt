@file:Suppress("unused")

package de.miraculixx.kpaper.extensions.events

import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

/**
 * Returns the item used in the interaction
 * with the use of the [EquipmentSlot] returned
 * by the value [PlayerInteractEntityEvent.hand].
 */
val PlayerInteractEntityEvent.interactItem: ItemStack?
    get() {
        val p: Player = this.player
        return when (this.hand) {
            EquipmentSlot.HAND -> p.inventory.itemInMainHand
            EquipmentSlot.OFF_HAND -> p.inventory.itemInOffHand
            EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.HEAD, EquipmentSlot.LEGS -> null
        }
    }
