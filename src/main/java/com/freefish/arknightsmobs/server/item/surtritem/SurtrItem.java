package com.freefish.arknightsmobs.server.item.surtritem;

import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import com.freefish.arknightsmobs.server.entity.surtr.Surtr;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.concurrent.Callable;

public class SurtrItem extends Item implements IAnimatable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public SurtrItem(Properties p_i48487_1_) {
        super(p_i48487_1_.setISTER(() -> new Callable() {
            private final SurtrItemRenderer renderer = new SurtrItemRenderer();
            @Override
            public SurtrItemRenderer call() throws Exception {
                return this.renderer;
            }
        }));
    }

    //@Override
    //public void onPlayerStoppedUsing(ItemStack itemStack, World world, LivingEntity livingEntity, int i) {
    //    super.onPlayerStoppedUsing(itemStack, world, livingEntity, i);
    //}

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        Surtr surtr = new Surtr(EntityRegistry.SURTR.get(), world, player.getEntityId());
        surtr.setPosition(player.getPosX(), player.getPosY(), player.getPosZ());
        surtr.rotationYaw = player.rotationYaw;
        world.addEntity(surtr);
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
