package com.freefish.arknightsmobs.server.entity.guerrillas;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class GuerrillasLeaderEntity extends GuerrillasEntity{
    private List<GuerrillasMemberEntity> pack = new ArrayList<>();
    public GuerrillasLeaderEntity(EntityType<? extends CreatureEntity> p_i48575_1_, World p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
    }

    public void addMember(GuerrillasMemberEntity guerrillasMemberEntity) {
        pack.add(guerrillasMemberEntity);
    }

    public void removeMember(GuerrillasMemberEntity guerrillasMemberEntity) {
        pack.remove(guerrillasMemberEntity);
    }

    public List<GuerrillasMemberEntity> getPack() {
        return pack;
    }
}
