package com.verdantartifice.primalmagic.common.capabilities;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Capability interface for storing a subset of the research completed by a tile's owner.  Owned by
 * block entities.
 * 
 * @author Daedalus4096
 */
public interface ITileResearchCache extends INBTSerializable<CompoundTag> {
    /**
     * Remove all research from the cache.
     */
    public void clear();
    
    /**
     * Determine if the given research has been completed, as known by the cache.
     * 
     * @param key a key for the desired research entry
     * @return true if the given research is complete, false otherwise
     */
    public boolean isResearchComplete(@Nullable SimpleResearchKey key);
    
    /**
     * Determine if the given research has been completed, as known by the cache.
     * 
     * @param key a key for the desired research entry
     * @return true if the given research is complete, false otherwise
     */
    public boolean isResearchComplete(@Nullable CompoundResearchKey key);
    
    /**
     * Updates the cache to contain a subset of the given player's research.  The subset to copy over
     * is defined as the set of all research keys for which the given filter predicate returns true.
     * Only completed research entries will be considered.
     * 
     * @param player the player whose research to cache
     * @param researchFilter the predicate defining which research keys to cache
     */
    public void update(@Nullable Player player, Predicate<SimpleResearchKey> researchFilter);
}
