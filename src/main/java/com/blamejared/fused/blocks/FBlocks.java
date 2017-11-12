package com.blamejared.fused.blocks;

import com.blamejared.fused.reference.Reference;
import com.blamejared.fused.tileentity.TileEntityFuse;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.*;
import java.util.*;

import static com.blamejared.fused.reference.Reference.DEVENV;
import static com.blamejared.fused.reference.Reference.TAB;

public class FBlocks {
    
    public static Map<String, Block> blockMap = new HashMap<>();
    public static final BlockFuse FUSE = new BlockFuse();
    
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        registerBlock(FUSE, "fuse", TileEntityFuse.class);
        for(Block block : blockMap.values()) {
            event.getRegistry().register(block);
        }
        
    }
    @SubscribeEvent
    public void registerItemBlocks(RegistryEvent.Register<Item> event) {
        for(Block block : blockMap.values()) {
            event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }
    private static void registerBlock(Block block, String key) {
        registerBlock(block, key, key, null, TAB);
    }
    
    private static void registerBlock(Block block, String key, String texture) {
        registerBlock(block, key, texture, null, TAB);
    }
    
    private static void registerBlock(Block block, String key, String texture, Class tile) {
        registerBlock(block, key, texture, tile, TAB);
    }
    
    
    private static void registerBlock(Block block, String key, Class tile) {
        registerBlock(block, key, key, tile, TAB);
    }
    
    private static void registerBlock(Block block, String key, Class tile, CreativeTabs tab) {
        registerBlock(block, key, key, tile, TAB);
    }
    
    private static void registerBlock(Block block, String key, String texture, Class tile, CreativeTabs tab) {
        block.setUnlocalizedName(key).setCreativeTab(TAB);
        if(DEVENV && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            writeFile(key, texture);
        blockMap.put(texture, block.setRegistryName(new ResourceLocation(Reference.MODID + ":" + key)));
        if(tile != null) {
            GameRegistry.registerTileEntity(tile, key);
        }
    }
    
    
    
    
    private static void writeFile(String key, String texture) {
        try {
            File baseBlockState = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/blockstates/" + key + ".json");
            File baseBlockModel = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/models/block/" + key + ".json");
            File baseItem = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/models/item/" + key + ".json");
            if(!baseBlockState.exists()) {
                baseBlockState.createNewFile();
                File base = new File(System.getProperty("user.home") + "/blamejared/baseBlockState.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
                replaceInLine(scan, key, texture, content);
                scan.close();
                FileWriter write = new FileWriter(baseBlockState);
                for(String s : content) {
                    write.write(s + "\n");
                }
                write.close();
            }
            if(!baseBlockModel.exists()) {
                baseBlockModel.createNewFile();
                File base = new File(System.getProperty("user.home") + "/blamejared/baseBlockModel.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
                replaceInLine(scan, key, texture, content);
                scan.close();
                FileWriter write = new FileWriter(baseBlockModel);
                for(String s : content) {
                    write.write(s + "\n");
                }
                write.close();
            }
            
            if(!baseItem.exists()) {
                baseItem.createNewFile();
                File base = new File(System.getProperty("user.home") + "/blamejared/baseBlockItem.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
                replaceInLine(scan, key, texture, content);
                scan.close();
                FileWriter write = new FileWriter(baseItem);
                for(String s : content) {
                    write.write(s + "\n");
                }
                write.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void replaceInLine(Scanner scan, String key, String texture, List<String> content) {
        while(scan.hasNextLine()) {
            String line = scan.nextLine();
            if(line.contains("%modid%")) {
                line = line.replace("%modid%", Reference.MODID);
            }
            if(line.contains("%key%")) {
                line = line.replace("%key%", key);
            }
            if(line.contains("%texture%")) {
                line = line.replace("%texture%", texture);
            }
            content.add(line);
        }
    }

    
}
