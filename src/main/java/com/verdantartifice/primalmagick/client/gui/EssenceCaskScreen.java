package com.verdantartifice.primalmagick.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.widgets.EssenceCaskWidget;
import com.verdantartifice.primalmagick.common.containers.EssenceCaskContainer;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.WithdrawCaskEssencePacket;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the essence cask block.
 * 
 * @author Daedalus4096
 */
public class EssenceCaskScreen extends AbstractContainerScreen<EssenceCaskContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/essence_cask.png");
    
    protected final List<EssenceCaskWidget> caskWidgets = new ArrayList<>();

    public EssenceCaskScreen(EssenceCaskContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
    }
    
    protected void initWidgets() {
        Minecraft mc = Minecraft.getInstance();
        this.clearWidgets();
        this.caskWidgets.clear();
        
        int visibleRows = Arrays.stream(EssenceType.values()).mapToInt(t -> this.menu.isEssenceTypeVisible(t, mc.player) ? 1 : 0).sum();
        int visibleCols = Source.SORTED_SOURCES.stream().mapToInt(s -> this.menu.isEssenceSourceVisible(s, mc.player) ? 1 : 0).sum();
        int startX = this.leftPos + 8 + (((Source.SORTED_SOURCES.size() - visibleCols) * 18) / 2);
        int startY = this.topPos + 18 + (((EssenceType.values().length - visibleRows) * 18) / 2);
        
        int index = 0;
        int xPos = startX;
        int yPos = startY;
        for (int row = 0; row < EssenceType.values().length; row++) {
            boolean rowPopulated = false;
            for (int col = 0; col < Source.SORTED_SOURCES.size(); col++) {
                EssenceType cellType = EssenceType.values()[row];
                Source cellSource = Source.SORTED_SOURCES.get(col);
                if (this.menu.isEssenceTypeVisible(cellType, mc.player) && this.menu.isEssenceSourceVisible(cellSource, mc.player)) {
                    int count = this.menu.getEssenceCount(index);
                    this.caskWidgets.add(this.addRenderableWidget(new EssenceCaskWidget(index, cellType, cellSource, count, xPos, yPos, this::onWidgetClicked)));
                    xPos += 18;
                    rowPopulated = true;
                }
                index++;
            }
            xPos = startX;
            if (rowPopulated) {
                yPos += 18;
            }
        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.initWidgets();
        
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        
        for (EssenceCaskWidget widget : this.caskWidgets) {
            if (widget.isHoveredOrFocused()) {
                renderSlotHighlight(matrixStack, widget.x, widget.y, this.getBlitOffset(), this.slotColor);
                widget.renderToolTip(matrixStack, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        super.renderLabels(matrixStack, mouseX, mouseY);
        Component contentsLabel = Component.translatable("primalmagick.essence_cask.contents", this.menu.getTotalEssenceCount(), this.menu.getTotalEssenceCapacity());
        this.font.draw(matrixStack, contentsLabel, 8, 92, 4210752);
    }

    protected void onWidgetClicked(EssenceCaskWidget widget, int clickButton) {
        int toRemove = clickButton == 1 ? 1 : 64;
        PacketHandler.sendToServer(new WithdrawCaskEssencePacket(widget.getEssenceType(), widget.getSource(), toRemove, this.menu.getTilePos()));
    }
}
