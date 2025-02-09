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

public class HamonMultiplierCounter {
    private final int entityId;
    private final float hamonMultiplier;
    public HamonMultiplierCounter(int entityId, float hamonMultiplier) {
        this.entityId = entityId;
        this.hamonMultiplier = hamonMultiplier;
    }

    public static void encode(HamonMultiplierCounter msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeFloat(msg.hamonMultiplier);
    }

    public static HamonMultiplierCounter decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        float hamonMultiplier = buf.readFloat();
        return new HamonMultiplierCounter(entityId, hamonMultiplier);
    }

    public static void handle(HamonMultiplierCounter msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                LazyOptional<PrivateUtilCap> playerDataOptional = livingEntity.getCapability(PrivateUtilCapProvider.CAPABILITY);
                playerDataOptional.ifPresent(playerData -> {
                    playerData.setPointsMultiplier(msg.hamonMultiplier);
                });

            }
        });
        ctx.get().setPacketHandled(true);
    }
}
