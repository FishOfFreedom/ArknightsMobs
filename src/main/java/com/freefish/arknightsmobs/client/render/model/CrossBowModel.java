package com.freefish.arknightsmobs.client.render.model;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.guerrillas.CrossBow;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CrossBowModel extends AnimatedGeoModel<CrossBow> {
    @Override
    public ResourceLocation getModelLocation(CrossBow object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/cross_bow.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CrossBow object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/cross_bow.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CrossBow animatable) {
        return null;
    }
}
