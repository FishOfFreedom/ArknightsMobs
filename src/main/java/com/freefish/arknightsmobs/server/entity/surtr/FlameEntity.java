package com.freefish.arknightsmobs.server.entity.surtr;

import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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

public class FlameEntity extends Entity implements IAnimatable {
	
	public int lifeTime = 0;
	
	public Entity summonedEntity = null;

	public AnimationFactory factory = GeckoLibUtil.createFactory(this);
	
    public FlameEntity(World worldIn) {
        super(EntityRegistry.FLAME.get(), worldIn);
    }
    
	public FlameEntity(EntityType<?> p_i48580_1_, World p_i48580_2_) {
		super(p_i48580_1_, p_i48580_2_);
	}

	@Override
	protected void registerData() {

	}

	@Override
	public void baseTick() {
		super.baseTick();
		
		this.lifeTime ++;
		
		if (!this.world.isRemote && this.lifeTime == 10 && this.summonedEntity != null) {
			summonedEntity.setPosition(this.entityBlockPosition.getX(), 0.0F, 0.0F);
			//summonedEntity.set(this.rand.nextInt(360));
			((ServerWorld)this.world).addEntity(summonedEntity);
		}
		
		if (!this.world.isRemote && this.lifeTime >= 6) {
			this.remove();
		}
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
	
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 1, this::predicate));
    }


    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
    		event.getController().setAnimation(new AnimationBuilder().addAnimation("flame", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

}
