package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.LivingEntity;

public class ThrowAnimationAI extends AnimationAI<Patriot> {
    Patriot patriot;
    public ThrowAnimationAI(Patriot patriot, Animation aThrow) {
        super(patriot);
        this.patriot = patriot;
    }

    @Override
    public void tick() {
        LivingEntity entityTarget = entity.getAttackTarget();
        entity.setMotion(0, entity.getMotion().y, 0);
        if (entity.getAnimationTick() < 30 && entityTarget != null) {
            entity.faceEntity(entityTarget, 30F, 30F);
        }
        entity.renderYawOffset = entity.prevRenderYawOffset;
    }
    @Override
    protected boolean test(Animation animation) {
        return animation == Patriot.THROW;
    }
}
