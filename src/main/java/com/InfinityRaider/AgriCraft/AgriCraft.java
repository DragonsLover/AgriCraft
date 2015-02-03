package com.InfinityRaider.AgriCraft;

/*
    This is my first "real" mod, I've made this while learning to use Minecraft Forge to Mod Minecraft. The code might not be optimal but that wasn't the point of this project.

    Cheers to:
        - Pam for trusting me with her source code and support
        - Pahimar for making his code open source and for creating his Let's Mod Reboot Youtube series, I've learned a lot from this (also used some code, credits due where credits due)
        - VSWE for his "Forging a Minecraft Mod" summer courses
        - NealeGaming for his Minecraft modding tutorials on youtube

    I've annotated my code heavily, for myself and for possible others who might learn from it.

    Oh and keep on modding in the free world

        ~ InfinityRaider
*/

import com.InfinityRaider.AgriCraft.compatibility.ModIntegration;
import com.InfinityRaider.AgriCraft.farming.SoilWhitelist;
import com.InfinityRaider.AgriCraft.farming.mutation.MutationHandler;
import com.InfinityRaider.AgriCraft.handler.ConfigurationHandler;
import com.InfinityRaider.AgriCraft.handler.GuiHandler;
import com.InfinityRaider.AgriCraft.init.*;
import com.InfinityRaider.AgriCraft.proxy.IProxy;
import com.InfinityRaider.AgriCraft.reference.Reference;
import com.InfinityRaider.AgriCraft.renderers.models.AgriCraftModelLoader;
import com.InfinityRaider.AgriCraft.renderers.models.ModelGenerator;
import com.InfinityRaider.AgriCraft.utility.LogHelper;
import com.InfinityRaider.AgriCraft.utility.SeedHelper;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = Reference.MOD_ID,name = Reference.MOD_NAME,version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class AgriCraft {
    @Mod.Instance(Reference.MOD_ID)
    public static AgriCraft instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS,serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        LogHelper.info("Starting Pre-Initialization");
        //find loaded mods
        ModIntegration.LoadedMods.init();
        //register forge event handlers
        proxy.registerEventHandlers();
        //setting up configuration file
        ConfigurationHandler.init(event);
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
        //initialize blocks
        Blocks.init();
        //initialize crops
        Crops.init();
        //initialize items
        Items.init();
        LogHelper.info("Pre-Initialization Complete");
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        LogHelper.info("Starting Initialization");
        ResourceCrops.init();
        Seeds.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance , new GuiHandler());
        proxy.registerTileEntities();
        proxy.registerRenderers();

        ModelGenerator.register();
        AgriCraftModelLoader.register();

        ModIntegration.init();

        LogHelper.info("Initialization Complete");
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        LogHelper.info("Starting Post-Initialization");

        Recipes.init();
        CustomCrops.initCustomCrops();
        SeedHelper.init();
        MutationHandler.init();
        SoilWhitelist.initSoils();
        CustomCrops.initGrassSeeds();

        if(!ConfigurationHandler.disableWorldGen) {
            WorldGen.init();
        }

        proxy.initNEI();
        proxy.initSeedInfo();
        LogHelper.info("Post-Initialization Complete");
    }
}
