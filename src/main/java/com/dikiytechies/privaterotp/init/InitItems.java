package com.dikiytechies.privaterotp.init;

import com.dikiytechies.privaterotp.AddonMain;
import com.dikiytechies.privaterotp.item.InjectionItem;
import com.github.standobyte.jojo.JojoMod;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AddonMain.MOD_ID);
    public static final RegistryObject<InjectionItem> INJECTION_MK1 = ITEMS.register("injection_mk1",
            () -> new InjectionItem(new Item.Properties().stacksTo(1).tab(JojoMod.MAIN_TAB).rarity(Rarity.RARE), InjectionItem.InjectionMark.MK_1));
    public static final RegistryObject<InjectionItem> INJECTION_MK2 = ITEMS.register("injection_mk2",
            () -> new InjectionItem(new Item.Properties().stacksTo(1).tab(JojoMod.MAIN_TAB).rarity(Rarity.RARE), InjectionItem.InjectionMark.MK_2));
    public static final RegistryObject<InjectionItem> INJECTION_MK3 = ITEMS.register("injection_mk3",
            () -> new InjectionItem(new Item.Properties().stacksTo(1).tab(JojoMod.MAIN_TAB).rarity(Rarity.RARE), InjectionItem.InjectionMark.MK_3));
}
