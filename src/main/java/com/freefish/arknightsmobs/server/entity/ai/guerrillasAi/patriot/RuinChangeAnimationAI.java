package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.vector.Vector3d;

public class RuinChangeAnimationAI extends AnimationAI<Patriot> {
    Vector3d targetPos;

    public RuinChangeAnimationAI(Patriot patriot, Animation pierce) {
        super(patriot);
    }

    @Override
    public void startExecuting() {
        entity.setIsRunning(false);
        LivingEntity target = entity.getAttackTarget();
        if(target!=null){
            targetPos = target.getPositionVec();
        }
        super.startExecuting();
    }

    @Override
    public void tick() {
        LivingEntity entityTarget = entity.getAttackTarget();
        Vector3d vector3d = targetPos.subtract(entity.getPositionVec()).scale(-0.25d);
        //Vector3d motion = new Vector3d(0,0,0.5).rotateYaw((float)(-entity.rotationYaw/180*Math.PI));
        int tick = entity.getAnimationTick();

        if(tick <= 23 && tick >= 18){
            double i=(tick - 17)/4d;
            Vector3d pos = new Vector3d(targetPos.getX(),targetPos.getY(),targetPos.getZ());
            pos.add(vector3d.mul(i,i,i));
            entity.setPosition(pos.x,pos.y,pos.z);
        }
        double arc = 40d;
        double range = 6;
        float damage = (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        if (entity.getAnimation() == Patriot.RUIN_CHANGE) {
            if (tick < 10 && entityTarget != null) {
                entity.faceEntity(entityTarget, 30F, 30F);
            } else {
                entity.renderYawOffset = entity.prevRenderYawOffset;
            }
            if (tick <= 23 && tick >= 18)
                entity.doRangeAttack(range,arc,damage,true);
        }
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == Patriot.RUIN_CHANGE;
    }
}
