package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.freefish.arknightsmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class RaiseChangeAnimationAI extends AnimationAI<Patriot> {
    public RaiseChangeAnimationAI(Patriot entity, Animation animation) {
        super(entity);
    }

    @Override
    public void tick() {
        LivingEntity entityTarget = entity.getAttackTarget();
        int tick = entity.getAnimationTick();
        double arc = 110d;
        double range = 6;
        Vector3d motion= new Vector3d(0, entity.getMotion().y, 0);
        if(entityTarget != null&&entity.getDistanceSq(entityTarget)>4) {
            if (entity.getAnimation() == Patriot.RAISE_CHANGE && entity.getAnimationTick() <= 25) {
                motion = new Vector3d(0, entity.getMotion().y, 0.1).rotateYaw((float) (-entity.renderYawOffset / 180 * Math.PI));
            }
        }
        entity.setMotion(motion);
        float damage = (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();

        if (entity.getAnimation() == Patriot.RAISE_CHANGE) {
            if (tick < 27 && entityTarget != null) {
                entity.faceEntity(entityTarget, 30F, 30F);
            } else {
                entity.renderYawOffset = entity.prevRenderYawOffset;
            }
            if (tick == 30) {
                entity.timeSinceThrow+=20;
                entity.doRangeAttack(range,arc,damage,false);
            }
        }
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == Patriot.RAISE_CHANGE;
    }
}
