package com.verdantartifice.primalmagic.common.tiles;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.tiles.mana.AncientManaFontTileEntity;
import com.verdantartifice.primalmagic.common.tiles.misc.AnalysisTableTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(PrimalMagic.MODID)
public class TileEntityTypesPM {
    public static final TileEntityType<AncientManaFontTileEntity> ANCIENT_MANA_FONT = null;
    public static final TileEntityType<AnalysisTableTileEntity> ANALYSIS_TABLE = null;
}
