package com.freefish.arknightsmobs.client.particle;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.client.render.MMRenderType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeRenderTypes;

import javax.annotation.Nullable;

public class VoidBeingEyeParticle extends Particle {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(ArknightsMobs.MOD_ID, "textures/particle/void_eye_0.png"),
            new ResourceLocation(ArknightsMobs.MOD_ID, "textures/particle/void_eye_1.png"),
            new ResourceLocation(ArknightsMobs.MOD_ID, "textures/particle/void_eye_2.png")
    };

    private int textureIndex = 0;
    private float prevCameraOffsetX = 0;
    private float prevCameraOffsetY = 0;
    private float cameraOffsetX = 0;
    private float cameraOffsetY = 0;
    private float animationOffset = 0;

    public VoidBeingEyeParticle(ClientWorld world, double x, double y, double z, float cameraOffsetX, float cameraOffsetY) {
        super(world, x, y, z);
        textureIndex = rand.nextInt(2);
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.animationOffset = (float) (rand.nextFloat() * Math.PI);
        this.maxAge = 300;
        this.particleGravity = 0.0F;
        this.prevCameraOffsetX = cameraOffsetX;
        this.prevCameraOffsetY = cameraOffsetY;
        this.cameraOffsetX = cameraOffsetX;
        this.cameraOffsetY = cameraOffsetY;
    }

    public void tick() {
        super.tick();
        this.prevCameraOffsetX = cameraOffsetX;
        this.prevCameraOffsetY = cameraOffsetY;
        if (age > 200) {
            float offsetX = (0 - cameraOffsetX) / (float) (maxAge - 200);
            float offsetY = (-5 - cameraOffsetY) / (float) (maxAge - 200);
            this.cameraOffsetX += offsetX;
            this.cameraOffsetY += offsetY;
        }
        if (age > maxAge - 20) {
            this.cameraOffsetY += -0.25F;
        }
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo camera, float partialTick) {
        this.particleAlpha = VoidBeingCloudParticle.getAlphaFromAge(maxAge, age);
        Vector3d vec3 = camera.getProjectedView();
        float f = (float) (MathHelper.lerp((double) partialTick, this.prevPosX, this.posX) - vec3.x);
        float f1 = (float) (MathHelper.lerp((double) partialTick, this.prevPosY, this.posY) - vec3.y);
        float f2 = (float) (MathHelper.lerp((double) partialTick, this.prevPosZ, this.posZ) - vec3.z);
        Quaternion quaternion;
        if (this.particleAngle == 0.0F) {
            quaternion = camera.getRotation();
        } else {
            quaternion = new Quaternion(camera.getRotation());
            float f3 = MathHelper.lerp(partialTick, this.prevParticleAngle, this.particleAngle);
            quaternion.multiply(Vector3f.ZP.rotation(f3));
        }
        IRenderTypeBuffer.Impl bufferSource = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

        IVertexBuilder portalStatic = bufferSource.getBuffer(ForgeRenderTypes.getUnlitTranslucent(TEXTURES[textureIndex]));
        //IVertexBuilder portalStatic = bufferSource.getBuffer(renderType);
        MatrixStack posestack = new MatrixStack();
        MatrixStack.Entry posestack$pose = posestack.getLast();
        Matrix3f matrix3f = posestack$pose.getNormal();

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.transform(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, -0.05F), new Vector3f(-1.0F, 1.0F, -0.05F), new Vector3f(1.0F, 1.0F, -0.05F), new Vector3f(1.0F, -1.0F, -0.05F)};
        float f4 = 0.5F;
        float offsetX = prevCameraOffsetX + (cameraOffsetX - prevCameraOffsetX) * partialTick;
        float offsetY = prevCameraOffsetY + (cameraOffsetY - prevCameraOffsetY) * partialTick;
        float shakeX = 0.0F;
        float shakeY = 0.0F;
        if (age > 200) {
            shakeX = 0.3F * (float) Math.sin((age + partialTick + 3 * animationOffset) * 0.54F);
            shakeY = 0.3F * (float) -Math.sin((age + partialTick + 3 * animationOffset) * 0.54F + 2);
        }
        for (int i = 0; i < 4; ++i) {
            avector3f[i].add(offsetX + shakeX, offsetY + shakeY + 0.2F * (float) Math.sin((age + partialTick + animationOffset) * 0.1F), 0);
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
        
        portalStatic.pos((double) avector3f[0].getX(), (double) avector3f[0].getY(), (double) avector3f[0].getZ()).color(this.particleRed,this.particleGreen,this.particleBlue,this.particleAlpha).tex(f8, f6).overlay(OverlayTexture.NO_OVERLAY).lightmap(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        portalStatic.pos((double) avector3f[1].getX(), (double) avector3f[1].getY(), (double) avector3f[1].getZ()).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).tex(f8, f5).overlay(OverlayTexture.NO_OVERLAY).lightmap(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        portalStatic.pos((double) avector3f[2].getX(), (double) avector3f[2].getY(), (double) avector3f[2].getZ()).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).tex(f7, f5).overlay(OverlayTexture.NO_OVERLAY).lightmap(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        portalStatic.pos((double) avector3f[3].getX(), (double) avector3f[3].getY(), (double) avector3f[3].getZ()).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).tex(f7, f6).overlay(OverlayTexture.NO_OVERLAY).lightmap(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();

        bufferSource.finish();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        @Nullable
        @Override
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new VoidBeingEyeParticle(worldIn, x, y, z, (float) xSpeed, (float) ySpeed);
        }
    }
}
