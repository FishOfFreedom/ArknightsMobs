package com.freefish.arknightsmobs.client.render.model;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.guerrillas.Soldier;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SoldierModel extends AnimatedGeoModel<Soldier> {
    @Override
    public ResourceLocation getModelLocation(Soldier object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/soldier.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Soldier object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/soldier.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Soldier animatable) {
        return null;
    }
}
