package com.freefish.arknightsmobs.client.render.entity;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.guerrillas.Soldier;
import com.freefish.arknightsmobs.client.render.model.SoldierModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SoldierRenderer extends GeoEntityRenderer<Soldier> {
    public SoldierRenderer(EntityRendererManager renderManager) {
        super(renderManager, new SoldierModel());
    }

    @Override
    public ResourceLocation getEntityTexture(Soldier entity) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/packhunter.png");
    }
}
