package com.verdantartifice.primalmagic.common.tiles.mana;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagic.common.containers.WandChargerContainer;
import com.verdantartifice.primalmagic.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagic.common.items.essence.EssenceType;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * Definition of a wand charger tile entity.  Provides the recharge and wand interaction functionality
 * for the corresponding block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.mana.WandChargerBlock}
 */
public class WandChargerTileEntity extends TileInventoryPM implements TickableBlockEntity, MenuProvider {
    protected int chargeTime;
    protected int chargeTimeTotal;
    
    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData chargerData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return WandChargerTileEntity.this.chargeTime;
            case 1:
                return WandChargerTileEntity.this.chargeTimeTotal;
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
            case 0:
                WandChargerTileEntity.this.chargeTime = value;
                break;
            case 1:
                WandChargerTileEntity.this.chargeTimeTotal = value;
                break;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };
    
    public WandChargerTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.WAND_CHARGER.get(), pos, state, 2);
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the charger's wand input/output stack for client rendering use
        return ImmutableSet.of(Integer.valueOf(1));
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.chargeTime = compound.getInt("ChargeTime");
        this.chargeTimeTotal = compound.getInt("ChargeTimeTotal");
    }
    
    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("ChargeTime", this.chargeTime);
        compound.putInt("ChargeTimeTotal", this.chargeTimeTotal);
        return super.save(compound);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new WandChargerContainer(windowId, playerInv, this, this.chargerData);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public void tick() {
        boolean shouldMarkDirty = false;

        if (!this.level.isClientSide) {
            ItemStack inputStack = this.items.get(0);
            ItemStack wandStack = this.items.get(1);
            if (!inputStack.isEmpty() && !wandStack.isEmpty()) {
                if (this.canCharge()) {
                    // If there's an essence in the input slot and the slotted wand isn't full, do the charge
                    this.chargeTime++;
                    if (this.chargeTime >= this.chargeTimeTotal) {
                        this.chargeTime = 0;
                        this.chargeTimeTotal = this.getChargeTimeTotal();
                        this.doCharge();
                        shouldMarkDirty = true;
                    }
                } else {
                    this.chargeTime = 0;
                }
            } else if (this.chargeTime > 0) {
                // Decay any charging progress if the charger isn't populated
                this.chargeTime = Mth.clamp(this.chargeTime - 2, 0, this.chargeTimeTotal);
            }
        }
        
        if (shouldMarkDirty) {
            this.setChanged();
            this.syncTile(true);
        }
    }
    
    protected int getChargeTimeTotal() {
        return 200;
    }
    
    protected boolean canCharge() {
        ItemStack inputStack = this.items.get(0);
        ItemStack wandStack = this.items.get(1);
        if (inputStack != null && !inputStack.isEmpty() && inputStack.getItem() instanceof EssenceItem &&
            wandStack != null && !wandStack.isEmpty() && wandStack.getItem() instanceof IWand) {
            // The wand can be charged if it and an essence are slotted, and the wand is not at max mana for the essence's source
            EssenceItem essence = (EssenceItem)inputStack.getItem();
            IWand wand = (IWand)wandStack.getItem();
            return (wand.getMana(wandStack, essence.getSource()) < wand.getMaxMana(wandStack));
        } else {
            return false;
        }
    }
    
    protected void doCharge() {
        ItemStack inputStack = this.items.get(0);
        ItemStack wandStack = this.items.get(1);
        if (this.canCharge()) {
            EssenceItem essence = (EssenceItem)inputStack.getItem();
            IWand wand = (IWand)wandStack.getItem();
            wand.addRealMana(wandStack, essence.getSource(), this.getManaForEssenceType(essence.getEssenceType()));
            inputStack.shrink(1);
        }
    }
    
    protected int getManaForEssenceType(EssenceType type) {
        switch (type) {
        case DUST:
            return 1;
        case SHARD:
            return 10;
        case CRYSTAL:
            return 100;
        case CLUSTER:
            return 1000;
        default:
            return 0;
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setItem(index, stack);
        boolean flag = !stack.isEmpty() && stack.sameItem(slotStack) && ItemStack.tagMatches(stack, slotStack);
        if (index == 0 && !flag) {
            this.chargeTimeTotal = this.getChargeTimeTotal();
            this.chargeTime = 0;
            this.setChanged();
        }
    }
}
