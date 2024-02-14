package com.freefish.arknightsmobs.client.render.entity;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.guerrillas.ShieldGuard;
import com.freefish.arknightsmobs.client.render.model.ShieldGuardModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ShieldGuardRenderer extends GeoEntityRenderer<ShieldGuard> {
    public ShieldGuardRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ShieldGuardModel());
    }

    @Override
    public ResourceLocation getEntityTexture(ShieldGuard entity) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/dunwei.png");
    }
}
