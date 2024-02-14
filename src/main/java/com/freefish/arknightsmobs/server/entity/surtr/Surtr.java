package com.freefish.arknightsmobs.server.entity.surtr;

import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
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

import javax.annotation.Nullable;
import java.util.List;

public class Surtr extends MobEntity implements IAnimatable , IAnimatedEntity{
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public PlayerEntity playerEntity;
    private int animationTick;
    private Animation animation = NO_ANIMATION;
    private static final DataParameter<Integer> USER_ID = EntityDataManager.createKey(Surtr.class, DataSerializers.VARINT);
    private LivingEntity userRef = null;

    public static final Animation ATTACK = Animation.create(15);
    public static final Animation MOVE = Animation.create(0);
    public static final Animation SKILL = Animation.create(26);
    private static final Animation[] ANIMATIONS = {
            ATTACK,MOVE,SKILL
    };

    public Surtr(EntityType<? extends MobEntity> p_i48576_1_, World p_i48576_2_) {
        super(p_i48576_1_, p_i48576_2_);
        setNoGravity(true);
        canUpdate(false);
    }

    public Surtr(EntityType<? extends MobEntity> p_i48576_1_, World p_i48576_2_, int playerUUID) {
        this(p_i48576_1_, p_i48576_2_);
        //this.playerEntity = player;
        getDataManager().set(USER_ID,playerUUID);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SimpleAnimationAI<Surtr>(this, SKILL) {
            @Override
            public void tick() {
                if(getAnimationTick() == 10) {
                    LivingEntity player = getUser();
                    for(int i = 1; i <= 4; i++) {
                        FlameEntity flameEntity = new FlameEntity(entity.world);
                        double r = entity.rotationYaw / 180.0 * Math.PI;
                        flameEntity.setPosition(entity.getPosX()-i*Math.sin(r), entity.getPosY(), entity.getPosZ()+i*Math.cos(r));
                        entity.world.addEntity(flameEntity);
                    }

                    List<LivingEntity> livingEntityList = world.getEntitiesWithinAABB(LivingEntity.class, getBoundingBox().grow(4,3,4), e ->
                            e!=entity && e!=player && getDistanceSq(e) <= 4 + e.getBoundingBox().getXSize());
                    float damage = (float)entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
                    for (LivingEntity entityHit : livingEntityList) {
                        float entityHitAngle = (float) ((Math.atan2(entityHit.getPosZ() - player.getPosZ(), entityHit.getPosX() - player.getPosX()) * (180 / Math.PI) - 90) % 360);
                        float entityAttackingAngle = player.renderYawOffset % 360;

                        if (entityHitAngle < 0) {
                            entityHitAngle += 360;
                        }
                        if (entityAttackingAngle < 0) {
                            entityAttackingAngle += 360;
                        }
                        float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                        float entityHitDistance = (float) Math.sqrt((entityHit.getPosZ() - player.getPosZ()) * (entityHit.getPosZ() - player.getPosZ()) + (entityHit.getPosX() - player.getPosX()) * (entityHit.getPosX() - player.getPosX()));
                        if (entityHitDistance <= 4 && (entityRelativeAngle <= 40F / 2 && entityRelativeAngle >= -40F / 2) || (entityRelativeAngle >= 360 - 40F / 2 || entityRelativeAngle <= -360 + 40F / 2)) {
                            entityHit.attackEntityFrom(DamageSource.causeMobDamage(entity), damage * 1.5F);
                        }
                    }
                }
                super.tick();
            }
        });
    }

    @Override
    public void tick() {
        if (getAnimation() != NO_ANIMATION) {
            animationTick++;
            if (world.isRemote && animationTick >= animation.getDuration()) {
                setAnimation(NO_ANIMATION);
            }
        }
        super.tick();
    }

    public void updatePosition() {
        this.setMotion(Vector3d.ZERO);
        this.tick();
        this.positionOwner(this, Entity::setPosition);
    }

    private void positionOwner(Entity entity, IMoveCallback callback) {
        LivingEntity livingEntity = getUser();
        this.rotationYaw = livingEntity.rotationYaw;
        callback.accept(entity, livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getPosZ());
    }

    @Override
    public void rotateTowards(double p_195049_1_, double p_195049_3_) {
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.registerAttributes()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 1).createMutableAttribute(Attributes.ATTACK_DAMAGE, 16);
    }

    @Override
    public void applyEntityCollision(Entity p_70108_1_) {
    }

    @Override
    protected void collideWithEntity(Entity p_82167_1_) {
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(USER_ID, -1);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> dataParameter) {
        super.notifyDataManagerChange(dataParameter);
        if (USER_ID.equals(dataParameter)) {
            updateUserFromNetwork(getDataManager().get(USER_ID));
        }
        
    }

    private void updateUserFromNetwork(int userId) {
        userRef = lookupUser(userId);
    }

    private LivingEntity lookupUser(int userId) {
        Entity user = world.getEntityByID(userId);
        if(user instanceof  LivingEntity)
            return (LivingEntity) user;
        return null;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "Controller", 20, this::predicate));
        data.addAnimationController(new AnimationController<>(this,"attackController",1,this::attackPredicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(this.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if(this.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
            if(this.getAnimation() == Surtr.ATTACK) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", ILoopType.EDefaultLoopTypes.LOOP));
            }
            if(this.getAnimation() == Surtr.SKILL) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("skill", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        else
            return PlayState.STOP;
    }

    @Nullable
    public LivingEntity getUser() {
        return userRef == null ? null : userRef;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }


    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.animation = animation;
        setAnimationTick(0);
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }
}
