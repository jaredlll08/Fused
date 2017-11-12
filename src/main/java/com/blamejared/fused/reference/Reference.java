package com.blamejared.fused.reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.launchwrapper.Launch;

public class Reference {
    
    public static final String MODID = "fused";
    public static final String NAME = "Fused";
    public static final String VERSION = "1.0.0";
    
    public static final CreativeTabs TAB = CreativeTabs.REDSTONE;
    public static final boolean DEVENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
}
