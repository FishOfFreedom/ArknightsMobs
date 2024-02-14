package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.guerrillas.GuerrillasEntity;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.List;

public class StrengthenAnimationAI extends AnimationAI<Patriot> {
    public StrengthenAnimationAI(Patriot entity, Animation animation) {
        super(entity);
    }

    @Override
    public void tick() {
        entity.setMotion(0, entity.getMotion().y, 0);
        entity.renderYawOffset = entity.prevRenderYawOffset;
        int tick = entity.getAnimationTick();

        if(tick == 30){
            List<GuerrillasEntity> entitiesNearby = entity.getEntitiesNearby(GuerrillasEntity.class, 15);
            for(GuerrillasEntity entity:entitiesNearby){
                entity.addPotionEffect(new EffectInstance(Effects.STRENGTH,100,1));
            }
        }
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == Patriot.STRENGTHEN;
    }
}
