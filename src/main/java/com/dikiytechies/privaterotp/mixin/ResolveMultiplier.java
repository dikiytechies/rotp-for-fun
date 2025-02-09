package com.dikiytechies.privaterotp.mixin;

import com.dikiytechies.privaterotp.capability.PrivateUtilCap;
import com.dikiytechies.privaterotp.capability.PrivateUtilCapProvider;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.StandUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StandUtil.class)
public abstract class ResolveMultiplier {
    @Inject(method = "addResolve", at = @At(value = "TAIL"), remap = false)
    private static void recalcResolve(IStandPower stand, LivingEntity target, float points, CallbackInfo ci) {
        LazyOptional<PrivateUtilCap> livingDataOptional = stand.getUser().getCapability(PrivateUtilCapProvider.CAPABILITY);
        points *= (livingDataOptional.map(PrivateUtilCap::getResolveMultiplier).isPresent()? livingDataOptional.map(PrivateUtilCap::getResolveMultiplier).get(): 1) - points;
        stand.getResolveCounter().addResolveOnAttack(points);
    }
}
