package com.verdantartifice.primalmagick.client.compat.jei;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.arcane_crafting.ArcaneCraftingRecipeCategory;
import com.verdantartifice.primalmagick.client.compat.jei.concocting.ConcoctingRecipeCategory;
import com.verdantartifice.primalmagick.client.compat.jei.concocting.ConcoctionSubtypeInterpreter;
import com.verdantartifice.primalmagick.client.compat.jei.dissolution.DissolutionRecipeCategory;
import com.verdantartifice.primalmagick.client.compat.jei.ritual.RitualRecipeCategory;
import com.verdantartifice.primalmagick.client.compat.jei.runecarving.RunecarvingRecipeCategory;
import com.verdantartifice.primalmagick.client.gui.ArcaneWorkbenchScreen;
import com.verdantartifice.primalmagick.client.gui.ConcocterScreen;
import com.verdantartifice.primalmagick.client.gui.DissolutionChamberScreen;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.containers.ArcaneWorkbenchContainer;
import com.verdantartifice.primalmagick.common.containers.ConcocterContainer;
import com.verdantartifice.primalmagick.common.containers.ContainersPM;
import com.verdantartifice.primalmagick.common.containers.DissolutionChamberContainer;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.IConcoctingRecipe;
import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Main class for JEI integration.
 * 
 * @author Daedalus4096
 */
@JeiPlugin
public class JeiHelper implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(PrimalMagick.MODID, "jei");
    
    @Nullable
    private IRecipeCategory<IArcaneRecipe> arcaneCategory;
    @Nullable
    private IRecipeCategory<IConcoctingRecipe> concoctingCategory;
    @Nullable
    private IRecipeCategory<IRunecarvingRecipe> runecarvingCategory;
    @Nullable
    private IRecipeCategory<IDissolutionRecipe> dissolutionCategory;
    @Nullable
    private IRecipeCategory<IRitualRecipe> ritualCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ItemsPM.CONCOCTION.get(), ConcoctionSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ItemsPM.ALCHEMICAL_BOMB.get(), ConcoctionSubtypeInterpreter.INSTANCE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        this.arcaneCategory = new ArcaneCraftingRecipeCategory(guiHelper);
        this.concoctingCategory = new ConcoctingRecipeCategory(guiHelper);
        this.runecarvingCategory = new RunecarvingRecipeCategory(guiHelper);
        this.dissolutionCategory = new DissolutionRecipeCategory(guiHelper);
        this.ritualCategory = new RitualRecipeCategory(guiHelper);
        registration.addRecipeCategories(
            this.arcaneCategory,
            this.concoctingCategory,
            this.runecarvingCategory,
            this.dissolutionCategory,
            this.ritualCategory
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        CategoryRecipes categoryRecipes = new CategoryRecipes();
        if (this.arcaneCategory != null) {
            registration.addRecipes(JeiRecipeTypesPM.ARCANE_CRAFTING, categoryRecipes.getArcaneRecipes(this.arcaneCategory));
        }
        if (this.concoctingCategory != null) {
            registration.addRecipes(JeiRecipeTypesPM.CONCOCTING, categoryRecipes.getConcoctingRecipes(this.concoctingCategory));
        }
        if (this.runecarvingCategory != null) {
            registration.addRecipes(JeiRecipeTypesPM.RUNECARVING, categoryRecipes.getRunecarvingRecipes(this.runecarvingCategory));
        }
        if (this.dissolutionCategory != null) {
            registration.addRecipes(JeiRecipeTypesPM.DISSOLUTION, categoryRecipes.getDissolutionRecipes(this.dissolutionCategory));
        }
        if (this.ritualCategory != null) {
            registration.addRecipes(JeiRecipeTypesPM.RITUAL, categoryRecipes.getRitualRecipes(this.ritualCategory));
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlocksPM.ARCANE_WORKBENCH.get()), JeiRecipeTypesPM.ARCANE_CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(BlocksPM.CONCOCTER.get()), JeiRecipeTypesPM.CONCOCTING);
        registration.addRecipeCatalyst(new ItemStack(BlocksPM.RUNECARVING_TABLE.get()), JeiRecipeTypesPM.RUNECARVING);
        registration.addRecipeCatalyst(new ItemStack(BlocksPM.DISSOLUTION_CHAMBER.get()), JeiRecipeTypesPM.DISSOLUTION);
        registration.addRecipeCatalyst(new ItemStack(BlocksPM.RITUAL_ALTAR.get()), JeiRecipeTypesPM.RITUAL);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ArcaneWorkbenchContainer.class, ContainersPM.ARCANE_WORKBENCH.get(), JeiRecipeTypesPM.ARCANE_CRAFTING, 1, 9, 11, 36);
        registration.addRecipeTransferHandler(ConcocterContainer.class, ContainersPM.CONCOCTER.get(), JeiRecipeTypesPM.CONCOCTING, 1, 9, 11, 36);
        registration.addRecipeTransferHandler(DissolutionChamberContainer.class, ContainersPM.DISSOLUTION_CHAMBER.get(), JeiRecipeTypesPM.DISSOLUTION, 1, 1, 3, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ArcaneWorkbenchScreen.class, 104, 52, 22, 15, JeiRecipeTypesPM.ARCANE_CRAFTING);
        registration.addRecipeClickArea(ConcocterScreen.class, 104, 35, 22, 15, JeiRecipeTypesPM.CONCOCTING);
        registration.addRecipeClickArea(DissolutionChamberScreen.class, 79, 35, 22, 15, JeiRecipeTypesPM.DISSOLUTION);
    }
    
    public static List<Component> getManaCostTooltipStrings(SourceList manaCosts) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("primalmagick.crafting.mana_cost_header"));
        for (Source source : manaCosts.getSourcesSorted()) {
            tooltip.add(Component.translatable("primalmagick.crafting.mana_tooltip", manaCosts.getAmount(source), source.getNameText()));
        }
        return tooltip;
    }
    
    public static List<Component> getRequiredResearchTooltipStrings(CompoundResearchKey compoundResearch) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("primalmagick.crafting.research_header"));
        for (SimpleResearchKey key : compoundResearch.getKeys()) {
            ResearchEntry entry = ResearchEntries.getEntry(key);
            if (entry == null) {
                tooltip.add(Component.translatable("primalmagick.research." + key.getRootKey() + ".text"));
            } else {
                MutableComponent comp = Component.translatable(entry.getNameTranslationKey());
                ResearchDiscipline disc = ResearchDisciplines.getDiscipline(entry.getDisciplineKey());
                if (disc != null) {
                    comp.append(Component.literal(" ("));
                    comp.append(Component.translatable(disc.getNameTranslationKey()));
                    comp.append(Component.literal(")"));
                }
                tooltip.add(comp);
            }
        }
        return tooltip;
    }
}
