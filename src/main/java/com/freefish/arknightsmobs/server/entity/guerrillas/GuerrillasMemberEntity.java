package com.freefish.arknightsmobs.server.entity.guerrillas;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

import java.util.List;

public abstract class GuerrillasMemberEntity extends GuerrillasEntity{
    private GuerrillasLeaderEntity Leader;
    private boolean isAttacking = false;

    public GuerrillasMemberEntity(EntityType<? extends CreatureEntity> p_i48575_1_, World p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1,new FollowLeaderGoal(Leader, this));
        super.registerGoals();
    }

    @Override
    public void tick() {
        if(Leader == null) {
            Leader = getLeader();
            if(Leader != null)
                Leader.addMember(this);
        }
        else {
            setAttackTarget(Leader.getAttackTarget());
        }
        super.tick();

    }

    @Override
    public void remove() {
        if(Leader != null)
            Leader.removeMember(this);
        super.remove();
    }

    private GuerrillasLeaderEntity getLeader() {
        List<GuerrillasLeaderEntity> leader = world.getEntitiesWithinAABB(GuerrillasLeaderEntity.class, getBoundingBox().grow(32, 32, 32));
        for(GuerrillasLeaderEntity entity: leader) {
            double d1 = getDistanceSq(leader.get(0));
            double d2 = getDistanceSq(entity);
            if(d1 > d2)
                leader.set(0, entity);
        }
        if (leader.isEmpty())
            return null;
        else
            return leader.get(0);
    }

    public boolean hasLeader() {
        return Leader != null;
    }

    public boolean isAttacking() { return isAttacking;}

    public void setAttacking(boolean isAttacking) {this.isAttacking = isAttacking;}
}
