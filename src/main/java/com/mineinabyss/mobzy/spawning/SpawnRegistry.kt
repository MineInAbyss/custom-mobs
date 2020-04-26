package com.mineinabyss.mobzy.spawning

import com.charleskorn.kaml.Yaml
import com.mineinabyss.idofront.messaging.logSuccess
import com.mineinabyss.mobzy.Mobzy
import com.mineinabyss.mobzy.registration.MobzyTypes
import com.mineinabyss.mobzy.spawning.SpawnRegistry.regionSpawns
import com.mineinabyss.mobzy.spawning.regions.SpawnRegion
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import kotlinx.serialization.Serializable
import org.bukkit.configuration.file.FileConfiguration
import java.util.*

@Serializable
data class SpawnConfiguration(
        val regions: Map<String, SpawnRegion>
)
/**
 * @property regionSpawns A map of region names to their [SpawnRegion].
 */
object SpawnRegistry {
    private val regionSpawns: MutableMap<String, SpawnRegion> = HashMap()

    fun unregisterSpawns() = regionSpawns.clear()

    @Suppress("UNCHECKED_CAST")
    fun readCfg(config: FileConfiguration) {
        val spawnConfiguration = Yaml.default.parse(SpawnConfiguration.serializer(), config.saveToString())
        regionSpawns.putAll(spawnConfiguration.regions)
        logSuccess("Reloaded spawns.yml")
    }

    fun reuseMobSpawn(reusedMob: String): MobSpawn = //TODO comment this because I have no idea what it's doing
            (regionSpawns[reusedMob.substring(0, reusedMob.indexOf(':'))]
                    ?: error("Could not find registered region for $reusedMob"))
                    .getSpawnOfType(MobzyTypes[reusedMob.substring(reusedMob.indexOf(':') + 1)])

    /**
     * Takes a list of spawn region names and converts to a list of [MobSpawn]s from those regions
     */
    fun List<ProtectedRegion>.getMobSpawnsForRegions(/*creatureType: String*/): List<MobSpawn> = this
            .filter { it.flags.containsKey(Mobzy.MZ_SPAWN_REGIONS) }
            .flatMap { it.getFlag(Mobzy.MZ_SPAWN_REGIONS)!!.split(",") }
            //up to this point, gets a list of the names of spawn areas in this region
            .mapNotNull { regionSpawns[it]?.spawns/*.getSpawnsFor(creatureType)*/ }
            .flatten()
}