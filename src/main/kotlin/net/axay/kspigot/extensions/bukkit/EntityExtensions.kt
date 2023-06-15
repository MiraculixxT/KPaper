@file:Suppress("Unused")

package net.axay.kspigot.extensions.bukkit

import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.main.PluginInstance
import net.axay.kspigot.pluginmessages.PluginMessageConnect
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

/**
 * Checks if the entities' head is in water.
 */
val LivingEntity.isHeadInWater: Boolean get() = this.eyeLocation.block.type == Material.WATER

/**
 * Checks if the entities' feet are in water.
 */
val Entity.isFeetInWater: Boolean get() = this.location.block.type == Material.WATER

/**
 * Returns the material that is present under the feet of this entity.
 */
val Entity.groundMaterial get() = this.location.add(0.0, -0.01, 0.0).block.type

/**
 * @returns true if the entity is supported by a block.
 * The method from bukkit is deprecated because it can be spoofed by the client.
 * This can't be spoofed.
 */
val Entity.isStandingOnBlock: Boolean
    get() = groundMaterial.isSolid

/**
 * @return true if the entity is standing in mid air.
 */
val Entity.isStandingInMidAir: Boolean
    get() = !isStandingOnBlock && vehicle == null && !location.clone().add(0.0, 0.1, 0.0).block.type.isSolid && !location.block.type.isSolid

/**
 * Kills the damageable with a hit animation
 */
fun Damageable.kill() {
    damage(0.1)
    if (health > 0.0) health = 0.0
}

/**
 * Sets the entities' health to the max possible value.
 * @throws NullPointerException if the entity does not have a max health value
 */
fun LivingEntity.heal() {
    health = getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value
        ?: throw NullPointerException("The entity does not have a max health value!")
}

/**
 * Sets the players' foodLevel and saturation to the max possible value.
 */
fun Player.feed() {
    foodLevel = 20
    saturation = 20f
}

/**
 * Hides the player for all [onlinePlayers].
 */
fun Player.hideSelf() {
    onlinePlayers.filter { it != this }.forEach { it.hidePlayer(PluginInstance, this) }
}

/**
 * Shows the player for all [onlinePlayers].
 */
fun Player.showSelf() {
    onlinePlayers.filter { it != this }.forEach { it.showPlayer(PluginInstance, this) }
}

/**
 * Hides all online players from this player.
 */
fun Player.hideOnlinePlayers() {
    onlinePlayers.filter { it != this }.forEach { this.hidePlayer(PluginInstance, it) }
}

/**
 * Shows all online players to this player.
 */
fun Player.showOnlinePlayers() {
    onlinePlayers.filter { it != this }.forEach { this.showPlayer(PluginInstance, it) }
}

/**
 * Hide everyone from everyone (nobody sees another player)
 */
fun hideEveryone() {
    onlinePlayers.forEach { it.hideOnlinePlayers() }
}

/**
 * Returns the itemInHand of the given [EquipmentSlot]
 * if it is an hand slot.
 */
fun Player.getHandItem(hand: EquipmentSlot) = when (hand) {
    EquipmentSlot.HAND -> inventory.itemInMainHand
    EquipmentSlot.OFF_HAND -> inventory.itemInOffHand
    else -> null
}

/**
 * Sends the player to the given server in the
 * BungeeCord network.
 */
fun Player.sendToServer(servername: String) {
    PluginMessageConnect(servername).sendWithPlayer(this)
}

/**
 * Adds the given ItemStacks to the player's inventory.
 * @return The items that did not fit into the player's inventory.
 */
fun Player.give(vararg itemStacks: ItemStack) = inventory.addItem(*itemStacks)

/**
 * Adds all equipment locks to every equipment slot
 */
fun ArmorStand.fullLock() {
    for (slot in EquipmentSlot.values()) {
        for (lock in ArmorStand.LockType.values()) {
            addEquipmentLock(slot, lock)
        }
    }
}

/**
 * Removes all equipment locks from every equipment slot
 */
fun ArmorStand.fullUnlock() {
    for (slot in EquipmentSlot.values()) {
        for (lock in ArmorStand.LockType.values()) {
            removeEquipmentLock(slot, lock)
        }
    }
}