package com.freefish.arknightsmobs.client.render.entity;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.guerrillas.Messenger;
import com.freefish.arknightsmobs.client.render.model.MessengerModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MessengerRenderer extends GeoEntityRenderer<Messenger> {
    public MessengerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new MessengerModel());
    }

    @Override
    public ResourceLocation getEntityTexture(Messenger entity) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/chuanlingbing.png");
    }
}
