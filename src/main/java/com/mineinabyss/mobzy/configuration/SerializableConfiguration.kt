package com.mineinabyss.mobzy.configuration

import com.charleskorn.kaml.Yaml
import com.mineinabyss.idofront.messaging.logInfo
import com.mineinabyss.idofront.messaging.logSuccess
import kotlinx.serialization.KSerializer
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File

abstract class SerializableConfiguration<T>(
        val file: File,
        val plugin: Plugin,
        val serializer: KSerializer<T>
) {
    val config: FileConfiguration
    var info: T private set
    val serialFormat = Yaml(configuration = com.charleskorn.kaml.YamlConfiguration(encodeDefaults = false))

    init {
        logInfo("Registering configuration ${file.name}")
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdir()
        if (!file.exists()) {
            file.parentFile.mkdirs()
            plugin.saveResource(file.name, false)
            logSuccess("${file.name} has been created")
        }
        config = YamlConfiguration.loadConfiguration(file)
        serialFormat
        info = parseConfiguration()
        logSuccess("Registered configuration: ${file.name}")
    }

    fun reload() {
        config.load(file)
        info = parseConfiguration()
    }

    fun save() {
        config.loadFromString(serialFormat.stringify(serializer, info))
        config.save(file)
    }

    private fun parseConfiguration(): T = serialFormat.parse(serializer, config.saveToString())
}