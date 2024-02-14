package com.freefish.arknightsmobs.client.particle;

import com.freefish.arknightsmobs.client.render.MMRenderType;
import com.freefish.arknightsmobs.client.render.model.tools.MathUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FireParticle extends Particle {
    private final int textureSize;
    private static int currentlyUsedTextures = 0;
    private final DynamicTexture dynamicTexture;
    private final RenderType renderType;
    private boolean requiresUpload = true;
    private float size;
    private int id = 0;

    private boolean spawnedExtras = false;

    public FireParticle(ClientWorld world, double x, double y, double z, int size, int target, int totalTendrils) {
        super(world, x, y, z);
        this.particleGravity = 0.0F;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.maxAge = 300;
        this.size = size + 1;
        this.setSize(this.size, this.size);
        textureSize = 32 + (int) size * 32;
        dynamicTexture = new DynamicTexture(textureSize, textureSize, true);
        id = currentlyUsedTextures;
        ResourceLocation resourcelocation = Minecraft.getInstance().textureManager.getDynamicTextureLocation("arknightsmobsvoid_particle/fire_" + id, dynamicTexture);
        currentlyUsedTextures++;
        this.renderType = MMRenderType.getVoidBeingCloud(resourcelocation);
    }

    public void tick() {
        if (this.age <= 0 && !spawnedExtras) {
            onSpawn();
            spawnedExtras = true;
        }
        super.tick();
        this.motionX *= 0.97D;
        this.motionY *= 0.97D;
        this.motionZ *= 0.97D;
        updateTexture();
    }

    private void onSpawn() {
    }

    private void updateTexture() {
        int center = textureSize / 2;
        int black = 0;
        double alphaFadeOut = age > maxAge - 10 ? (maxAge - age) / 10F : 1F;
        double radiusSq = center * center * getAlphaFromAge(age, maxAge);
        for (int i = 0; i < textureSize; ++i) {
            for (int j = 0; j < textureSize; ++j) {
                double d0 = center - i;
                double d1 = center - j;
                double f1 = (MathUtils.sampleNoise3D(i, age, j + age, 15) + 1F) / 2F;
                double d2 = (d0 * d0 + d1 * d1);
                double alpha = ((1F - d2 / (radiusSq - f1 * f1 * radiusSq))+(textureSize-i)/(double)textureSize) * alphaFadeOut;
                if (alpha < 0) {
                    this.dynamicTexture.getTextureData().setPixelRGBA(j, i, 0);
                } else {
                    this.dynamicTexture.getTextureData().setPixelRGBA(j, i, MathUtils.ABGR32.color((int) Math.min(alpha * 255, 255), black, black, 255));
                }
            }
        }
        this.dynamicTexture.updateDynamicTexture();
    }

    public static float getAlphaFromAge(int age, int maxAge) {
        float alphaFadeIn = Math.min(20, age) / 20F;
        float alphaFadeOut = age > maxAge - 10 ? (maxAge - age) / 10F : 1F;
        return alphaFadeIn * alphaFadeOut;
    }

    public void remove() {
        this.isExpired = true;
        currentlyUsedTextures--;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo camera, float partialTicks) {
        if (this.requiresUpload) {
            this.updateTexture();
            this.requiresUpload = false;
        }
        Vector3d vec3 = camera.getProjectedView();
        float f = (float) (MathHelper.lerp((double) partialTicks, this.prevPosX, this.posX) - vec3.getX());
        float f1 = (float) (MathHelper.lerp((double) partialTicks, this.prevPosY, this.posY) - vec3.getY());
        float f2 = (float) (MathHelper.lerp((double) partialTicks, this.prevPosZ, this.posZ) - vec3.getZ());
        Quaternion quaternion;
        if (this.particleAngle == 0.0F) {
            quaternion = camera.getRotation();
        } else {
            quaternion = new Quaternion(camera.getRotation());
            float f3 = MathHelper.lerp(partialTicks, this.prevParticleAngle, this.particleAngle);
            quaternion.multiply(Vector3f.ZP.rotation(f3));
        }
        IRenderTypeBuffer.Impl multibuffersource$buffersource = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder vertexConsumer1 = multibuffersource$buffersource.getBuffer(renderType);
        MatrixStack posestack = new MatrixStack();
        MatrixStack.Entry posestack$pose = posestack.getLast();
        Matrix3f matrix3f = posestack$pose.getNormal();
        float zFightFix = 0;
        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.transform(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, zFightFix), new Vector3f(-1.0F, 1.0F, zFightFix), new Vector3f(1.0F, 1.0F, zFightFix), new Vector3f(1.0F, -1.0F, zFightFix)};
        float f4 = size;

        for (int i = 0; i < 4; ++i) {
            avector3f[i].add(0, 0.2F * (float) Math.sin((age + partialTicks) * 0.1F), 0);
            Vector3f vector3f = avector3f[i];
            vector3f.transform(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0;
        float f8 = 1;
        float f5 = 0;
        float f6 = 1;
        int j = 240;
        vertexConsumer1.pos((double) avector3f[0].getX(), (double) avector3f[0].getY(), (double) avector3f[0].getZ()).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).tex(f8, f6).overlay(OverlayTexture.NO_OVERLAY).lightmap(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer1.pos((double) avector3f[1].getX(), (double) avector3f[1].getY(), (double) avector3f[1].getZ()).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).tex(f8, f5).overlay(OverlayTexture.NO_OVERLAY).lightmap(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer1.pos((double) avector3f[2].getX(), (double) avector3f[2].getY(), (double) avector3f[2].getZ()).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).tex(f7, f5).overlay(OverlayTexture.NO_OVERLAY).lightmap(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer1.pos((double) avector3f[3].getX(), (double) avector3f[3].getY(), (double) avector3f[3].getZ()).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).tex(f7, f6).overlay(OverlayTexture.NO_OVERLAY).lightmap(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        multibuffersource$buffersource.finish();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FireParticle(worldIn, x, y, z, (int) xSpeed, (int) ySpeed, (int) zSpeed);
        }
    }
}
