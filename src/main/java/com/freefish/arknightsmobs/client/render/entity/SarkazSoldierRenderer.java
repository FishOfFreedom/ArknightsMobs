package com.freefish.arknightsmobs.client.render.entity;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.guerrillas.SarkazSoldier;
import com.freefish.arknightsmobs.client.render.model.SarkazSoldierModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SarkazSoldierRenderer extends GeoEntityRenderer<SarkazSoldier> {
    public SarkazSoldierRenderer(EntityRendererManager renderManager) {
        super(renderManager, new SarkazSoldierModel());
    }

    @Override
    public ResourceLocation getEntityTexture(SarkazSoldier entity) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/packhunter.png");
    }
}
