package com.freefish.arknightsmobs.client.render.model;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.guerrillas.ShieldGuard;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ShieldGuardModel extends AnimatedGeoModel<ShieldGuard> {
    @Override
    public ResourceLocation getModelLocation(ShieldGuard object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/dunwei.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ShieldGuard object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/dunwei.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ShieldGuard animatable) {
        return null;
    }
}
