package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.common.init.InitBlocks;
import com.verdantartifice.primalmagic.common.init.InitItems;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistrationEvents {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        InitBlocks.initBlocks(event.getRegistry());
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        InitItems.initBlockItems(event.getRegistry());
        InitItems.initItems(event.getRegistry());
    }
}
