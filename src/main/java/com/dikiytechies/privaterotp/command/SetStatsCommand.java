package com.dikiytechies.privaterotp.command;

import com.dikiytechies.privaterotp.capability.PrivateUtilCapProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;

public class SetStatsCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("setextrastats").requires(ctx -> ctx.hasPermission(4))
                .executes(ctx -> setStats(ctx.getSource(), 0))
                    .then(Commands.argument("level", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                .executes(ctx -> setStats(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "level"))))
        );
    }

    private static int setStats(CommandSource source, int level) throws CommandSyntaxException {
        if (!source.getLevel().isClientSide()) {
            PlayerEntity player = source.getPlayerOrException();
            player.getCapability(PrivateUtilCapProvider.CAPABILITY).ifPresent(data -> {
                data.setResolveMultiplier(level + 1);
                data.setResistanceStage(level);
                data.setPointsMultiplier(1.0f + level * 0.33f);
            });
        }
        return 0;
    }
}
