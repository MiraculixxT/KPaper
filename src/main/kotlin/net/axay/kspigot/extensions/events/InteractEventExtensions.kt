@file:Suppress("unused")

package net.axay.kspigot.extensions.events

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

/**
 * @return True, if the action was a left mouse button click.
 */
val Action.isLeftClick get() = this == Action.LEFT_CLICK_BLOCK || this == Action.LEFT_CLICK_AIR

/**
 * @return True, if the action was a right mouse button click.
 */
val Action.isRightClick get() = this == Action.RIGHT_CLICK_BLOCK || this == Action.RIGHT_CLICK_AIR