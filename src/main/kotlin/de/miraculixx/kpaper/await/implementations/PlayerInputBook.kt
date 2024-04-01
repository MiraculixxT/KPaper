package de.miraculixx.kpaper.await.implementations

import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.event.unregister
import de.miraculixx.kpaper.items.itemStack
import de.miraculixx.kpaper.items.meta
import de.miraculixx.kpaper.main.PluginInstance
import de.miraculixx.kpaper.runnables.taskRunLater
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEditBookEvent
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.persistence.PersistentDataType

internal class PlayerInputBookComprehensive(
    player: Player,
    onInput: (String) -> Unit,
    onTimeout: () -> Unit,
    timeoutSeconds: Int,
) : PlayerInputBook<String>(player, onInput, onTimeout, timeoutSeconds) {
    override fun loadBookContent(bookMeta: BookMeta) = buildString {
        bookMeta.pages().forEach { page -> append(PlainTextComponentSerializer.plainText().serialize(page)) }
    }
}

internal class PlayerInputBookPaged(
    player: Player,
    onInput: (List<Component>) -> Unit,
    onTimeout: () -> Unit,
    timeoutSeconds: Int,
) : PlayerInputBook<List<Component>>(player, onInput, onTimeout, timeoutSeconds) {
    override fun loadBookContent(bookMeta: BookMeta): List<Component> = bookMeta.pages()
}

internal abstract class PlayerInputBook<T>(
    private val player: Player,
    onInput: (T) -> Unit,
    private val onTimeout: () -> Unit,
    timeoutSeconds: Int,
) {
    private val id = getID()
    private var active = true

    val bookItemStack = itemStack(Material.WRITABLE_BOOK) {
        meta {
            persistentDataContainer[idKey, PersistentDataType.INTEGER] = id
        }
    }

    init {
        player.inventory.addItem(bookItemStack)
        taskRunLater(timeoutSeconds.toLong()) { if (active) onTimeout() }
    }

    abstract fun loadBookContent(bookMeta: BookMeta): T

    private val inputListener = listen<PlayerEditBookEvent>(register = true) {
        val meta = it.newBookMeta
        if (meta.persistentDataContainer[idKey, PersistentDataType.INTEGER] == id) {
            onInput.invoke(loadBookContent(meta))
            it.isCancelled = true
            player.inventory.removeItem(bookItemStack)
            stop()
        }
    }

    private fun stop() {
        inputListener.unregister()
        usedIDs -= id
        active = false
    }


    private fun onTimeout() {
        player.closeInventory()
        onTimeout.invoke()
        stop()
    }

    companion object {
        val idKey = NamespacedKey(PluginInstance, "kpaper_bookinput_id")

        internal val usedIDs = ArrayList<Int>()

        fun getID(): Int {
            var returnID = (0..Int.MAX_VALUE).random()
            while (usedIDs.contains(returnID)) returnID = (0..Int.MAX_VALUE).random()
            return returnID
        }
    }
}
