package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class WandInventory implements IInventory {
    protected final NonNullList<ItemStack> stackWand = NonNullList.withSize(1, ItemStack.EMPTY);
    protected final Container container;
    
    public WandInventory(Container container) {
        this.container = container;
    }

    @Override
    public void clear() {
        this.stackWand.clear();
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.stackWand.get(0).isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.stackWand.get(0);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return this.removeStackFromSlot(index);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = ItemStackHelper.getAndRemove(this.stackWand, 0);
        this.container.onCraftMatrixChanged(this);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.stackWand.set(0, stack);
        this.container.onCraftMatrixChanged(this);
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof IWand;
    }
}
