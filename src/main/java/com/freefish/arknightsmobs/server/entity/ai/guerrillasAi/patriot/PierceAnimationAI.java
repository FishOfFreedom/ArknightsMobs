package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.freefish.arknightsmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class PierceAnimationAI extends AnimationAI<Patriot> {
    public PierceAnimationAI(Patriot patriot, Animation pierce) {
        super(patriot);
    }

    @Override
    public void startExecuting() {
        entity.setIsRunning(false);
        super.startExecuting();
    }

    @Override
    public void tick() {
        LivingEntity entityTarget = entity.getAttackTarget();
        //Vector3d motion = new Vector3d(0,0,0.5).rotateYaw((float)(-entity.rotationYaw/180*Math.PI));
        Vector3d motion;
        int tick = entity.getAnimationTick();

        if(tick <= 5){
            motion = new Vector3d(0,entity.getMotion().y,0.2).rotateYaw((float)(-entity.rotationYaw/180*Math.PI));
        }
        else if(tick <= 15){
            double rate = (15 - tick)/40d;
            motion = new Vector3d(0,entity.getMotion().y,rate).rotateYaw((float)(-entity.rotationYaw/180*Math.PI));
        }
        else
            motion = new Vector3d(0, entity.getMotion().y, 0);
        entity.setMotion(motion);
        double arc = 40d;
        double range = 8;

        if(tick==5) entity.isCanBeAttacking=true;
        else if(tick==20) entity.isCanBeAttacking=false;
        float damage = (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        if (entity.getAnimation() == Patriot.PIERCE) {
            if (tick < 5 && entityTarget != null) {
                entity.faceEntity(entityTarget, 30F, 30F);
            } else {
                entity.renderYawOffset = entity.prevRenderYawOffset;
            }if (tick == 10)
                entity.doRangeAttack(range,arc,damage,false);
        }
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == Patriot.PIERCE;
    }
}

