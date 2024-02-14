package com.freefish.arknightsmobs.server.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.Random;

public class Terrify extends Effect {
    public Terrify() {
        super(EffectType.BENEFICIAL, 9643043);
    }

    //这个是buff在身上持续作用的效果函数
    @Override
    public void performEffect(LivingEntity living, int amplified) {
        amplified ++;
        Random ran = new Random();
        int co = ran.nextInt(5);
        for (EquipmentSlotType slot: EquipmentSlotType.values()) {
            DamageItemInSlot(slot, living, co*amplified);
        }

    }

    public void DamageItemInSlot(EquipmentSlotType slot, LivingEntity livingBase, int amount)
    {
        ItemStack stack = livingBase.getItemStackFromSlot(slot);
        stack.damageItem(1, livingBase, (p_220287_1_) -> {
            p_220287_1_.sendBreakAnimation(slot);
        });
    }

    //声明buff是好buff还是debuff
    @Override
    public boolean isBeneficial() {
        return false;
    }
}
