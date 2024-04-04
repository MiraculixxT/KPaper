package de.miraculixx.kpaper.await.implementations

import de.miraculixx.kpaper.extensions.bukkit.language
import de.miraculixx.kpaper.gui.data.CustomInventory
import de.miraculixx.kpaper.gui.data.InventoryManager
import de.miraculixx.kpaper.gui.items.ItemProvider
import de.miraculixx.kpaper.gui.items.skullTexture
import de.miraculixx.kpaper.items.customModel
import de.miraculixx.kpaper.items.itemStack
import de.miraculixx.kpaper.items.meta
import de.miraculixx.kpaper.items.name
import de.miraculixx.mcommons.extensions.soundStone
import de.miraculixx.mcommons.namespace
import de.miraculixx.mcommons.statics.KHeads
import de.miraculixx.mcommons.text.*
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

class AwaitItemSelection(player: Player, filter: String, random: Boolean, callback: (ItemStack?) -> Unit) {
    private val lore = listOf(emptyComponent(), player.language().msgClick() + cmp("Choose Item"))

    class Items(private val filter: String, private val lore: List<Component>) : ItemProvider {
        override fun getItemList(from: Int, to: Int): List<ItemStack> {
            return Material.entries
                .filter { it.isItem && it.creativeCategory != null && it.name.contains(filter.uppercase().replace(' ', '_')) }
                .map {
                    itemStack(it) {
                        meta {
                            name = cmp(it.name.replace('_', ' '), cHighlight)
                            customModel = 1
                            lore(this@Items.lore)
                            persistentDataContainer.set(NamespacedKey(namespace, "gui.await.item"), PersistentDataType.STRING, it.name)
                        }
                    }
                }
        }
    }

    init {
        InventoryManager.storageBuilder(player.uniqueId.toString()) {
            title = cmp("Select Item", cHighlight)
            header = itemStack(Material.PLAYER_HEAD) {
                meta<SkullMeta> {
                    name = cmp("Cancel", cError)
                    customModel = 2
                    lore(listOf(cmp("Search: $filter")))
                    skullTexture(KHeads.X_RED)
                }
            }
            scrollable = true
            clickAction = event@{ it: InventoryClickEvent, _: CustomInventory ->
                it.isCancelled = true
                val item = it.currentItem
                when (item?.itemMeta?.customModel) {
                    1 -> callback.invoke(it.currentItem)
                    2 -> callback.invoke(null)
                    else -> player.soundStone()
                }
            }
            closeAction = event@{ _: InventoryCloseEvent, _: CustomInventory ->
                callback.invoke(null)
            }
            itemProvider = Items(filter, lore)
        }
    }
}