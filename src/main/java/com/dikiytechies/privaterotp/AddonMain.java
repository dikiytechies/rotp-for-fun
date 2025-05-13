package com.dikiytechies.privaterotp;

import com.dikiytechies.privaterotp.capability.CapabilityHandler;
import com.dikiytechies.privaterotp.init.InitItems;
import com.dikiytechies.privaterotp.init.InitPotions;
import com.dikiytechies.privaterotp.init.InitSounds;
import com.dikiytechies.privaterotp.network.AddonPackets;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(AddonMain.MOD_ID)
public class AddonMain {
    // The mod's id. Used quite often, mostly when creating ResourceLocation (objects).
    // Its value should match the "modid" entry in the META-INF/mods.toml file
    public static final String MOD_ID = "privaterotp";
    public static final Logger LOGGER = LogManager.getLogger();

    public AddonMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::preInit);
        InitItems.ITEMS.register(modEventBus);
        InitSounds.SOUNDS.register(modEventBus);
    }

    private void preInit(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CapabilityHandler.commonSetupRegister();
            AddonPackets.init();
            InitPotions.registerRecipes();
        });
    }
}
