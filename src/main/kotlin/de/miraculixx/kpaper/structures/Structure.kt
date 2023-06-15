package de.miraculixx.kpaper.structures

import de.miraculixx.kpaper.annotations.NMS_General
import de.miraculixx.kpaper.data.nbtData
import de.miraculixx.kpaper.extensions.bukkit.spawnCleanEntity
import de.miraculixx.kpaper.extensions.geometry.SimpleLocation3D
import de.miraculixx.kpaper.particles.KSpigotParticle
import net.minecraft.nbt.CompoundTag
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

interface StructureData {
    fun createAt(loc: Location)
}

class SingleStructureData(
    val location: SimpleLocation3D,
    val structureData: StructureData,
)

data class Structure(
    val structureData: Set<SingleStructureData>,
) {
    constructor(vararg structureDataSets: Set<SingleStructureData>)
            : this(structureDataSets.flatMapTo(HashSet()) { it })
}

data class StructureDataMaterial(
    val material: Material,
) : StructureData {
    override fun createAt(loc: Location) {
        loc.block.type = material
    }
}

data class StructureDataBlock(
    val material: Material,
    val blockData: BlockData,
) : StructureData {
    constructor(block: Block) : this(block.type, block.blockData)

    override fun createAt(loc: Location) {
        loc.block.let {
            it.type = material
            it.blockData = blockData
        }
    }
}

@NMS_General
data class StructureDataEntity(
    val entityType: EntityType,
    val nbtData: CompoundTag,
) : StructureData {
    constructor(entity: Entity) : this(entity.type, entity.nbtData)
    constructor(entityType: EntityType) : this(entityType, CompoundTag())

    @Suppress("DEPRECATION")
    override fun createAt(loc: Location) {
        (loc.spawnCleanEntity(entityType) as CraftEntity).handle.load(nbtData)
    }
}

data class StructureDataParticle(
    val particle: KSpigotParticle,
) : StructureData {
    override fun createAt(loc: Location) {
        particle.spawnAt(loc)
    }
}
