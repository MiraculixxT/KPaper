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

private var localization: MutableMap<String, Config> = mutableMapOf()
private var miniMessage = MiniMessage.miniMessage()
private var defaultLocale = "en_US"

/**
 * Get a translation for the given key. If no translation were found the key will be returned in red. All inputs will be deserialized with miniMessages
 * @param key Localization Key
 * @param input Input variables. <input-i>
 */
fun msg(key: String, input: List<String> = emptyList(), locale: String = defaultLocale)
    = miniMessage.deserialize("<!i>" + (localization[locale]?.get<String>(key)?.replaceInput(input) ?: "<red>$key"))

/**
 * Get a translation for the given key. If no translation were found the key will be returned instead.
 * @param key Localization Key
 * @param input Input variables. <input-i>
 */
fun msgString(key: String, input: List<String> = emptyList(), locale: String = defaultLocale)
    = localization[locale]?.get<String>(key)?.replaceInput(input) ?: key

/**
 * Get a translation for the given key. If no translation were found the key will be returned in red.
 * @param key Localization Key
 * @param input Input variables. <input-i>
 * @param inline Inline string before every line (useful for listing)
 */
fun msgList(key: String, input: List<String> = emptyList(), inline: String = "<grey>   ", locale: String = defaultLocale)
    = msgString(key, input, locale).split("<br>").map {
        miniMessage.deserialize("$inline<!i>$it")
    }.ifEmpty { listOf(cmp(inline + key, KPaperConfiguration.Text.errorColor)) }

/**
 * Get the current used local if the proper syntax (e.g. en_US) is used.
 */
fun getLocal(): Locale {
    return try {
        Locale.forLanguageTag(defaultLocale) ?: Locale.ENGLISH
    } catch (_: Exception) {
        Locale.ENGLISH
    }
}

private fun String.replaceInput(input: List<String>): String {
    var msg = this
    input.forEachIndexed { index, s -> msg = msg.replace("<input-${index.plus(1)}>", s) }
    return msg
}

/**
 * Create your global localization instance. Creating multiple instances overrides each other and is not recommended.
 * @param folder The language folder that contains all language files
 * @param active The language that should be active from the start
 * @param keys All languages that should be loaded by default (all non-custom languages)
 * @see msg
 * @see msgList
 */
class Localization(private val folder: File, active: String, keys: List<Pair<String, InputStream?>>) {
    private val languages: MutableList<String> = mutableListOf()
    private val prefix: Component = KPaperConfiguration.Text.prefix

    /**
     * All currently loaded language keys. Does not include custom language files that are added in runtime
     */
    fun getLoadedKeys(): List<String> {
        return languages
    }

    /**
     * Change the current language to a new one. The entered key must not be a loaded language, if the key can not be found, it will rescan the language folder.
     * @param key The new language key
     * @return false if the entered language key can not be found or point to an invalid config
     */
    fun setLanguage(key: String): Boolean {
        return if (localization[key] != null || checkFile(key)) {
            defaultLocale = key
            console.sendMessage(prefix + cmp("Changed default language to ") + cmp(key, KPaperConfiguration.Text.highlightColor))
            true
        } else false
    }

    /**
     * Reload all loaded language keys from the language folder
     */
    private fun checkFiles() {
        languages.clear()
        folder.listFiles()?.forEach {
            val key = it.nameWithoutExtension
            if (checkFile(key)) {
                languages.add(key)
                console.sendMessage(prefix + cmp("Loaded language ") + cmp(key, KPaperConfiguration.Text.highlightColor))
            }
        }
    }

    private fun checkFile(key: String): Boolean {
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
        localization[key] = config
        return true
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
