package com.freefish.arknightsmobs.server.entity.ai.guerrillasAi.patriot;

import com.freefish.arknightsmobs.server.entity.EntityRegistry;
import com.freefish.arknightsmobs.server.entity.ai.AnimationAI;
import com.freefish.arknightsmobs.server.entity.guerrillas.Patriot;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class RuinHeavyStompAnimationAI extends AnimationAI<Patriot> {
    public RuinHeavyStompAnimationAI(Patriot entity, Animation animation) {
        super(entity);
    }

    @Override
    public void tick() {
        entity.setMotion(0, entity.getMotion().y, 0);
        entity.renderYawOffset = entity.prevRenderYawOffset;
        double facingAngle = entity.rotationYaw;
        double maxDistance = 5d;
        World world = entity.world;

        int tick = entity.getAnimationTick();
        if(tick == 30||tick==55||tick==100||tick==140) {
            List<Entity> list = entity.getEntitiesNearby(Entity.class, tick/2.0);
            for (Entity target : list) {
                if (target.isOnGround()) {
                    float factor = 1;
                    if (target == this.entity || target instanceof EntityFallingBlock) {
                        continue;
                    }
                    float applyKnockbackResistance = 0;
                    if (target instanceof LivingEntity) {
                        target.attackEntityFrom(DamageSource.causeMobDamage(this.entity), 10);
                        applyKnockbackResistance = (float) ((LivingEntity) target).getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
                    }
                    double magnitude = world.rand.nextDouble() * 0.15 + 0.1;
                    float x = 0, y = 0, z = 0;
                    x += Math.cos(entity.renderYawOffset / 180.0 * Math.PI) * factor * magnitude * (1 - applyKnockbackResistance);
                    y += 0.1 * (1 - applyKnockbackResistance) + factor * 0.15 * (1 - applyKnockbackResistance);
                    z += Math.sin(entity.renderYawOffset / 180.0 * Math.PI) * factor * magnitude * (1 - applyKnockbackResistance);
                    target.setMotion(target.getMotion().add(x, y, z));
                    if (target instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) target).connection.sendPacket(new SEntityVelocityPacket(target));
                    }
                }
            }
            for (int i1 = 1; i1 <= 8 + tick/10; i1++) {
                double spread = Math.PI * 2;
                int arcLen = MathHelper.ceil(i1 * spread * 2);
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = entity.getPosX() + vx * i1;
                    double pz = entity.getPosZ() + vz * i1;
                    float factor = 1 - i1 / (float) maxDistance;
                    int hitY = MathHelper.floor(entity.getBoundingBox().minY - 0.5);
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
        return animation == Patriot.RUIN_HEAVY_STOMP;
    }
}
