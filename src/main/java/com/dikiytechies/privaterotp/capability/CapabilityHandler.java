package com.dikiytechies.privaterotp.capability;

import com.dikiytechies.privaterotp.AddonMain;
import com.github.standobyte.jojo.JojoMod;
import com.github.standobyte.jojo.power.IPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID)
public class CapabilityHandler {
    private static final ResourceLocation PRIVATE_UTIL_CAP = new ResourceLocation(AddonMain.MOD_ID, "private_util");

    public static void commonSetupRegister() {
        CapabilityManager.INSTANCE.register(
                PrivateUtilCap.class,
                new Capability.IStorage<PrivateUtilCap>() {
                    @Override public INBT writeNBT(Capability<PrivateUtilCap> capability, PrivateUtilCap instance, Direction side) { return instance.serializeNBT(); }
                    @Override public void readNBT(Capability<PrivateUtilCap> capability, PrivateUtilCap instance, Direction side, INBT nbt) { instance.deserializeNBT((CompoundNBT) nbt); }
                },
                () -> new PrivateUtilCap(null));
    }
    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            event.addCapability(PRIVATE_UTIL_CAP, new PrivateUtilCapProvider(player));
        }
    }

    @SubscribeEvent
    public static void syncWithNewPlayer(PlayerEvent.StartTracking event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity original = event.getOriginal();
        PlayerEntity player = event.getPlayer();
        original.getCapability(PrivateUtilCapProvider.CAPABILITY).ifPresent((oldCap) -> {
            player.getCapability(PrivateUtilCapProvider.CAPABILITY).ifPresent((newCap) -> {
                newCap.onClone(oldCap);
            });
        });
    }

    private static <T extends IPower<T, ?>> void cloneCap(LazyOptional<T> oldCap, LazyOptional<T> newCap, boolean wasDeath, String warning) {
        if (oldCap.isPresent() && newCap.isPresent()) {
            newCap.resolve().get().onClone(oldCap.resolve().get(), wasDeath);
        } else {
            JojoMod.getLogger().warn("Failed to copy data!");
        }

    }

    private static void syncAttachedData(PlayerEntity player) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        player.getCapability(PrivateUtilCapProvider.CAPABILITY).ifPresent(data -> {
            data.syncWithEntityOnly(serverPlayer);
            data.syncWithAnyPlayer(serverPlayer);
        });
    }
}
