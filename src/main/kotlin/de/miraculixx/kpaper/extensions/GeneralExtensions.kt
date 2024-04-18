@file:Suppress("Unused")

package de.miraculixx.kpaper.extensions

import de.miraculixx.kpaper.extensions.bukkit.msg
import de.miraculixx.kpaper.main.PluginInstance
import de.miraculixx.mcommons.text.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Shortcut to get all online players.
 * @see Bukkit.getOnlinePlayers
 */
val onlinePlayers: Collection<Player> get() = Bukkit.getOnlinePlayers()

/**
 * Shortcut to get a collection of all
 * online players plus the console command sender.
 */
val onlineSenders: Collection<CommandSender> get() = onlinePlayers.plus(Bukkit.getConsoleSender())

/**
 * Shortcut to get the Server.
 * @see Bukkit.getServer
 */
val server get() = Bukkit.getServer()

/**
 * Shortcut to get the PluginManager.
 * @see Bukkit.getPluginManager
 */
val pluginManager get() = Bukkit.getPluginManager()

/**
 * Broadcasts a message ([msg]) on the server.
 * @return the number of recipients
 * @see Bukkit.broadcastMessage
 */
fun broadcast(msg: Component) = server.broadcast(msg)

/**
 * Broadcast a translated message to all online players.
 * Each player will receive the message with their locale.
 */
fun broadcast(prefix: Component, key: String, variables: List<String> = emptyList()) {
    onlinePlayers.forEach {
        it.sendMessage(prefix + it.msg(key, variables))
    }
}

/**
 * Shortcut to get the ConsoleSender.
 * @see Bukkit.getConsoleSender
 */
val console get() = Bukkit.getConsoleSender()

/**
 * Shortcut for creating a new [NamespacedKey]
 */
fun pluginKey(key: String) = NamespacedKey(PluginInstance, key)

/**
 * Shortcut to get a collection of all worlds
 */
val worlds: List<World> get() = Bukkit.getWorlds()