package com.freefish.arknightsmobs.server.entity;

import com.freefish.arknightsmobs.client.sound.BossMusicPlayer;
import com.freefish.arknightsmobs.server.attribute.AttributeRegistry;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class FreeFishEntity extends CreatureEntity implements IAnimatedEntity, IAnimatable {
    private static final DataParameter<Float> ARMORDURABILITY = EntityDataManager.createKey(FreeFishEntity.class, DataSerializers.FLOAT);
    private int animationTick;
    private Animation animation = NO_ANIMATION;
    public int frame;
    private static final byte MUSIC_PLAY_ID = 67;
    private static final byte MUSIC_STOP_ID = 68;

    public FreeFishEntity(EntityType<? extends CreatureEntity> p_i48575_1_, World p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
        this.setArmorDurability(this.getMaxArmorDurability());
    }

    @Override
    public void tick() {
        super.tick();
        frame++;
        if (getAnimation() != NO_ANIMATION) {
            animationTick++;
            if (world.isRemote && animationTick >= animation.getDuration()) {
                setAnimation(NO_ANIMATION);
            }
        }
        if (!world.isRemote && getBossMusic() != null) {
            if (canPlayMusic()) {
                this.world.setEntityState(this, MUSIC_PLAY_ID);
            }
            else {
                this.world.setEntityState(this, MUSIC_STOP_ID);
            }
        }
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        float armorDurability = this.dataManager.get(ARMORDURABILITY);
        if (armorDurability > 0) {
            if (amount <= 0) return false;
            amount = this.applyArmorCalculations(source, amount);
            amount = this.applyPotionDamageCalculations(source, amount);
            float f2 = Math.max(amount - this.getAbsorptionAmount(), 0.0F);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (amount - f2));
            float f = amount - f2;
            if (f > 0.0F && f < 3.4028235E37F && source.getTrueSource() instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity)source.getTrueSource()).addStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(f * 10.0F));
            }
            if (f2 != 0.0F) {
                this.getCombatTracker().trackDamage(source, armorDurability, f2);
                setArmorDurability(armorDurability - f2);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - f2);
            }
            return super.attackEntityFrom(source, 0);
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == MUSIC_PLAY_ID) {
            BossMusicPlayer.playBossMusic(this);
        }
        else if (id == MUSIC_STOP_ID) {
            BossMusicPlayer.stopBossMusic(this);
        }
        else super.handleStatusUpdate(id);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ARMORDURABILITY, 0f);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat("ArmorDurability", this.getArmorDurability());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        prevRotationYaw = rotationYaw;
        prevRenderYawOffset = renderYawOffset = prevRotationYawHead = rotationYawHead;
        super.readAdditional(compound);
        this.setArmorDurability(compound.getFloat("ArmorDurability"));
    }

    protected void onAnimationFinish(Animation animation) {}

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
        return this.animation;
    }

    @Override
    public void setAnimation(Animation animation) {
        if (animation == NO_ANIMATION) {
            onAnimationFinish(this.animation);
        }
        this.animation = animation;
        setAnimationTick(0);
    }

    public abstract Animation getDeathAnimation();

    public abstract Animation getHurtAnimation();

    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }

    public List<PlayerEntity> getPlayersNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().grow(distanceX, distanceY, distanceZ));
        List<PlayerEntity> listEntityPlayers = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof PlayerEntity && getDistance(entityNeighbor) <= radius + entityNeighbor.getWidth() / 2f).map(entityNeighbor -> (PlayerEntity) entityNeighbor).collect(Collectors.toList());
        return listEntityPlayers;
    }

    public List<LivingEntity> getAttackableEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().grow(distanceX, distanceY, distanceZ));
        List<LivingEntity> listEntityLivingBase = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof LivingEntity && ((LivingEntity)entityNeighbor).attackable() && (!(entityNeighbor instanceof PlayerEntity) || !((PlayerEntity)entityNeighbor).isCreative()) && getDistance(entityNeighbor) <= radius + entityNeighbor.getWidth() / 2f).map(entityNeighbor -> (LivingEntity) entityNeighbor).collect(Collectors.toList());
        return listEntityLivingBase;
    }

    public  List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(r, r, r), e -> e != this && getDistance(e) <= r + e.getWidth() / 2f);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(dX, dY, dZ), e -> e != this && getDistance(e) <= r + e.getWidth() / 2f && e.getPosY() <= getPosY() + dY);
    }

    protected void repelEntities(float x, float y, float z, float radius) {
        List<LivingEntity> nearbyEntities = getEntityLivingBaseNearby(x, y, z, radius);
        for (Entity entity : nearbyEntities) {
            if (entity.canBeCollidedWith() && !entity.noClip) {
                double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
                entity.setMotion(-0.1 * Math.cos(angle), entity.getMotion().y, -0.1 * Math.sin(angle));
            }
        }
    }

    public static boolean spawnPredicate(EntityType type, IWorld world, SpawnReason reason, BlockPos spawnPos, Random rand) {
        return true;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }

    public double getAngleBetweenEntities(Entity first, Entity second) {
        return Math.atan2(second.getPosZ() - first.getPosZ(), second.getPosX() - first.getPosX()) * (180 / Math.PI) + 90;
    }

    public void setArmorDurability(float armorDurability) {
        this.dataManager.set(ARMORDURABILITY, MathHelper.clamp(armorDurability, 0.0F, this.getMaxArmorDurability()));
    }

    public float getArmorDurability() {
        return this.dataManager.get(ARMORDURABILITY);
    }

    public final float getMaxArmorDurability() {
        return (float)this.getAttributeValue(AttributeRegistry.ARMOR_DURABILITY.get());
    }

    protected boolean canPlayMusic() {
        return !isSilent() && getAttackTarget() instanceof PlayerEntity;
    }

    public SoundEvent getBossMusic() {
        return null;
    }

    public boolean canPlayerHearMusic(PlayerEntity player) {
        return player != null
                && canAttack(player)
                && getDistance(player) < 2500;
    }
}
