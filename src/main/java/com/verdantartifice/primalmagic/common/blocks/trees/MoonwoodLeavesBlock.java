package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.world.IWorld;

/**
 * Block definition for moonwood leaves.
 * 
 * @author Daedalus4096
 */
public class MoonwoodLeavesBlock extends AbstractPhasingLeavesBlock {
    public MoonwoodLeavesBlock() {
        super(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT));
        this.setRegistryName(PrimalMagic.MODID, "moonwood_leaves");
    }

    @Override
    protected TimePhase getCurrentPhase(IWorld world) {
        return TimePhase.getMoonPhase(world);
    }
}
