package com.freefish.arknightsmobs.client.render.model;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.client.render.model.tools.MathUtils;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class PatriotModel extends AnimatedGeoModel<Patriot> {
    @Override
    public ResourceLocation getModelLocation(Patriot object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/patriot.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Patriot object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/patriot/patriot.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Patriot animatable) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "animations/patriot.animation.json");
    }


    @Override
    public void setLivingAnimations(Patriot entity, Integer uniqueID, @Nullable AnimationEvent animationEvent) {
        super.setLivingAnimations(entity, uniqueID, animationEvent);
        IBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * 0.017453292F);
        head.setRotationY(extraData.netHeadYaw * 0.017453292F);
    }
}
