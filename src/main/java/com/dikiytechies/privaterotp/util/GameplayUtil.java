package com.dikiytechies.privaterotp.util;

import com.dikiytechies.privaterotp.AddonMain;
import com.dikiytechies.privaterotp.command.BossificateCommand;
import com.dikiytechies.privaterotp.command.SetStatsCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID)
public class GameplayUtil {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        BossificateCommand.register(dispatcher);
        SetStatsCommand.register(dispatcher);
    }
    private static float barStage = 0.0f;
    public static void setBarStage(float i) {
        barStage = i;
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (!player.level.isClientSide()) {
            if (BossificateCommand.getBoss() != null && BossificateCommand.getBoss().equals(player)) {
                if (!player.isAlive()) {
                    BossificateCommand.setBoss(null);
                    BossificateCommand.getBossInfo().removeAllPlayers();
                    BossificateCommand.getBossInfo().setVisible(false);
                    BossificateCommand.revertDamageOld();
                } else {
                    if (BossificateCommand.getBossInit()) {
                        BossificateCommand.setBossValue(1.0f / (player.getMaxHealth() / player.getHealth()));
                    } else { //animate
                        if (barStage < 1.0f / (player.getMaxHealth() / player.getHealth())) {
                            BossificateCommand.setBossValue(Math.min(barStage, 1.0f / (player.getMaxHealth() / player.getHealth())));
                            barStage += 0.005f;
                        } else {
                            BossificateCommand.setBossInited(true);
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void FMLServerStoppedEvent(FMLServerStoppedEvent event) {
        if (BossificateCommand.getBoss() != null) BossificateCommand.revertDamageOld();
    }
}
