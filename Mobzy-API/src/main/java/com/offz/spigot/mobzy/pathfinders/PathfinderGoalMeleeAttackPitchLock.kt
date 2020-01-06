package com.offz.spigot.mobzy.pathfinders

import com.offz.spigot.mobzy.mobs.CustomMob

class PathfinderGoalMeleeAttackPitchLock(mob: CustomMob, private val speed: Double = 1.0) : MobzyPathfinderGoal(mob) {
    override fun shouldExecute(): Boolean {
        target = nmsEntity.goalTarget?.living ?: return false
        if (target!!.isDead) return false
        return true
    }

    override fun shouldKeepExecuting(): Boolean {
        if (target == null || target!!.isDead) return true
        return false
    }

    private var cooldown: Int = 0
    override fun execute() {
        mob.lookAtPitchLock(target!!)
        if (--cooldown < 0)
            cooldown = 10
        else return
        if (mob.canReach(target!!)) {
            target!!.damage(mob.staticTemplate.attackDamage ?: 0.0, entity)
            //TODO knockback
        }
        navigation.moveToEntity(target!!, speed)
    }
}