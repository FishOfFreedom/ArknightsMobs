package com.freefish.arknightsmobs.client.render.entity;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.guerrillas.CrossBow;
import com.freefish.arknightsmobs.client.render.model.CrossBowModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CrossBowRenderer extends GeoEntityRenderer<CrossBow> {
    public CrossBowRenderer(EntityRendererManager renderManager) {
        super(renderManager, new CrossBowModel());
    }

    @Override
    public ResourceLocation getEntityTexture(CrossBow entity) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/packhunter.png");
    }
}
