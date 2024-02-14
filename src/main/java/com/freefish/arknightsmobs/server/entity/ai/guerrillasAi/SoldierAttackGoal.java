package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi;

import com.freefish.arknightsmobs.server.entity.guerrillas.Soldier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SoldierAttackGoal extends Goal {
    private final Soldier soldier;

    private int repath;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int attackCoolDown;

    public SoldierAttackGoal(Soldier soldier) {
        this.soldier = soldier;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity target = this.soldier.getAttackTarget();
        return target != null && target.isAlive() && soldier.isAttacking();
    }

    @Override
    public void startExecuting() {
        this.repath = 0;
    }

    @Override
    public void resetTask() {
        this.soldier.getNavigator().clearPath();
    }

    @Override
    public void tick() {
        LivingEntity target = this.soldier.getAttackTarget();
        if (target == null) return;
        double dist = this.soldier.getDistanceSq(this.targetX, this.targetY, this.targetZ);
        this.soldier.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
        if (--this.repath <= 0 && (
                this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
                        target.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
                this.soldier.getNavigator().noPath()
        ) {
            this.targetX = target.getPosX();
            this.targetY = target.getPosY();
            this.targetZ = target.getPosZ();
            this.repath = 4 + this.soldier.getRNG().nextInt(7);
            if (dist > 32.0D * 32.0D) {
                this.repath += 10;
            } else if (dist > 16.0D * 16.0D) {
                this.repath += 5;
            }
            if (!this.soldier.getNavigator().tryMoveToEntityLiving(target, 1D)) {
                this.repath += 15;
            }
        }
        double dist1 = this.soldier.getDistanceSq(target);
        if(attackCoolDown < 10) attackCoolDown ++;
        if (dist1 <= dist && attackCoolDown == 10) {
            this.soldier.attackEntityAsMob(target);
            attackCoolDown = 0;
            }
        }
}
