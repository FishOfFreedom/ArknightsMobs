package com.freefish.arknightsmobs.client.render.entity;

import com.freefish.arknightsmobs.client.render.model.FlameEntityModel;
import com.freefish.arknightsmobs.server.entity.surtr.FlameEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class FlameEntityRenderer extends GeoProjectilesRenderer<FlameEntity> {
   public FlameEntityRenderer(EntityRendererManager renderManager) {
      super(renderManager, new FlameEntityModel());
   }
   
	@Override
	public void renderEarly(FlameEntity animatable, MatrixStack stackIn, float partialTicks,
                            IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float alpha) {
		if (animatable.lifeTime <= 1) {
			float scaleFactor = 0.0F;
			stackIn.scale(scaleFactor, scaleFactor, scaleFactor);
		}
	}
	
	@Override
	protected int getBlockLight(FlameEntity p_225624_1_, BlockPos p_225624_2_) {
			return 15;
		}


   @Override
   public RenderType getRenderType(FlameEntity animatable, float partialTicks, MatrixStack stack,
                                   IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                   ResourceLocation textureLocation) {
      return RenderType.getEntityTranslucent(getTextureLocation(animatable));
   }
}