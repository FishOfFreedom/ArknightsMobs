package com.freefish.arknightsmobs.client.particle;

import com.freefish.arknightsmobs.client.render.MMRenderType;
import com.freefish.arknightsmobs.client.render.model.tools.MathUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.*;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.system.CallbackI;

import java.util.Locale;

public class WindParticle extends Particle {

    private final int textureSize;
    private static int currentlyUsedTextures = 0;
    private final DynamicTexture dynamicTexture;
    private final RenderType renderType;
    private boolean requiresUpload = true;
    private float size;
    private int id = 0;
    private Vector3f start,in,end;

    private boolean spawnedExtras = false;

    public WindParticle(ClientWorld world, double x, double y, double z, int size, Vector3f start,Vector3f in, Vector3f end) {
        super(world, start.getX(), start.getY(), start.getZ());
        this.particleGravity = 0.0F;
        this.posX = start.getX();
        this.posY = start.getY();
        this.posZ = start.getZ();
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.particleRed= 0.8f;
        this.particleGreen=0.4f;
        this.particleBlue=0.05f;
        this.maxAge = 300;
        this.start = start;this.in = in;this.end = end;
        this.size = size + 1;
        this.setSize(this.size, this.size);
        textureSize = 32 + (int) size * 32;
        dynamicTexture = new DynamicTexture(textureSize, textureSize, true);
        id = currentlyUsedTextures;
        ResourceLocation resourcelocation = Minecraft.getInstance().textureManager.getDynamicTextureLocation("arknightsmobsvoid_particle/wind_" + id, dynamicTexture);
        currentlyUsedTextures++;
        this.renderType = MMRenderType.getVoidBeingCloud(resourcelocation);
    }

    public void tick() {
        if (this.age <= 0 && !spawnedExtras) {
            spawnedExtras = true;
        }
        super.tick();
        this.motionX *= 0.97D;
        this.motionY *= 0.97D;
        this.motionZ *= 0.97D;
        updateTexture();
    }

    private void updateTexture() {
        int center = textureSize / 2;
        double black = Math.floor(age/10d);
        double alphaFadeOut = age > maxAge - 10 ? (maxAge - age) / 10F : 1F;
        double radiusSq = center * center * getAlphaFromAge(age, maxAge);
        for (int i = 0; i < textureSize; ++i) {
            for (int j = 0; j < textureSize; ++j) {
                double d0 = center - i;
                double d1 = center - j;
                double f1 = (MathUtils.sampleNoise3D(i, (int)black, j+age, 15) + 1F) / 2F;
                double d2 = (d0 * d0 + d1 * d1);
                double alpha = (1F - d2 / (radiusSq - f1 * f1 * radiusSq)) * alphaFadeOut;
                if (alpha < 0) {
                    this.dynamicTexture.getTextureData().setPixelRGBA(j, i, 0);
                } else {
                    this.dynamicTexture.getTextureData().setPixelRGBA(j, i, MathUtils.ABGR32.color((int) Math.min(alpha * 255, 255), 255, 255, 255));
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
        IRenderTypeBuffer.Impl multibuffersource$buffersource = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder vertexConsumer1 = multibuffersource$buffersource.getBuffer(renderType);
        MatrixStack posestack = new MatrixStack();
        MatrixStack.Entry posestack$pose = posestack.getLast();
        Matrix3f matrix3f = posestack$pose.getNormal();
        float zFightFix = 0;
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, zFightFix), new Vector3f(-1.0F, 1F, zFightFix), new Vector3f(10.0F, 1F, zFightFix), new Vector3f(10F, -1.0F, zFightFix)};
        float f4 = size*2;

        for (int i = 0; i < 4; ++i) {
            avector3f[i].add(0, 0.2F * (float) Math.sin((age + partialTicks) * 0.1F), 0);
            Vector3f vector3f = avector3f[i];
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0;
        float f8 = 1;
        float f5 = 0;
        float f6 = 1;
        int j = 240;

        float trailHeight = 1.5F;
        float trailZRot = 0;
        Vector3d topAngleVec = new Vector3d(0, trailHeight, 0).rotateRoll(trailZRot);
        Vector3d bottomAngleVec = new Vector3d(0, -trailHeight, 0).rotateRoll(trailZRot);
        Vector3d drawFrom = getVec(0).add(-vec3.x,-vec3.y,-vec3.z);
        for(int i=0;i<10;i++){
            float u1 = i / 10f;
            float u2 = (i + 1)/10f;
            Vector3d sample = getVec(u2).add(-vec3.x,-vec3.y,-vec3.z);
            Vector3d draw1 = drawFrom;
            Vector3d draw2 = sample;
            vertexConsumer1.pos( (float) draw1.x + (float) bottomAngleVec.x, (float) draw1.y + (float) bottomAngleVec.y, (float) draw1.z + (float) bottomAngleVec.z).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).tex(u1, 1F).overlay(OverlayTexture.NO_OVERLAY).lightmap(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            vertexConsumer1.pos( (float) draw2.x + (float) bottomAngleVec.x, (float) draw2.y + (float) bottomAngleVec.y, (float) draw2.z + (float) bottomAngleVec.z).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).tex(u2, 1F).overlay(OverlayTexture.NO_OVERLAY).lightmap(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            vertexConsumer1.pos( (float) draw2.x + (float) topAngleVec.x, (float) draw2.y + (float) topAngleVec.y, (float) draw2.z + (float) topAngleVec.z).color(         this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).tex(u2, 0 ).overlay(OverlayTexture.NO_OVERLAY).lightmap(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            vertexConsumer1.pos( (float) draw1.x + (float) topAngleVec.x, (float) draw1.y + (float) topAngleVec.y, (float) draw1.z + (float) topAngleVec.z).color(         this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).tex(u1, 0 ).overlay(OverlayTexture.NO_OVERLAY).lightmap(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            drawFrom = sample;
        }

        multibuffersource$buffersource.finish();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

    public Vector3d getVec(double i){
        Vector3d vector1 = new Vector3d(start).scale(1+i*i-2*i);
        Vector3d vector2 = new Vector3d(in).scale(2*i-2*i*i);
        Vector3d vector3 = new Vector3d(end).scale(i*i);
        return vector1.add(vector2).add(vector3);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<WindParticleData> {
        public Particle makeParticle(WindParticleData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new WindParticle(worldIn, x, y, z, (int) xSpeed, typeIn.getStart(), typeIn.getIn(),typeIn.getEnd());
        }
    }

    public static class WindParticleData implements IParticleData {
        public static final IDeserializer<WindParticle.WindParticleData> DESERIALIZER = new IDeserializer<WindParticle.WindParticleData>() {
            public WindParticle.WindParticleData deserialize(ParticleType<WindParticle.WindParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float startX =reader.readFloat();
                reader.expect(' ');
                float startY =reader.readFloat();
                reader.expect(' ');
                float startZ =reader.readFloat();
                reader.expect(' ');
                float inX =reader.readFloat();
                reader.expect(' ');
                float inY =reader.readFloat();
                reader.expect(' ');
                float inZ =reader.readFloat();
                reader.expect(' ');
                float endX =reader.readFloat();
                reader.expect(' ');
                float endY =reader.readFloat();
                reader.expect(' ');
                float endZ =reader.readFloat();
                return new WindParticle.WindParticleData(new Vector3f(startX,startY,startZ), new Vector3f(inX,inY,inZ), new Vector3f(endX,endY,endZ));
            }

            public WindParticle.WindParticleData read(ParticleType<WindParticle.WindParticleData> particleTypeIn, PacketBuffer buffer) {
                return new WindParticle.WindParticleData(new Vector3f(buffer.readFloat(),buffer.readFloat(),buffer.readFloat()),
                        new Vector3f(buffer.readFloat(),buffer.readFloat(),buffer.readFloat())
                        , new Vector3f(buffer.readFloat(),buffer.readFloat(),buffer.readFloat()));
            }
        };

        private final Vector3f start,end,in;

        public WindParticleData(Vector3f start,Vector3f in, Vector3f end) {
            this.start = start;
            this.end = end;
            this.in = in;
        }
        public WindParticleData(Vector3d start,Vector3d in, Vector3d end) {
            this.start = new Vector3f(start);
            this.end = new Vector3f(end);
            this.in = new Vector3f(in);
        }

        @Override
        public void write(PacketBuffer buffer) {
            buffer.writeFloat(this.start.getX());
            buffer.writeFloat(this.start.getY());
            buffer.writeFloat(this.start.getZ());
            buffer.writeFloat(this.in.getX());
            buffer.writeFloat(this.in.getY());
            buffer.writeFloat(this.in.getZ());
            buffer.writeFloat(this.end.getX());
            buffer.writeFloat(this.end.getY());
            buffer.writeFloat(this.end.getZ());
        }

        @SuppressWarnings("deprecation")
        @Override
        public String getParameters() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getKey(this.getType()),
                    this.start.getX(), this.start.getY(), this.start.getZ(),this.in.getX(), this.in.getY(), this.in.getZ(), this.end.getX(), this.end.getY(), this.end.getZ());
        }

        @OnlyIn(Dist.CLIENT)
        public Vector3f getStart() {
            return this.start;
        }

        @OnlyIn(Dist.CLIENT)
        public Vector3f getIn() {
            return this.in;
        }

        @OnlyIn(Dist.CLIENT)
        public Vector3f getEnd() {
            return this.end;
        }

        @Override
        public ParticleType<WindParticle.WindParticleData> getType() {
            return ParticleHandler.WIND.get();
        }

        public static Codec<WindParticle.WindParticleData> CODEC() {
            return Codec.unit(new WindParticleData(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0)));
        }
    }
}
