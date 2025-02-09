package com.dikiytechies.privaterotp.mixin;

import com.dikiytechies.privaterotp.capability.PrivateUtilCap;
import com.dikiytechies.privaterotp.capability.PrivateUtilCapProvider;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.vampirism.VampirismUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VampirismUtil.class)
public abstract class SunResistanceMixin {
    @Inject(method = "getSunDamage", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void resist(LivingEntity entity, CallbackInfoReturnable<Float> cir) {
        if (entity instanceof PlayerEntity && !((PlayerEntity) entity).abilities.instabuild && entity.getCapability(PrivateUtilCapProvider.CAPABILITY).map(PrivateUtilCap::getResistanceStage).get() > 0) {
            cir.setReturnValue(0.0f);
            World world = entity.level;
            if (
                    world.dimensionType().hasSkyLight()
                            && !world.dimensionType().hasCeiling()
                            && world.isDay()
                            && !world.isRaining()
                            && !world.isThundering()) {
                float brightness = entity.getBrightness();
                BlockPos blockPos = entity.getVehicle() instanceof BoatEntity ?
                        (new BlockPos(entity.getX(), (double)Math.round(entity.getY(1.0)), entity.getZ())).above()
                        : new BlockPos(entity.getX(), (double)Math.round(entity.getY(1.0)), entity.getZ());
                if (brightness > 0.5F && world.canSeeSky(blockPos)) {
                    INonStandPower.getPlayerNonStandPower((PlayerEntity) entity)
                            .addEnergy((entity.getCapability(PrivateUtilCapProvider.CAPABILITY).map(PrivateUtilCap::getResistanceStage).get() - 3) * 0.0625f);
                }
            }
            cir.cancel();
        }
    }
}
