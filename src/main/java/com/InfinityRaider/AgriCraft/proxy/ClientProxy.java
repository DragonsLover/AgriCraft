package com.InfinityRaider.AgriCraft.proxy;

import com.InfinityRaider.AgriCraft.compatibility.ModIntegration;
import com.InfinityRaider.AgriCraft.handler.ItemToolTipHandler;
import com.InfinityRaider.AgriCraft.init.Blocks;
import com.InfinityRaider.AgriCraft.init.Items;
import com.InfinityRaider.AgriCraft.reference.Constants;
import com.InfinityRaider.AgriCraft.reference.SeedInformation;
import com.InfinityRaider.AgriCraft.renderers.*;
import com.InfinityRaider.AgriCraft.tileentity.*;
import com.InfinityRaider.AgriCraft.utility.LogHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {

    public static int cropRenderId = -1;
    public static int tankRenderId = -1;
    public static int channelRenderId = -1;
    public static int valveRenderId = -1;

    @Override
    public int getRenderId(int nr) {
        switch(nr) {
            case Constants.cropId: return cropRenderId;
            case Constants.tankId: return tankRenderId;
            case Constants.channelId: return channelRenderId;
            case Constants.valveId: return valveRenderId;
        }
        return -1;
    }

    //register custom renderers
    @Override
    public void registerRenderers() {
        //crops
        // cropRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderCrop renderCrops = new RenderCrop();
        // RenderingRegistry.registerBlockHandler(cropRenderId, renderCrops);

        //seed analyzer
        TileEntitySpecialRenderer  renderAnalyzer = new RenderSeedAnalyzer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySeedAnalyzer.class, renderAnalyzer);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Blocks.seedAnalyzer), new RenderItemSeedAnalyzer(renderAnalyzer, new TileEntitySeedAnalyzer()));

        //water tank
        //tankRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderTank renderTank = new RenderTank();
        //RenderingRegistry.registerBlockHandler(tankRenderId, renderTank);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Blocks.blockWaterTank), new RenderItemTank(new TileEntityTank()));

        //water channel
        //channelRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderChannel renderChannel = new RenderChannel();
        //RenderingRegistry.registerBlockHandler(channelRenderId, renderChannel);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Blocks.blockWaterChannel), new RenderItemChannel(new TileEntityChannel()));

        //channel valve
        // valveRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderValve renderValve = new RenderValve();
        // RenderingRegistry.registerBlockHandler(valveRenderId, renderValve);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Blocks.blockChannelValve), new RenderItemValve(new TileEntityValve()));

        //sprinkler
        TileEntitySpecialRenderer renderSprinkler = new RenderSprinkler();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySprinkler.class, renderSprinkler);
        MinecraftForgeClient.registerItemRenderer(Items.sprinkler, new RenderItemSprinkler());

        LogHelper.info("Renderers registered");
    }

    //register forge event handlers
    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
        FMLCommonHandler.instance().bus().register(new ItemToolTipHandler());
        MinecraftForge.EVENT_BUS.register(new ItemToolTipHandler());
    }

    //initialize NEI
    @Override
    public void initNEI() {
        if (ModIntegration.LoadedMods.nei) {
            // TODO: enable once we have NEI support again
            // NEIConfig configNEI = new NEIConfig();
            // configNEI.loadConfig();
        }
    }

    //hide items in NEI
    @Override
    public void hideItemInNEI(ItemStack stack) {
        if (ModIntegration.LoadedMods.nei) {
            // TODO: enable again when we have NEI support
            // API.hideItem(stack);
        }
    }

    //initialize seed information
    @Override
    public void initSeedInfo() {
        SeedInformation.init();
    }
}
