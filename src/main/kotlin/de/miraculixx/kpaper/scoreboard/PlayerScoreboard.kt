@file:Suppress("unused")

package de.miraculixx.kpaper.scoreboard

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import java.util.UUID

private val scoreboards: MutableMap<UUID, PlayerScoreboard> = mutableMapOf()

/**
 * @return The custom [PlayerScoreboard] saved for this player
 * @see createCustomScoreboard
 */
fun Player.getCustomScoreboard() = scoreboards[uniqueId]

/**
 * Create a new custom scoreboard for this player. The Old scoreboard will be deactivated and override!
 * @return The new [PlayerScoreboard]
 * @see getCustomScoreboard
 */
fun Player.createCustomScoreboard(displayName: Component): PlayerScoreboard {
    scoreboards[uniqueId]?.removeComplete()
    return PlayerScoreboard(this, displayName)
}

/**
 * Represents custom scoreboards for a single player for easier modification and editing.
 * @see createCustomScoreboard
 */
class PlayerScoreboard(player: Player, displayName: Component) {
    private val scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private val objective = scoreboard.registerNewObjective("kpaper_player_scoreboard", Criteria.DUMMY, displayName)
    private val lines: MutableMap<Int, String> = mutableMapOf()

    init {
        objective.displaySlot = DisplaySlot.SIDEBAR
        player.scoreboard = scoreboard
        scoreboards[player.uniqueId] = this
    }

    /**
     * Remove and unregister the scoreboard completely
     */
    fun removeComplete() {
        objective.unregister()
    }

    /**
     * Display the scoreboard in the sidebar
     */
    fun show() {
        objective.displaySlot = DisplaySlot.SIDEBAR
    }

    /**
     * Removes the scoreboard display
     */
    fun hide() {
        objective.displaySlot = null
    }

    /**
     * Updates the scoreboard titel
     */
    fun setTitle(title: Component) {
        objective.displayName(title)
    }

    /**
     * Add a new line to the scoreboard. A scoreboard can only display 15 lines at the same time!
     */
    fun addLine(name: String) {
        val id = lines.size
        objective.getScore(name).score = 15 - id
        lines[id] = name
    }

    /**
     * Edit an existing line with new content
     * @return false if the line does not exist
     */
    fun editLine(line: Int, name: String): Boolean {
        objective.getScore(lines[line] ?: return false).resetScore()
        objective.getScore(name).score = 15 - line
        lines[line] = name
        return true
    }

    /**
     * Remove an existing line and push all lines after the selected line one up
     * @return false if the line does not exist
     */
    fun removeLine(line: Int): Boolean {
        val key = lines[line] ?: return false // The Line is too high
        if (lines.size - 1 == line) { // Last line
            objective.getScore(key).resetScore()
            lines.remove(line)
            return true

        } else { // Not last line, higher all lines after this line and remove last
            var riser = line + 1
            while (lines.containsKey(riser)) {
                val currentKey = lines[riser]!!
                objective.getScore(currentKey).resetScore()
                lines.remove(riser)
                editLine(riser - 1, currentKey)
                riser++
            }
            return true
        }
    }
}