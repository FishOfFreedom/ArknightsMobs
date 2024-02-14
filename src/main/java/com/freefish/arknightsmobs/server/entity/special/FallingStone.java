package com.freefish.arknightsmobs.server.entity.special;


import com.freefish.arknightsmobs.client.particle.ParticleHandler;
import com.freefish.arknightsmobs.client.particle.ParticleRing;
import com.freefish.arknightsmobs.client.particle.WindParticle;
import com.freefish.arknightsmobs.client.particle.util.AdvancedParticleBase;
import com.freefish.arknightsmobs.client.particle.util.ParticleComponent;
import com.freefish.arknightsmobs.client.particle.util.RibbonComponent;
import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class FallingStone extends MobEntity implements IAnimatable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private Vector3d[] trailPositions = new Vector3d[64];
    private int trailPointer = -1;
    private int index = 0;
    private Entity owner;
    private Vector3d[] vector3ds = new Vector3d[]{new Vector3d(2,0,2),new Vector3d(2,0,-2),new Vector3d(-2,0,-2),new Vector3d(-2,0,2)};

    public FallingStone(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
    }
    public FallingStone(EntityType<? extends MobEntity> type, World worldIn ,Entity entity) {
        super(type, worldIn);
        this.owner = entity;
    }

    @Override
    public void tick() {
        super.tick();
        tickTrail(getPositionVec());
        Vector3d vector3d = getMotion();
        if(vector3d.length()>= 0.8)
            setMotion(vector3d.scale(0.4));
        if(world.isRemote && ticksExisted%8 == 0){
            world.addParticle(new ParticleRing.RingData(0f, (float)Math.PI/2f, 17, 0.8f, 0.4f, 0.1f, 1f, 60f, false, ParticleRing.EnumRingBehavior.GROW), getPosX(), getPosY() + 0.2f, getPosZ(), 0, 0, 0);
        }
        if(world.isRemote && ticksExisted%2 == 0){
            Vector3d vector3d1 = new Vector3d( this.prevPosX, this.prevPosY,this.prevPosZ);
            Vector3d vector3d2 =getPositionVec();
            for (int i = 0;i<4;i++){
                if(index == 4) index =0;
                int in = i-index;
                vector3d1 = vector3d1.add(vector3ds[in&3]);
                vector3d2 = vector3d2.add(vector3ds[in-1&3]);
                AdvancedParticleBase.spawnParticle(world, ParticleHandler.ARROW_HEAD.get(), vector3d1.x, vector3d1.y, vector3d1.z, 0, 0, 0, false, 0, 0, 0, 0, 2f, 0.8f, 0.4f, 0.05f, 0.75, 1, 2, true, false, new ParticleComponent[]{
                        new ParticleComponent.Attractor(new Vector3d[]{new Vector3d(vector3d2.x,vector3d2.y,vector3d2.z)}, 0.5f, 0.2f, ParticleComponent.Attractor.EnumAttractorBehavior.LINEAR),
                        new RibbonComponent(ParticleHandler.RIBBON_FLAT.get(), 4, 0, 0, 0, 0.6F, 0.8f, 0.4f, 0.05f, 0.75, true, true, new ParticleComponent[]{
                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0))
                        }),
                        new ParticleComponent.FaceMotion(),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{0, 0, 1}, new float[]{0, 0.05f, 0.06f}), false),
                });
            }
            index++;
        }
    }

    public void tickTrail(Vector3d currentPosition) {
        if (trailPointer == -1) {
            for (int i = 0; i < trailPositions.length; i++) {
                trailPositions[i] = currentPosition;
            }
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = currentPosition;
    }

    public Vector3d getTrailPosition(int pointer, float partialTick) {
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vector3d d0 = this.trailPositions[j];
        Vector3d d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale(partialTick));
    }

    @Override
    public void onDeath(DamageSource cause) {
        if(owner!=null){
            //EntityCameraBomb.cameraShake(world,owner.getPositionVec(),10,100,10);
            EntityCameraShake.cameraShake(world,getPositionVec(),10,100,50,100);
        }
        if (world.isRemote) {
            float scale = 8.2f;
            for (int i = 0; i < 15; i++) {
                float phaseOffset = rand.nextFloat();
                AdvancedParticleBase.spawnParticle(world, ParticleHandler.ARROW_HEAD.get(), getPosX(), getPosY(), getPosZ(), 0, 0, 0, false, 0, 0, 0, 0, 8F, 0.95, 0.9, 0.35, 1, 1, 30, true, true, new ParticleComponent[]{
                        new ParticleComponent.Orbit(new Vector3d[]{getPositionVec().add(0, getHeight() / 2, 0)}, ParticleComponent.KeyTrack.startAndEnd(0 + phaseOffset, 1.6f + phaseOffset), new ParticleComponent.KeyTrack(
                                new float[]{0.2f * scale, 0.63f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, 1f * scale},
                                new float[]{0, 0.15f, 0.3f, 0.45f, 0.6f, 0.75f}
                        ), ParticleComponent.KeyTrack.startAndEnd(rand.nextFloat() * 2 - 1, rand.nextFloat() * 2 - 1), ParticleComponent.KeyTrack.startAndEnd(rand.nextFloat() * 2 - 1, rand.nextFloat() * 2 - 1), ParticleComponent.KeyTrack.startAndEnd(rand.nextFloat() * 2 - 1, rand.nextFloat() * 2 - 1), false),
                        new RibbonComponent(ParticleHandler.RIBBON_FLAT.get(), 10, 0, 0, 0, 0.2F, 0.95, 0.9, 0.35, 1, true, true, new ParticleComponent[]{
                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0)),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1, 0), false)
                        }),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1, 0), false),
                        new ParticleComponent.FaceMotion()
                });
            }
        }
        super.onDeath(cause);
    }

    public boolean hasTrail() {
        return trailPointer != -1;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "Controller", 3, this::predicate));
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("i", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
        return PlayState.CONTINUE;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
