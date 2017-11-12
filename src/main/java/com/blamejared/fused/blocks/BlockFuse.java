package com.blamejared.fused.blocks;

import com.blamejared.fused.tileentity.TileEntityFuse;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nullable;
import java.util.Arrays;

public class BlockFuse extends Block implements ITileEntityProvider {
    
    public static final PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST));
    
    public BlockFuse() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
    }
    
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        
        boolean north = source.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock() instanceof BlockTNT || source.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock() instanceof BlockFuse;
        boolean east = source.getBlockState(pos.offset(EnumFacing.EAST)).getBlock() instanceof BlockTNT || source.getBlockState(pos.offset(EnumFacing.EAST)).getBlock() instanceof BlockFuse;
        boolean south = source.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock() instanceof BlockTNT || source.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock() instanceof BlockFuse;
        boolean west = source.getBlockState(pos.offset(EnumFacing.WEST)).getBlock() instanceof BlockTNT || source.getBlockState(pos.offset(EnumFacing.WEST)).getBlock() instanceof BlockFuse;
        
        double minX = 0.4375, minY = 0, minZ = 0.4375;
        double maxX = 0.5625, maxY = 0.0625, maxZ = 0.5625;
        if(north) {
            minZ = 0;
        }
        if(east) {
            maxX = 1;
        }
        if(south) {
            maxZ = 1;
        }
        if(west) {
            minX = 0;
        }
        
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.getHeldItem(hand).getItem() != Items.FLINT_AND_STEEL) {
            return false;
        }
        if(!worldIn.isRemote) {
            TileEntityFuse fuse = (TileEntityFuse) worldIn.getTileEntity(pos);
            fuse.setLit(true);
            fuse.setPlayer(playerIn);
        }
        return true;
    }
    
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if(!worldIn.isRemote) {
            if(!this.canPlaceBlockAt(worldIn, pos)) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }
    
    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        
        return new BlockStateContainer(this, FACING);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        
        return state.getValue(FACING).ordinal();
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState();
    }
    
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    /**
     * Checks if this block can be placed exactly at the given position.
     */
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isTopSolid() || worldIn.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE;
    }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFuse();
    }
}
