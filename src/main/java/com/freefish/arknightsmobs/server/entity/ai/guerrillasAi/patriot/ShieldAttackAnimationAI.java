package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.ilexiconn.llibrary.server.animation.Animation;

public class ShieldAttackAnimationAI extends AnimationAI<Patriot> {
    public ShieldAttackAnimationAI(Patriot entity, Animation animation) {
        super(entity);
    }

    @Override
    public void tick() {
        entity.setMotion(0, entity.getMotion().y, 0);
        entity.renderYawOffset = entity.prevRenderYawOffset;
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == Patriot.SHIELD_ATTACK;
    }
}
