package com.blamejared.fused;

import com.blamejared.fused.blocks.FBlocks;
import com.blamejared.fused.proxy.CommonProxy;
import com.blamejared.fused.reference.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Fused {
    
    @Mod.Instance(Reference.MODID)
    public static Fused INSTANCE;
    
    @SidedProxy(clientSide = "com.blamejared.fused.proxy.ClientProxy", serverSide = "com.blamejared.fused.proxy.CommonProxy")
    public static CommonProxy PROXY;
    
    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new FBlocks());
        PROXY.registerEvents();
    }
    
    @Mod.EventHandler
    public void onFMLInitialization(FMLInitializationEvent event) {
        PROXY.registerRenderers();
        PROXY.registerColours();
    }
    
    @Mod.EventHandler
    public void onFMLPostInitialization(FMLPostInitializationEvent event) {
    
    }
}
