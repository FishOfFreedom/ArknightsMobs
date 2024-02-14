package com.freefish.arknightsmobs.server.item.shield;

import com.freefish.arknightsmobs.server.entity.special.SOTFEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ShieldOfTheInfected extends ShieldItem implements IAnimatable, ISyncable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final String CONTROLLER_NAME = "Controller";
    public static final int Hand = 0;

    public ShieldOfTheInfected(Properties builder) {
        super(builder);
        GeckoLibNetwork.registerSyncable(this);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, net.minecraft.util.Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        if (!worldIn.isRemote) {
            final int id = GeckoLibUtil.guaranteeIDForStack(itemstack, (ServerWorld) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerIn);
            GeckoLibNetwork.syncAnimation(target, this, id, Hand);
        }
        //SOTFEntity sotfEntity = new SOTFEntity(worldIn,playerIn,itemstack);
        //worldIn.addEntity(sotfEntity);
        return ActionResult.resultConsume(itemstack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World worldIn, LivingEntity livingEntity, int p_77615_4_) {
        if (!worldIn.isRemote) {
            final int id = GeckoLibUtil.guaranteeIDForStack(itemStack, (ServerWorld) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity);
            GeckoLibNetwork.syncAnimation(target, this, id, 1);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, CONTROLLER_NAME, 3, this::predicate));
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, CONTROLLER_NAME);
        //if (controller.getAnimationState() == AnimationState.Stopped) {
        //    if (state == Hand) {
        //        controller.markNeedsReload();
        //        controller.setAnimation(new AnimationBuilder().addAnimation("right_hand", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        //    }
        //    if(state == 1){
        //        controller.markNeedsReload();
        //        controller.setAnimation(new AnimationBuilder().addAnimation("pose_1", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        //    }
        //}
    }
}
