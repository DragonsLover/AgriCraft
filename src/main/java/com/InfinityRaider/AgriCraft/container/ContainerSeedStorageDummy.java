package com.InfinityRaider.AgriCraft.container;

import com.InfinityRaider.AgriCraft.network.MessageContainerSeedStorage;
import com.InfinityRaider.AgriCraft.network.NetworkWrapperAgriCraft;
import com.InfinityRaider.AgriCraft.tileentity.storage.ISeedStorageControllable;
import com.InfinityRaider.AgriCraft.tileentity.storage.ISeedStorageController;
import com.InfinityRaider.AgriCraft.tileentity.storage.SeedStorageSlot;
import com.InfinityRaider.AgriCraft.utility.LogHelper;
import com.InfinityRaider.AgriCraft.utility.SeedHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public abstract class ContainerSeedStorageDummy extends ContainerAgricraft {
    public ContainerSeedStorageDummy(InventoryPlayer inventory, int xOffset, int yOffset) {
        super(inventory, xOffset, yOffset);
    }

    /**
     * tries to add a stack to the storage, return true on success
     */
    public abstract boolean addSeedToStorage(ItemStack stack);

    /**
     * Gets a list off all the different kinds of seeds in the storage
     */
    public abstract List<ItemStack> getSeedEntries();

    /**
     * Gets a list off all the slots corresponding to this seed and meta
     */
    public abstract List<SeedStorageSlot> getSeedSlots(ItemSeeds seed, int meta);

    /**
     * Gets a list off all the slots corresponding to this seed and meta
     */
    public abstract TileEntity getTileEntity();

    public ISeedStorageControllable getControllable(ItemStack stack) {
        TileEntity te = this.getTileEntity();
        ISeedStorageControllable controllable = null;
        if(te != null) {
            if (te instanceof ISeedStorageController) {
                controllable = ((ISeedStorageController) te).getControllable(stack);
            } else if (te instanceof ISeedStorageControllable) {
                controllable = (ISeedStorageControllable) te;
            }
        }
        return controllable;
    }

    /**
     * Tries to move an item stack form the correct tile entity to the player's inventory
     */
    public void moveStackFromTileEntityToPlayer(int slotId, ItemStack stack) {
        ISeedStorageControllable controllable = this.getControllable(stack);
        if(controllable!=null) {
            ItemStack stackToMove = controllable.getStackInSlot(slotId);
            stackToMove.stackSize = stack.stackSize;
            stackToMove.stackTagCompound = controllable.getStackInSlot(slotId).stackTagCompound;
            if (this.mergeItemStack(stackToMove, 0, PLAYER_INVENTORY_SIZE, false)) {
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                    LogHelper.debug("Sending command to server");
                    //this method is only called form the gui client side, so we need to manually tell the server to execute it there
                    NetworkWrapperAgriCraft.wrapper.sendToServer(new MessageContainerSeedStorage(stack, Minecraft.getMinecraft().thePlayer, slotId));
                }
                else {
                    LogHelper.debug("Command received");
                    //on the server decrease the size of the stack, where it is synced to the client
                    controllable.decrStackSize(slotId, stack.stackSize - stackToMove.stackSize);
                }
            } else {
                return;
            }
        }
    }

    /**
     * Handles shift clicking in the inventory, return the stack that was transferred
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlot) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(clickedSlot);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(slot!=null) {
                //try to move item from the player's inventory into the container
                if(SeedHelper.isAnalyzedSeed(itemstack1)) {
                    if (this.addSeedToStorage(itemstack1)) {
                        itemstack1.stackSize = 0;
                    } else {
                        return null;
                    }
                }
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            }
            else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(player, itemstack1);
        }
        return itemstack;
    }

    /**
     * Tries to merge an itemstack into a range of slots, return true if the stack was (partly) merged
     */
    @Override
    protected boolean mergeItemStack(ItemStack stack, int startSlot, int endSlot, boolean iterateBackwards) {
        boolean flag = false;
        int k = iterateBackwards?endSlot - 1:startSlot;
        Slot currentSlot;
        ItemStack currentStack;
        //look for identical stacks to merge with
        while (stack.stackSize > 0 && (!iterateBackwards && k < endSlot || iterateBackwards && k >= startSlot)) {
            currentSlot = (Slot)this.inventorySlots.get(k);
            currentStack = currentSlot.getStack();
            if (currentStack != null && currentStack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == currentStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, currentStack)) {
                int l = currentStack.stackSize + stack.stackSize;
                //total stacksize is smaller than the limit: merge entire stack into this stack
                if (l <= stack.getMaxStackSize()) {
                    stack.stackSize = 0;
                    currentStack.stackSize = l;
                    currentSlot.onSlotChanged();
                    flag = true;
                }
                //total stacksize exceeds the limit: merge part of the stack into this stack
                else if (currentStack.stackSize < stack.getMaxStackSize()) {
                    stack.stackSize -= stack.getMaxStackSize() - currentStack.stackSize;
                    currentStack.stackSize = stack.getMaxStackSize();
                    currentSlot.onSlotChanged();
                    flag = true;
                }
            }
            k = iterateBackwards?k-1:k+1;
        }
        //couldn't completely merge stack with an existing slot, find the first empty slot to put the rest of the stack in
        if (stack.stackSize > 0) {
            k = iterateBackwards?endSlot-1:startSlot;
            while (!iterateBackwards && k < endSlot || iterateBackwards && k >= startSlot) {
                currentSlot = (Slot)this.inventorySlots.get(k);
                currentStack = currentSlot.getStack();
                if (currentStack == null) {
                    currentSlot.putStack(stack.copy());
                    currentSlot.onSlotChanged();
                    stack.stackSize = 0;
                    flag = true;
                    break;
                }
                k = iterateBackwards?k-1:k+1;
            }
        }
        return flag;
    }

    //par1: slotIndex
    //par2: 0 = LMB, 1 = RMB, 2 = MMB
    //par3: 1 = shift, 3 = MMB
    @Override
    public ItemStack slotClick(int slotIndex, int mouseButton, int shiftHeld, EntityPlayer player) {
        LogHelper.debug("Slot CLicked: par1 = " + slotIndex + ", par2 = " + mouseButton + ", par3 = " + shiftHeld);
        return super.slotClick(slotIndex, mouseButton, shiftHeld, player);
    }
}
