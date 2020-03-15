package com.verdantartifice.primalmagic.common.blocks.rituals;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.rituals.ISaltPowered;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * Block definition for the ritual altar.  It is the central component of magical rituals, providing
 * the salt "power" that enables the functionality of other ritual props.  It's also where the ritual
 * output appears.
 * 
 * @author Daedalus4096
 */
public class RitualAltarBlock extends Block implements ISaltPowered {
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/ritual_altar"));
    
    public RitualAltarBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        for (Direction dir : Direction.values()) {
            worldIn.notifyNeighborsOfStateChange(pos.offset(dir), this);
        }
    }
    
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving) {
            for (Direction dir : Direction.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(dir), this);
            }
        }
    }
    
    @Override
    public boolean canProvideSaltPower(BlockState state) {
        return true;
    }
    
    @Override
    public int getWeakSaltPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return (side != Direction.UP) ? 15 : 0;
    }
    
    @Override
    public int getStrongSaltPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return (side == Direction.DOWN && blockState.getBlock() instanceof ISaltPowered) ?
                ((ISaltPowered)blockState.getBlock()).getWeakSaltPower(blockState, blockAccess, pos, side) :
                0;
    }
}