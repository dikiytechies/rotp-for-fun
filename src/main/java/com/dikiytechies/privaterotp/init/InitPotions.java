package com.dikiytechies.privaterotp.init;

import com.github.standobyte.jojo.crafting.PotionBrewingRecipeBuilder;
import com.github.standobyte.jojo.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import javax.annotation.Nullable;

public class InitPotions {
    public static void registerRecipes() {
        registerRecipes(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.MUNDANE)), Ingredient.of(ModItems.AJA_STONE.get()), new ItemStack(InitItems.INJECTION_MK1.get()));
        registerRecipes(Ingredient.of(InitItems.INJECTION_MK1.get()), Ingredient.of(ModItems.METEORIC_IRON.get()), new ItemStack(InitItems.INJECTION_MK2.get()));
        registerRecipes(Ingredient.of(InitItems.INJECTION_MK2.get()), Ingredient.of(ModItems.SUPER_AJA_STONE.get()), new ItemStack(InitItems.INJECTION_MK3.get()));
    }
    private static void registerRecipes(Ingredient initialPotion, Ingredient initialIngredient,
                                        ItemStack basePotion) {
        BrewingRecipeRegistry.addRecipe(initialPotion, initialIngredient, basePotion);
    }
}
