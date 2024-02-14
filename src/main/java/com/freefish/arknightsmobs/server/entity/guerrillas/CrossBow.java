package com.freefish.arknightsmobs.server.entity.guerrillas;

import com.freefish.arknightsmobs.server.attribute.AttributeRegistry;
import com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.CrossBowAttackAI;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class CrossBow extends GuerrillasMemberEntity implements IRangedAttackMob {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public CrossBow(EntityType<? extends CreatureEntity> p_i48575_1_, World p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return GuerrillasMemberEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 8.0f)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2f)
                .createMutableAttribute(Attributes.ARMOR, 4D)
                .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 2D)
                .createMutableAttribute(AttributeRegistry.ARMOR_DURABILITY.get(), 2D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this , 1F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, true));
        this.targetSelector.addGoal(2, new CrossBowAttackAI(this, 40));
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

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.findAmmo(this.getHeldItem(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.item.BowItem)));
        AbstractArrowEntity abstractarrowentity = this.fireArrow(itemstack, distanceFactor);
        if (this.getHeldItemMainhand().getItem() instanceof net.minecraft.item.BowItem)
            abstractarrowentity = ((net.minecraft.item.BowItem)this.getHeldItemMainhand().getItem()).customArrow(abstractarrowentity);
        double d0 = target.getPosX() - this.getPosX();
        double d1 = target.getPosYHeight(0.3333333333333333D) - abstractarrowentity.getPosY();
        double d2 = target.getPosZ() - this.getPosZ();
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(abstractarrowentity);
    }

    protected AbstractArrowEntity fireArrow(ItemStack arrowStack, float distanceFactor) {
        return ProjectileHelper.fireArrow(this, arrowStack, distanceFactor);
    }
}
