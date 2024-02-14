package com.freefish.arknightsmobs.client.render.model;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.ganranzhezhiji.GanRanZheZhiJi;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class GanRanZheZhiJiModel extends AnimatedGeoModel<GanRanZheZhiJi> {
    @Override
    public void setLivingAnimations(GanRanZheZhiJi entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone body = this.getAnimationProcessor().getBone("halber");
        body.setRotationZ(-90.0f * (float)(Math.PI) / 180.0f);
    }

    @Override
    public ResourceLocation getModelLocation(GanRanZheZhiJi object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/ganranzhezhiji.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GanRanZheZhiJi object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/item/halberd_of_the_infected.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GanRanZheZhiJi animatable) {
        return null;
    }
}
