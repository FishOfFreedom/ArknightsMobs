package com.freefish.arknightsmobs.client.render.model;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.guerrillas.SarkazSoldier;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SarkazSoldierModel extends AnimatedGeoModel<SarkazSoldier> {
    @Override
    public ResourceLocation getModelLocation(SarkazSoldier object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/sarkaz_soldier.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SarkazSoldier object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/sarkaz_soldier.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(SarkazSoldier animatable) {
        return null;
    }
}
