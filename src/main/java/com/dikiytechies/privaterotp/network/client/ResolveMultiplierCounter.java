package com.dikiytechies.privaterotp.network.client;

import com.dikiytechies.privaterotp.capability.PrivateUtilCap;
import com.dikiytechies.privaterotp.capability.PrivateUtilCapProvider;
import com.github.standobyte.jojo.client.ClientUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ResolveMultiplierCounter {
    private final int entityId;
    private final int resolveMultiplier;
    public ResolveMultiplierCounter(int entityId, int resolveMultiplier) {
        this.entityId = entityId;
        this.resolveMultiplier = resolveMultiplier;
    }

    public static void encode(ResolveMultiplierCounter msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.resolveMultiplier);
    }

    public static ResolveMultiplierCounter decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        int resolveMultiplier = buf.readInt();
        return new ResolveMultiplierCounter(entityId, resolveMultiplier);
    }

    public static void handle(ResolveMultiplierCounter msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                LazyOptional<PrivateUtilCap> playerDataOptional = livingEntity.getCapability(PrivateUtilCapProvider.CAPABILITY);
                playerDataOptional.ifPresent(playerData -> {
                    playerData.setResolveMultiplier(msg.resolveMultiplier);
                });

            }
        });
        ctx.get().setPacketHandled(true);
    }
}
