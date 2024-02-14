package com.freefish.arknightsmobs.server.world.gen;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArknightsMobs.MOD_ID)
public class AMEntitySpawnEvent {
    @SubscribeEvent
    public static void BiomeLoading(BiomeLoadingEvent event){
        ResourceLocation biomeName = event.getName();
        if(biomeName == null) return;
        MobSpawnInfoBuilder mobSpawnInfoBuilder = event.getSpawns();
        if(biomeName.toString().equals(Biomes.SNOWY_TUNDRA.getLocation().toString())){
            mobSpawnInfoBuilder.getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(EntityRegistry.PATRIOT.get(),10,1,1));
        }
    }
}
