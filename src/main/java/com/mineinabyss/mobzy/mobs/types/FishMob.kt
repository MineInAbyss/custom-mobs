package com.mineinabyss.mobzy.mobs.types

import com.mineinabyss.idofront.nms.aliases.*
import com.mineinabyss.mobzy.processor.GenerateFromBase
import net.minecraft.server.v1_16_R3.EntityFishSchool

@GenerateFromBase(base = MobBase::class, createFor = [EntityFishSchool::class])
open class FishMob(type: NMSEntityType<*>, world: NMSWorld) : MobzyEntityFishSchool(world, type) {
    //bucket you get from picking up fish (we disable this interaction anyways)
    override fun eK(): NMSItemStack = NMSItemStack(NMSItems.WATER_BUCKET)

    //can't be null so it's harder to make this configurable
    override fun getSoundFlop(): NMSSound = NMSSounds.ENTITY_COD_FLOP

    //on player interact
    override fun b(entityhuman: NMSEntityHuman, enumhand: NMSHand) = NMSInteractionResult.PASS

    init {
        //TODO dont add these scoreboard tags, I think they're only used to filter by type when spawning, just use a
        // better system than this...
        addScoreboardTag("fishMob")
//        entity.removeWhenFarAway = true
    }
}
