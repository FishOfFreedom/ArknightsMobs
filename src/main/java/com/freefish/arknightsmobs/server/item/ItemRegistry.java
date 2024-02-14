package com.freefish.arknightsmobs.server.item;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import com.freefish.arknightsmobs.server.item.halberd.GanRanZheZhiJiItem;
import com.freefish.arknightsmobs.server.item.halberd.HOTIRenderer;
import com.freefish.arknightsmobs.server.item.shield.SOTIRenderer;
import com.freefish.arknightsmobs.server.item.shield.ShieldOfTheInfected;
import com.freefish.arknightsmobs.server.item.surtritem.SurtrItem;
import com.freefish.arknightsmobs.server.util.AMGroup;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.codec.language.RefinedSoundex;

import java.util.concurrent.Callable;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ArknightsMobs.MOD_ID);

    public static final RegistryObject<GanRanZheZhiJiItem> GAN_RAN_ZHE_ZHI_JI_ITEM =
            ITEMS.register("halberd_of_the_infected",() ->
                    new GanRanZheZhiJiItem((new Item.Properties()).maxDamage(250).group(AMGroup.itemGroup).setISTER(() -> new Callable() {
                        private final HOTIRenderer hotiRenderer = new HOTIRenderer();

                        @Override
                        public HOTIRenderer call(){
                            return hotiRenderer;
                        }
                    })));

    public static final RegistryObject<TestItem> TEST_ITEM =
            ITEMS.register("test_item", () ->
                    new TestItem((new Item.Properties()).group(AMGroup.itemGroup)));

    public static final RegistryObject<SurtrItem> SURTR_ITEM =
                    ITEMS.register("laiwanting",() ->
                    new SurtrItem(new Item.Properties().group(AMGroup.itemGroup)));

    public static final RegistryObject<ShieldOfTheInfected> SHIELD_OF_THE_INFECTED =
            ITEMS.register("shield_of_the_infected",() ->
                    new ShieldOfTheInfected(new Item.Properties().group(AMGroup.itemGroup).setISTER(() -> new Callable() {
                        private final SOTIRenderer sotiRenderer = new SOTIRenderer();

                        @Override
                        public SOTIRenderer call(){
                            return sotiRenderer;
                        }
                    })));

    public static final RegistryObject<SpawnEggItem> CROSS_BOW_EGG =
            ITEMS.register("cross_bow_egg", () ->
                    new ForgeSpawnEggItem(EntityRegistry.GUERRILLAS_CROSS_BOW, 4996656, 986895, new Item.Properties().group(AMGroup.itemGroup)));
    public static final RegistryObject<SpawnEggItem> MESSENGER_EGG =
            ITEMS.register("messenger_egg", () ->
                    new ForgeSpawnEggItem(EntityRegistry.GUERRILLAS_MESSENGER, 4996656, 986895, new Item.Properties().group(AMGroup.itemGroup)));
    public static final RegistryObject<SpawnEggItem> SARKAZ_SOLDIER_EGG =
            ITEMS.register("sarkaz_soldier_egg", () ->
                    new ForgeSpawnEggItem(EntityRegistry.GUERRILLAS_SARKAZ_SOLDIER, 4996656, 986895, new Item.Properties().group(AMGroup.itemGroup)));
    public static final RegistryObject<SpawnEggItem> SHIELD_GUARD_EGG =
            ITEMS.register("shield_guard_egg", () ->
                    new ForgeSpawnEggItem(EntityRegistry.GUERRILLAS_SHIELD_GUARD, 4996656, 986895, new Item.Properties().group(AMGroup.itemGroup)));
    public static final RegistryObject<SpawnEggItem> SOLDIER_EGG =
            ITEMS.register("soldier_egg", () ->
                    new ForgeSpawnEggItem(EntityRegistry.GUERRILLAS_SOLDIER, 4996656, 986895, new Item.Properties().group(AMGroup.itemGroup)));
    public static final RegistryObject<SpawnEggItem> PATRIOT_EGG =
            ITEMS.register("patriot_egg", () ->
                    new ForgeSpawnEggItem(EntityRegistry.PATRIOT, 4996656, 986895, new Item.Properties().group(AMGroup.itemGroup)));
}
