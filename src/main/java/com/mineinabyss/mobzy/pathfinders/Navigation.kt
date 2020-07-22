package com.mineinabyss.mobzy.pathfinders

import com.mineinabyss.mobzy.toNMS
import net.minecraft.server.v1_16_R1.EntityInsentient
import net.minecraft.server.v1_16_R1.EntityLiving
import net.minecraft.server.v1_16_R1.GenericAttributes
import net.minecraft.server.v1_16_R1.NavigationAbstract
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * Original methods by Yannick Lamprecht under the MIT license from [PathfindergoalAPI](https://github.com/yannicklamprecht/PathfindergoalAPI)
 */

class Navigation(private val navigationAbstract: NavigationAbstract, private val handle: EntityInsentient) {
    val doneNavigating get() = navigationAbstract.m()
    val inProgress get() = navigationAbstract.n()
    val pathSearchRange get() = handle.getAttributeInstance(GenericAttributes.FOLLOW_RANGE)?.value?.toFloat()
    fun moveToPosition(x: Double, y: Double, z: Double, speed: Double) = navigationAbstract.a(x, y, z, speed)
    fun moveToEntity(entity: Entity, speed: Double) = navigationAbstract.a(entity.toNMS(), speed)
    fun setSpeed(speed: Double) = navigationAbstract.a(speed)
    fun stopNavigation() = navigationAbstract.o()
}

val EntityInsentient.navigationMZ get() = Navigation(this.navigation, this)
val EntityLiving.living get() = this.bukkitEntity as LivingEntity