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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public class RaiseAttackAnimationAI extends AnimationAI<Patriot> {
    public RaiseAttackAnimationAI(Patriot entity, Animation animation) {
        super(entity);
    }

    @Override
    public void tick() {
        LivingEntity entityTarget = entity.getAttackTarget();
        int tick = entity.getAnimationTick();
        World world = entity.world;
        double perpFacing = entity.renderYawOffset * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        int hitY = MathHelper.floor(entity.getBoundingBox().minY - 0.5);
        final int maxDistance = 6;

        double arc = 220d;
        double range = 6;
        Vector3d motion= new Vector3d(0, entity.getMotion().y, 0);
        if(entityTarget != null&&entity.getDistanceSq(entityTarget)>4) {
            if (entity.getAnimationTick() <= 20) {
                motion = new Vector3d(0, entity.getMotion().y, 0.2).rotateYaw((float) (-entity.renderYawOffset / 180 * Math.PI));
            }
        }
        entity.setMotion(motion);
        if(tick==15) entity.isCanBeAttacking=true;
        else if(tick==65) entity.isCanBeAttacking=false;

        float damage = (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        if (tick < 40 && entityTarget != null) {
            entity.faceEntity(entityTarget, 30F, 30F);
        } else {
            entity.renderYawOffset = entity.prevRenderYawOffset;
        }
        if (tick == 46) {
            entity.doRangeAttack(range, arc, damage * 1.5f,true);
            entity.timeSinceThrow+=20;
            entity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 3, 1F + entity.getRNG().nextFloat() * 0.1F);
            EntityCameraShake.cameraShake(entity.world, entity.getPositionVec(), 25, 0.1f, 0, 20);
        }
        if(tick >= 46 && tick <= 66){
            if (tick % 2 == 0) {
                int distance = (tick - 44) / 2 - 1;
                double spread = Math.PI;
                int arcLen = MathHelper.ceil(distance * spread * 2);
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = entity.getPosX() + vx * distance;
                    double pz = entity.getPosZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
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
        return animation == Patriot.RAISE_ATTACK;
    }
}
