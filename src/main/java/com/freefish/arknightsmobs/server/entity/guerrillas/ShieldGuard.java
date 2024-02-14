package com.freefish.arknightsmobs.server.entity.guerrillas;

import com.freefish.arknightsmobs.server.attribute.AttributeRegistry;
import com.freefish.arknightsmobs.server.entity.guerrillas.GuerrillasEntity;
import com.freefish.arknightsmobs.server.entity.guerrillas.GuerrillasLeaderEntity;
import com.freefish.arknightsmobs.server.entity.guerrillas.GuerrillasMemberEntity;
import com.freefish.arknightsmobs.server.entity.guerrillas.Messenger;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class ShieldGuard extends GuerrillasLeaderEntity {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final float R = 3;
    private Messenger messenger;
    public static final Animation ATTACK = Animation.create(25);
    public static final Animation MOVE = Animation.create(0);
    private static final Animation[] ANIMATIONS = {
            ATTACK,MOVE
    };

    public ShieldGuard(EntityType<? extends CreatureEntity> p_i48575_1_, World p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        //this.goalSelector.addGoal(8, new RandomWalkingGoal(this , 1F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, true));
    }

    @Override
    public void tick() {
        if(messenger == null) {
            messenger = getLeader();
            if(messenger != null)
                messenger.addShieldGuardMember(this);
        }

        attackModel();
        super.tick();
    }

    @Override
    public void remove() {
        if(messenger != null) {
            messenger.removeShieldGuardMember(this);
        }
        super.remove();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        Entity entitySource = source.getTrueSource();
        if (entitySource != null) {
            int arc = 220;
            float entityHitAngle = (float) ((Math.atan2(entitySource.getPosZ() - getPosZ(), entitySource.getPosX() - getPosX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = renderYawOffset % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            if ((entityRelativeAngle <= arc / 2f && entityRelativeAngle >= -arc / 2f) || (entityRelativeAngle >= 360 - arc / 2f || entityRelativeAngle <= -arc + 90f / 2f)) {
                //playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED.get(), 0.4F, 2);
                return super.attackEntityFrom(source, damage / 2);
            } else {
                return super.attackEntityFrom(source, damage);
            }
        }
        return super.attackEntityFrom(source, damage);
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
        data.addAnimationController(new AnimationController<>(this, "Controller", 20, this::predicate));
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        return PlayState.CONTINUE;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return GuerrillasEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 50.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 20.0f)
                .createMutableAttribute(Attributes.ATTACK_SPEED, 1.0f)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.15f)
                .createMutableAttribute(Attributes.ARMOR, 8D)
                .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 4D)
                .createMutableAttribute(AttributeRegistry.ARMOR_DURABILITY.get(),10);
    }

    public void attackModel() {
        if(!getPack().isEmpty()) {
            int n = getPack().size();
            double r = this.rotationYaw<0?(this.rotationYaw + 360)+90+180.0f/(n+1):this.rotationYaw+90+180f/(n+1);
            for(int n1 = 0; n1 < n; n1++) {
                GuerrillasMemberEntity member = getPack().get(n1);
                if(member.getAttackTarget() == null) {
                    double r1 = (r / 180) * Math.PI;
                    member.getNavigator().tryMoveToXYZ(getPosX() - R * Math.sin(r1), getPosY(), getPosZ() + R * Math.cos(r1), 1.5);
                    r += 180.0f / n;
                }
            }
        }
    }

    private Messenger getLeader() {
        List<Messenger> leader = world.getEntitiesWithinAABB(Messenger.class, getBoundingBox().grow(32, 32, 32));
        for(Messenger entity: leader) {
            double d1 = getDistanceSq(leader.get(0));
            double d2 = getDistanceSq(entity);
            if(d1 > d2)
                leader.set(0, entity);
        }
        if (leader.isEmpty())
            return null;
        else {
            return leader.get(0);
        }
    }
}
