package com.freefish.arknightsmobs.client.render.layer;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class PatriotWindLayer extends GeoLayerRenderer<Patriot> {
    public ResourceLocation[] textureLocations = new ResourceLocation[]{
        new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/patriot/wind_1.png"),
                new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/patriot/wind_2.png"),
                new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/patriot/wind_3.png")
    };


    public PatriotWindLayer(IGeoRenderer<Patriot> endermanReplacementRenderer) {
        super(endermanReplacementRenderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
                       Patriot entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        GeoModelProvider<Patriot> geomodel = this.getEntityModel();
        renderModel(geomodel, textureLocations[entitylivingbaseIn.pointer], matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, 1.0F, 1, 1, 1);
    }

    @Override
    public RenderType getRenderType(ResourceLocation textureLocation) {
        return RenderType.getEntityTranslucent(textureLocation);
    }
}
