package com.InfinityRaider.AgriCraft.compatibility;

import com.InfinityRaider.AgriCraft.reference.Names;
import com.InfinityRaider.AgriCraft.utility.LogHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class ModIntegration {

    public static void init() {
        //Hunger Overhaul
        if(LoadedMods.hungerOverhaul) {
            FMLInterModComms.sendMessage("HungerOverhaul", "BlacklistRightClick", "com.InfinityRaider.AgriCraft.blocks.BlockCrop");
        }
        //Waila
        if(LoadedMods.waila) {
            FMLInterModComms.sendMessage(Names.Mods.waila, "register", "com.InfinityRaider.AgriCraft.compatibility.waila.WailaRegistry.initWaila");
        }
    }
    public static class LoadedMods {
        public static boolean nei;
        public static boolean harvestcraft;
        public static boolean natura;
        public static boolean thaumicTinkerer;
        public static boolean hungerOverhaul;
        public static boolean weeeFlowers;
        public static boolean exNihilo;
        public static boolean plantMegaPack;
        public static boolean magicalCrops;
        public static boolean railcraft;
        public static boolean thaumcraft;
        public static boolean mfr;
        public static boolean waila;
        public static boolean forestry;
        public static boolean chococraft;
        public static boolean mcMultipart;
        public static boolean minetweaker;
        public static boolean extraUtilities;
        public static boolean botania;
        public static boolean tconstruct;

        public static void init() {
            nei = Loader.isModLoaded(Names.Mods.nei);
            harvestcraft = Loader.isModLoaded(Names.Mods.harvestcraft);
            natura = Loader.isModLoaded(Names.Mods.natura);
            weeeFlowers = Loader.isModLoaded(Names.Mods.weeeFlowers);
            forestry = Loader.isModLoaded(Names.Mods.forestry);
            thaumicTinkerer = Loader.isModLoaded(Names.Mods.thaumicTinkerer);
            hungerOverhaul = Loader.isModLoaded(Names.Mods.hungerOverhaul);
            exNihilo = Loader.isModLoaded(Names.Mods.exNihilo);
            plantMegaPack = Loader.isModLoaded(Names.Mods.plantMegaPack);
            magicalCrops = Loader.isModLoaded(Names.Mods.magicalCrops);
            railcraft = Loader.isModLoaded(Names.Mods.railcraft);
            thaumcraft = Loader.isModLoaded(Names.Mods.thaumcraft);
            mfr = Loader.isModLoaded(Names.Mods.mfr);
            waila = Loader.isModLoaded(Names.Mods.waila);
            chococraft = Loader.isModLoaded(Names.Mods.chococraft);
            mcMultipart = Loader.isModLoaded(Names.Mods.mcMultipart);
            minetweaker = Loader.isModLoaded(Names.Mods.minetweaker);
            extraUtilities = Loader.isModLoaded(Names.Mods.extraUtilities);
            botania = Loader.isModLoaded(Names.Mods.botania);
            tconstruct = Loader.isModLoaded(Names.Mods.tconstruct);

            LogHelper.info("Checking for loaded mods:");
            LogHelper.info(" - NEI loaded: "+nei);
            LogHelper.info(" - Pam's HarvestCraft loaded: "+harvestcraft);
            LogHelper.info(" - Natura loaded: "+natura);
            LogHelper.info(" - Pam's Weee Flowers loaded: "+weeeFlowers);
            LogHelper.info(" - Forestry loaded: "+forestry);
            LogHelper.info(" - Thaumic tinkerer loaded: "+thaumicTinkerer);
            LogHelper.info(" - Hunger Overhaul loaded: "+hungerOverhaul);
            LogHelper.info(" - Ex Nihilo loaded: "+exNihilo);
            LogHelper.info(" - Plant Mega Pack loaded: "+plantMegaPack);
            LogHelper.info(" - Magical Crops loaded: "+magicalCrops);
            LogHelper.info(" - Railcraft loaded: "+railcraft);
            LogHelper.info(" - Thaumcraft loaded: "+thaumcraft);
            LogHelper.info(" - MineFactory Reloaded loaded: "+mfr);
            LogHelper.info(" - Waila loaded: "+waila);
            LogHelper.info(" - Chococraft loaded: "+chococraft);
            LogHelper.info(" - McMultipart loaded: "+mcMultipart);
            LogHelper.info(" - MineTweaker loaded: "+minetweaker);
            LogHelper.info(" - ExtraUtilities loaded: "+extraUtilities);
            LogHelper.info(" - Botania loaded: "+botania);
            LogHelper.info(" - Tinker's Construct loaded: "+tconstruct);
            LogHelper.info("Done");
        }
    }

}
