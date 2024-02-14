package com.freefish.arknightsmobs.client.render.entity;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.client.render.model.SurtrModel;
import com.freefish.arknightsmobs.server.entity.surtr.Surtr;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SurtrRenderer extends GeoEntityRenderer<Surtr> {
    public SurtrRenderer(EntityRendererManager renderManager) {
        super(renderManager, new SurtrModel());
    }

    @Override
    public ResourceLocation getEntityTexture(Surtr entity) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/surtr.png");
    }
}
