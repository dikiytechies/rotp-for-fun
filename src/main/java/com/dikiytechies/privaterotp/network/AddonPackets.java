package com.dikiytechies.privaterotp.network;

import com.dikiytechies.privaterotp.AddonMain;
import com.dikiytechies.privaterotp.network.client.HamonMultiplierCounter;
import com.dikiytechies.privaterotp.network.client.PlayBossMusicPacket;
import com.dikiytechies.privaterotp.network.client.ResistanceStageCounter;
import com.dikiytechies.privaterotp.network.client.ResolveMultiplierCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import javax.annotation.Nullable;
import java.util.Optional;

public class AddonPackets {
    private static final String PROTOCOL_VERSION = "1";
    private static SimpleChannel channel;

    public static void init() {
        channel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(AddonMain.MOD_ID, "channel"))
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();

        int packetIndex = 0;
        // Whenever you add a new network packet, you have to register it here like so:
        channel.registerMessage(packetIndex++, HamonMultiplierCounter.class,
                HamonMultiplierCounter::encode, HamonMultiplierCounter::decode, HamonMultiplierCounter::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        channel.registerMessage(packetIndex++, ResistanceStageCounter.class,
                ResistanceStageCounter::encode, ResistanceStageCounter::decode, ResistanceStageCounter::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        channel.registerMessage(packetIndex++, ResolveMultiplierCounter.class,
                ResolveMultiplierCounter::encode, ResolveMultiplierCounter::decode, ResolveMultiplierCounter::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        channel.registerMessage(packetIndex++, PlayBossMusicPacket.class,
                PlayBossMusicPacket::encode, PlayBossMusicPacket::decode, PlayBossMusicPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }



    public static void sendToServer(Object msg) {
        channel.sendToServer(msg);
    }

    public static void sendToClient(Object msg, ServerPlayerEntity player) {
        if (!(player instanceof FakePlayer)) {
            channel.send(PacketDistributor.PLAYER.with(() -> player), msg);
        }
    }

    public static void sendToClientsTracking(Object msg, Entity entity) {
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
    }

    public static void sendToClientsTrackingAndSelf(Object msg, Entity entity) {
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
    }
    public static void sendGlobally(Object msg, @Nullable RegistryKey<World> dimension) {
        if (dimension != null) {
            channel.send(PacketDistributor.DIMENSION.with(() -> dimension), msg);
        }
        else {
            channel.send(PacketDistributor.ALL.noArg(), msg);
        }
    }
}
