package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.awt.Color;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

/**
 * Grimoire page showing the attunements gained from a research stage.
 * 
 * @author Daedalus4096
 */
public class AttunementGainPage extends AbstractPage {
    protected SourceList attunements;
    
    public AttunementGainPage(@Nonnull SourceList attunements) {
        this.attunements = attunements;
    }
    
    @Override
    protected boolean renderTopTitleBar() {
        return false;
    }

    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        // Render page title
        this.renderTitle(matrixStack, side, x, y, mouseX, mouseY, null);
        y += 53;

        // Render attunement gain list
        Minecraft mc = Minecraft.getInstance();
        for (Source source : this.attunements.getSourcesSorted()) {
            int amount = Mth.clamp(this.attunements.getAmount(source), 0, 5);
            Component labelText = source.isDiscovered(mc.player) ?
                    Component.translatable(source.getNameTranslationKey()) :
                    Component.translatable(Source.getUnknownTranslationKey());
            Component amountText = Component.translatable("primalmagick.attunement_gain." + Integer.toString(amount));
            Component fullText = Component.translatable("primalmagick.attunement_gain.text", labelText, amountText);
            mc.font.draw(matrixStack, fullText, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB());
            y += mc.font.lineHeight;
        }
    }

    @Override
    protected String getTitleTranslationKey() {
        return "primalmagick.grimoire.attunement_gain_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {}
}
