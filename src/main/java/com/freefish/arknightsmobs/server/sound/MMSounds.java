package com.freefish.arknightsmobs.server.sound;

import com.freefish.arknightsmobs.ArknightsMobs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MMSounds {
    public static final DeferredRegister<SoundEvent> REG = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ArknightsMobs.MOD_ID);

    public static final RegistryObject<SoundEvent> ENTITY_PATRIOT_STEP = create("step");
    public static final RegistryObject<SoundEvent> ENTITY_WROUGHT_AXE_HIT = create("wroughtnaut.axe_hit");
    public static final RegistryObject<SoundEvent> PATRIOT_THEME = create("music.patriot_theme");

    private static RegistryObject<SoundEvent> create(String name) {
        return REG.register(name, () -> new SoundEvent(new ResourceLocation(ArknightsMobs.MOD_ID, name)));
    }
}