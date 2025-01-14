package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom item stack renderer for a hexium trident.
 * 
 * @author Daedalus4096
 */
public class HexiumTridentISTER extends AbstractTieredTridentISTER {
    protected static final ModelResourceLocation MRL = new ModelResourceLocation(new ResourceLocation(PrimalMagick.MODID, "hexium_trident"), "inventory");
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/entity/trident/hexium_trident.png");

    @Override
    public ModelResourceLocation getModelResourceLocation() {
        return MRL;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
