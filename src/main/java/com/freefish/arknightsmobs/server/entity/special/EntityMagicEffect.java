package com.freefish.arknightsmobs.server.entity.special;

import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public abstract class EntityMagicEffect extends Entity {
    public LivingEntity caster;
    private static final DataParameter<Integer> CASTER = EntityDataManager.createKey(EntityMagicEffect.class, DataSerializers.VARINT);

    public EntityMagicEffect(EntityType<? extends EntityMagicEffect> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public PushReaction getPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    protected void registerData() {
        getDataManager().register(CASTER, -1);
    }

    public int getCasterID() {
        return getDataManager().get(CASTER);
    }

    public void setCasterID(int id) {
        getDataManager().set(CASTER, id);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
    }

    @Override
    public void tick() {
        super.tick();
        if (ticksExisted == 1) {
            caster = (LivingEntity) world.getEntityByID(getCasterID());
        }
        if (caster != null) {
            if (!caster.isAlive()) remove();
            setPositionAndRotation(caster.getPosX(), caster.getPosY() + caster.getEyeHeight(), caster.getPosZ(), caster.rotationYaw, caster.rotationPitch);
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    public List<LivingEntity> getEntityLivingBaseNearby(double radius) {
        return getEntitiesNearby(LivingEntity.class, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(r, r, r), e -> e != this && getDistance(e) <= r + e.getWidth() / 2f);
    }

    public <T extends Entity> List<T> getEntitiesNearbyCube(Class<T> entityClass, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(r, r, r), e -> e != this);
    }

    public boolean raytraceCheckEntity(Entity entity) {
        Vector3d from = this.getPositionVec();
        int numChecks = 3;
        for (int i = 0; i < numChecks; i++) {
            float increment = entity.getHeight() / (numChecks + 1);
            Vector3d to = entity.getPositionVec().add(0, increment * (i + 1), 0);
            BlockRayTraceResult result = world.rayTraceBlocks(new RayTraceContext(from, to, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
            if (result.getType() != RayTraceResult.Type.BLOCK) {
                return true;
            }
        }
        return false;
    }
}
