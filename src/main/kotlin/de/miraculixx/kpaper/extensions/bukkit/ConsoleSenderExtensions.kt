@file:Suppress("unused")

package de.miraculixx.kpaper.extensions.bukkit

import de.miraculixx.mcommons.extensions.soundError
import de.miraculixx.mcommons.text.msg
import de.miraculixx.mcommons.text.plus
import de.miraculixx.mcommons.text.prefix
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Dispatches the command given by [commandLine].
 *
 * @param commandLine the command without a leading /
 */
fun CommandSender.dispatchCommand(commandLine: String) =
    Bukkit.dispatchCommand(this, commandLine)

/**
 * Checks if the [CommandSender] has the given [permission].
 * If not, the sender receives an error message containing the missing permission.
 * @param permission the permission to check for
 * @param key the translation key for the error message. Usually "command.noPermission"
 */
fun CommandSender.checkPermission(permission: String, key: String): Boolean {
    return if (hasPermission(permission)) true
    else {
        if (this is Player) soundError()
        sendMessage(prefix + msg(key, listOf(permission), language()))
        false
    }
}
