package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.freefish.arknightsmobs.server.item.ItemRegistry;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;

public class PrizeAnimationAI extends AnimationAI<Patriot> {
    public PrizeAnimationAI(Patriot entity, Animation animation) {
        super(entity);
    }

    @Override
    public void tick() {
        LivingEntity entityTarget = entity.getAttackTarget();
        entity.setMotion(0, entity.getMotion().y, 0);
        if(entity.getAnimationTick()>= 25) {
            entity.renderYawOffset = entity.prevRenderYawOffset;
            if(entityTarget instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) entityTarget;
                if(player.getDistanceSq(entity)<=9){
                    ItemStack shield = new ItemStack(ItemRegistry.SHIELD_OF_THE_INFECTED.get(), 1);
                    ItemStack halberd = new ItemStack(ItemRegistry.GAN_RAN_ZHE_ZHI_JI_ITEM.get(), 1);
                    player.inventory.addItemStackToInventory(shield);
                    player.inventory.addItemStackToInventory(halberd);
                    entity.setAttackTarget(null);
                    AnimationHandler.INSTANCE.sendAnimationMessage(entity,Patriot.NO_ANIMATION);
                }
            }
        }
        else if(entityTarget!=null)
            entity.faceEntity(entityTarget, 30F, 30F);
        if(entity.getAnimationTick()== 159){
            double yaw = entity.renderYawOffset/180*Math.PI;
            ItemStack shield = new ItemStack(ItemRegistry.SHIELD_OF_THE_INFECTED.get(), 1);
            ItemStack halberd = new ItemStack(ItemRegistry.GAN_RAN_ZHE_ZHI_JI_ITEM.get(), 1);
            Vector3d vector3d = entity.getPositionVec().add(-2*Math.sin(yaw),1,2*Math.cos(yaw));
            ItemEntity item1 = new ItemEntity(entity.world,vector3d.x,vector3d.y,vector3d.z,shield );
            ItemEntity item2 = new ItemEntity(entity.world,vector3d.x,vector3d.y,vector3d.z,halberd);
            entity.world.addEntity(item1);
            entity.world.addEntity(item2);
        }
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == Patriot.PRIZE;
    }
}
