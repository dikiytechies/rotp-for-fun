package com.dikiytechies.privaterotp.network.client;

import com.dikiytechies.privaterotp.command.BossificateCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayBossMusicPacket {
    private final int bossId;
    public PlayBossMusicPacket(int bossId) {
        this.bossId = bossId;
    }
    public static void encode(PlayBossMusicPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.bossId);
    }
    public static PlayBossMusicPacket decode(PacketBuffer buf) {
        int bossId = buf.readInt();
        return new PlayBossMusicPacket(bossId);
    }
    public static void handle(PlayBossMusicPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft.getInstance().getSoundManager().play(new BossificateCommand.BossMusic((PlayerEntity) Minecraft.getInstance().level.getEntity(msg.bossId)));
        });
        ctx.get().setPacketHandled(true);
    }
}
