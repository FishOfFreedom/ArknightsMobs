package com.freefish.arknightsmobs.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class MMRenderType extends RenderType {

    public MMRenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType getLantern(ResourceLocation locationIn) {
        State rendertype$state = State.getBuilder().texture(new TextureState(locationIn, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).overlay(OVERLAY_ENABLED).build(true);
        return makeType("lantern", DefaultVertexFormats.ENTITY, 7, 256, true, true, rendertype$state);
    }

    public static RenderType getGlowingEffect(ResourceLocation locationIn) {
        State rendertype$state = State.getBuilder().texture(new TextureState(locationIn, false, false)).transparency(RenderState.TRANSLUCENT_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_DISABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).overlay(OVERLAY_DISABLED).build(true);
        return makeType("glow_effect", DefaultVertexFormats.ENTITY, 7, 256, true, true, rendertype$state);
    }

    public static RenderType getSolarFlare(ResourceLocation locationIn) {
        State rendertype$state = State.getBuilder().texture(new TextureState(locationIn, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).depthTest(DEPTH_ALWAYS).diffuseLighting(DIFFUSE_LIGHTING_DISABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).overlay(OVERLAY_DISABLED).build(true);
        return makeType("solar_flare", DefaultVertexFormats.ENTITY, 7, 256, true, true, rendertype$state);
    }

    public static RenderType getEntityCutoutCull(ResourceLocation locationIn) {
        State rendertype$state = State.getBuilder().texture(new TextureState(locationIn, false, false)).transparency(NO_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_ENABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(true);
        return makeType("entity_cutout_cull", DefaultVertexFormats.ENTITY, 7, 256, true, false, rendertype$state);
    }

    public static RenderType getVoidBeingCloud(ResourceLocation resourceLocation) {
        return create("void_being", DefaultVertexFormats.ENTITY, 7, 256, true, true, RenderType.State.getBuilder()
                .texture(new RenderState.TextureState(resourceLocation, false, false))
                .lightmap(LIGHTMAP_DISABLED).cull(RenderState.CULL_DISABLED)
                .transparency(TRANSLUCENT_TRANSPARENCY)
                .diffuseLighting(DIFFUSE_LIGHTING_ENABLED)
                .alpha(DEFAULT_ALPHA)
                .overlay(RenderState.OVERLAY_ENABLED).build(true));
    }

    public static RenderType getParticleTrail(ResourceLocation resourceLocation) {
        return create("particle_trail", DefaultVertexFormats.ENTITY, 7, 256, true, true, RenderType.State.getBuilder()
                .shadeModel(RenderState.SHADE_DISABLED)
                .texture(new RenderState.TextureState(resourceLocation, true, true))
                .lightmap(LIGHTMAP_ENABLED).cull(RenderState.CULL_DISABLED)
                .transparency(TRANSLUCENT_TRANSPARENCY)
                .overlay(RenderState.OVERLAY_ENABLED).depthTest(DEPTH_LEQUAL).build(true));
    }

    public static RenderType create(String p_173216_, VertexFormat p_173217_, int p_173218_, int p_173219_, boolean p_173220_, boolean p_173221_, RenderType.State p_173222_) {
        return RenderType.makeType(p_173216_, p_173217_, p_173218_, p_173219_, p_173220_, p_173221_, p_173222_);
    }

    public static IParticleRenderType PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH = new IParticleRenderType() {
        public void beginRender(BufferBuilder p_217600_1_, TextureManager p_217600_2_) {
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            p_217600_2_.bindTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.alphaFunc(516, 0.003921569F);
            p_217600_1_.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        }

        public void finishRender(Tessellator p_217599_1_) {
            p_217599_1_.draw();
        }

        public String toString() {
            return "PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH";
        }
    };
}
