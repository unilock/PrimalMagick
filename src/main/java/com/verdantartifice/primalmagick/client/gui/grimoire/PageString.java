package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;

/**
 * A string element to be rendered on a grimoire page.
 * 
 * @author Daedalus4096
 */
public class PageString implements IPageElement {
    protected String str;
    
    public PageString(String str) {
        this.str = str;
    }
    
    public String getString() {
        return this.str;
    }

    @Override
    public void render(PoseStack matrixStack, int side, int x, int y) {
        // Render this element's string to the screen
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        mc.font.draw(matrixStack, this.str.replace("~B", ""), x - 1 + (side * 138), y - 6, Color.BLACK.getRGB());
    }

    @Override
    public int getNextY(int y) {
        Minecraft mc = Minecraft.getInstance();
        y += mc.font.lineHeight;
        if (this.str.endsWith("~B")) {
            // If this element ends with a <BR> tag, leave some extra vertical space after it
            y += (int)(mc.font.lineHeight * 0.66D);
        }
        return y;
    }
}
