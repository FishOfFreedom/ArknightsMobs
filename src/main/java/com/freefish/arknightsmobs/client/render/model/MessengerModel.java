package com.freefish.arknightsmobs.client.render.model;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.guerrillas.Messenger;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MessengerModel extends AnimatedGeoModel<Messenger> {
    @Override
    public ResourceLocation getModelLocation(Messenger object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/messenger.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Messenger object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/messenger.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Messenger animatable) {
        return null;
    }
}
