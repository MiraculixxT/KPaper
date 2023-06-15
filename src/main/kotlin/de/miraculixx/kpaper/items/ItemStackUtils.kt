@file:Suppress("unused")

package de.miraculixx.kpaper.items

import de.miraculixx.kpaper.extensions.bukkit.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

/**
 * Converts this string into a list of components, which
 * can be used for minecraft lorelists.
 * @param linePrefix Phrase that will be present before each. Supports minimessage
 */
fun String.toLoreList(linePrefix: String = "<!i>", lineLength: Int = 40): List<Component> {
    val loreList = ArrayList<Component>()
    val lineBuilder = StringBuilder()
    val prefix = MiniMessage.miniMessage().deserialize(linePrefix)
    fun submitLine() {
        loreList += prefix + Component.text(lineBuilder.toString())
        lineBuilder.clear()
    }

    fun addWord(word: String) {
        if (lineBuilder.lengthWithoutMinecraftColour + word.lengthWithoutMinecraftColour > lineLength)
            submitLine()

        if (lineBuilder.isNotEmpty())
            lineBuilder.append(" ")

        lineBuilder.append(word)
    }

    split(" ").forEach { addWord(it) }

    if (lineBuilder.isNotEmpty())
        submitLine()

    return loreList
}

/**
 * Returns the length of this sequence, ignoring
 * all minecraft colour codes.
 */
val CharSequence.lengthWithoutMinecraftColour: Int
    get() {
        var count = 0
        var isPreviousColourCode = false
        this.forEachIndexed { index, char ->
            if (isPreviousColourCode) {
                isPreviousColourCode = false
                return@forEachIndexed
            }

            if (char == '§') {
                if (lastIndex >= index + 1) {
                    val nextChar = this[index + 1]
                    if (nextChar.isLetter() || nextChar.isDigit())
                        isPreviousColourCode = true
                    else
                        count++
                }
            } else count++
        }

        return count
    }
