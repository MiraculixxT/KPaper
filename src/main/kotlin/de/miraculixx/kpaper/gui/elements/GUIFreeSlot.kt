package de.miraculixx.kpaper.gui.elements

import de.miraculixx.kpaper.gui.ForInventory
import de.miraculixx.kpaper.gui.GUIClickEvent
import de.miraculixx.kpaper.gui.GUISlot

class GUIFreeSlot<T : ForInventory> : GUISlot<T>() {
    override fun onClick(clickEvent: GUIClickEvent<T>) {
        /* do nothing */
    }
}
