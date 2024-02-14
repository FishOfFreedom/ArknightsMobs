package com.freefish.arknightsmobs.client.render.entity;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.client.render.model.FallingStoneModel;
import com.freefish.arknightsmobs.client.render.model.tools.MathUtils;
import com.freefish.arknightsmobs.server.entity.special.FallingStone;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexConsumer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.ForgeRenderTypes;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class FallingStoneRenderer extends GeoEntityRenderer<FallingStone> {
    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation(ArknightsMobs.MOD_ID, "textures/particle/trail.png");
    public FallingStoneRenderer(EntityRendererManager renderManager) {
        super(renderManager, new FallingStoneModel());
    }


    @Override
    public void render(FallingStone entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.hasTrail()) {
            double x = MathHelper.lerp(partialTicks, entityIn.prevPosX, entityIn.getPosX());
            double y = MathHelper.lerp(partialTicks, entityIn.prevPosY, entityIn.getPosY());
            double z = MathHelper.lerp(partialTicks, entityIn.prevPosZ, entityIn.getPosZ());
            matrixStackIn.push();
            matrixStackIn.translate(-x, -y, -z);
            renderTrail(entityIn, partialTicks, matrixStackIn, bufferIn, 0.8f, 0.4f, 0.05f, 0.6F, packedLightIn);
            matrixStackIn.pop();
        }

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(FallingStone entity) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/falling_stone.png");
    }

    private void renderTrail(FallingStone entityIn, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer bufferIn, float trailR, float trailG, float trailB, float trailA, int packedLightIn) {
        int samples = 0;
        int sampleSize = 20;
        float trailHeight = 1.5F;
        float trailZRot = 0;
        Vector3d topAngleVec = new Vector3d(   trailHeight , -1, 0).rotateRoll(trailZRot);
        Vector3d bottomAngleVec = new Vector3d(-trailHeight, -1, 0).rotateRoll(trailZRot);
        Vector3d drawFrom = entityIn.getTrailPosition(0, partialTicks);
        IVertexConsumer vertexconsumer =(IVertexConsumer)bufferIn.getBuffer(ForgeRenderTypes.getUnlitTranslucent(TRAIL_TEXTURE));
        while (samples < sampleSize) {
            Vector3d sample = entityIn.getTrailPosition(samples + 2, partialTicks);
            float u1 = samples / (float) sampleSize;
            float u2 = u1 + 1 / (float) sampleSize;

            Vector3d draw1 = drawFrom;
            Vector3d draw2 = sample;

            MatrixStack.Entry posestack$pose = poseStack.getLast();
            Matrix4f matrix4f = posestack$pose.getMatrix();
            Matrix3f matrix3f = posestack$pose.getNormal();
            vertexconsumer.pos( matrix4f, (float) draw1.x + (float) bottomAngleVec.x, (float) draw1.y + (float) bottomAngleVec.y, (float) draw1.z + (float) bottomAngleVec.z).color(trailR, trailG, trailB, trailA).tex(u1, 1F).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.pos( matrix4f, (float) draw2.x + (float) bottomAngleVec.x, (float) draw2.y + (float) bottomAngleVec.y, (float) draw2.z + (float) bottomAngleVec.z).color(trailR, trailG, trailB, trailA).tex(u2, 1F).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.pos( matrix4f, (float) draw2.x + (float) topAngleVec.x, (float) draw2.y + (float) topAngleVec.y, (float) draw2.z + (float) topAngleVec.z         ).color(trailR, trailG, trailB, trailA).tex(u2, 0 ).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.pos( matrix4f, (float) draw1.x + (float) topAngleVec.x, (float) draw1.y + (float) topAngleVec.y, (float) draw1.z + (float) topAngleVec.z         ).color(trailR, trailG, trailB, trailA).tex(u1, 0 ).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            samples++;
            drawFrom = sample;
        }
    }
}
