package com.verdantartifice.primalmagic.datagen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;

public class AffinityProvider implements IDataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    protected final DataGenerator generator;
    
    public AffinityProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Map<ResourceLocation, IFinishedAffinity> map = new HashMap<>();
        this.registerAffinities((affinity) -> {
            if (map.put(affinity.getId(), affinity) != null) {
                PrimalMagic.LOGGER.debug("Duplicate affinity in data generation: " + affinity.getId().toString());
            }
        });
        for (Map.Entry<ResourceLocation, IFinishedAffinity> entry : map.entrySet()) {
            IFinishedAffinity affinity = entry.getValue();
            this.saveAffinity(cache, affinity.getAffinityJson(), path.resolve("data/" + entry.getKey().getNamespace() + "/affinities/" + entry.getKey().getPath() + ".json"));
        }
    }
    
    private void saveAffinity(DirectoryCache cache, JsonObject json, Path path) {
        try {
            String jsonStr = GSON.toJson((JsonElement)json);
            String hash = HASH_FUNCTION.hashUnencodedChars(jsonStr).toString();
            if (!Objects.equals(cache.getPreviousHash(path), hash) || !Files.exists(path)) {
                Files.createDirectories(path.getParent());
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    writer.write(jsonStr);
                }
            }
            cache.recordHash(path, hash);
        } catch (IOException e) {
            PrimalMagic.LOGGER.error("Couldn't save affinity {}", path, e);
        }
    }
    
    protected void registerAffinities(Consumer<IFinishedAffinity> consumer) {
        SourceList auraUnit = new SourceList().add(Source.EARTH, 1).add(Source.SEA, 1).add(Source.SKY, 1).add(Source.SUN, 1).add(Source.MOON, 1);
        
        // Define vanilla item affinities
        ItemAffinityBuilder.itemAffinity(Items.STONE).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRANITE).base(Items.STONE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POLISHED_GRANITE).base(Items.GRANITE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DIORITE).base(Items.STONE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POLISHED_DIORITE).base(Items.DIORITE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ANDESITE).base(Items.STONE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POLISHED_ANDESITE).base(Items.ANDESITE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRASS_BLOCK).base(Items.DIRT).add(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DIRT).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COARSE_DIRT).base(Items.DIRT).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PODZOL).base(Items.DIRT).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_NYLIUM).base(Items.NETHERRACK).add(Source.MOON, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_NYLIUM).base(Items.NETHERRACK).add(Source.MOON, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COBBLESTONE).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_PLANKS).set(Source.EARTH, 2).set(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_PLANKS).base(Items.OAK_PLANKS).add(Source.MOON, 2).remove(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_PLANKS).base(Items.OAK_PLANKS).add(Source.MOON, 2).remove(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_SAPLING).set(Source.EARTH, 10).set(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BEDROCK).set(Source.EARTH, 20).set(Source.VOID, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SAND).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_SAND).base(Items.SAND).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRAVEL).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GOLD_ORE).base(Items.STONE).add(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.IRON_ORE).base(Items.STONE).add(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COAL_ORE).base(Items.STONE).add(Source.EARTH, 5).add(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.NETHER_GOLD_ORE).base(Items.NETHERRACK).add(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_LOG).set(Source.EARTH, 10).set(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_STEM).base(Items.OAK_LOG).add(Source.MOON, 10).remove(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_STEM).base(Items.OAK_LOG).add(Source.MOON, 10).remove(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_OAK_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_SPRUCE_LOG).base(Items.SPRUCE_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_BIRCH_LOG).base(Items.BIRCH_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_JUNGLE_LOG).base(Items.JUNGLE_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_ACACIA_LOG).base(Items.ACACIA_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_DARK_OAK_LOG).base(Items.DARK_OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_CRIMSON_STEM).base(Items.CRIMSON_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_WARPED_STEM).base(Items.WARPED_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_OAK_WOOD).base(Items.OAK_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_SPRUCE_WOOD).base(Items.SPRUCE_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_BIRCH_WOOD).base(Items.BIRCH_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_JUNGLE_WOOD).base(Items.JUNGLE_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_ACACIA_WOOD).base(Items.ACACIA_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_DARK_OAK_WOOD).base(Items.DARK_OAK_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_CRIMSON_HYPHAE).base(Items.CRIMSON_HYPHAE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_WARPED_HYPHAE).base(Items.WARPED_HYPHAE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_WOOD).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_WOOD).base(Items.SPRUCE_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_WOOD).base(Items.BIRCH_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_WOOD).base(Items.JUNGLE_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_WOOD).base(Items.ACACIA_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_WOOD).base(Items.DARK_OAK_LOG).build(consumer);
        
        ItemAffinityBuilder.itemAffinity(Items.POTION).set(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ENCHANTED_BOOK).set(auraUnit.copy().multiply(5)).build(consumer);
        
        // Define mod affinities
        ItemAffinityBuilder.itemAffinity(ItemsPM.MARBLE_RAW.get()).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(ItemsPM.MARBLE_ENCHANTED.get()).base(ItemsPM.MARBLE_RAW.get()).add(auraUnit.copy()).build(consumer);
        
        // Define potion bonuses
        PotionBonusAffinityBuilder.potionBonusAffinity(Potions.NIGHT_VISION).bonus(Source.SUN, 2).build(consumer);
        
        // Define enchantment bonuses
        EnchantmentBonusAffinityBuilder.enchantmentBonusAffinity(Enchantments.PROTECTION).multiplier(Source.EARTH).build(consumer);
    }

    @Override
    public String getName() {
        return "Primal Magic Affinities";
    }
}
