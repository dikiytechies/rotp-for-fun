package com.dikiytechies.privaterotp.init;

import com.dikiytechies.privaterotp.AddonMain;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(
            ForgeRegistries.SOUND_EVENTS, AddonMain.MOD_ID);

    public static final RegistryObject<SoundEvent> TRIPLET = register("triplet_after_triplet");

    private static RegistryObject<SoundEvent> register(String regPath) {
        return SOUNDS.register(regPath, () -> new SoundEvent(new ResourceLocation(AddonMain.MOD_ID, regPath)));
    }
}
