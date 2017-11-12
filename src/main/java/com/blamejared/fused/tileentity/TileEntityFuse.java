package com.blamejared.fused.tileentity;

import com.blamejared.fused.blocks.*;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandEnchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.minecraft.block.BlockTNT.EXPLODE;

public class TileEntityFuse extends TileEntity implements ITickable {
    
    private int fuseTimeDefault = 5;
    private int fuseTime = fuseTimeDefault;
    private boolean lit = false;
    private EntityPlayer player;
    
    @Override
    public void update() {
        if(!world.isRemote)
            if(lit) {
                fuseTime--;
                if(fuseTime < 0) {
                    light(world, getPos(), getPlayer());
                }
            }
    }
    
    public void light(World world, BlockPos pos, EntityPlayer player) {
        world.setBlockToAir(pos);
        for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos posOff = pos.offset(facing);
            IBlockState state = world.getBlockState(posOff);
            if(state.getBlock() == FBlocks.FUSE) {
                TileEntityFuse tile = (TileEntityFuse) world.getTileEntity(posOff);
                tile.setLit(true);
            } else if(state.getBlock() instanceof BlockTNT) {
                ((BlockTNT) state.getBlock()).explode(world, posOff, state.withProperty(EXPLODE, true), player);
                world.setBlockToAir(posOff);
            }
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.lit = compound.getBoolean("lit");
        this.fuseTime = compound.getInteger("fuseTime");
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("lit", lit);
        compound.setInteger("fuseTime", fuseTime);
        return super.writeToNBT(compound);
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        super.onDataPacket(net, pkt);
        
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 0, tag);
    }
    
    public int getFuseTimeDefault() {
        return fuseTimeDefault;
    }
    
    public void setFuseTimeDefault(int fuseTimeDefault) {
        this.fuseTimeDefault = fuseTimeDefault;
    }
    
    public int getFuseTime() {
        return fuseTime;
    }
    
    public void setFuseTime(int fuseTime) {
        this.fuseTime = fuseTime;
    }
    
    public boolean isLit() {
        return lit;
    }
    
    public void setLit(boolean lit) {
        this.lit = lit;
    }
    
    public EntityPlayer getPlayer() {
        return player;
    }
    
    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }
}
