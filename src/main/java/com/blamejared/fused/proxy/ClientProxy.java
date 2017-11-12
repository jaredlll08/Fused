package com.blamejared.fused.proxy;

import com.blamejared.fused.blocks.FBlocks;
import com.blamejared.fused.client.render.RenderFuse;
import com.blamejared.fused.events.ClientEvents;
import com.blamejared.fused.reference.Reference;
import com.blamejared.fused.tileentity.TileEntityFuse;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.Map;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void registerRenderers() {
        super.registerRenderers();
        ClientRegistry.registerTileEntity(TileEntityFuse.class, "fuse", new RenderFuse());
    }
    
    public void registerColours() {
    }
    
    @Override
    public void registerEvents() {
        super.registerEvents();
        new ClientEvents();
    }
}
