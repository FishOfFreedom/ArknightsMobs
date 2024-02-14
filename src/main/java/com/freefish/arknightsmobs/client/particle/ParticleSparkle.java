package com.freefish.arknightsmobs.client.particle;

import com.freefish.arknightsmobs.client.render.MMRenderType;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by BobMowzie on 6/2/2017.
 */
public class ParticleSparkle extends SpriteTexturedParticle {
    private final float red;
    private final float green;
    private final float blue;
    private final float scale;

    public ParticleSparkle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b, double scale, int duration) {
        super(world, x, y, z);
        this.scale = (float) scale * 1f;
        maxAge = duration;
        motionX = vx;
        motionY = vy;
        motionZ = vz;
        red = (float) r;
        green = (float) g;
        blue = (float) b;
        canCollide = false;
    }

    @Override
    protected float getMaxU() {
        return super.getMaxU() - (super.getMaxU() - super.getMinU())/16f;
    }

    @Override
    protected float getMaxV() {
        return super.getMaxV() - (super.getMaxV() - super.getMinV())/16f;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        float a = ((float)age + partialTicks)/maxAge;
        particleAlpha = -4 * a * a + 4 * a;
        if (particleAlpha < 0.01) particleAlpha = 0.01f;
        particleScale = (-4 * a * a + 4 * a) * scale;

        super.renderParticle(buffer, renderInfo, partialTicks);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class SparkleFactory implements IParticleFactory<BasicParticleType> {

        private final IAnimatedSprite spriteSet;

        public SparkleFactory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleSparkle particle = new ParticleSparkle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 1, 1, 1, 0.4d, 13);
            particle.selectSpriteRandomly(spriteSet);
            return particle;
        }
    }
}
