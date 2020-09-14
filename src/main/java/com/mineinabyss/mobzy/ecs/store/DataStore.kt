package com.mineinabyss.mobzy.ecs.store

import com.mineinabyss.geary.ecs.MobzyComponent
import com.mineinabyss.geary.ecs.engine.Engine
import com.mineinabyss.geary.ecs.serialization.Formats.cborFormat
import com.mineinabyss.mobzy.mobzy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

inline fun <reified T : MobzyComponent> PersistentDataContainer.encode(serializer: KSerializer<T> = cborFormat.serializersModule.serializer(), value: T) {
    val encoded = cborFormat.encodeToByteArray(serializer, value)
    this[NamespacedKey(mobzy, T::class.qualifiedName ?: error("")), PersistentDataType.BYTE_ARRAY] = encoded
}

//TODO make others pass plugin here
inline fun <reified T : MobzyComponent> PersistentDataContainer.decode(serializer: KSerializer<T> = cborFormat.serializersModule.serializer()): T? {
    val encoded = this[NamespacedKey(mobzy, T::class.qualifiedName ?: error("")), PersistentDataType.BYTE_ARRAY]
            ?: return null
    return cborFormat.decodeFromByteArray(serializer, encoded)
}

fun PersistentDataContainer.encodeComponents(components: Collection<MobzyComponent>) {
    isGearyEntity = true
    //remove all currently present keys, since removing a component should be reflected here as well
    keys.filter { it.namespace == "gearyecs" }.forEach { remove(it) }

    //get the serializer registered under the MobzyComponent class through polymorphic serialization, and use it to
    // write a serialized value under its serialname
    components.forEach { value ->
        val serializer = cborFormat.serializersModule.getPolymorphic(MobzyComponent::class, value)
                ?: return@forEach //TODO error?
        @Suppress("DEPRECATION") //we really want this to be a unique key!
        this[NamespacedKey("gearyecs", serializer.descriptor.serialName.toMCKey()), PersistentDataType.BYTE_ARRAY] =
                cborFormat.encodeToByteArray(serializer, value)
    }
}

fun String.toMCKey() = replace(":", "_")
fun String.toSerialKey() = replace("_", ":")

fun PersistentDataContainer.decodeComponents(): Set<MobzyComponent> {
    //key is serialname, we find all the valid ones registered in our module and use those serializers to deserialize
    return keys.mapNotNull { key ->
        val serializer = cborFormat.serializersModule.getPolymorphic(MobzyComponent::class, key.key.toSerialKey())
                ?: return@mapNotNull null
        val encoded = this[key, PersistentDataType.BYTE_ARRAY] ?: return@mapNotNull null
        cborFormat.decodeFromByteArray(serializer, encoded)
    }.toSet()
}

var PersistentDataContainer.isGearyEntity
    get() = has(Engine.componentsKey, PersistentDataType.BYTE)
    set(value) =
        if (value)
            set(Engine.componentsKey, PersistentDataType.BYTE, 1) //TODO are there any empty keys?
        else
            remove(Engine.componentsKey)
