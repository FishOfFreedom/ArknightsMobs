package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.special.EntityFallingBlock;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class LightSweptAnimationAI extends AnimationAI<Patriot> {
    public LightSweptAnimationAI(Patriot entity, Animation animation) {
        super(entity);
    }

    @Override
    public void tick() {
        LivingEntity entityTarget = entity.getAttackTarget();
        int tick = entity.getAnimationTick();
        double arc = 110d;
        double range = 6;
        Vector3d motion=new Vector3d(0, entity.getMotion().y, 0);
        if(entityTarget != null&&entity.getDistanceSq(entityTarget)>4) {
            if (entity.getAnimation() == Patriot.LIFT && entity.getAnimationTick() <= 25) {
                motion = new Vector3d(0, entity.getMotion().y, 0.1).rotateYaw((float) (-entity.renderYawOffset / 180 * Math.PI));
            }
        }
        entity.setMotion(motion);
        float damage = (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        if (entity.getAnimation() == Patriot.LIGHT_SWEPT) {
            if (tick < 5 && entityTarget != null) {
                entity.faceEntity(entityTarget, 30F, 30F);
            } else {
                entity.renderYawOffset = entity.prevRenderYawOffset;
            }
            if (tick == 11) {
                entity.doRangeAttack(4, 80d, damage * 0.8f, false);
                entity.timeSinceThrow+=20;
            }
            if(tick == 18)
                AnimationHandler.INSTANCE.sendAnimationMessage(entity, Patriot.SWEPT_2);
        } else if(entity.getAnimation() == Patriot.SWEPT_2){
            entity.rotationYaw = entity.prevRotationYaw;
            if(tick == 4) entity.isCanBeAttacking=true;
            else if(tick == 27) entity.isCanBeAttacking=false;
            if (tick == 28) {
                entity.doRangeAttack(range, arc, damage, true);
                entity.timeSinceThrow+=20;
            }
            if(tick == 29&&entityTarget != null && (entity.getDistanceSq(entityTarget) <= 25 || entity.getRNG().nextDouble() <= 0.5))
                AnimationHandler.INSTANCE.sendAnimationMessage(entity, Patriot.LIFT);
        } else if(entity.getAnimation() == Patriot.LIFT){
            if (tick < 15 && entityTarget != null) {
                entity.faceEntity(entityTarget, 30F, 30F);
            } else {
                entity.rotationYaw = entity.prevRotationYaw;
            }
            if (tick == 28) {
                entity.timeSinceThrow+=20;
                entity.doRangeAttack(5, 60d, damage * 1.2f, true);
                entity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1, 1F + entity.getRNG().nextFloat() * 0.1F);
                double yaw = entity.renderYawOffset/180.0*Math.PI;
                Vector3d front = entity.getPositionVec().add(-Math.sin(yaw)*5,0,Math.cos(yaw)*5);
                for(int i=0;i<16;i++){
                    Vector3d vector3d = front.add(new Vector3d(1+entity.getRNG().nextDouble()*2,-0.5,0).rotateYaw((float) Math.PI*i/8));
                    EntityFallingBlock entityFallingBlock = new EntityFallingBlock(EntityRegistry.FALLING_BLOCK.get(),entity.world,entity.world.getBlockState(new BlockPos(vector3d)),entity.getRNG().nextFloat());
                    entityFallingBlock.setPosition(vector3d.x,vector3d.y,vector3d.z);
                    entity.world.addEntity(entityFallingBlock);
                }
            }
        }
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == Patriot.LIGHT_SWEPT || animation == Patriot.SWEPT_2 || animation == Patriot.LIFT;
    }
}
