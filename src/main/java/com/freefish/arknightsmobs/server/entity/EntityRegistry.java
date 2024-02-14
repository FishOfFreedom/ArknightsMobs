package com.freefish.arknightsmobs.server.entity;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.ganranzhezhiji.GanRanZheZhiJi;
import com.freefish.arknightsmobs.server.entity.guerrillas.CrossBow;
import com.freefish.arknightsmobs.server.entity.guerrillas.Messenger;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.freefish.arknightsmobs.server.entity.guerrillas.SarkazSoldier;
import com.freefish.arknightsmobs.server.entity.guerrillas.ShieldGuard;
import com.freefish.arknightsmobs.server.entity.guerrillas.Soldier;
import com.freefish.arknightsmobs.server.entity.special.*;
import com.freefish.arknightsmobs.server.entity.surtr.FlameEntity;
import com.freefish.arknightsmobs.server.entity.surtr.Surtr;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = ArknightsMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE =
            DeferredRegister.create(ForgeRegistries.ENTITIES, ArknightsMobs.MOD_ID);

    public static final RegistryObject<EntityType<GanRanZheZhiJi>> GAN_RAN_ZHE_ZHI_JI = ENTITY_TYPE.register("gan_ran_zhe_zhi_ji",
            () -> EntityType.Builder.<GanRanZheZhiJi>create(GanRanZheZhiJi::new, EntityClassification.MISC)
                    .size(1F, 1F)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "gan_ran_zhe_zhi_ji").toString()));
    public static final RegistryObject<EntityType<Soldier>> GUERRILLAS_SOLDIER = ENTITY_TYPE.register("guerrillas_soldier",
            () -> EntityType.Builder.<Soldier>create(Soldier::new, EntityClassification.CREATURE)
                    .size(0.8F, 1.8F)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "guerrillas_soldier").toString()));

    public static final RegistryObject<EntityType<CrossBow>> GUERRILLAS_CROSS_BOW = ENTITY_TYPE.register("guerrillas_cross_bow",
            () -> EntityType.Builder.<CrossBow>create(CrossBow::new, EntityClassification.CREATURE)
                    .size(0.8F, 1.8F)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "guerrillas_cross_bow").toString()));

    public static final RegistryObject<EntityType<SarkazSoldier>> GUERRILLAS_SARKAZ_SOLDIER = ENTITY_TYPE.register("guerrillas_sarkaz_soldier",
            () -> EntityType.Builder.<SarkazSoldier>create(SarkazSoldier::new, EntityClassification.CREATURE)
                    .size(0.8F, 1.8F)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "guerrillas_sarkaz_soldier").toString()));

    public static final RegistryObject<EntityType<ShieldGuard>> GUERRILLAS_SHIELD_GUARD = ENTITY_TYPE.register("guerrillas_shield_guard",
            () -> EntityType.Builder.<ShieldGuard>create(ShieldGuard::new, EntityClassification.CREATURE)
                    .size(1F, 2F)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "guerrillas_shield_guard").toString()));

    public static final RegistryObject<EntityType<Messenger>> GUERRILLAS_MESSENGER = ENTITY_TYPE.register("guerrillas_messenger",
            () -> EntityType.Builder.<Messenger>create(Messenger::new, EntityClassification.CREATURE)
                    .size(0.8F, 1.8F)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "guerrillas_messenger").toString()));

    public static final RegistryObject<EntityType<Patriot>> PATRIOT = ENTITY_TYPE.register("patriot",
            () -> EntityType.Builder.<Patriot>create(Patriot::new, EntityClassification.CREATURE)
                    .size(1F, 3.5F)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "patriot").toString()));

    public static final RegistryObject<EntityType<Surtr>> SURTR = ENTITY_TYPE.register("surtr",
            () -> EntityType.Builder.<Surtr>create(Surtr::new, EntityClassification.CREATURE)
                    .size(0.4F, 0.5F)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "surtr").toString()));

    public static final RegistryObject<EntityType<FlameEntity>> FLAME = ENTITY_TYPE.register("flame",
            () -> EntityType.Builder.<FlameEntity>create(FlameEntity::new, EntityClassification.MISC)
                    .size(1f, 1f)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID,"flame").toString()));

    public static final RegistryObject<EntityType<EntityCameraShake>> CAMERA_SHAKE = ENTITY_TYPE.register("camera_shake",
            () -> EntityType.Builder.<EntityCameraShake>create(EntityCameraShake::new, EntityClassification.MISC)
                    .size(1, 1).setUpdateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "camera_shake").toString()));

    public static final RegistryObject<EntityType<SOTFEntity>> SOTF_ENTITY = ENTITY_TYPE.register("sotf_entity",
            () -> EntityType.Builder.<SOTFEntity>create(SOTFEntity::new, EntityClassification.MISC)
                    .size(1, 1).setUpdateInterval(Integer.MAX_VALUE)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "sotf_entity").toString()));

    public static final RegistryObject<EntityType<TerrifyingFog>> TERRIFYING_FOG = ENTITY_TYPE.register("terrifying_fog",
            () -> EntityType.Builder.<TerrifyingFog>create(TerrifyingFog::new, EntityClassification.MISC)
                    .size(0.2f, 0.2f)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "terrifying_fog").toString()));

    public static final RegistryObject<EntityType<EntityFallingBlock>> FALLING_BLOCK = ENTITY_TYPE.register("falling_block",
            () -> EntityType.Builder.<EntityFallingBlock>create(EntityFallingBlock::new, EntityClassification.MISC)
                    .size(1, 1)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "falling_block").toString()));

    public static final RegistryObject<EntityType<FallingStone>> FALLING_STONE = ENTITY_TYPE.register("falling_stone",
            () -> EntityType.Builder.<FallingStone>create(FallingStone::new, EntityClassification.MISC)
                    .size(1, 1)
                    .build(new ResourceLocation(ArknightsMobs.MOD_ID, "falling_stone").toString()));

    @SubscribeEvent
    public static void onCreateAttributes(EntityAttributeCreationEvent event){
        event.put(EntityRegistry.GUERRILLAS_SOLDIER.get(), Soldier.createAttributes().create());
        event.put(EntityRegistry.GUERRILLAS_SARKAZ_SOLDIER.get(), SarkazSoldier.createAttributes().create());
        event.put(EntityRegistry.GUERRILLAS_CROSS_BOW.get(), CrossBow.createAttributes().create());
        event.put(EntityRegistry.GUERRILLAS_MESSENGER.get(), Messenger.createAttributes().create());
        event.put(EntityRegistry.GUERRILLAS_SHIELD_GUARD.get(), ShieldGuard.createAttributes().create());
        event.put(EntityRegistry.PATRIOT.get(), Patriot.createAttributes().create());
        event.put(EntityRegistry.SURTR.get(), Surtr.createAttributes().create());
        event.put(EntityRegistry.FALLING_STONE.get(), Surtr.createAttributes().create());
    }
}
