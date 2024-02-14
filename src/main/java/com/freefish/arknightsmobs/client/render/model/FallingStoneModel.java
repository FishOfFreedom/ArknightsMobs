package com.freefish.arknightsmobs.client.render.model;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.special.FallingStone;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FallingStoneModel extends AnimatedGeoModel<FallingStone> {
    @Override
    public ResourceLocation getModelLocation(FallingStone object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/falling_stone.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FallingStone object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/falling_stone.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(FallingStone animatable) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "animations/falling_stone.animation.json");
    }
}
