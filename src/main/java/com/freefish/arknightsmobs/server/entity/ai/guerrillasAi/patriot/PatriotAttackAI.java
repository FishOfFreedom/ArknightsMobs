package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.Random;

public class PatriotAttackAI extends Goal {
    private final Patriot patriot;

    private int repath;
    private double targetX;
    private double targetY;
    private double targetZ;

    private int timeSinceStrengthen;
    private int timeSinceRunning;
    private int timeNormalAttack;

    public PatriotAttackAI(Patriot patriot) {
        this.patriot = patriot;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity target = this.patriot.getAttackTarget();
        return target != null && target.isAlive() && this.patriot.getAnimation() == IAnimatedEntity.NO_ANIMATION && !patriot.getIsEnhanced();
    }

    @Override
    public void startExecuting() {
        this.repath = 0;
    }

    @Override
    public void resetTask() {
        patriot.setIsRunning(false);
        this.patriot.getNavigator().clearPath();
    }

    @Override
    public void tick() {
        LivingEntity target = this.patriot.getAttackTarget();
        if (target == null) return;
        double dist = this.patriot.getDistanceSq(this.targetX, this.targetY, this.targetZ);
        Random random = patriot.getRNG();
        this.patriot.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
        if (--this.repath <= 0 && (
                this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
                        target.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
                this.patriot.getNavigator().noPath()
        ) {
            this.targetX = target.getPosX();
            this.targetY = target.getPosY();
            this.targetZ = target.getPosZ();
            this.repath = 4 + random.nextInt(7);
            if (dist > 32.0D * 32.0D) {
                this.repath += 10;
            } else if (dist > 16.0D * 16.0D) {
                this.repath += 5;
            }
            if (!this.patriot.getNavigator().tryMoveToEntityLiving(target, 1D)) {
                this.repath += 15;
            }
        }
        dist = this.patriot.getDistanceSq(this.targetX, this.targetY, this.targetZ);
        if(patriot.getIsRunning()){
            if(target.getPosY() - this.patriot.getPosY() >= -1 && target.getPosY() - this.patriot.getPosY() <= 3){
                if(patriot.getPredicate()==2&& dist < 16D * 16D)
                    AnimationHandler.INSTANCE.sendAnimationMessage(this.patriot, Patriot.RUIN_CHANGE);
                else if(dist< 3.5d*3.5d)
                    AnimationHandler.INSTANCE.sendAnimationMessage(this.patriot, Patriot.PIERCE);
                if(timeSinceRunning >= 150)
                    patriot.setIsRunning(false);
            }
            timeSinceRunning++;
        }
        else if (dist < 10000  && patriot.timeSinceThrow > 400 && !(target instanceof PlayerEntity)) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this.patriot, Patriot.THROW);
            patriot.timeSinceThrow = 0;
        }
        else if (dist < 10000 && dist > 100 && patriot.timeSinceThrow > 400) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this.patriot, Patriot.THROW);
            patriot.timeSinceThrow = 0;
        }
        else if (target.getPosY() - this.patriot.getPosY() >= -1 && target.getPosY() - this.patriot.getPosY() <= 3) {
            if (dist < 7D * 7D && Math.abs(MathHelper.wrapDegrees(this.patriot.getAngleBetweenEntities(target, this.patriot) - this.patriot.rotationYaw)) < 35.0D) {
                if(shouldFollowUp(3.5-timeNormalAttack*1.5)) {
                    if (random.nextDouble() >= 0.5)
                        AnimationHandler.INSTANCE.sendAnimationMessage(this.patriot, Patriot.RIGHT_SWEPT);
                    else
                        AnimationHandler.INSTANCE.sendAnimationMessage(this.patriot, Patriot.LIGHT_SWEPT);
                    timeNormalAttack++;
                }
                else if(random.nextDouble() <=(0.02+timeNormalAttack*0.01)){
                    if (random.nextDouble() >= 0.5)
                        AnimationHandler.INSTANCE.sendAnimationMessage(this.patriot, Patriot.RAISE_ATTACK);
                    else
                        AnimationHandler.INSTANCE.sendAnimationMessage(this.patriot, Patriot.RAISE_CHANGE);
                    timeNormalAttack=0;
                }
            }
            else if(dist < 3.5D * 3.5D && random.nextDouble() <= 0.5){
                repath = 0;
                AnimationHandler.INSTANCE.sendAnimationMessage(this.patriot, Patriot.STOMP);
            }else if (patriot.timeSincePierce > 150 && !patriot.getIsRunning()){
                patriot.setIsRunning(true);
                patriot.timeSincePierce = 0;
                this.timeSinceRunning = 0;
            }else if (this.timeSinceStrengthen > 200){
                AnimationHandler.INSTANCE.sendAnimationMessage(this.patriot, Patriot.STRENGTHEN);
                timeSinceStrengthen = 0;
            }
        }
        patriot.timeSinceThrow++;patriot.timeSincePierce++;this.timeSinceStrengthen++;
    }

    private boolean shouldFollowUp(double bonusRange) {
        LivingEntity entityTarget = patriot.getAttackTarget();
        if (entityTarget != null && entityTarget.isAlive()) {
            Vector3d targetMoveVec = entityTarget.getMotion();
            Vector3d betweenEntitiesVec = patriot.getPositionVec().subtract(entityTarget.getPositionVec());
            boolean targetComingCloser = targetMoveVec.dotProduct(betweenEntitiesVec) < 0;
            double targetDistance = patriot.getDistance(entityTarget);
            return targetDistance < bonusRange || (targetDistance <5 + bonusRange && targetComingCloser);
        }
        return false;
    }
}
