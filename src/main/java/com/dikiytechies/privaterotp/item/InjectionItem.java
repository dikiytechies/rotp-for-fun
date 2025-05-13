package com.dikiytechies.privaterotp.item;

import com.dikiytechies.privaterotp.capability.PrivateUtilCap;
import com.dikiytechies.privaterotp.capability.PrivateUtilCapProvider;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class InjectionItem extends Item {
    private final InjectionMark mark;
    public InjectionItem(Properties properties, InjectionMark mark) {
        super(properties);
        this.mark = mark;
    }

    public enum InjectionMark {
        MK_1,
        MK_2,
        MK_3
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        switch(mark) {
            case MK_1:
                return chooseUpgrade(1, player, stack);
            case MK_2:
                return chooseUpgrade(2, player, stack);
            case MK_3:
                return chooseUpgrade(3, player, stack);
        }
        return ActionResult.fail(stack);
    }
    private static ActionResult<ItemStack> chooseUpgrade(int stage, PlayerEntity player, ItemStack stack) {
        if (player.getCapability(PrivateUtilCapProvider.CAPABILITY).map(PrivateUtilCap::getPointsMultiplier).get() == 1 + 0.33f * (stage - 1) && INonStandPower.getPlayerNonStandPower(player).getType() == ModPowers.HAMON.get()) {
            player.getCapability(PrivateUtilCapProvider.CAPABILITY).ifPresent(data -> data.setPointsMultiplier(1 + 0.33f * stage));
            if (!player.isCreative()) stack.shrink(1);
            return ActionResult.success(stack);
        } else if (player.getCapability(PrivateUtilCapProvider.CAPABILITY).map(PrivateUtilCap::getResistanceStage).get() == stage - 1 && JojoModUtil.isUndeadOrVampiric(player)) {
            player.getCapability(PrivateUtilCapProvider.CAPABILITY).ifPresent(data -> data.setResistanceStage(stage));
            if (!player.isCreative()) stack.shrink(1);
            return ActionResult.success(stack);
        } else if (player.getCapability(PrivateUtilCapProvider.CAPABILITY).map(PrivateUtilCap::getResolveMultiplier).get() == stage && IStandPower.getPlayerStandPower(player).getType() != null) {
            player.getCapability(PrivateUtilCapProvider.CAPABILITY).ifPresent(data -> data.setResolveMultiplier(stage + 1));
            if (!player.isCreative()) stack.shrink(1);
            return ActionResult.success(stack);
        }
        return ActionResult.fail(stack);
    }
}
