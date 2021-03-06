package com.InfinityRaider.AgriCraft.gui;

import com.InfinityRaider.AgriCraft.container.ContainerSeedStorage;
import com.InfinityRaider.AgriCraft.reference.Reference;
import com.InfinityRaider.AgriCraft.tileentity.storage.TileEntitySeedStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiSeedStorage extends GuiSeedStorageDummy {
    private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/gui/GuiSeedStorage.png");
    private static final int sizeX = 237;
    private static final int sizeY = 131;

    public GuiSeedStorage(InventoryPlayer inventory, TileEntitySeedStorage te) {
        super(new ContainerSeedStorage(inventory, te), 0, 14, 170, 48, -1, -1, 6, 8);
        this.xSize = sizeX;
        this.ySize = sizeY;
        this.activeSeed = (ItemSeeds) te.getLockedSeed().getItem();
        this.activeMeta = te.getLockedSeed().getItemDamage();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        this.loadButtons();
        if(this.activeSeed!=null) {
            this.drawActiveEntries(this.texture, 6, 35);
        }
    }
}
