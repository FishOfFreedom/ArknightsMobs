package com.freefish.arknightsmobs.client.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;

public class ParticleScratches extends SpriteTexturedParticle {
    protected ParticleScratches(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return null;
    }
}
