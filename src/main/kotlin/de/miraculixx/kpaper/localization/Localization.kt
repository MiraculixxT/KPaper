package de.miraculixx.kpaper.localization

import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import de.miraculixx.kpaper.extensions.console
import de.miraculixx.kpaper.main.KPaperConfiguration
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import java.io.File
import java.io.InputStream
import java.util.Locale

private var localization: Config? = null
private var miniMessage = MiniMessage.miniMessage()

/**
 * Get a translation for the given key. If no translation were found the key will be returned in red. All inputs will be deserialized with miniMessages
 * @param key Localization Key
 * @param input Input variables. <input-i>
 */
fun msg(key: String, input: List<String> = emptyList()) = miniMessage.deserialize("<!i>" + (localization?.get<String>(key)?.replaceInput(input) ?: "<red>$key"))

/**
 * Get a translation for the given key. If no translation were found the key will be returned instead.
 * @param key Localization Key
 * @param input Input variables. <input-i>
 */
fun msgString(key: String, input: List<String> = emptyList()) = localization?.get<String>(key)?.replaceInput(input) ?: key

/**
 * Get a translation for the given key. If no translation were found the key will be returned in red.
 * @param key Localization Key
 * @param input Input variables. <input-i>
 * @param inline Inline string before every line (useful for listing)
 */
fun msgList(key: String, input: List<String> = emptyList(), inline: String = "<grey>   ") = msgString(key, input).split("<br>").map {
    miniMessage.deserialize("$inline<!i>$it")
    }.ifEmpty { listOf(cmp(inline + key, KPaperConfiguration.Text.errorColor)) }

fun getLocal(): Locale {
    return try {
        Locale.forLanguageTag(localization?.name) ?: Locale.ENGLISH
    } catch (_: Exception) {
        Locale.ENGLISH
    }
}

private fun String.replaceInput(input: List<String>): String {
    var msg = this
    input.forEachIndexed { index, s -> msg = msg.replace("<input-${index.plus(1)}>", s) }
    return msg
}

class Localization(private val folder: File, active: String, keys: List<Pair<String, InputStream?>>, private val prefix: Component) {
    private val languages: MutableList<String> = mutableListOf()

    fun getLoadedKeys(): List<String> {
        return languages
    }

    fun setLanguage(key: String): Boolean {
        val file = File("${folder.path}/$key.yml")
        if (!file.exists()) {
            console.sendMessage(prefix + cmp("LANG - $key file not exist"))
            return false
        }
        val config = Config(file.inputStream(), key, file)
        if (config.get<String>("version") == null) {
            console.sendMessage(prefix + cmp("LANG - $key file is not a valid language config"))
            return false
        }
        console.sendMessage(prefix + cmp("Changed language to ") + cmp(key, KPaperConfiguration.Text.highlightColor))
        localization = config
        return true
    }

    private fun checkFiles() {
        languages.clear()
        folder.listFiles()?.forEach {
            languages.add(it.nameWithoutExtension)
        }
    }

    init {
        if (!folder.exists()) folder.mkdirs()
        keys.forEach {
            it.second?.readAllBytes()?.let { bytes -> File("${folder.path}/${it.first}.yml").writeBytes(bytes) }
        }
        checkFiles()
        setLanguage(active)
    }
}
