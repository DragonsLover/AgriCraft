package com.InfinityRaider.AgriCraft.gui;

import com.InfinityRaider.AgriCraft.container.ContainerSeedStorageController;
import com.InfinityRaider.AgriCraft.container.ContainerSeedStorageDummy;
import com.InfinityRaider.AgriCraft.container.SlotSeedStorage;
import com.InfinityRaider.AgriCraft.network.MessageContainerSeedStorage;
import com.InfinityRaider.AgriCraft.network.NetworkWrapperAgriCraft;
import com.InfinityRaider.AgriCraft.reference.Names;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

public abstract class GuiSeedStorageDummy extends GuiContainer {
    public ContainerSeedStorageDummy container;
    public ItemSeeds activeSeed;
    public int activeMeta;

    protected int scrollPositionVertical;
    protected int scrollPositionHorizontal;
    public static final int maxNrVerticalSeeds = 10;
    public static final int maxNrHorizontalSeeds = 6;

    protected static final int buttonIdGrowth = 0;
    protected static final int buttonIdGain = 1;
    protected static final int buttonIdStrength = 2;
    protected static final int buttonIdScrollDown = 3;
    protected static final int buttonIdScrollUp = 4;
    protected static final int buttonIdScrollLeft = 5;
    protected static final int buttonIdScrollRight = 6;
    protected static final int buttonIdDownEnd = 7;
    protected static final int buttonIdUpEnd = 8;
    protected static final int buttonIdLeftEnd = 9;
    protected static final int buttonIdRightEnd = 10;

    protected static final int lastButtonId = 10;

    public GuiSeedStorageDummy(ContainerSeedStorageDummy container) {
        super(container);
        this.container = container;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id <= buttonIdStrength && this.activeSeed != null) {
            this.sortByStat(button.id);
        } else if (button.id <= lastButtonId) {
            switch (button.id) {
                case buttonIdScrollDown:
                    this.scrollVertical(-1);
                    break;
                case buttonIdScrollUp:
                    this.scrollVertical(1);
                    break;
                case buttonIdScrollRight:
                    this.scrollHorizontal(1);
                    break;
                case buttonIdScrollLeft:
                    this.scrollHorizontal(-1);
                    break;
                case buttonIdDownEnd:
                    this.scrollVertical(this.getMaxVerticalScroll());
                    break;
                case buttonIdUpEnd:
                    this.scrollVertical(-this.getMaxVerticalScroll());
                    break;
                case buttonIdLeftEnd:
                    this.scrollHorizontal(-this.getMaxHorizontalScroll());
                    break;
                case buttonIdRightEnd:
                    this.scrollHorizontal(this.getMaxHorizontalScroll());
                    break;
            }
        }
        this.container.resetActiveEntries();
    }

    protected void sortByStat(int id) {
        String stat=null;
        switch(id) {
            case buttonIdGrowth: stat = Names.NBT.growth; break;
            case buttonIdGain: stat = Names.NBT.gain; break;
            case buttonIdStrength: stat = Names.NBT.strength; break;
        }
        if(stat!=null && this.activeSeed!=null) {
            ArrayList<SlotSeedStorage> list = this.getActiveEntries();
            for(int i=list.size()-1;i>=0;i--) {
                if(list.get(i)==null) {
                    list.remove(i);
                }
            }
            if(list.size()==0) {return;}
            SlotSeedStorage first;
            ArrayList<SlotSeedStorage> newList = new ArrayList<SlotSeedStorage>();
            while(list.size()>0) {
                first = list.get(0);
                for (SlotSeedStorage slot:list) {
                    if(slot!=null && slot.getStack()!=null) {
                        NBTTagCompound firstTag = first.getStack().stackTagCompound;
                        NBTTagCompound thisTag = slot.getStack().stackTagCompound;
                        if (thisTag.getInteger(stat) > firstTag.getInteger(stat)) {
                            first = slot;
                        }
                    }
                }
                newList.add(first);
                list.remove(first);
            }
            ((ContainerSeedStorageController) this.inventorySlots).entries.get(this.activeSeed).put(this.activeMeta, newList);
        }
    }

    protected void scrollVertical(int amount) {
        int newPos = this.scrollPositionVertical+amount;
        newPos = newPos<0?0:newPos;
        int maxScrollY = this.getMaxVerticalScroll();
        newPos = newPos>maxScrollY?maxScrollY:newPos;
        this.scrollPositionVertical = newPos;
    }

    protected int getMaxVerticalScroll() {
        int nrSeedButtons = this.buttonList.size()-buttonIdScrollRight-1;
        int nrRows = (nrSeedButtons%4>0?1:0) + nrSeedButtons/4;
        if(nrRows<=maxNrVerticalSeeds) {
            return 0;
        }
        else {
            return nrRows-maxNrVerticalSeeds;
        }
    }

    protected void scrollHorizontal(int amount) {
        if(this.activeSeed!=null) {
            int newPos = this.scrollPositionHorizontal + amount;
            newPos = newPos < 0 ? 0 : newPos;
            int maxScrollX = this.getMaxHorizontalScroll();
            newPos = newPos > maxScrollX ? maxScrollX : newPos;
            this.scrollPositionHorizontal = newPos;
            this.setActiveEntries();
        }
    }

    protected int getMaxHorizontalScroll() {
        int nrSlots = this.getActiveEntries().size();
        if(nrSlots<=maxNrHorizontalSeeds) {
            return 0;
        }
        else {
            return nrSlots-maxNrHorizontalSeeds;
        }
    }

    protected void setActiveEntries() {
        if(this.activeSeed!=null) {
            this.setActiveEntries(new ItemStack(this.activeSeed, 1, this.activeMeta));
        }
    }

    protected void setActiveEntries(ItemStack stack) {
        this.activeSeed = (ItemSeeds) stack.getItem();
        this.activeMeta = stack.getItemDamage();
        //clear previous active entries
        container.clearActiveEntries();
        //set the new active entries
        container.setActiveEntries(stack, this.scrollPositionHorizontal);
        //tell the server to load the slots for the active entries
        NetworkWrapperAgriCraft.wrapper.sendToServer(new MessageContainerSeedStorage(Minecraft.getMinecraft().thePlayer, stack.getItem(), stack.getItemDamage(), this.scrollPositionHorizontal));
    }

    //gets an array list of all the slots in the container corresponding to the active seed
    protected ArrayList<SlotSeedStorage> getActiveEntries() {
        ContainerSeedStorageController container = (ContainerSeedStorageController) this.container;
        ArrayList<SlotSeedStorage> list = new ArrayList<SlotSeedStorage>();
        if(this.activeSeed!=null) {
            list = container.entries.get(this.activeSeed).get(this.activeMeta);
        }
        return list;
    }

    //gets an array list of the active slots in the container corresponding to the active seed
    protected ArrayList<SlotSeedStorage> getActiveSlots() {
        ArrayList<SlotSeedStorage> list = new ArrayList<SlotSeedStorage>();
        if(this.activeSeed!=null) {
            for(int i=container.PLAYER_INVENTORY_SIZE;i<container.inventorySlots.size();i++) {
                list.add((SlotSeedStorage) this.container.inventorySlots.get(i));
            }
        }
        return list;
    }

}