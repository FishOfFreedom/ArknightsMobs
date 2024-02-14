package com.freefish.arknightsmobs.client.render.entity;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.client.render.MMRenderType;
import com.freefish.arknightsmobs.client.render.layer.PatriotWindLayer;
import com.freefish.arknightsmobs.client.render.layer.PulsatingGlowLayer;
import com.freefish.arknightsmobs.client.render.model.tools.MathUtils;
import com.freefish.arknightsmobs.client.render.model.tools.Parabola;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.freefish.arknightsmobs.client.render.model.PatriotModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.IVertexConsumer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.ForgeRenderTypes;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class PatriotRenderer extends GeoEntityRenderer<Patriot> {
    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation(ArknightsMobs.MOD_ID, "textures/particle/trail.png");
    private static final ResourceLocation ARMOR = new ResourceLocation(ArknightsMobs.MOD_ID, "textures/particle/hexagon.png");


    public PatriotRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PatriotModel());
        this.addLayer(new PulsatingGlowLayer<>(this, new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/patriot/eyes.png"), 0.1F, 1.0F, 0.25F));
        this.addLayer(new PatriotWindLayer(this));
        this.shadowSize = 0.6f;
    }

    @Override
    public ResourceLocation getEntityTexture(Patriot entity) {
        return new ResourceLocation(ArknightsMobs.MOD_ID, "textures/entity/aiguozhe.png");
    }
//todo
    @Override
    public void render(Patriot entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        AnimatedGeoModel<Patriot> geoModelProvider = (AnimatedGeoModel<Patriot>)getGeoModelProvider();
        GeoBone halberd = (GeoBone) geoModelProvider.getAnimationProcessor().getBone("halberd_lock");
        GeoBone shield = (GeoBone) geoModelProvider.getAnimationProcessor().getBone("shield_lock");
        if(halberd != null) {
            if(entity.time != entity.ticksExisted){
                entity.time = entity.ticksExisted;
                Vector3d worldPosFromModel = MathUtils.getWorldPosFromModel(entity, entity.renderYawOffset, halberd);
                tickTrail(entity,worldPosFromModel);
                entity.setClientVectors(0,worldPosFromModel);
                entity.setClientVectors(1,MathUtils.getWorldPosFromModel(entity, entity.renderYawOffset, shield));
            }
            double x = MathHelper.lerp(partialTicks, entity.prevPosX, entity.getPosX());
            double y = MathHelper.lerp(partialTicks, entity.prevPosY, entity.getPosY());
            double z = MathHelper.lerp(partialTicks, entity.prevPosZ, entity.getPosZ());
            stack.push();
            stack.translate(-x,-y,-z);
            if(entity.trailPointer!=-1&&entity.getPredicate()==2)
                renderTrail(entity,partialTicks, stack, bufferIn, 1f, 0.1f, 0.1f, 0.5f, packedLightIn);
                //renderArmor(entity,stack,bufferIn,packedLightIn);
            stack.pop();
        }
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    private void renderArmor(Patriot entity, MatrixStack poseStack, IRenderTypeBuffer bufferIn, int packedLightIn){
        Vector3d vector3d = new Vector3d(entity.clientVectors[1].x,entity.clientVectors[1].y,entity.clientVectors[1].z);
        IVertexConsumer vertexConsumer= (IVertexConsumer) bufferIn.getBuffer(ForgeRenderTypes.getUnlitTranslucent(ARMOR));
        MatrixStack.Entry posestack$pose = poseStack.getLast();
        Matrix3f matrix3f = posestack$pose.getNormal();
        float f4 = 0.5f;

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, -0.05F), new Vector3f(-1.0F, 1.0F, -0.05F), new Vector3f(1.0F, 1.0F, -0.05F), new Vector3f(1.0F, -1.0F, -0.05F)};
        Vector3d vector3d1 = new Vector3d(1,1,0);
        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.mul(f4);
            avector3f[i].add((float)vector3d.x,(float)vector3d.y,(float)vector3d.z);
        }

        vertexConsumer.pos( avector3f[0].getX(), avector3f[0].getY(), avector3f[0].getZ()).color(1, 0.1f, 0.1f, 0.5f).tex(1, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.pos( avector3f[1].getX(), avector3f[1].getY(), avector3f[1].getZ()).color(1, 0.1f, 0.1f, 0.5f).tex(1, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.pos( avector3f[2].getX(), avector3f[2].getY(), avector3f[2].getZ()).color(1, 0.1f, 0.1f, 0.5f).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.pos( avector3f[3].getX(), avector3f[3].getY(), avector3f[3].getZ()).color(1, 0.1f, 0.1f, 0.5f).tex(0, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private void renderTrail(Patriot entity, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer bufferIn, float trailR, float trailG, float trailB, float trailA, int packedLightIn) {
        int samples = 0;
        int sampleSize = 10;
        float trailHeight = 1.5F;
        float trailZRot = 0;
        Vector3d topAngleVec = new Vector3d(0, trailHeight, 0).rotateRoll(trailZRot);
        Vector3d bottomAngleVec = new Vector3d(0, -trailHeight, 0).rotateRoll(trailZRot);
        Vector3d drawFrom = getTrailPosition(entity,0, partialTicks);
        IVertexConsumer vertexconsumer =(IVertexConsumer)bufferIn.getBuffer(ForgeRenderTypes.getUnlitTranslucent(TRAIL_TEXTURE));
        //IVertexConsumer vertexconsumer =(IVertexConsumer) bufferIn.getBuffer(MMRenderType.getParticleTrail(TRAIL_TEXTURE));
        while (samples < sampleSize) {
            Vector3d sample = getTrailPosition(entity,samples + 2, partialTicks);
            float u1 = samples / (float) sampleSize;
            float u2 = u1 + 1 / (float) sampleSize;

            Vector3d draw1 = drawFrom;
            Vector3d draw2 = sample;

            MatrixStack.Entry posestack$pose = poseStack.getLast();
            Matrix4f matrix4f = posestack$pose.getMatrix();
            Matrix3f matrix3f = posestack$pose.getNormal();
            vertexconsumer.pos(matrix4f, (float) draw1.x + (float) bottomAngleVec.x, (float) draw1.y + (float) bottomAngleVec.y, (float) draw1.z + (float) bottomAngleVec.z).color(trailR, trailG, trailB, trailA).tex(u1, 1F).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.pos(matrix4f, (float) draw2.x + (float) bottomAngleVec.x, (float) draw2.y + (float) bottomAngleVec.y, (float) draw2.z + (float) bottomAngleVec.z).color(trailR, trailG, trailB, trailA).tex(u2, 1F).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.pos(matrix4f, (float) draw2.x + (float) topAngleVec.x, (float) draw2.y + (float) topAngleVec.y, (float) draw2.z + (float) topAngleVec.z).color(trailR, trailG, trailB, trailA         ).tex(u2, 0 ).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.pos(matrix4f, (float) draw1.x + (float) topAngleVec.x, (float) draw1.y + (float) topAngleVec.y, (float) draw1.z + (float) topAngleVec.z).color(trailR, trailG, trailB, trailA         ).tex(u1, 0 ).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            samples++;
            drawFrom = sample;
        }
    }

    public void tickTrail(Patriot entity ,Vector3d currentPosition) {
        if (entity.trailPointer == -1) {
            for (int i = 0; i < entity.trailPositions.length; i++) {
                entity.trailPositions[i] = currentPosition;
            }
        }
        if (++entity.trailPointer == entity.trailPositions.length) {
            entity.trailPointer = 0;
        }
        entity.trailPositions[entity.trailPointer] = currentPosition;
    }

    public Vector3d getTrailPosition(Patriot entity ,int pointer, float partialTick) {
        int i = entity.trailPointer - pointer & 63;
        int j = entity.trailPointer - pointer - 1 & 63;
        Vector3d d0 = entity.trailPositions[j];
        Vector3d d1 = entity.trailPositions[i].subtract(d0);
        return d0.add(d1.scale(partialTick));
    }
}
