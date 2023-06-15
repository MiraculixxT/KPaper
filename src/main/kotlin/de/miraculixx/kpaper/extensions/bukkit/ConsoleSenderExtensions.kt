@file:Suppress("unused")

package de.miraculixx.kpaper.extensions.bukkit

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

/**
 * Dispatches the command given by [commandLine].
 *
 * @param commandLine the command without a leading /
 */
fun CommandSender.dispatchCommand(commandLine: String) =
    Bukkit.dispatchCommand(this, commandLine)
