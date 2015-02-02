package com.InfinityRaider.AgriCraft.items;

import com.InfinityRaider.AgriCraft.blocks.BlockCrop;
import com.InfinityRaider.AgriCraft.creativetab.AgriCraftTab;
import com.InfinityRaider.AgriCraft.reference.Names;
import com.InfinityRaider.AgriCraft.tileentity.TileEntityCrop;
import com.InfinityRaider.AgriCraft.utility.SeedHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemTrowel extends ModItem {

    // TODO: textures in 1.8?
    // private IIcon[] icons = new IIcon[2];

    public ItemTrowel() {
        super();
        this.setCreativeTab(AgriCraftTab.agriCraftTab);
        this.maxStackSize=1;
    }

    //I'm overriding this just to be sure
    @Override
    public boolean canItemEditBlocks() {return true;}

    //this is called when you right click with this item in hand
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            Block block = world.getBlockState(pos).getBlock();
            if (block != null && block instanceof BlockCrop) {
                TileEntity te = world.getTileEntity(pos);
                if (te != null && te instanceof TileEntityCrop) {
                    TileEntityCrop crop = (TileEntityCrop) te;
                    //clear weed
                    if (crop.weed) {
                        crop.clearWeed();
                    }
                    //put plant on trowel
                    else if (crop.hasPlant() && stack.getItemDamage() == 0) {
                        //put plant on trowel
                        NBTTagCompound tag = new NBTTagCompound();
                        tag.setShort(Names.NBT.growth, (short) crop.growth);
                        tag.setShort(Names.NBT.gain, (short) crop.gain);
                        tag.setShort(Names.NBT.strength, (short) crop.strength);
                        tag.setBoolean(Names.NBT.analyzed, crop.analyzed);
                        tag.setString(Names.Objects.seed, crop.getSeedString());
                        tag.setShort(Names.NBT.meta, (short) crop.seedMeta);
                        tag.setShort(Names.NBT.materialMeta, (short) block.getMetaFromState(world.getBlockState(pos)));
                        stack.setTagCompound(tag);
                        stack.setItemDamage(1);
                        //clear crop
                        crop.clearPlant();
                        //return true to avoid further processing
                        return true;
                    }
                    //plant crop from trowel
                    else if (!crop.hasPlant() && !crop.crossCrop && stack.getItemDamage() == 1) {
                        //set crop
                        NBTTagCompound tag = stack.getTagCompound();
                        ItemSeeds seed = (ItemSeeds) Item.itemRegistry.getObject(tag.getString(Names.Objects.seed));
                        int seedMeta = tag.getShort(Names.NBT.meta);
                        Block blockBelow = world.getBlockState(pos.down()).getBlock();
                        if(SeedHelper.isCorrectSoil(blockBelow, blockBelow.getMetaFromState(world.getBlockState(pos.down())), seed, seedMeta)) {
                            crop.growth = tag.getShort(Names.NBT.growth);
                            crop.gain = tag.getShort(Names.NBT.gain);
                            crop.strength = tag.getShort(Names.NBT.strength);
                            crop.analyzed = tag.getBoolean(Names.NBT.analyzed);
                            crop.seed = seed;
                            crop.seedMeta = seedMeta;
                            world.setBlockState(pos, block.getStateFromMeta(tag.getShort(Names.NBT.materialMeta)), 3);
                            crop.markDirtyAndMarkForUpdate();
                            //clear trowel
                            stack.setTagCompound(null);
                            stack.setItemDamage(0);
                            //return true to avoid further processing
                            return true;
                        }
                    }
                }
            }
        }
        return false;   //return false or else no other use methods will be called (for instance "onBlockActivated" on the crops block)
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        if(stack.getItemDamage()==0) {
            list.add(StatCollector.translateToLocal("agricraft_tooltip.trowel"));
        }
        else if(stack.hasTagCompound() && stack.getTagCompound().hasKey(Names.Objects.seed) && stack.getTagCompound().hasKey(Names.NBT.meta)) {
            NBTTagCompound tag = stack.getTagCompound();
            ItemStack seed = new ItemStack((Item) Item.itemRegistry.getObject(tag.getString(Names.Objects.seed)), 1, tag.getShort(Names.NBT.meta));
            list.add(StatCollector.translateToLocal("agricraft_tooltip.seed")+": "+ seed.getItem().getItemStackDisplayName(seed));
        }
    }

    // TODO: textures in 1.8?
    /*
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        LogHelper.debug("registering icon for: " + this.getUnlocalizedName());
        icons[0] = reg.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf('.')+1)+"_empty");
        icons[1] = reg.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf('.')+1)+"_full");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        if(meta<=1) {
            return this.icons[meta];
        }
        return null;
    }
    */
}
