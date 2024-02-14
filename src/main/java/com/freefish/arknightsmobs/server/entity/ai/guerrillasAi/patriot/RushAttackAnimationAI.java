package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.ilexiconn.llibrary.server.animation.Animation;

public class RushAttackAnimationAI extends AnimationAI<Patriot> {
    public RushAttackAnimationAI(Patriot entity,Animation animation) {
        super(entity);
    }
    @Override
    protected boolean test(Animation animation) {
        return animation == Patriot.RUSH_ATTACK;
    }
}
