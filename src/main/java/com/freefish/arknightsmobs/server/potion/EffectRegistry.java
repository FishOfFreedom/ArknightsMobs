package com.freefish.arknightsmobs.server.potion;

import com.freefish.arknightsmobs.ArknightsMobs;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry {
    public static final DeferredRegister<Effect> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, ArknightsMobs.MOD_ID);

    public static final RegistryObject<GuerrillaSong> GUERRILLA_SONG =
            POTIONS.register("guerrilla_song", GuerrillaSong::new);

    public static final RegistryObject<Terrify> TERRIFY =
            POTIONS.<Terrify>register("terrify", Terrify::new);
 }
