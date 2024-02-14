package com.freefish.arknightsmobs.server.attribute;

import com.freefish.arknightsmobs.ArknightsMobs;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributeRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ArknightsMobs.MOD_ID);

    public static final RegistryObject<Attribute> ARMOR_DURABILITY = ATTRIBUTES.register("armor_durability",
            () -> new RangedAttribute("attribute.name.generic.max_health", 0.0, 0.0, 1024.0).setShouldWatch(true));
}
