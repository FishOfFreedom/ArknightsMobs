package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi;

import com.freefish.arknightsmobs.server.entity.guerrillas.CrossBow;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

public class CrossBowAttackAI extends Goal {
    private final CrossBow crossBow;
    private int attackCool = 0;

    public CrossBowAttackAI(CrossBow crossBow, int attackCool) {
        this.crossBow = crossBow;
    }
    @Override
    public boolean shouldExecute() {
        LivingEntity target = crossBow.getAttackTarget();
        return target != null && target.isAlive() && crossBow.isAttacking();
    }

    @Override
    public boolean shouldContinueExecuting() {
        LivingEntity target = crossBow.getAttackTarget();
        return target != null && target.isAlive() && crossBow.isAttacking();
    }

    @Override
    public void tick() {
        LivingEntity target = crossBow.getAttackTarget();
        if(target != null) {
            if(attackCool < 40) attackCool++;

            if(attackCool==40) {
                crossBow.attackEntityWithRangedAttack(target , 1);
                attackCool = 0;
            }
        }
    }
}
