package de.miraculixx.kpaper.chat.input.implementations

import de.miraculixx.kpaper.chat.input.PlayerInput
import de.miraculixx.kpaper.chat.input.PlayerInputResult
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.bukkit.content
import de.miraculixx.kpaper.items.itemStack
import de.miraculixx.kpaper.items.meta
import de.miraculixx.kpaper.main.PluginInstance
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEditBookEvent
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.persistence.PersistentDataType

internal class PlayerInputBookComprehensive(
    player: Player,
    callback: (PlayerInputResult<String>) -> Unit,
    timeoutSeconds: Int,
) : PlayerInputBook<String>(player, callback, timeoutSeconds) {
    override fun loadBookContent(bookMeta: BookMeta) = bookMeta.content
}

internal class PlayerInputBookPaged(
    player: Player,
    callback: (PlayerInputResult<List<Component>>) -> Unit,
    timeoutSeconds: Int,
) : PlayerInputBook<List<Component>>(player, callback, timeoutSeconds) {
    override fun loadBookContent(bookMeta: BookMeta): List<Component> = bookMeta.pages()
}

internal abstract class PlayerInputBook<T>(
    player: Player,
    callback: (PlayerInputResult<T>) -> Unit,
    timeoutSeconds: Int,
) : PlayerInput<T>(player, callback, timeoutSeconds) {
    private val id = getID()

    val bookItemStack = itemStack(Material.WRITABLE_BOOK) {
        meta {
            persistentDataContainer[idKey, PersistentDataType.INTEGER] = id
        }
    }

    init {
        player.inventory.addItem(bookItemStack)
    }

    abstract fun loadBookContent(bookMeta: BookMeta): T

    override val inputListeners = listOf(
        listen<PlayerEditBookEvent> {
            val meta = it.newBookMeta
            if (meta.persistentDataContainer[idKey, PersistentDataType.INTEGER] == id) {
                onReceive(loadBookContent(meta))
                usedIDs -= id
                it.isCancelled = true
                player.inventory.removeItem(bookItemStack)
            }
        }
    )

    override fun onTimeout() {
        player.closeInventory()
        usedIDs -= id
    }

    companion object {
        val idKey = NamespacedKey(PluginInstance, "kspigot_bookinput_id")

        internal val usedIDs = ArrayList<Int>()

        fun getID(): Int {
            var returnID = (0..Int.MAX_VALUE).random()
            while (usedIDs.contains(returnID)) returnID = (0..Int.MAX_VALUE).random()
            return returnID
        }
    }
}
