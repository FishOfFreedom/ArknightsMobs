package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
import com.freefish.arknightsmobs.server.entity.special.EntityCameraShake;
import com.freefish.arknightsmobs.server.entity.special.EntityFallingBlock;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EnhanceAnimationAI extends AnimationAI<Patriot> {
    private Patriot patriot;

    public EnhanceAnimationAI(Patriot entity, Animation animation) {
        super(entity);
        patriot = entity;
    }

    @Override
    public void resetTask() {
        if(entity.getAnimation() == Patriot.ENHANCED_2)
            patriot.setIsEnhanced(false);
        super.resetTask();
    }

    @Override
    public void tick() {
        entity.setMotion(0, entity.getMotion().y, 0);
        entity.renderYawOffset = entity.prevRenderYawOffset;
        int tick = entity.getAnimationTick();
        World world = entity.world;
        double facingAngle = entity.rotationYaw;
        double maxDistance = 5d;
        if (entity.getAnimation() == Patriot.ENHANCED_2 && tick > 9 && tick < 17){
            int hitY = MathHelper.floor(entity.getBoundingBox().minY - 0.5);
            if (tick == 12) {
                entity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2, 1F + entity.getRNG().nextFloat() * 0.1F);
                EntityCameraShake.cameraShake(entity.world, entity.getPositionVec(), 25, 0.1f, 0, 20);
            }
            if (tick % 2 == 0) {
                int distance = tick / 2 - 2;
                double spread = Math.PI * 2;
                int arcLen = MathHelper.ceil(distance * spread);
                double minY = entity.getBoundingBox().minY;
                double maxY = entity.getBoundingBox().maxY;
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = entity.getPosX() + vx * distance;
                    double pz = entity.getPosZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    AxisAlignedBB selection = new AxisAlignedBB(px - 1.5, minY, pz - 1.5, px + 1.5, maxY, pz + 1.5);
                    List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, selection);
                    for (Entity entity : hit) {
                        if (entity.isOnGround()) {
                            if (entity == this.entity || entity instanceof EntityFallingBlock) {
                                continue;
                            }
                            float applyKnockbackResistance = 0;
                            if (entity instanceof LivingEntity) {
                                entity.attackEntityFrom(DamageSource.causeMobDamage(this.entity), (factor * 5 + 1));
                                applyKnockbackResistance = (float) ((LivingEntity) entity).getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
                            }
                            double magnitude = world.rand.nextDouble() * 0.15 + 0.1;
                            float x = 0, y = 0, z = 0;
                            x += vx * factor * magnitude * (1 - applyKnockbackResistance);
                            y += 0.1 * (1 - applyKnockbackResistance) + factor * 0.15 * (1 - applyKnockbackResistance);
                            z += vz * factor * magnitude * (1 - applyKnockbackResistance);
                            entity.setMotion(entity.getMotion().add(x, y, z));
                            if (entity instanceof ServerPlayerEntity) {
                                ((ServerPlayerEntity) entity).connection.sendPacket(new SEntityVelocityPacket(entity));
                            }
                        }
                    }
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
        }
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == Patriot.ENHANCED_2 || animation == Patriot.ENHANCED_1;
    }
}
