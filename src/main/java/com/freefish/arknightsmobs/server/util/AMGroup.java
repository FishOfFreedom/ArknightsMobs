package com.freefish.arknightsmobs.server.util;

import com.freefish.arknightsmobs.server.item.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class AMGroup extends ItemGroup {
    public static final ItemGroup itemGroup = new AMGroup();

    public AMGroup() {
        super("ArknightsMobs");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemRegistry.SHIELD_OF_THE_INFECTED.get());
    }
}
