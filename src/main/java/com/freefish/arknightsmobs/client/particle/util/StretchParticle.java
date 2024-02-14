package com.freefish.arknightsmobs.client.particle.util;

import com.freefish.arknightsmobs.client.render.MMRenderType;
import com.freefish.arknightsmobs.client.render.model.tools.MathUtils;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public class StretchParticle extends SpriteTexturedParticle {
    public ParticleRotation rotation;
    public float red, green, blue, alpha;
    public float prevRed, prevGreen, prevBlue, prevAlpha;
    public float scale, prevScale, particleScale;
    public float stretch,prevStretch,stretchY,prevStretchY;

    public StretchParticle(ClientWorld world, double x, double y, double z ,double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double duration) {
        super(world, x, y, z,0.0D, 0.0D, 0.0D);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.red = (float) (r);
        this.green = (float) (g);
        this.blue = (float) (b);
        this.alpha = (float) (a);
        this.scale = (float)scale;
        this.maxAge = (int)duration;
        this.rotation = rotation;
        this.stretch = 0;
        this.stretchY = 1;

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRed = this.red;
        this.prevGreen = this.green;
        this.prevBlue = this.blue;
        this.prevAlpha = this.alpha;
        this.rotation.setPrevValues();
        this.prevScale = this.scale;
        this.prevStretch = this.stretch;
        this.prevStretchY = this.stretchY;
        this.canCollide = false;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }
    public int getBrightnessForRender(float partialTick)
    {
        return super.getBrightnessForRender(partialTick);
    }

    @Override
    public void tick() {
        prevRed = red;
        prevGreen = green;
        prevBlue = blue;
        prevAlpha = alpha;
        prevScale = scale;
        rotation.setPrevValues();
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        updateStretch();

        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
        }
    }
    private void updateStretch(){
        prevStretch = stretch;
        prevStretchY = stretchY;
        if(age < maxAge/2){
            stretch += 1.0f/(maxAge/2.0f);
        }
        if(age <= maxAge && age > maxAge/2 - 5){
            stretchY -= 1.0f/(maxAge/2.0f+5);
        }
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        particleAlpha = prevAlpha + (alpha - prevAlpha) * partialTicks;
        if (particleAlpha < 0.01) particleAlpha = 0.01f;
        particleRed = prevRed + (red - prevRed) * partialTicks;
        particleGreen = prevGreen + (green - prevGreen) * partialTicks;
        particleBlue = prevBlue + (blue - prevBlue) * partialTicks;
        particleScale = prevScale + (scale - prevScale) * partialTicks;
        float renderStretch = MathUtils.fade(prevStretch +(stretch - prevStretch)*partialTicks);
        float renderStretchY = MathUtils.fade(prevStretchY +(stretchY - prevStretchY)*partialTicks);

        Vector3d Vector3d = renderInfo.getProjectedView();
        float f = (float)(MathHelper.lerp(partialTicks, this.prevPosX, this.posX) - Vector3d.getX());
        float f1 = (float)(MathHelper.lerp(partialTicks, this.prevPosY, this.posY) - Vector3d.getY());
        float f2 = (float)(MathHelper.lerp(partialTicks, this.prevPosZ, this.posZ) - Vector3d.getZ());

        Quaternion quaternion = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        if (rotation instanceof ParticleRotation.FaceCamera) {
            ParticleRotation.FaceCamera faceCameraRot = (ParticleRotation.FaceCamera) rotation;
            if (faceCameraRot.faceCameraAngle == 0.0F && faceCameraRot.prevFaceCameraAngle == 0.0F) {
                quaternion = renderInfo.getRotation();
            } else {
                quaternion = new Quaternion(renderInfo.getRotation());
                float f3 = MathHelper.lerp(partialTicks, faceCameraRot.prevFaceCameraAngle, faceCameraRot.faceCameraAngle);
                quaternion.multiply(Vector3f.ZP.rotation(f3));
            }
        }
        else if (rotation instanceof ParticleRotation.EulerAngles) {
            ParticleRotation.EulerAngles eulerRot = (ParticleRotation.EulerAngles) rotation;
            float rotX = eulerRot.prevPitch + (eulerRot.pitch - eulerRot.prevPitch) * partialTicks;
            float rotY = eulerRot.prevYaw + (eulerRot.yaw - eulerRot.prevYaw) * partialTicks;
            float rotZ = eulerRot.prevRoll + (eulerRot.roll - eulerRot.prevRoll) * partialTicks;
            Quaternion quatX = new Quaternion(rotX, 0, 0, false);
            Quaternion quatY = new Quaternion(0, rotY, 0, false);
            Quaternion quatZ = new Quaternion(0, 0, rotZ, false);
            quaternion.multiply(quatY);
            quaternion.multiply(quatX);
            quaternion.multiply(quatZ);
        }
        if (rotation instanceof ParticleRotation.OrientVector) {
            ParticleRotation.OrientVector orientRot = (ParticleRotation.OrientVector) rotation;
            double x = orientRot.prevOrientation.x + (orientRot.orientation.x - orientRot.prevOrientation.x) * partialTicks;
            double y = orientRot.prevOrientation.y + (orientRot.orientation.y - orientRot.prevOrientation.y) * partialTicks;
            double z = orientRot.prevOrientation.z + (orientRot.orientation.z - orientRot.prevOrientation.z) * partialTicks;
            float pitch = (float) Math.asin(-y);
            float yaw = (float) (MathHelper.atan2(x, z));
            Quaternion quatX = new Quaternion(pitch, 0, 0, false);
            Quaternion quatY = new Quaternion(0, yaw, 0, false);
            quaternion.multiply(quatY);
            quaternion.multiply(quatX);
        }

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -renderStretchY, 0.0F), new Vector3f(-1.0F, renderStretchY, 0.0F), new Vector3f(1.0F, renderStretchY, 0.0F), new Vector3f(1.0F, -renderStretchY, 0.0F)};
        float f4 = particleScale * 0.1f;

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.mul(renderStretch);
            vector3f.transform(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float f7 = this.getMinU();
        float f8 = this.getMaxU();
        float f5 = this.getMinV();
        float f6 = this.getMaxV();
        int j = this.getBrightnessForRender(partialTicks);
        buffer.pos(avector3f[0].getX(), avector3f[0].getY(), avector3f[0].getZ()).tex(f8, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos(avector3f[1].getX(), avector3f[1].getY(), avector3f[1].getZ()).tex(f8, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos(avector3f[2].getX(), avector3f[2].getY(), avector3f[2].getZ()).tex(f7, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos(avector3f[3].getX(), avector3f[3].getY(), avector3f[3].getZ()).tex(f7, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<StretchParticleData> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(StretchParticleData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            StretchParticle particle = new StretchParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getRotation(), typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getDuration());
            particle.setColor((float) typeIn.getRed(), (float) typeIn.getGreen(), (float) typeIn.getBlue());
            particle.selectSpriteRandomly(spriteSet);
            return particle;
        }
    }
    
    public static class StretchParticleData implements IParticleData{
        public static final IDeserializer<StretchParticleData> DESERIALIZER = new IDeserializer<StretchParticleData>() {
            public StretchParticleData deserialize(ParticleType<StretchParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                double red = reader.readDouble();
                reader.expect(' ');
                double green = reader.readDouble();
                reader.expect(' ');
                double blue = reader.readDouble();
                reader.expect(' ');
                double alpha = reader.readDouble();
                reader.expect(' ');
                String rotationMode = reader.readString();
                reader.expect(' ');
                double scale = reader.readDouble();
                reader.expect(' ');
                double yaw = reader.readDouble();
                reader.expect(' ');
                double pitch = reader.readDouble();
                reader.expect(' ');
                double roll = reader.readDouble();
                reader.expect(' ');
                double duration = reader.readDouble();
                reader.expect(' ');
                double faceCameraAngle = reader.readDouble();
                ParticleRotation rotation;
                if (rotationMode.equals("face_camera")) rotation = new ParticleRotation.FaceCamera((float) faceCameraAngle);
                else if (rotationMode.equals("euler")) rotation = new ParticleRotation.EulerAngles((float)yaw, (float)pitch, (float)roll);
                else rotation = new ParticleRotation.OrientVector(new Vector3d(yaw, pitch, roll));
                return new StretchParticleData(particleTypeIn, rotation, scale, red, green, blue, alpha, duration);
            }

            public StretchParticleData read(ParticleType<StretchParticleData> particleTypeIn, PacketBuffer buffer) {
                double red = buffer.readFloat();
                double green = buffer.readFloat();
                double blue = buffer.readFloat();
                double alpha = buffer.readFloat();
                String rotationMode = buffer.readString();
                double scale = buffer.readFloat();
                double yaw = buffer.readFloat();
                double pitch = buffer.readFloat();
                double roll = buffer.readFloat();
                double duration = buffer.readFloat();
                double faceCameraAngle = buffer.readFloat();
                ParticleRotation rotation;
                if (rotationMode.equals("face_camera")) rotation = new ParticleRotation.FaceCamera((float) faceCameraAngle);
                else if (rotationMode.equals("euler")) rotation = new ParticleRotation.EulerAngles((float)yaw, (float)pitch, (float)roll);
                else rotation = new ParticleRotation.OrientVector(new Vector3d(yaw, pitch, roll));
                return new StretchParticleData(particleTypeIn, rotation, scale, red, green, blue, alpha, duration);
            }
        };

        private final ParticleType<? extends StretchParticleData> type;

        private final float red, green, blue, alpha;
        private final ParticleRotation rotation;
        private final float scale;
        private final float duration;

        public StretchParticleData(ParticleType<? extends StretchParticleData> type, ParticleRotation rotation, double scale, double r, double g, double b, double a, double duration) {
            this.type = type;

            this.rotation = rotation;

            this.scale = (float) scale;

            this.red = (float) r;
            this.green = (float) g;
            this.blue = (float) b;
            this.alpha = (float) a;

            this.duration = (float) duration;
        }

        @Override
        public void write(PacketBuffer buffer) {
            String rotationMode;
            float faceCameraAngle = 0;
            float yaw = 0;
            float pitch = 0;
            float roll = 0;
            if (rotation instanceof ParticleRotation.FaceCamera) {
                rotationMode = "face_camera";
                faceCameraAngle = ((ParticleRotation.FaceCamera) rotation).faceCameraAngle;
            }
            else if (rotation instanceof ParticleRotation.EulerAngles) {
                rotationMode = "euler";
                yaw = ((ParticleRotation.EulerAngles) rotation).yaw;
                pitch = ((ParticleRotation.EulerAngles) rotation).pitch;
                roll = ((ParticleRotation.EulerAngles) rotation).roll;
            }
            else {
                rotationMode = "orient";
                Vector3d vec = ((ParticleRotation.OrientVector)rotation).orientation;
                yaw = (float) vec.x;
                pitch = (float) vec.y;
                roll = (float) vec.z;
            }

            buffer.writeFloat(this.red);
            buffer.writeFloat(this.green);
            buffer.writeFloat(this.blue);
            buffer.writeFloat(this.alpha);
            buffer.writeString(rotationMode);
            buffer.writeFloat(this.scale);
            buffer.writeFloat(yaw);
            buffer.writeFloat(pitch);
            buffer.writeFloat(roll);
            buffer.writeFloat(this.duration);
            buffer.writeFloat(faceCameraAngle);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String getParameters() {
            String rotationMode;
            float faceCameraAngle = 0;
            float yaw = 0;
            float pitch = 0;
            float roll = 0;
            if (rotation instanceof ParticleRotation.FaceCamera) {
                rotationMode = "face_camera";
                faceCameraAngle = ((ParticleRotation.FaceCamera) rotation).faceCameraAngle;
            }
            else if (rotation instanceof ParticleRotation.EulerAngles) {
                rotationMode = "euler";
                yaw = ((ParticleRotation.EulerAngles) rotation).yaw;
                pitch = ((ParticleRotation.EulerAngles) rotation).pitch;
                roll = ((ParticleRotation.EulerAngles) rotation).roll;
            }
            else {
                rotationMode = "orient";
                Vector3d vec = ((ParticleRotation.OrientVector)rotation).orientation;
                yaw = (float) vec.x;
                pitch = (float) vec.y;
                roll = (float) vec.z;
            }

            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %s %.2f %.2f %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getKey(this.getType()),
                    this.red, this.green, this.blue, this.alpha, rotationMode, this.scale, yaw, pitch, roll, this.duration, faceCameraAngle);
        }

        @Override
        public ParticleType<? extends StretchParticleData> getType() {
            return type;
        }

        @OnlyIn(Dist.CLIENT)
        public double getRed() {
            return this.red;
        }

        @OnlyIn(Dist.CLIENT)
        public double getGreen() {
            return this.green;
        }

        @OnlyIn(Dist.CLIENT)
        public double getBlue() {
            return this.blue;
        }

        @OnlyIn(Dist.CLIENT)
        public double getAlpha() {
            return this.alpha;
        }

        @OnlyIn(Dist.CLIENT)
        public ParticleRotation getRotation() {
            return rotation;
        }

        @OnlyIn(Dist.CLIENT)
        public double getScale() {
            return scale;
        }

        @OnlyIn(Dist.CLIENT)
        public double getDuration() {
            return duration;
        }

        public static Codec<StretchParticleData> CODEC(ParticleType<StretchParticleData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.DOUBLE.fieldOf("scale").forGetter(StretchParticleData::getScale),
                            Codec.DOUBLE.fieldOf("r").forGetter(StretchParticleData::getRed),
                            Codec.DOUBLE.fieldOf("g").forGetter(StretchParticleData::getGreen),
                            Codec.DOUBLE.fieldOf("b").forGetter(StretchParticleData::getBlue),
                            Codec.DOUBLE.fieldOf("a").forGetter(StretchParticleData::getAlpha),
                            Codec.DOUBLE.fieldOf("duration").forGetter(StretchParticleData::getDuration)
                    ).apply(codecBuilder, (scale, r, g, b, a, duration) ->
                            new StretchParticleData(particleType, new ParticleRotation.FaceCamera(0), scale, r, g, b, a, duration))
            );
        }
    }
}
