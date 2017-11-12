package com.blamejared.fused.client.render;

import com.blamejared.fused.blocks.*;
import com.blamejared.fused.reference.Reference;
import com.blamejared.fused.tileentity.TileEntityFuse;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

public class RenderFuse extends TileEntitySpecialRenderer<TileEntityFuse> {
    
    @Override
    public void render(TileEntityFuse te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GL11.glTranslated(x, y, z);
        GL11.glDisable(GL11.GL_LIGHTING);
        
        GlStateManager.color(1, 1, 1, 1);
        renderBlockModel(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()).withProperty(BlockFuse.FACING, EnumFacing.UP), true);
        for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            Block block = te.getWorld().getBlockState(te.getPos().offset(facing)).getBlock();
            if(block == FBlocks.FUSE || block instanceof BlockTNT)
                renderBlockModel(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()).withProperty(BlockFuse.FACING, facing), true);
        }
        
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glTranslated(-x, -y, -z);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
    
    public static void renderBlockModel(World world, BlockPos pos, IBlockState state, boolean translateToOrigin) {
        
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        BufferBuilder wr = Tessellator.getInstance().getBuffer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        if(translateToOrigin) {
            wr.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
        }
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockModelShapes modelShapes = blockrendererdispatcher.getBlockModelShapes();
        IBakedModel ibakedmodel = modelShapes.getModelForState(state);
        final IBlockAccess worldWrapper = world;
        for(BlockRenderLayer layer : BlockRenderLayer.values()) {
            if(state.getBlock().canRenderInLayer(state, layer)) {
                ForgeHooksClient.setRenderLayer(layer);
                blockrendererdispatcher.getBlockModelRenderer().renderModel(worldWrapper, ibakedmodel, state, pos, wr, false);
            }
        }
        ForgeHooksClient.setRenderLayer(null);
        if(translateToOrigin) {
            wr.setTranslation(0, 0, 0);
        }
        Tessellator.getInstance().draw();
    }
}
