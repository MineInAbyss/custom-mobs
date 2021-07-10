package com.mineinabyss.mobzy.mobs.types

import com.mineinabyss.idofront.nms.aliases.NMSEntityType
import com.mineinabyss.idofront.nms.aliases.NMSWorld
import com.mineinabyss.mobzy.processor.GenerateFromBase
import net.minecraft.server.v1_16_R3.EntityAgeable
import net.minecraft.server.v1_16_R3.EntityAnimal
import net.minecraft.server.v1_16_R3.WorldServer

@GenerateFromBase(base = MobBase::class, createFor = [EntityAnimal::class])
open class PassiveMob(type: NMSEntityType<*>, world: NMSWorld) : MobzyEntityAnimal(world, type) {
    override fun createPathfinders() {
//        addPathfinderGoal(1, PathfinderGoalFloat(this))
//        addPathfinderGoal(2, PathfinderGoalPanic(this, 1.25))
//        addPathfinderGoal(3, PathfinderGoalBreed(this, 1.0))
//        addPathfinderGoal(5, PathfinderGoalFollowParent(this, 1.1))
//        addPathfinderGoal(6, PathfinderGoalRandomStrollLand(this, 1.0))
//        addPathfinderGoal(7, PathfinderGoalLookAtPlayer(this, EntityPlayer::class.java, 6.0f))
    }

    override fun createChild(p0: WorldServer?, p1: EntityAgeable?): EntityAgeable? = null

    init {
        addScoreboardTag("passiveMob")
        entity.removeWhenFarAway = false
    }
}
