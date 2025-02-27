package com.verdantartifice.primalmagick.common.containers;

import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.containers.slots.WandCapSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WandCoreSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WandGemSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WandSlot;
import com.verdantartifice.primalmagick.common.crafting.WandGlamourRecipe;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public class WandGlamourTableContainer extends AbstractContainerMenu {
    protected static final ResourceLocation RECIPE_LOC = new ResourceLocation(PrimalMagick.MODID, "wand_glamour");

    protected final ContainerLevelAccess worldPosCallable;
    protected final GlamourComponentInventory componentInv = new GlamourComponentInventory();
    protected final ResultContainer resultInv = new ResultContainer();
    protected final Player player;
    protected final Slot wandSlot;
    protected final Slot coreSlot;
    protected final Slot capSlot;
    protected final Slot gemSlot;
    
    public WandGlamourTableContainer(int windowId, Inventory inv) {
        this(windowId, inv, ContainerLevelAccess.NULL);
    }
    
    public WandGlamourTableContainer(int windowId, Inventory inv, ContainerLevelAccess callable) {
        super(ContainersPM.WAND_GLAMOUR_TABLE.get(), windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        
        // Slot 0: Result
        this.addSlot(new ResultSlot(this.player, this.componentInv, this.resultInv, 0, 124, 35));
        
        // Slot 1: Wand/staff
        this.wandSlot = this.addSlot(new WandSlot(this.componentInv, 0, 66, 35, true));
        
        // Slot 2: Wand core
        this.coreSlot = this.addSlot(new WandCoreSlot(this.componentInv, 1, 30, 17));
        
        // Slot 3: Wand caps
        this.capSlot = this.addSlot(new WandCapSlot(this.componentInv, 2, 30, 35));
        
        // Slot 4: Wand gem
        this.gemSlot = this.addSlot(new WandGemSlot(this.componentInv, 3, 30, 53));
        
        // Slots 5-31: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
            }
        }
        
        // Slots 32-40: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + (i * 18), 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 0) {
                // If transferring the output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index >= 5 && index < 32) {
                // If transferring from the backpack, move wands, cores, caps, and gems to the appropriate slots, and everything else to the hotbar
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.coreSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.capSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.gemSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 32 && index < 41) {
                // If transferring from the hotbar, move wands, cores, caps, and gems to the appropriate slots, and everything else to the backpack
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.coreSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.capSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.gemSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, 5, 41, false)) {
                // Move all other transfers to the backpack or hotbar
                return ItemStack.EMPTY;
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, slotStack);
        }
        return stack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.worldPosCallable, player, BlocksPM.WAND_GLAMOUR_TABLE.get());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.clearContainer(player, this.componentInv);
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultInv && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public void slotsChanged(Container inv) {
        super.slotsChanged(inv);
        this.worldPosCallable.execute((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }

    protected void slotChangedCraftingGrid(Level world) {
        if (!world.isClientSide && this.player instanceof ServerPlayer spe) {
            ItemStack stack = ItemStack.EMPTY;
            Optional<? extends Recipe<?>> opt = world.getServer().getRecipeManager().byKey(RECIPE_LOC);
            if (opt.isPresent() && opt.get() instanceof WandGlamourRecipe recipe) {
                // If the inputs are valid, show the output
                if (recipe.matches(this.componentInv, world)) {
                    stack = recipe.assemble(this.componentInv);
                }
            }
            
            // Send a packet to the client to update its GUI with the shown output
            this.resultInv.setItem(0, stack);
            spe.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, stack));
        }
    }

    protected class GlamourComponentInventory extends CraftingContainer {
        public GlamourComponentInventory() {
            super(WandGlamourTableContainer.this, 2, 2);
        }
        
        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
