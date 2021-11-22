package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.IngredientWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeTypeWidget;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;

/**
 * Grimoire page showing a smelting recipe.
 * 
 * @author Daedalus4096
 */
public class SmeltingRecipePage extends AbstractRecipePage {
    protected SmeltingRecipe recipe;
    
    public SmeltingRecipePage(SmeltingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    protected String getTitleTranslationKey() {
        return this.recipe.getResultItem().getDescriptionId();
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "primalmagick.grimoire.smelting_recipe_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 124;
        int overlayWidth = 51;

        // Render ingredient stacks
        screen.addWidgetToScreen(new IngredientWidget(this.recipe.getIngredients().get(0), x - 5 + (side * 140) + (indent / 2) - (overlayWidth / 2) + 32, y + 67 + 14, screen));

        // Render output stack
        ItemStack output = this.recipe.getResultItem();
        screen.addWidgetToScreen(new ItemStackWidget(output, x + 27 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, false));
        
        // Render recipe type widget
        screen.addWidgetToScreen(new RecipeTypeWidget(this.recipe, x - 22 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, new TranslatableComponent(this.getRecipeTypeTranslationKey())));
    }

    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        super.render(matrixStack, side, x, y, mouseX, mouseY);
        y += 53;
        
        int indent = 124;
        int overlayWidth = 46;
        int overlayHeight = 46;
        
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderTexture(0, OVERLAY);
        
        // Render overlay background
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        matrixStack.translate(x - 6 + (side * 140) + (indent / 2), y + 49 + (overlayHeight / 2), 0.0F);
        matrixStack.scale(2.0F, 2.0F, 1.0F);
        this.blit(matrixStack, -(overlayWidth / 2), -(overlayHeight / 2), 51, 0, overlayWidth, overlayHeight);
        matrixStack.popPose();
    }
}