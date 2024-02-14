package com.freefish.arknightsmobs.server.item.surtritem;

import com.freefish.arknightsmobs.ArknightsMobs;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SurtrItemModel extends AnimatedGeoModel<SurtrItem> {
    @Override
    public ResourceLocation getModelLocation(SurtrItem object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/laiwanting.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SurtrItem object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/item/laiwanting.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(SurtrItem animatable) {
        return null;
    }
}
