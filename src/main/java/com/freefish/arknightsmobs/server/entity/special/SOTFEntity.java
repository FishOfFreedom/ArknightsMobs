package com.freefish.arknightsmobs.server.entity.special;

import com.freefish.arknightsmobs.client.particle.ParticleHandler;
import com.freefish.arknightsmobs.client.particle.util.AdvancedParticleBase;
import com.freefish.arknightsmobs.client.particle.util.ParticleComponent;
import com.freefish.arknightsmobs.client.particle.util.RibbonComponent;
import com.freefish.arknightsmobs.client.render.model.tools.Parabola;
import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import com.freefish.arknightsmobs.server.entity.ganranzhezhiji.GanRanZheZhiJi;
import com.freefish.arknightsmobs.server.item.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.List;

public class SOTFEntity extends EntityMagicEffect{
    int i = 0;
    LivingEntity target;
    private final Parabola parabola = new Parabola();
    ItemStack itemStack;
    private static final DataParameter<Float> TARGET_POSX = EntityDataManager.createKey(SOTFEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> TARGET_POSY = EntityDataManager.createKey(SOTFEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> TARGET_POSZ = EntityDataManager.createKey(SOTFEntity.class, DataSerializers.FLOAT);

    public SOTFEntity(EntityType<? extends EntityMagicEffect> type, World worldIn) {
        super(type, worldIn);
    }

    public SOTFEntity(World world, LivingEntity caster) {
        this(EntityRegistry.SOTF_ENTITY.get(), world);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
        setPositionAndRotation(caster.getPosX(),caster.getPosY(),caster.getPosZ(),caster.rotationYaw,caster.rotationPitch);
    }

    public SOTFEntity(World worldIn, LivingEntity entityLiving, ItemStack stack) {
        this(worldIn,entityLiving);
        itemStack = stack;

        List<LivingEntity> livingEntityList = world.getEntitiesWithinAABB(LivingEntity.class,getBoundingBox().grow(30),(entity)->{
            double v = Math.atan2(entityLiving.getPosZ() - entity.getPosZ(), entityLiving.getPosX() - entity.getPosX()) * (180 / Math.PI) + 90;
            boolean b = Math.abs(MathHelper.wrapDegrees(v - entityLiving.rotationYaw)) < 35.0D;
            return entity != entityLiving && b;
        });
        if(!livingEntityList.isEmpty()) target = livingEntityList.get(0);
        for(LivingEntity livingEntity:livingEntityList){
            double health = livingEntity.getHealth();
            double health1 = target.getHealth();
            if(health>health1) target = livingEntity;
        }
        if(target!=null){
            worldIn.playMovingSound((PlayerEntity) null, this, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
            playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 3, 1F + rand.nextFloat() * 0.1F);
            if(worldIn.isRemote){
                for(int i =0;i<6;i++){
                    double yaw = Math.PI*i/3;
                    Vector3d vector3d = entityLiving.getPositionVec().add(new Vector3d(Math.cos(yaw),0,Math.sin(yaw)));
                    worldIn.addParticle(ParticleTypes.EXPLOSION, vector3d.x, vector3d.y, vector3d.z, 1.0D, 0.1D, 0.0D);
                }
            }
            if (!((PlayerEntity)entityLiving).abilities.isCreativeMode) {
                ((PlayerEntity)entityLiving).inventory.deleteStack(stack);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(target!=null)
            setTargetPos(new Vector3f(target.getPositionVec()));
        if (ticksExisted == 2 || ticksExisted ==25) {
            parabola.mathParabola(caster, getTargetPos());
        }
        if(this.world.isRemote&&ticksExisted<=34){
            double i = ((double)ticksExisted) / 34;
            double i1 = ((double)ticksExisted+1) / 34;
            double posX = getPosX() + parabola.getX() * i;
            double posZ = getPosZ() + parabola.getZ() * i;
            double targetPosX = getPosX() + parabola.getX() * i1;
            double targetPosZ = getPosZ() + parabola.getZ() * i1;
            double x3 = parabola.getX2() * i;
            double targetX3 = parabola.getX2() * i1;
            double posY = getPosY() + parabola.getY(x3);
            double targetPosY = getPosY() + parabola.getY(targetX3);
            Vector3d particleMotion = new Vector3d(posX-targetPosX, posY-targetPosY,posZ-targetPosZ);
            int smokeNumber = 4 + rand.nextInt(5);
            for(int j =1;j<=smokeNumber;j++){
                double speed = 0.5 + rand.nextDouble();
                Vector3d newParticleMotion = particleMotion.rotateYaw((float)(Math.PI*Math.cos(2*i/smokeNumber*Math.PI)/6)).rotatePitch((float)(Math.PI*Math.sin(2*i/smokeNumber*Math.PI)/6)).mul(speed,speed,speed);
                world.addParticle(ParticleTypes.SMOKE, posX,posY,posZ,newParticleMotion.x,newParticleMotion.y,newParticleMotion.z);
            }
            AdvancedParticleBase.spawnParticle(world, ParticleHandler.ARROW_HEAD.get(), posX, posY, posZ, 0, 0, 0, false, 0, 0, 0, 0, 40f, 1, 1, 1, 0.75, 1, 2, true, false, new ParticleComponent[]{
                    new ParticleComponent.Attractor(new Vector3d[]{new Vector3d(targetPosX,targetPosY,targetPosZ)}, 0.5f, 0.2f, ParticleComponent.Attractor.EnumAttractorBehavior.LINEAR),
                    new RibbonComponent(ParticleHandler.RIBBON_FLAT.get(), 10, 0, 0, 0, 0.8F, 1, 1, 1, 0.75, true, true, new ParticleComponent[]{
                            new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0))
                    }),
                    new ParticleComponent.FaceMotion(),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{0, 0, 1}, new float[]{0, 0.05f, 0.06f}), false),
            });
        }
        if (!world.isRemote&&target!=null&&ticksExisted == 26) {
            double i = ((double)ticksExisted) / 34;
            double x3 = parabola.getX2() * i;
            double posX = getPosX() + parabola.getX() * i;
            double posZ = getPosZ() + parabola.getZ() * i;
            double posY = getPosY() + parabola.getY(x3);
            Vector3d vector3d = new Vector3d(target.getPosX() - posX,target.getPosY() -posY,target.getPosZ() -posZ);
            float f = MathHelper.sqrt(horizontalMag(vector3d));
            float yaw = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
            float pitch = (float)(MathHelper.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI));
            GanRanZheZhiJi ganRanZheZhiJi = new GanRanZheZhiJi(world, caster, itemStack == null?new ItemStack(ItemRegistry.GAN_RAN_ZHE_ZHI_JI_ITEM.get()):itemStack,true);
            ganRanZheZhiJi.setPositionAndRotation(getPosX() + parabola.getX()*i,getPosY() + parabola.getY(x3),getPosZ() + parabola.getZ()*i, -pitch, yaw);
            ganRanZheZhiJi.setDirectionAndMovement(this, -pitch, -yaw, 0.0F, 6F, 0f);
            ganRanZheZhiJi.setNoGravity(true);
            if(caster instanceof PlayerEntity) {
                if (((PlayerEntity)caster).abilities.isCreativeMode) {
                    ganRanZheZhiJi.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                }
            }
            world.addEntity(ganRanZheZhiJi);
        }
        if(ticksExisted>35) remove();
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(TARGET_POSX, 0f);
        this.dataManager.register(TARGET_POSY, 0f);
        this.dataManager.register(TARGET_POSZ, 0f);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        Vector3f vector3f = getTargetPos();
        compound.putFloat("targetPosX",vector3f.getX());
        compound.putFloat("targetPosY",vector3f.getY());
        compound.putFloat("targetPosZ",vector3f.getZ());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        float x = compound.getFloat("targetPosX");
        float y = compound.getFloat("targetPosY");
        float z = compound.getFloat("targetPosZ");
        setTargetPos(new Vector3f(x,y,z));
    }

    public void setTargetPos(Vector3f vector3f) {
        getDataManager().set(TARGET_POSX, vector3f.getX());
        getDataManager().set(TARGET_POSY, vector3f.getY()+1);
        getDataManager().set(TARGET_POSZ, vector3f.getZ());
    }

    public Vector3f getTargetPos() {
        float x = getDataManager().get(TARGET_POSX);
        float y = getDataManager().get(TARGET_POSY);
        float z = getDataManager().get(TARGET_POSZ);
        return new Vector3f(x,y,z);
    }
}
