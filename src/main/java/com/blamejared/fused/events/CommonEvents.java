package com.blamejared.fused.events;

import com.blamejared.fused.blocks.*;
import net.minecraft.init.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonEvents {
    
    public CommonEvents() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void rightClickItem(PlayerInteractEvent.RightClickBlock e) {
        if(!e.getItemStack().isEmpty() && e.getItemStack().getItem() == Items.GUNPOWDER) {
            e.setCanceled(true);
            BlockFuse fuse = FBlocks.FUSE;
            if(e.getWorld().isAirBlock(e.getPos().up()) &&   fuse.canPlaceBlockAt(e.getWorld(), e.getPos().up())) {
                e.getEntityPlayer().swingArm(e.getHand());
                if(!e.getWorld().isRemote) {
                    if(!e.getEntityPlayer().isCreative()) {
                        e.getItemStack().shrink(1);
                    }
                    e.getWorld().setBlockState(e.getPos().up(), fuse.getDefaultState());
                   
                }
            }
        }
    }
}
