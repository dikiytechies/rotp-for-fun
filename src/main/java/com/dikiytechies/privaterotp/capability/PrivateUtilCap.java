package com.dikiytechies.privaterotp.capability;

import com.dikiytechies.privaterotp.network.AddonPackets;
import com.dikiytechies.privaterotp.network.client.HamonMultiplierCounter;
import com.dikiytechies.privaterotp.network.client.ResistanceStageCounter;
import com.dikiytechies.privaterotp.network.client.ResolveMultiplierCounter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class PrivateUtilCap implements INBTSerializable<CompoundNBT> {
    private final LivingEntity livingEntity;
    private float pointsMultiplier = 1.0f;
    private int resolveMultiplier = 1;
    private int resistanceStage = 0;
    public PrivateUtilCap(LivingEntity livingEntity) { this.livingEntity = livingEntity; }

    public float getPointsMultiplier() {
        return pointsMultiplier;
    }
    public int getResolveMultiplier() { return resolveMultiplier; }
    public int getResistanceStage() { return resistanceStage; }
    public void setPointsMultiplier(float value) {
        this.pointsMultiplier = value;
        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;
            AddonPackets.sendToClient(new HamonMultiplierCounter(livingEntity.getId(), pointsMultiplier), player);
        }
    }
    public void setResistanceStage(int value) {
        this.resistanceStage = value;
        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;
            AddonPackets.sendToClient(new ResistanceStageCounter(livingEntity.getId(), resistanceStage), player);
        }
    }
    public void setResolveMultiplier(int value) {
        this.resolveMultiplier = value;
        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;
            AddonPackets.sendToClient(new ResolveMultiplierCounter(livingEntity.getId(), resolveMultiplier), player);
        }
    }

    public void onClone(PrivateUtilCap old) {
        this.pointsMultiplier = old.pointsMultiplier;
        this.resolveMultiplier = old.resolveMultiplier;
        this.resistanceStage = old.resistanceStage;
    }

    // Sync all the data that should be available to all players
    public void syncWithAnyPlayer(ServerPlayerEntity player) {
    }

    // Sync all the data that only this player needs to know
    public void syncWithEntityOnly(ServerPlayerEntity player) {
        AddonPackets.sendToClient(new HamonMultiplierCounter(livingEntity.getId(), pointsMultiplier), player);
    }
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("HamonPointsMultiplier", pointsMultiplier);
        nbt.putInt("ResolveMultiplier", resolveMultiplier);
        nbt.putInt("ResistanceStage", resistanceStage);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        pointsMultiplier = nbt.getFloat("HamonPointsMultiplier");
        resolveMultiplier = nbt.getInt("ResolveMultiplier");
        resistanceStage = nbt.getInt("ResistanceStage");
    }
}
