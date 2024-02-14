package com.freefish.arknightsmobs.server.world.savedate;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class GuerrillasWorldData extends WorldSavedData {
    private static final String IDENTIFIER = "arknightsmobs_guerrilla";
    private World world;
    private boolean isSeePlayer;
    private boolean isWinOnce;
    private int existedNumber;

    public GuerrillasWorldData() {
        super(IDENTIFIER);
    }

    public GuerrillasWorldData(World world) {
        super(IDENTIFIER);
        this.world = world;
        //this.markDirty();
    }

    public static GuerrillasWorldData get(World world) {
        if (world instanceof ServerWorld) {
            ServerWorld overworld = world.getServer().getWorld(world.getDimensionKey());

            DimensionSavedDataManager storage = overworld.getSavedData();
            GuerrillasWorldData data = storage.getOrCreate(GuerrillasWorldData::new, IDENTIFIER);
            if (data != null) {
                data.world = world;
                data.markDirty();
            }
            return data;
        }
        //If the world is ClientLevel just return empty non significant data object
        return new GuerrillasWorldData();
    }

    @Override
    public void read(CompoundNBT nbt) {
        this.existedNumber = nbt.getInt("existedNumber");
        this.isSeePlayer = nbt.getBoolean("isSeePlayer");
        this.isWinOnce = nbt.getBoolean("isWinOnce");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("existedNumber", existedNumber);
        compound.putBoolean("isSeePlayer", isSeePlayer);
        compound.putBoolean("isWinOnce", isWinOnce);

        return compound;
    }

    public void setExistedNumber(int existedNumber) {
        this.existedNumber = existedNumber;
    }

    public int getExistedNumber() {
        return existedNumber;
    }

    public void setIsWinOnce(boolean isWinOnce) {
        this.isWinOnce = isWinOnce;
    }

    public boolean getIsWinOnce() {
        return isWinOnce;
    }
}
