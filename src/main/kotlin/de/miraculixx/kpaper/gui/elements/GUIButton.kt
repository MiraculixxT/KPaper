package de.miraculixx.kpaper.gui.elements

import de.miraculixx.kpaper.gui.ForInventory
import de.miraculixx.kpaper.gui.GUIClickEvent
import de.miraculixx.kpaper.gui.GUIElement
import org.bukkit.inventory.ItemStack

open class GUIButton<T : ForInventory>(
    private val icon: ItemStack,
    private val action: (GUIClickEvent<T>) -> Unit,
) : GUIElement<T>() {
    final override fun getItemStack(slot: Int) = icon
    override fun onClickElement(clickEvent: GUIClickEvent<T>) {
        clickEvent.bukkitEvent.isCancelled = true
        action(clickEvent)
    }
}
