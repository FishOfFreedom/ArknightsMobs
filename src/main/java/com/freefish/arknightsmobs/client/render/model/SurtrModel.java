package com.freefish.arknightsmobs.client.render.model;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.surtr.Surtr;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SurtrModel extends AnimatedGeoModel<Surtr> {
    @Override
    public ResourceLocation getModelLocation(Surtr object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/surtr.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Surtr object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/surtr.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Surtr animatable) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "animations/surtr.animation.json");
    }
}
