@file:Suppress("MemberVisibilityCanBePrivate", "Unused")

package de.miraculixx.kpaper.chat.await

import de.miraculixx.kpaper.chat.await.implementations.AwaitChatMessage
import de.miraculixx.kpaper.chat.await.implementations.PlayerInputBookComprehensive
import de.miraculixx.kpaper.chat.await.implementations.PlayerInputBookPaged
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

/**
 * Asks the player a question and uses the next
 * chat input of the player as his input.
 */
fun Player.awaitChatInput(
    name: String,
    maxSeconds: Int,
    before: String? = null,
    advancedMode: Boolean = false,
    initMessage: Component,
    onChat: (String) -> Unit,
    callback: () -> Unit
) {
    AwaitChatMessage(this, name, maxSeconds, before, advancedMode, initMessage, onChat, callback)
}

/**
 * Opens a book and uses the text the player inserted
 * on all sites as the players' input.
 * In this case, all pages will be flattened to a single string.
 */
fun Player.awaitBookInputAsString(
    timeoutSeconds: Int = 1 * 60,
    callback: (String) -> Unit,
) = PlayerInputBookComprehensive(this, callback, timeoutSeconds).bookItemStack

/**
 * Opens a book and uses the text the player inserted
 * on all sites as the players' input.
 * In this case, every page is represented by one string
 * element in a list of strings.
 */
fun Player.awaitBookInputAsList(
    timeoutSeconds: Int = 1 * 60,
    callback: (List<Component>) -> Unit,
) = PlayerInputBookPaged(this, callback, timeoutSeconds).bookItemStack
