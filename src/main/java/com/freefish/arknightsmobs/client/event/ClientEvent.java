package com.freefish.arknightsmobs.client.event;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.client.particle.*;
import com.freefish.arknightsmobs.client.particle.util.AdvancedParticleBase;
import com.freefish.arknightsmobs.client.particle.util.StretchParticle;
import com.freefish.arknightsmobs.client.render.entity.*;
import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import com.freefish.arknightsmobs.server.entity.special.EntityCameraShake;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ArknightsMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.GAN_RAN_ZHE_ZHI_JI.get(), GanRanZheZhiJiRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.PATRIOT.get(), PatriotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.GUERRILLAS_CROSS_BOW.get(), CrossBowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.GUERRILLAS_MESSENGER.get(), MessengerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.GUERRILLAS_SARKAZ_SOLDIER.get(), SarkazSoldierRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.GUERRILLAS_SHIELD_GUARD.get(), ShieldGuardRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.GUERRILLAS_SOLDIER.get(), SoldierRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.SURTR.get(), SurtrRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.FLAME.get(), FlameEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.FALLING_BLOCK.get(), RenderFallingBlock::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.CAMERA_SHAKE.get(), RenderNothing::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.SOTF_ENTITY.get(), RenderNothing::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.FALLING_STONE.get(), FallingStoneRenderer::new);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.SPARKLE.get(), ParticleSparkle.SparkleFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.VANILLA_CLOUD_EXTENDED.get(), ParticleVanillaCloudExtended.CloudFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.SNOWFLAKE.get(), ParticleSnowFlake.SnowFlakeFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.CLOUD.get(), ParticleCloud.CloudFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.ORB.get(), ParticleOrb.OrbFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RING.get(), ParticleRing.RingFactory::new);

        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RING2.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RING_BIG.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.PIXEL.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.ORB2.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.EYE.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.BUBBLE.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.SUN.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.SUN_NOVA.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.FLARE.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.FLARE_RADIAL.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.BURST_IN.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.BURST_MESSY.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RING_SPARKS.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.BURST_OUT.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.GLOW.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.ARROW_HEAD.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.SIGNAL_LOOP.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.CRACK_1.get(), StretchParticle.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.CRACK_3.get(), StretchParticle.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.HEXAGON.get(), AdvancedParticleBase.Factory::new);

        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RIBBON_FLAT.get(), ParticleRibbon.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RIBBON_STREAKS.get(), ParticleRibbon.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RIBBON_GLOW.get(), ParticleRibbon.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RIBBON_SQUIGGLE.get(), ParticleRibbon.Factory::new);

        Minecraft.getInstance().particles.registerFactory(ParticleHandler.VOID_BEING_CLOUD.get(), new VoidBeingCloudParticle.Factory());
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.FIRE.get(), new FireParticle.Factory());
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.VOID_BEING_EYE.get(), new VoidBeingEyeParticle.Factory());
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.WIND.get(), new WindParticle.Factory());
    }
}
