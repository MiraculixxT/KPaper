package de.miraculixx.kpaper.extensions.events

import org.bukkit.Material
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack

/**
 * Checks if the event is "cancelled"
 * by returning if the material of
 * the result is equal to [Material.AIR].
 */
var PrepareItemCraftEvent.isCancelled: Boolean
    get() = inventory.result?.type == Material.AIR
    set(value) { if (value) inventory.result = ItemStack(Material.AIR) else inventory.result = inventory.recipe?.result }

