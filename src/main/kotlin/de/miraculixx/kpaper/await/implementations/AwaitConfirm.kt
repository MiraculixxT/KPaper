package de.miraculixx.kpaper.await.implementations

import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.event.unregister
import de.miraculixx.kpaper.extensions.bukkit.language
import de.miraculixx.kpaper.gui.GUIEvent
import de.miraculixx.kpaper.gui.data.CustomInventory
import de.miraculixx.kpaper.gui.data.InventoryManager
import de.miraculixx.kpaper.gui.items.ItemProvider
import de.miraculixx.kpaper.gui.items.skullTexture
import de.miraculixx.kpaper.items.customModel
import de.miraculixx.kpaper.items.itemStack
import de.miraculixx.kpaper.items.meta
import de.miraculixx.kpaper.items.name
import de.miraculixx.kpaper.runnables.taskRunLater
import de.miraculixx.mcommons.extensions.soundError
import de.miraculixx.mcommons.statics.KHeads
import de.miraculixx.mcommons.text.*
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.Locale

class AwaitConfirm(source: Player, onConfirm: () -> Unit, onCancel: () -> Unit) {
    private val gui = InventoryManager.inventoryBuilder("${source.uniqueId}-CONFIRM") {
        val locale = source.language()
        title = cmp("• ") + cmp(locale.msgString("common.confirm"), NamedTextColor.DARK_GREEN)
        size = 3
        player = source
        itemProvider = InternalItemProvider(locale)
        clickAction = InternalClickProvider(source, onConfirm, onCancel, this@AwaitConfirm).run
    }

    private val onClose = listen<InventoryCloseEvent> {
        if (it.inventory != gui.get()) return@listen
        if (it.reason != InventoryCloseEvent.Reason.PLAYER) return@listen
        disable()
        taskRunLater(1) {
            onCancel.invoke()
            it.player.soundError()
        }
    }

    private fun disable() {
        onClose.unregister()
    }

    private class InternalItemProvider(private val locale: Locale) : ItemProvider {
        override fun getSlotMap(): Map<Int, ItemStack> {
            return mapOf(
                12 to itemStack(Material.PLAYER_HEAD) {
                    meta<SkullMeta> {
                        customModel = 1
                        name = cmp(locale.msgString("common.confirm"), cSuccess)
                        skullTexture(KHeads.CHECKMARK_GREEN)
                    }
                },
                14 to itemStack(Material.PLAYER_HEAD) {
                    meta<SkullMeta> {
                        customModel = 2
                        name = cmp(locale.msgString("common.cancel"), cError)
                        skullTexture(KHeads.X_RED)
                    }
                }
            )
        }
    }

    private class InternalClickProvider(player: Player, onConfirm: () -> Unit, onCancel: () -> Unit, confirmer: AwaitConfirm) : GUIEvent {
        override val run: (InventoryClickEvent, CustomInventory) -> Unit = event@{ it: InventoryClickEvent, _: CustomInventory ->
            it.isCancelled = true
            if (it.whoClicked != player) return@event

            when (it.currentItem?.itemMeta?.customModel) {
                1 -> {
                    player.closeInventory()
                    onConfirm.invoke()
                }

                2 -> {
                    player.closeInventory()
                    onCancel.invoke()
                }

                else -> return@event
            }
            confirmer.disable()
        }
    }
}