package net.axay.kspigot.extensions.bukkit

import org.bukkit.Chunk
import org.bukkit.block.Block

/**
 * @return All blocks in this chunk.
 */
val Chunk.allBlocks
    get() = LinkedHashSet<Block>().apply {
        for (y in world.minHeight until world.maxHeight) {
            for (x in 0 until 16)
                for (z in 0 until 16)
                    add(getBlock(x, y, z))
        }
    }
