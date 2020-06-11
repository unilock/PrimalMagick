package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.runes.Rune;
import com.verdantartifice.primalmagic.common.runes.RuneManager;

import net.minecraft.enchantment.Enchantments;

/**
 * Point of registration for mod rune enchantment combinations.
 * 
 * @author Daedalus4096
 */
public class InitRunes {
    public static void initRuneEnchantments() {
        // Register rune combinations for vanilla enchantments
        RuneManager.registerRuneEnchantment(Enchantments.PROTECTION, Rune.PROTECT, Rune.SELF, Rune.EARTH);
        RuneManager.registerRuneEnchantment(Enchantments.FIRE_PROTECTION, Rune.PROTECT, Rune.SELF, Rune.INFERNAL);
        RuneManager.registerRuneEnchantment(Enchantments.FEATHER_FALLING, Rune.PROJECT, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.BLAST_PROTECTION, Rune.PROTECT, Rune.SELF, Rune.VOID);
        RuneManager.registerRuneEnchantment(Enchantments.PROJECTILE_PROTECTION, Rune.PROTECT, Rune.SELF, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.RESPIRATION, Rune.PROJECT, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.AQUA_AFFINITY, Rune.PROJECT, Rune.ITEM, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.THORNS, Rune.PROJECT, Rune.CREATURE, Rune.BLOOD);
        RuneManager.registerRuneEnchantment(Enchantments.DEPTH_STRIDER, Rune.PROJECT, Rune.ITEM, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.FROST_WALKER, Rune.PROJECT, Rune.AREA, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.SHARPNESS, Rune.PROJECT, Rune.ITEM, Rune.EARTH);
        RuneManager.registerRuneEnchantment(Enchantments.KNOCKBACK, Rune.PROJECT, Rune.CREATURE, Rune.EARTH);
        RuneManager.registerRuneEnchantment(Enchantments.FIRE_ASPECT, Rune.PROJECT, Rune.ITEM, Rune.INFERNAL);
        RuneManager.registerRuneEnchantment(Enchantments.LOOTING, Rune.PROJECT, Rune.ITEM, Rune.MOON);
        RuneManager.registerRuneEnchantment(Enchantments.SWEEPING, Rune.PROJECT, Rune.AREA, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.EFFICIENCY, Rune.PROJECT, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.SILK_TOUCH, Rune.PROJECT, Rune.ITEM, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.UNBREAKING, Rune.PROTECT, Rune.ITEM, Rune.EARTH);
        RuneManager.registerRuneEnchantment(Enchantments.FORTUNE, Rune.PROJECT, Rune.ITEM, Rune.MOON);
        RuneManager.registerRuneEnchantment(Enchantments.POWER, Rune.PROJECT, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.PUNCH, Rune.PROJECT, Rune.CREATURE, Rune.EARTH);
        RuneManager.registerRuneEnchantment(Enchantments.FLAME, Rune.PROJECT, Rune.ITEM, Rune.INFERNAL);
        RuneManager.registerRuneEnchantment(Enchantments.INFINITY, Rune.SUMMON, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.LUCK_OF_THE_SEA, Rune.PROJECT, Rune.ITEM, Rune.MOON);
        RuneManager.registerRuneEnchantment(Enchantments.LURE, Rune.SUMMON, Rune.CREATURE, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.LOYALTY, Rune.SUMMON, Rune.ITEM, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.IMPALING, Rune.PROJECT, Rune.ITEM, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.RIPTIDE, Rune.SUMMON, Rune.SELF, Rune.SEA);
        RuneManager.registerRuneEnchantment(Enchantments.CHANNELING, Rune.SUMMON, Rune.AREA, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.MULTISHOT, Rune.SUMMON, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.QUICK_CHARGE, Rune.PROJECT, Rune.ITEM, Rune.SKY);
        RuneManager.registerRuneEnchantment(Enchantments.PIERCING, Rune.PROJECT, Rune.ITEM, Rune.EARTH);
        RuneManager.registerRuneEnchantment(Enchantments.MENDING, Rune.ABSORB, Rune.ITEM, Rune.EARTH);
    }
}