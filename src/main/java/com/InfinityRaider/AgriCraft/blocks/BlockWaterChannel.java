package com.InfinityRaider.AgriCraft.blocks;

import com.InfinityRaider.AgriCraft.AgriCraft;
import com.InfinityRaider.AgriCraft.reference.Constants;
import com.InfinityRaider.AgriCraft.tileentity.TileEntityChannel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockWaterChannel extends BlockCustomWood {

    public BlockWaterChannel() {
        super();
        this.setBlockBounds(4*Constants.unit, 4*Constants.unit, 4*Constants.unit, 12*Constants.unit, 12*Constants.unit, 12*Constants.unit);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityChannel();
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!world.isRemote) {
            // FIXME: will most likely end up in an infinity loop
            ItemStack drop = new ItemStack(com.InfinityRaider.AgriCraft.init.Blocks.blockWaterChannel, 1);
            setTag(world, pos, drop);
            dropBlockAsItem(world, pos, state, 0);
        }
    }

    //creative item picking
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos){
        ItemStack stack = new ItemStack(com.InfinityRaider.AgriCraft.init.Blocks.blockWaterChannel, 1);
        setTag(world, pos, stack);
        return stack;
    }

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    @Override
    public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity entity) {
        //adjacent boxes
        TileEntityChannel channel = (TileEntityChannel) world.getTileEntity(pos);
        float f = Constants.unit;   //one 16th of a block
        float min = 4*f;
        float max = 12*f;
        if(channel.hasNeighbour('x', 1)) {
            this.setBlockBounds(max-f, min, min, f*16, max, max);
            super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
        }
        if(channel.hasNeighbour('x', -1)) {
            this.setBlockBounds(0, min, min, min+f, max, max);
            super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
        }
        if(channel.hasNeighbour('z', 1)) {
            this.setBlockBounds(min, min, max-f, max, max,  f*16);
            super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
        }
        if(channel.hasNeighbour('z', -1)) {
            this.setBlockBounds(min, min, 0, max, max, min+f);
            super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
        }
        //central box
        this.setBlockBounds(min, min, min, max, max, max);
        super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos pos) {
        // TODO: implement this, old implementation which uses 1.7 API can be found in git
        return super.getSelectedBoundingBox(world, pos);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));    //wooden channel
        list.add(new ItemStack(item, 1, 1));    //iron pipe
    }

    @Override
    public int damageDropped(IBlockState state) {
        // TODO: implement this as it was in 1.7
        return super.damageDropped(state);
    }

    //render methods
    //--------------
    @Override
    public int getRenderType() {return AgriCraft.proxy.getRenderId(Constants.channelId);}
    @Override
    public boolean isOpaqueCube() {return false;}           //tells minecraft that this is not a block (no levers can be placed on it, it's transparent, ...)

    // TODO: textures in 1.8?
    /*
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if(meta==0) {
            return Blocks.planks.getIcon(0, 0);
        }
        else if(meta==1) {
            return Blocks.iron_block.getIcon(0, 0);
        }
        return null;
    }
    */
}
