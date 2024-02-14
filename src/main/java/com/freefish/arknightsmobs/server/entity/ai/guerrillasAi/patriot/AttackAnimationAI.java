package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.vector.Vector3d;

public class AttackAnimationAI extends AnimationAI<Patriot> {
    public AttackAnimationAI(Patriot entity, Animation animation) {
        super(entity);
    }

    @Override
    public void tick() {
        LivingEntity entityTarget = entity.getAttackTarget();
        Vector3d motion = new Vector3d(0, entity.getMotion().y, 0);
        int tick = entity.getAnimationTick();
        if(entityTarget != null&&entity.getDistanceSq(entityTarget)>4) {
            if (entity.getAnimation() == Patriot.RIGHT_SWEPT && tick >= 25 && tick <= 35) {
                double rate = (35 - tick) / 50d;
                motion = new Vector3d(0, entity.getMotion().y, rate).rotateYaw((float) (-entity.renderYawOffset / 180 * Math.PI));
            } else if (entity.getAnimation() == Patriot.LEFT_SWEPT && tick <= 45) {
                double rate;
                if (tick <= 30)
                    rate = 0.045;
                else
                    rate = (45 - tick) / 40d;
                motion = new Vector3d(0, entity.getMotion().y, rate).rotateYaw((float) (-entity.renderYawOffset / 180 * Math.PI));
            }
        }
        entity.setMotion(motion);

        double arc = 200d;
        double range = 6;
        float damage = (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        if (entity.getAnimation() == Patriot.RIGHT_SWEPT) {
            if (tick < 23 && entityTarget != null) {
                entity.faceEntity(entityTarget, 30F, 30F);
            } else {
                entity.renderYawOffset = entity.prevRenderYawOffset;
            }
            if(tick == 10) entity.isCanBeAttacking = true;
            else if(tick==24)entity.isCanBeAttacking = false;
            if (tick == 27) {
                entity.doRangeAttack(range, arc, damage, true);
                entity.timeSinceThrow+=20;
            }
            if(tick == 30){
                if(entityTarget != null && (entity.getDistanceSq(entityTarget) <= 25 || entity.getRNG().nextDouble() <= 0.5))
                    AnimationHandler.INSTANCE.sendAnimationMessage(entity, Patriot.LEFT_SWEPT);
            }
        }else if(entity.getAnimation() == Patriot.LEFT_SWEPT){
            if (tick < 42 && entityTarget != null) {
                entity.faceEntity(entityTarget, 30F, 30F);
            } else {
                entity.renderYawOffset = entity.prevRenderYawOffset;
            }
            if(tick==32)entity.isCanBeAttacking = true;
            else if(tick==50)entity.isCanBeAttacking = false;
            if (tick == 40) {
                entity.doRangeAttack(range, arc, damage * 1.2f, true);
                entity.timeSinceThrow += 20;
            }
        }
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == Patriot.RIGHT_SWEPT || animation == Patriot.LEFT_SWEPT;
    }
}
