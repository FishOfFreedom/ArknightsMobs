package com.freefish.arknightsmobs.server.entity.special;

import com.freefish.arknightsmobs.client.particle.ParticleCloud;
import com.freefish.arknightsmobs.client.particle.ParticleHandler;
import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import com.freefish.arknightsmobs.server.entity.guerrillas.GuerrillasEntity;
import com.freefish.arknightsmobs.server.potion.Terrify;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class TerrifyingFog extends Entity {
    private LivingEntity owner;
    private int time;

    public TerrifyingFog(EntityType<?> entityTypeIn, World worldIn) {
        super(EntityRegistry.TERRIFYING_FOG.get(), worldIn);

        time = 60;
    }

    public TerrifyingFog(World worldIn,LivingEntity owner,int time){
        super(EntityRegistry.TERRIFYING_FOG.get(),worldIn);
        this.owner = owner;
        this.time = time;
    }

    public TerrifyingFog(World worldIn,int time){
        super(EntityRegistry.TERRIFYING_FOG.get(),worldIn);
        this.time = time;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.ticksExisted >= time)
            remove();
        if(this.ticksExisted == time -1){
            specialAttack();
        }
    }


    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(world.isRemote){
            world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.93f, 0.172f, 0.172f, 5f + rand.nextFloat() * 15f, 70, ParticleCloud.EnumCloudBehavior.CONSTANT, 1f), getPosX(), getPosY(), getPosZ(), 0, 0, 0);
        }
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void specialAttack() {
        List<LivingEntity> livingEntities = this.world.getEntitiesWithinAABB(LivingEntity.class, getBoundingBox().grow(4.9), livingEntity ->
                livingEntity.getDistance(this) <= 5);
        LivingEntity nearlyLivingEntity = null;
        if(!livingEntities.isEmpty()){
            nearlyLivingEntity = livingEntities.get(0);
            for(LivingEntity livingEntity:livingEntities){
                if(livingEntity.getDistance(this) < nearlyLivingEntity.getDistance(this)){
                    if(livingEntity instanceof GuerrillasEntity) continue;
                    nearlyLivingEntity = livingEntity;
                }
            }
        }
        else return;

        double damage = owner.getAttributeValue(Attributes.ATTACK_DAMAGE);
        DamageSource damageSource = DamageSource.causeMobDamage(owner);
        nearlyLivingEntity.attackEntityFrom(damageSource , (float) damage);
        nearlyLivingEntity.addPotionEffect(new EffectInstance(new Terrify(), 40));
    }
}
