package com.freefish.arknightsmobs.server.entity.ganranzhezhiji;

import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import com.freefish.arknightsmobs.server.entity.guerrillas.GuerrillasEntity;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.freefish.arknightsmobs.server.entity.special.EntityCameraShake;
import com.freefish.arknightsmobs.server.entity.special.EntityFallingBlock;
import com.freefish.arknightsmobs.server.entity.special.SOTFEntity;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;

public class GanRanZheZhiJi extends AbstractArrowEntity implements IAnimatable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public HashSet<LivingEntity> livingEntities = new HashSet<LivingEntity>();
    private static final DataParameter<Boolean> IS_CARRY = EntityDataManager.createKey(GanRanZheZhiJi.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Byte> LOYALTY_LEVEL = EntityDataManager.createKey(GanRanZheZhiJi.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> field_226571_aq_ = EntityDataManager.createKey(GanRanZheZhiJi.class, DataSerializers.BOOLEAN);
    private ItemStack thrownStack = new ItemStack(Items.TRIDENT);
    private boolean dealtDamage;
    public int returningTicks;
    private boolean isFirstOnGround;

    public GanRanZheZhiJi(EntityType<? extends GanRanZheZhiJi> type, World worldIn) {
        super(type, worldIn);
    }

    public GanRanZheZhiJi(World worldIn, LivingEntity thrower, ItemStack thrownStackIn,boolean isCarry) {
        super(EntityRegistry.GAN_RAN_ZHE_ZHI_JI.get(), thrower, worldIn);
        this.thrownStack = thrownStackIn.copy();
        this.dataManager.set(LOYALTY_LEVEL, (byte)EnchantmentHelper.getLoyaltyModifier(thrownStackIn));
        this.dataManager.set(field_226571_aq_, thrownStackIn.hasEffect());
        this.isFirstOnGround = false;
        this.getDataManager().set(IS_CARRY,isCarry);
    }

    @OnlyIn(Dist.CLIENT)
    public GanRanZheZhiJi(World worldIn, double x, double y, double z) {
        super(EntityRegistry.GAN_RAN_ZHE_ZHI_JI.get(), x, y, z, worldIn);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(LOYALTY_LEVEL, (byte)0);
        this.dataManager.register(field_226571_aq_, false);
        this.dataManager.register(IS_CARRY, false);
    }

    public void tick() {
        if (this.timeInGround > 4) {
            this.dealtDamage = true;
        }
        if(OnGround() && !isFirstOnGround&&getDataManager().get(IS_CARRY)){
            Vector3d vector3d = getPositionVec();
            EntityCameraShake.cameraShake(world, vector3d, 25, 0.1f, 0, 20);
            if(world.isRemote) {
                playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 3, 1F + rand.nextFloat() * 0.1F);
                for(int j = 0;j<8;j++) {
                    for (int i = 0; i < 36-j*4; i++) {
                        Vector3d bomb = vector3d.add(new Vector3d(4-j/2.0, j*0.5, 0).rotateYaw((float) Math.PI / (18-j*2) * i+rand.nextFloat()*0.1f));
                        world.addParticle(ParticleTypes.EXPLOSION, bomb.x, bomb.y, bomb.z, 1.0D, 0.1D, 0.0D);
                    }
                }
            }
            Entity shooter = getShooter();
            if(shooter instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) shooter;
                float damage = (float) living.getAttribute(Attributes.ATTACK_DAMAGE).getValue()/2;
                List<LivingEntity> livingEntities = world.getEntitiesWithinAABB(LivingEntity.class, getBoundingBox().grow(10));
                for (LivingEntity livingEntity : livingEntities) {
                    if (livingEntity.getDistanceSq(this) > 25 || Math.abs(livingEntity.getPosY() - getPosY()) > 3)
                        continue;
                    if(livingEntity instanceof GuerrillasEntity)
                        continue;
                    float d = 1.0f - (float) getDistanceSq(livingEntity)/25.0f;
                    Vector3d vector3d1 = livingEntity.getPositionVec().subtract(this.getPositionVec()).normalize();
                    livingEntity.setMotion(vector3d1.scale(d).add(0,0.5,0));
                    livingEntity.attackEntityFrom(DamageSource.causeTridentDamage(this,shooter), damage*d);
                }
            }

            for (int i1 = 1; i1 <= 10; i1++) {
                double spread = Math.PI * 2;
                int arcLen = MathHelper.ceil(i1 * spread * 2);
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = vector3d.x + vx * i1;
                    double pz = vector3d.z + vz * i1;
                    float factor = 1 - i1 / (float) 5;
                    int hitY = (int)(vector3d.y-0.5);
                    if (world.rand.nextBoolean()) {
                        int hitX = MathHelper.floor(px);
                        int hitZ = MathHelper.floor(pz);
                        BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                        BlockPos abovePos = new BlockPos(pos).up();
                        BlockState block = world.getBlockState(pos);
                        BlockState blockAbove = world.getBlockState(abovePos);
                        if (block.getMaterial() != Material.AIR && block.isNormalCube(world, pos) && !block.getBlock().hasTileEntity(block) && !blockAbove.getMaterial().blocksMovement()) {
                            EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityRegistry.FALLING_BLOCK.get(), world, block, (float) (0.4 + factor * 0.2));
                            fallingBlock.setPosition(hitX + 0.5, hitY + 1, hitZ + 0.5);
                            world.addEntity(fallingBlock);
                        }
                    }
                }
            }
            isFirstOnGround = true;
        }

        Entity entity = this.getShooter();
        if ((this.dealtDamage || this.getNoClip()) && entity != null) {
            int i = this.dataManager.get(LOYALTY_LEVEL);
            if (i > 0 && !this.shouldReturnToThrower()) {
                if (!this.world.isRemote && this.pickupStatus == PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1F);
                }

                this.remove();
            } else if (i > 0) {
                this.setNoClip(true);
                Vector3d vector3d = new Vector3d(entity.getPosX() - this.getPosX(), entity.getPosYEye() - this.getPosY(), entity.getPosZ() - this.getPosZ());
                this.setRawPosition(this.getPosX(), this.getPosY() + vector3d.y * 0.015D * (double)i, this.getPosZ());
                if (this.world.isRemote) {
                    this.lastTickPosY = this.getPosY();
                }

                double d0 = 0.05D * (double)i;
                this.setMotion(this.getMotion().scale(0.95D).add(vector3d.normalize().scale(d0)));
                if (this.returningTicks == 0) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returningTicks;
            }
        }
        if(this.ticksExisted > 200) {
            List<Entity> ridingEntitys = getPassengers();
            for(Entity entity1:ridingEntitys)
                entity1.stopRiding();
            if(getShooter() instanceof Patriot)
                this.remove();
        }

        super.tick();
    }

    private boolean shouldReturnToThrower() {
        Entity entity = this.getShooter();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    private boolean OnGround(){
        BlockState blockState = world.getBlockState(getPosition());
        return !(blockState.getBlock() instanceof AirBlock);
    }

    protected ItemStack getArrowStack() {
        return this.thrownStack.copy();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean func_226572_w_() {
        return this.dataManager.get(field_226571_aq_);
    }

    @Nullable
    protected EntityRayTraceResult rayTraceEntities(Vector3d startVec, Vector3d endVec) {
        return this.dealtDamage ? null : super.rayTraceEntities(startVec, endVec);
    }

    protected void onEntityHit(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
        float f = 20.0F;
        if(getShooter()instanceof Patriot)
            f = (float) ((Patriot)getShooter()).getAttributeValue(Attributes.ATTACK_DAMAGE)*1.2f;
        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity;
            if(entity instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) entity;
                ItemStack itemStackFromSlot = player.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
                if(player.isActiveItemStackBlocking()&&itemStackFromSlot.getItem() instanceof ShieldItem){
                    player.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                    player.resetActiveHand();
                }
            }
            f += EnchantmentHelper.getModifierForCreature(this.thrownStack, livingentity.getCreatureAttribute());
        }
        Entity entity1 = this.getShooter();
        DamageSource damagesource = DamageSource.causeTridentDamage(this, (Entity)(entity1 == null ? this : entity1));
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.ITEM_TRIDENT_HIT;

        if (entity instanceof LivingEntity) {
            LivingEntity livingentity1 = (LivingEntity)entity;
            if(!livingEntities.contains(livingentity1)&&getDataManager().get(IS_CARRY)) {
                entity.attackEntityFrom(damagesource, f);
                this.arrowHit(livingentity1);
                livingEntities.add(livingentity1);
                livingentity1.startRiding(this);
            }
        }
        float f1 = 1.0F;
        if (this.world instanceof ServerWorld && this.world.isThundering() && EnchantmentHelper.hasChanneling(this.thrownStack)) {
            BlockPos blockpos = entity.getPosition();
            if (this.world.canSeeSky(blockpos)) {
                LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.world);
                lightningboltentity.moveForced(Vector3d.copyCenteredHorizontally(blockpos));
                lightningboltentity.setCaster(entity1 instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity1 : null);
                this.world.addEntity(lightningboltentity);
                soundevent = SoundEvents.ITEM_TRIDENT_THUNDER;
                f1 = 5.0F;
            }
        }

        this.playSound(soundevent, f1, 1.0F);
    }

    protected SoundEvent getHitEntitySound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    public void onCollideWithPlayer(PlayerEntity entityIn) {
        Entity entity = this.getShooter();
        if (entity == null || entity.getUniqueID() == entityIn.getUniqueID()) {
            super.onCollideWithPlayer(entityIn);
        }
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("Trident", 10)) {
            this.thrownStack = ItemStack.read(compound.getCompound("Trident"));
        }
        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.dataManager.set(LOYALTY_LEVEL, (byte)EnchantmentHelper.getLoyaltyModifier(this.thrownStack));
        getDataManager().set(IS_CARRY,compound.getBoolean("isCarry"));
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("Trident", this.thrownStack.write(new CompoundNBT()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
        compound.putBoolean("isCarry", getDataManager().get(IS_CARRY));
    }

    public void func_225516_i_() {
        int i = this.dataManager.get(LOYALTY_LEVEL);
        if (this.pickupStatus != PickupStatus.ALLOWED || i <= 0) {
            super.func_225516_i_();
        }
    }

    protected float getWaterDrag() {
        return 0.99F;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
