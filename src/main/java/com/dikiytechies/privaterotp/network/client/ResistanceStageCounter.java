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

public class ResistanceStageCounter {
    private final int entityId;
    private final int resistanceStage;
    public ResistanceStageCounter(int entityId, int resistanceStage) {
        this.entityId = entityId;
        this.resistanceStage = resistanceStage;
    }

    public static void encode(ResistanceStageCounter msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.resistanceStage);
    }

    public static ResistanceStageCounter decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        int resistanceStage = buf.readInt();
        return new ResistanceStageCounter(entityId, resistanceStage);
    }

    public static void handle(ResistanceStageCounter msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                LazyOptional<PrivateUtilCap> playerDataOptional = livingEntity.getCapability(PrivateUtilCapProvider.CAPABILITY);
                playerDataOptional.ifPresent(playerData -> {
                    playerData.setResistanceStage(msg.resistanceStage);
                });

            }
        });
        ctx.get().setPacketHandled(true);
    }
}
