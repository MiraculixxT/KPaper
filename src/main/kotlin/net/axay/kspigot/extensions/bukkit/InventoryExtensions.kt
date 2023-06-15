package net.axay.kspigot.extensions.bukkit

import org.bukkit.inventory.Inventory

/**
 * Closes the inventory for all viewers.
 */
fun Inventory.closeForViewers() = HashSet(viewers).forEach { it.closeInventory() }