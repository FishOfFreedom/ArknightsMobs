package com.freefish.arknightsmobs.server.item.shield;

import com.freefish.arknightsmobs.ArknightsMobs;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SOTIModel extends AnimatedGeoModel<ShieldOfTheInfected> {
    @Override
    public ResourceLocation getModelLocation(ShieldOfTheInfected object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/shield_of_the_infected.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ShieldOfTheInfected object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/item/shield_of_the_infected.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ShieldOfTheInfected animatable) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "animations/shield_of_the_infected.animation.json");
    }
}
