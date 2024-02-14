package com.freefish.arknightsmobs.server.entity.guerrillas;

import com.freefish.arknightsmobs.server.entity.FreeFishEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GuerrillasEntity extends FreeFishEntity {

    public GuerrillasEntity(EntityType<? extends CreatureEntity> p_i48575_1_, World p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
