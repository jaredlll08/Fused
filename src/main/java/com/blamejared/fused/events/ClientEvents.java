package com.blamejared.fused.events;

import com.blamejared.fused.blocks.FBlocks;
import com.blamejared.fused.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class ClientEvents {
    
    public ClientEvents() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onModelRegistry(ModelRegistryEvent event) {
        for(Map.Entry<String, Block> ent : FBlocks.blockMap.entrySet()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ent.getValue()), 0, new ModelResourceLocation(Reference.MODID + ":" + ent.getKey(), "inventory"));
        }
    }
}
