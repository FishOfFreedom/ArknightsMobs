package com.freefish.arknightsmobs.server.entity.guerrillas;

import com.freefish.arknightsmobs.server.attribute.AttributeRegistry;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
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

import java.util.ArrayList;
import java.util.List;

public class Messenger extends GuerrillasLeaderEntity {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private Patriot patriot;
    private List<ShieldGuard> shieldGuardPack = new ArrayList<>();
    private static final float R = 3;
    public static final Animation ATTACK = Animation.create(25);
    public static final Animation MOVE = Animation.create(0);
    private static final Animation[] ANIMATIONS = {
            ATTACK,MOVE
    };

    public Messenger(EntityType<? extends CreatureEntity> p_i48575_1_, World p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
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
    public void tick() {
        if(patriot == null) {
            patriot = lookUpPatriot();
            if(patriot != null)
                patriot.addMessengerMember(this);
        }

        SoldierAttackModel();
        shieldGuardAttackModel();
        super.tick();
    }

    @Override
    public void remove() {
        if(patriot != null)
            patriot.removeMessengerMember(this);
        super.remove();
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
    public void addShieldGuardMember(ShieldGuard shieldGuard) {
        shieldGuardPack.add(shieldGuard);
    }
    public void removeShieldGuardMember(ShieldGuard shieldGuard){
        shieldGuardPack.remove(shieldGuard);
    }
    public void shieldGuardAttackModel(){
        if(!getShieldGuardPack().isEmpty()) {
            int n = getShieldGuardPack().size();
            double r = 2*Math.PI/n;
            for(int n1 = 0; n1 < n; n1++) {
                ShieldGuard member = getShieldGuardPack().get(n1);
                //if(member.getAttackTarget() == null) {
                member.getNavigator().tryMoveToXYZ(getPosX()-4*Math.sin((n1+1)*r),getPosY(),getPosZ()+4*Math.cos((n1+1)*r), 1.5);
                //}
            }
        }
    }

    public List<ShieldGuard> getShieldGuardPack() {
        return shieldGuardPack;
    }

    public void SoldierAttackModel() {
        if(!getPack().isEmpty()) {
            int n = getPack().size();
            double r = 2*Math.PI/n;
            for(int n1 = 0; n1 < n; n1++) {
                GuerrillasMemberEntity member = getPack().get(n1);
                if(member.getAttackTarget() == null) {
                    member.getNavigator().tryMoveToXYZ(getPosX()-R*Math.sin((n1+1)*r),getPosY(),getPosZ()-R*Math.cos((n1+1)*r), 1.5);
                }
            }
        }
    }

    private Patriot lookUpPatriot() {
        List<Patriot> leader = world.getEntitiesWithinAABB(Patriot.class, getBoundingBox().grow(32, 32, 32));
        for(Patriot entity: leader) {
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
