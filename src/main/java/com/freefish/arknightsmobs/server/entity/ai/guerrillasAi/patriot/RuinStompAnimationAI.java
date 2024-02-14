package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.freefish.arknightsmobs.server.entity.special.EntityFallingBlock;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class RuinStompAnimationAI extends AnimationAI<Patriot> {
    public RuinStompAnimationAI(Patriot entity,Animation animation) {
        super(entity);
    }

    @Override
    public void tick() {
    }

    @Override
    protected boolean test(Animation animation) {
        return animation== Patriot.RUIN_STOMP;
    }
}
