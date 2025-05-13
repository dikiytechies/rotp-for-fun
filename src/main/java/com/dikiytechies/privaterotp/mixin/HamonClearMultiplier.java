package com.dikiytechies.privaterotp.mixin;

import com.dikiytechies.privaterotp.capability.PrivateUtilCapProvider;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonData;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonPowerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(HamonPowerType.class)
public abstract class HamonClearMultiplier extends NonStandPowerType<HamonData> {

    public HamonClearMultiplier(Action<INonStandPower>[] startingAttacks, Action<INonStandPower>[] startingAbilities, Action<INonStandPower> defaultQuickAccess, Supplier<HamonData> dataFactory) {
        super(startingAttacks, startingAbilities, defaultQuickAccess, dataFactory);
    }

    @Inject(method = "onClear", at = @At(value = "HEAD"), remap = false)
    public void clearMultiplier(INonStandPower power, CallbackInfo ci) {
        power.getUser().getCapability(PrivateUtilCapProvider.CAPABILITY).ifPresent(data -> data.setPointsMultiplier(1.0f));
    }
}
