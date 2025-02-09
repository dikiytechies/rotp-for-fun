package com.dikiytechies.privaterotp.command;

import com.dikiytechies.privaterotp.init.InitSounds;
import com.dikiytechies.privaterotp.util.GameplayUtil;
import com.github.standobyte.jojo.JojoModConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

public class BossificateCommand {
    private static final ServerBossInfo bossInfo = new ServerBossInfo(new StringTextComponent("boss"),
            BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_10);
    private static boolean isInited = false;
    private static PlayerEntity boss;
    private static double standDamageOld;
    private static double hamonDamageOld;
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("bossificate").requires(ctx -> ctx.hasPermission(4))
                .executes(ctx -> bossificate(ctx.getSource()))
        );
    }

    private static int bossificate(CommandSource source) throws CommandSyntaxException {
        boss = source.getPlayerOrException();
        if (!boss.level.isClientSide()) {
            isInited = false;
            bossInfo.setName(boss.getName());
            bossInfo.setVisible(true);
            standDamageOld = JojoModConfig.getCommonConfigInstance(false).standDamageMultiplier.get();
            hamonDamageOld = JojoModConfig.getCommonConfigInstance(false).standDamageMultiplier.get();
            JojoModConfig.getCommonConfigInstance(false).standDamageMultiplier.set(0.5 * standDamageOld);
            JojoModConfig.getCommonConfigInstance(false).hamonDamageMultiplier.set(0.5 * hamonDamageOld);
            boss.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(new AttributeModifier("booss :o", 2,AttributeModifier.Operation.MULTIPLY_TOTAL));
            boss.setHealth(boss.getMaxHealth());
            GameplayUtil.setBarStage(0);
            Minecraft.getInstance().getSoundManager().play(new BossMusic(boss));
            for (ServerPlayerEntity players: boss.level.getEntitiesOfClass(ServerPlayerEntity.class, new AxisAlignedBB(boss.blockPosition()))) {
                bossInfo.addPlayer(players);
            }
        }
        return 0;
    }
    public static void revertDamageOld() {
        JojoModConfig.getCommonConfigInstance(false).standDamageMultiplier.set(standDamageOld);
        JojoModConfig.getCommonConfigInstance(false).hamonDamageMultiplier.set(hamonDamageOld);
    }
    public static ServerBossInfo getBossInfo() { return bossInfo; }
    public static PlayerEntity getBoss() { return boss; }
    public static void setBoss(PlayerEntity player) { boss = player; }
    public static void setBossValue(float p) { bossInfo.setPercent(p); }
    public static boolean getBossInit() { return isInited; }
    public static void setBossInited(boolean isInited) { BossificateCommand.isInited = isInited; }
    @OnlyIn(Dist.CLIENT)
    private static class BossMusic extends TickableSound {
        PlayerEntity boss;
        public BossMusic(PlayerEntity boss) {
            super(InitSounds.TRIPLET.get(), SoundCategory.RECORDS);
            this.boss = boss;
            this.looping = true;
        }

        @Override
        public void tick() {
            if (!boss.isAlive()) {
                stop();
            }
        }
    }
}
