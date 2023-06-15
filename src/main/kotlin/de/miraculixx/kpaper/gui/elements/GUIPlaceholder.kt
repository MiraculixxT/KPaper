package de.miraculixx.kpaper.gui.elements

import de.miraculixx.kpaper.gui.ForInventory
import de.miraculixx.kpaper.gui.GUIClickEvent
import de.miraculixx.kpaper.gui.GUIElement
import org.bukkit.inventory.ItemStack

class GUIPlaceholder<T : ForInventory>(
    private val icon: ItemStack,
) : GUIElement<T>() {
    override fun getItemStack(slot: Int) = icon
    override fun onClickElement(clickEvent: GUIClickEvent<T>) {
        clickEvent.bukkitEvent.isCancelled = true
    }
}
