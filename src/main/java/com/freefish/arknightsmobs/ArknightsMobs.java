package com.freefish.arknightsmobs;

import com.freefish.arknightsmobs.client.event.ClientEventHandler;
import com.freefish.arknightsmobs.client.particle.ParticleHandler;
import com.freefish.arknightsmobs.server.ServerNetwork;
import com.freefish.arknightsmobs.server.attribute.AttributeRegistry;
import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.freefish.arknightsmobs.server.item.ItemRegistry;
import com.freefish.arknightsmobs.server.potion.EffectRegistry;
import com.freefish.arknightsmobs.server.sound.MMSounds;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(ArknightsMobs.MOD_ID)
public class ArknightsMobs {
    public final static String MOD_ID = "arknightsmobs";
    public static final Logger LOGGER = LogManager.getLogger();
    public static SimpleChannel NETWORK;

    public ArknightsMobs() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemRegistry.ITEMS.register(bus);
        EntityRegistry.ENTITY_TYPE.register(bus);
        MMSounds.REG.register(bus);
        AttributeRegistry.ATTRIBUTES.register(bus);
        ParticleHandler.REG.register(bus);
        EffectRegistry.POTIONS.register(bus);

    }

    private void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(()->{
            ServerNetwork.initNetwork();
            MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
            EntitySpawnPlacementRegistry.register(EntityRegistry.PATRIOT.get(),
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,Patriot::spawnPredicate);
        });
    }
}
