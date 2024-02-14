package com.freefish.arknightsmobs.server.entity.guerrillas;

import com.freefish.arknightsmobs.server.attribute.AttributeRegistry;
import com.freefish.arknightsmobs.server.entity.guerrillas.GuerrillasMemberEntity;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class SarkazSoldier extends GuerrillasMemberEntity {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public SarkazSoldier(EntityType<? extends CreatureEntity> p_i48575_1_, World p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return GuerrillasMemberEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 8.0f)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3f)
                .createMutableAttribute(Attributes.ARMOR, 4D)
                .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 2D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1)
                .createMutableAttribute(AttributeRegistry.ARMOR_DURABILITY.get(),10);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this , 1F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, true));
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount);
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        //if(event.isMoving())
        //    event.getController().setAnimation(new AnimationBuilder().addAnimation("running_legs", ILoopType.EDefaultLoopTypes.LOOP));
        //else
        //    event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
