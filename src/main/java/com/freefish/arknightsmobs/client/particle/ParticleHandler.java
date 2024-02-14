package com.freefish.arknightsmobs.client.particle;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.client.particle.util.AdvancedParticleBase;
import com.freefish.arknightsmobs.client.particle.util.AdvancedParticleData;
import com.freefish.arknightsmobs.client.particle.util.RibbonParticleData;
import com.freefish.arknightsmobs.client.particle.util.StretchParticle;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = ArknightsMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleHandler {

    public static final DeferredRegister<ParticleType<?>> REG = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ArknightsMobs.MOD_ID);

    public static final RegistryObject<BasicParticleType> SPARKLE = register("sparkle", false);
    public static final RegistryObject<ParticleType<ParticleVanillaCloudExtended.VanillaCloudData>> VANILLA_CLOUD_EXTENDED = REG.register("vanilla_cloud_extended", () -> new ParticleType<ParticleVanillaCloudExtended.VanillaCloudData>(false, ParticleVanillaCloudExtended.VanillaCloudData.DESERIALIZER) {
        @Override
        public Codec<ParticleVanillaCloudExtended.VanillaCloudData> func_230522_e_() {
            return ParticleVanillaCloudExtended.VanillaCloudData.CODEC(VANILLA_CLOUD_EXTENDED.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleSnowFlake.SnowflakeData>> SNOWFLAKE = REG.register("snowflake", () -> new ParticleType<ParticleSnowFlake.SnowflakeData>(false, ParticleSnowFlake.SnowflakeData.DESERIALIZER) {
        @Override
        public Codec<ParticleSnowFlake.SnowflakeData> func_230522_e_() {
            return ParticleSnowFlake.SnowflakeData.CODEC(SNOWFLAKE.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleCloud.CloudData>> CLOUD = REG.register("cloud_soft", () -> new ParticleType<ParticleCloud.CloudData>(false, ParticleCloud.CloudData.DESERIALIZER) {
        @Override
        public Codec<ParticleCloud.CloudData> func_230522_e_() {
            return ParticleCloud.CloudData.CODEC(CLOUD.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleOrb.OrbData>> ORB = REG.register("orb_0", () -> new ParticleType<ParticleOrb.OrbData>(false, ParticleOrb.OrbData.DESERIALIZER) {
        @Override
        public Codec<ParticleOrb.OrbData> func_230522_e_() {
            return ParticleOrb.OrbData.CODEC(ORB.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleRing.RingData>> RING = REG.register("ring_0", () -> new ParticleType<ParticleRing.RingData>(false, ParticleRing.RingData.DESERIALIZER) {
        @Override
        public Codec<ParticleRing.RingData> func_230522_e_() {
            return ParticleRing.RingData.CODEC(RING.get());
        }
    });

    public static final RegistryObject<ParticleType<StretchParticle.StretchParticleData>> CRACK_1 = registerStretch("crack_1", StretchParticle.StretchParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<StretchParticle.StretchParticleData>> CRACK_3 = registerStretch("crack_3", StretchParticle.StretchParticleData.DESERIALIZER);

    public static final RegistryObject<ParticleType<AdvancedParticleData>> RING2 = register("ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> RING_BIG = register("ring_big", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> PIXEL = register("pixel", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ORB2 = register("orb", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> EYE = register("eye", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BUBBLE = register("bubble", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> SUN = register("sun", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> SUN_NOVA = register("sun_nova", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> FLARE = register("flare", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> FLARE_RADIAL = register("flare_radial", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BURST_IN = register("ring1", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BURST_MESSY = register("burst_messy", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> RING_SPARKS = register("sparks_ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BURST_OUT = register("ring2", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> GLOW = register("glow", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ARROW_HEAD = register("arrow_head", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> HEXAGON = register("hexagon", AdvancedParticleData.DESERIALIZER);

    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_FLAT = registerRibbon("ribbon_flat", RibbonParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_STREAKS = registerRibbon("ribbon_streaks", RibbonParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_GLOW = registerRibbon("ribbon_glow", RibbonParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_SQUIGGLE = registerRibbon("ribbon_squiggle", RibbonParticleData.DESERIALIZER);

    public static final RegistryObject<BasicParticleType> VOID_BEING_CLOUD = REG.register("void_being_cloud", () -> new BasicParticleType(false));
    public static final RegistryObject<BasicParticleType> FIRE = REG.register("fire", () -> new BasicParticleType(false));
    public static final RegistryObject<BasicParticleType> VOID_BEING_EYE = REG.register("void_being_eye", () -> new BasicParticleType(false));
    public static final RegistryObject<ParticleType<WindParticle.WindParticleData>> WIND = REG.register("wind", () -> new ParticleType<WindParticle.WindParticleData>(false, WindParticle.WindParticleData.DESERIALIZER) {
        @Override
        public Codec<WindParticle.WindParticleData> func_230522_e_() {
            return WindParticle.WindParticleData.CODEC();
        }
    });
    public static final RegistryObject<ParticleType<AdvancedParticleData>> SIGNAL_LOOP = register("signal_loop", AdvancedParticleData.DESERIALIZER);


    private static RegistryObject<BasicParticleType> register(String key, boolean alwaysShow) {
        return REG.register(key, () -> new BasicParticleType(alwaysShow));
    }

    private static RegistryObject<ParticleType<AdvancedParticleData>> register(String key, IParticleData.IDeserializer<AdvancedParticleData> deserializer) {
        return REG.register(key, () -> new ParticleType<AdvancedParticleData>(false, deserializer) {
            public Codec<AdvancedParticleData> func_230522_e_() {
                return AdvancedParticleData.CODEC(this);
            }
        });
    }

    private static RegistryObject<ParticleType<StretchParticle.StretchParticleData>> registerStretch(String key, IParticleData.IDeserializer<StretchParticle.StretchParticleData> deserializer) {
        return REG.register(key, () -> new ParticleType<StretchParticle.StretchParticleData>(false, deserializer) {
            public Codec<StretchParticle.StretchParticleData> func_230522_e_() {
                return StretchParticle.StretchParticleData.CODEC(this);
            }
        });
    }

    private static RegistryObject<ParticleType<RibbonParticleData>> registerRibbon(String key, IParticleData.IDeserializer<RibbonParticleData> deserializer) {
        return REG.register(key, () -> new ParticleType<RibbonParticleData>(false, deserializer) {
            public Codec<RibbonParticleData> func_230522_e_() {
                return RibbonParticleData.CODEC_RIBBON(this);
            }
        });
    }
}
