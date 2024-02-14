package com.freefish.arknightsmobs.server.item.halberd;

import com.freefish.arknightsmobs.ArknightsMobs;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HOTIModel extends AnimatedGeoModel<GanRanZheZhiJiItem> {
    @Override
    public ResourceLocation getModelLocation(GanRanZheZhiJiItem object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "geo/halberd_of_the_infected.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GanRanZheZhiJiItem object) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/item/halberd_of_the_infected.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GanRanZheZhiJiItem animatable) {
        return null;
    }
}
