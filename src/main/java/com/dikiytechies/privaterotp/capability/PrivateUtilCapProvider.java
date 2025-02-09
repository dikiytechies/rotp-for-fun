package com.dikiytechies.privaterotp.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PrivateUtilCapProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(PrivateUtilCap.class)
    public static Capability<PrivateUtilCap> CAPABILITY = null;
    private LazyOptional<PrivateUtilCap> instance;

    public PrivateUtilCapProvider(PlayerEntity player) {
        this.instance = LazyOptional.of(() -> new PrivateUtilCap(player));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Player capability LazyOptional is not attached.")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Player capability LazyOptional is not attached.")), null, nbt);
    }
}
