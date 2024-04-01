package de.miraculixx.kpaper.await.implementations

import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.event.unregister
import de.miraculixx.kpaper.main.KPaperConfiguration
import de.miraculixx.kpaper.runnables.sync
import de.miraculixx.kpaper.runnables.task
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.time.Duration

/**
 * @param name Name in the countdown title of the action
 * @param advancedMode Enable advanced more for formatting and utils
 * @param before Previous value to copy (only in advanced mode)
 * @param onChat Only called if a chat input occurred
 * @param callback Always called after timeout or chat input
 */
class AwaitChatMessage(
    private val player: Player,
    name: String,
    maxSeconds: Int,
    before: String?,
    private val advancedMode: Boolean,
    initMessage: Component,
    onChat: (String) -> Unit,
    private val callback: () -> Unit
) {
    private var counter = maxSeconds

    private val onChat = listen<AsyncChatEvent> {
        if (it.player != player) return@listen
        it.isCancelled = true
        val message = PlainTextComponentSerializer.plainText().serialize(it.message())
        val final = if (advancedMode) {
            when (message) {
                "#exit" -> before ?: ""
                "#clear" -> ""
                else -> message.replace('_', ' ')
            }
        } else message
        onChat.invoke(final)
        stop()
    }

    private val scheduler = task(false, 0, 20) run@{
        if (counter <= 0) {
            stop()
            return@run
        }
        player.showTitle(
            Title.title(
                Component.text("Enter $name", KPaperConfiguration.Text.highlightColor),
                Component.text("${counter}s"),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(5), Duration.ZERO)
            )
        )
        sync {
            player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 99999, 1, false, false, false))
        }
        counter--
    }

    private fun stop() {
        scheduler?.cancel()
        onChat.unregister()
        player.showTitle(Title.title(Component.empty(), Component.empty()))
        sync {
            player.removePotionEffect(PotionEffectType.BLINDNESS)
            callback.invoke()
        }
    }

    private fun Component.addHover(display: Component) = hoverEvent(asHoverEvent().value(display))

    init {
        player.closeInventory()
        player.sendMessage(initMessage)
        if (advancedMode && before != null) {
            val realBefore = before.replace(' ', '_')
            player.sendMessage(
                KPaperConfiguration.Text.prefix.append(
                    (
                            Component.text(realBefore)
                                .append(Component.text(" (copy)", KPaperConfiguration.Text.highlightColor))
                                .addHover(Component.text("Copy: $realBefore")))
                )
                    .clickEvent(ClickEvent.suggestCommand(realBefore))
            )
        }
    }
}