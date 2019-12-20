package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SpellcraftingRecipe extends SpecialRecipe {
    public SpellcraftingRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        return inv.getStackInSlot(0).getItem().equals(ItemsPM.SPELL_SCROLL_BLANK);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        return new ItemStack(ItemsPM.SPELL_SCROLL_FILLED);
    }

    @Override
    public boolean canFit(int width, int height) {
        return (width * height) >= 1;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.SPELLCRAFTING_SPECIAL;
    }
}