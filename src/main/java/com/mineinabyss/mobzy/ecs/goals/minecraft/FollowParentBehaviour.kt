package com.mineinabyss.mobzy.ecs.goals.minecraft

import com.mineinabyss.idofront.nms.aliases.NMSPathfinderGoal
import com.mineinabyss.idofront.nms.aliases.toNMS
import com.mineinabyss.mobzy.ecs.components.initialization.pathfinding.PathfinderComponent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.server.v1_16_R2.EntityAnimal
import net.minecraft.server.v1_16_R2.PathfinderGoalFollowParent
import org.bukkit.entity.Mob

@Serializable
@SerialName("minecraft:behavior.follow_parent")
class FollowParentBehaviour(
    private val speedModifier: Double = 1.0,
) : PathfinderComponent() {
    override fun build(mob: Mob): NMSPathfinderGoal = PathfinderGoalFollowParent(
        mob.toNMS<EntityAnimal>(),
        speedModifier
    )
}
